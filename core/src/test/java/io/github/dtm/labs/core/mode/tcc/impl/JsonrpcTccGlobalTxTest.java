package io.github.dtm.labs.core.mode.tcc.impl;

import static io.github.dtm.labs.core.mode.tcc.impl.TestAction.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.dtm.labs.core.HttpServer;
import io.github.dtm.labs.core.HttpServer.Req;
import io.github.dtm.labs.core.HttpServer.Res;
import io.github.dtm.labs.core.TestData;
import jakarta.ws.rs.core.Response.Status;
import java.util.Map;
import java.util.Objects;
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
class JsonrpcTccGlobalTxTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonrpcTccGlobalTxTest.class);
    private static final int port = 12345;
    private static final Map<String, CountDownLatch> blockerMap = new ConcurrentHashMap<>(8);

    static {
        HttpServer httpServer = new HttpServer();
        httpServer.start(port);
        Function<Req, Res<Object>> handler = req -> {
            TestData map = HttpServer.gson.fromJson(req.getBody(), TestData.class);
            logger.info("{} received: {}", req.getPath(), map);
            blockerMap.computeIfPresent(map.id, (s, countDownLatch) -> {
                countDownLatch.countDown();
                return countDownLatch;
            });
            return new Res<>(
                    Objects.equals(req.getPath(), "/try") && map.id.contains("rollback") ? Status.CONFLICT : Status.OK,
                    map);
        };
        httpServer.addJsonHandler("/try", handler);
        httpServer.addJsonHandler("/confirm", handler);
        httpServer.addJsonHandler("/cancel", handler);
    }

    @Test
    public void testCommitTransactionNormally() throws InterruptedException {
        String submitId = "submit" + new Random().nextInt(100);
        logger.info("submitId: {}", submitId);
        CountDownLatch blocker = new CountDownLatch(1);
        blockerMap.put(submitId, blocker);
        assertEquals(HttpTccGlobalTxTest.submitSuccess, test(submitId, new JsonrpcTccGlobalTx(), blocker, port));
    }

    @Test
    public void testRollbackTransaction() throws InterruptedException {
        String rollbackId = "rollback" + new Random().nextInt(100);
        logger.info("rollbackId: {}", rollbackId);
        CountDownLatch blocker = new CountDownLatch(1);
        blockerMap.put(rollbackId, blocker);
        assertEquals(HttpTccGlobalTxTest.rollbackSuccess, test(rollbackId, new JsonrpcTccGlobalTx(), blocker, port));
    }
}
