package io.github.dtm.labs.core.mode.tcc.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.dtm.labs.core.mode.tcc.TccGlobalTx;
import io.github.dtm.labs.core.mode.tcc.entity.BusinessService;
import io.github.dtm.labs.core.mode.tcc.impl.HttpServer.Req;
import io.github.dtm.labs.core.mode.tcc.impl.HttpServer.Res;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Response.Status;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhuhf
 */
class HttpTccGlobalTxTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpTccGlobalTxTest.class);
    static final String submitSuccess = "submitSuccess";
    static final String rollbackSuccess = "rollbackSuccess";
    private static final int port = 12346;
    private static final Map<String, CountDownLatch> blockerMap = new ConcurrentHashMap<>(8);

    static {
        io.github.dtm.labs.core.mode.tcc.impl.HttpServer.start(port);
        Function<Req, Res<Object>> handler = req -> {
            TestData map =
                    io.github.dtm.labs.core.mode.tcc.impl.HttpServer.gson.fromJson(req.getBody(), TestData.class);
            logger.info("{} received: {}", req.getPath(), map);
            blockerMap.computeIfPresent(map.id, (s, countDownLatch) -> {
                countDownLatch.countDown();
                return countDownLatch;
            });
            return new Res<>(map.id.contains("rollback") ? Status.CONFLICT : Status.OK, map);
        };
        io.github.dtm.labs.core.mode.tcc.impl.HttpServer.addJsonHandler("/try", handler);
        io.github.dtm.labs.core.mode.tcc.impl.HttpServer.addJsonHandler("/confirm", handler);
        HttpServer.addJsonHandler("/cancel", handler);
    }

    @Test
    public void testCommitTransactionNormally() throws InterruptedException {
        String submitId = "submit" + new Random().nextInt(100);
        CountDownLatch blocker = new CountDownLatch(1);
        blockerMap.put(submitId, blocker);
        assertEquals(HttpTccGlobalTxTest.submitSuccess, test(submitId, new HttpTccGlobalTx(), blocker));
    }

    @Test
    public void testRollbackTransaction() throws InterruptedException {
        String rollbackId = "rollback" + new Random().nextInt(100);
        CountDownLatch blocker = new CountDownLatch(1);
        blockerMap.put(rollbackId, blocker);
        assertEquals(HttpTccGlobalTxTest.rollbackSuccess, test(rollbackId, new HttpTccGlobalTx(), blocker));
    }

    public static String test(String id, TccGlobalTx tccGlobalTx, CountDownLatch blocker) throws InterruptedException {
        tccGlobalTx.prepare();
        String prefix = "http://host.docker.internal:" + port;
        String body = io.github.dtm.labs.core.mode.tcc.impl.HttpServer.gson.toJson(new TestData(id));
        boolean tryAndRegistryBranchTx = tccGlobalTx.tryAndRegistryBranchTx(new BusinessService()
                .setConfirmRequest(prefix + "/confirm")
                .setCancelRequest(prefix + "/cancel")
                .setTryRequest(HttpRequest.newBuilder()
                        .uri(URI.create(prefix + "/try"))
                        .method(HttpMethod.POST, BodyPublishers.ofString(body))
                        .build())
                .setConfirmAndCancelRequestData(body));
        if (!tryAndRegistryBranchTx) {
            tccGlobalTx.rollback();
            blocker.await();
            return HttpTccGlobalTxTest.rollbackSuccess;
        }
        tccGlobalTx.submit();
        blocker.await();

        return HttpTccGlobalTxTest.submitSuccess;
    }
}
