package com.github.imythu.core.mode.tcc.impl;

import static com.github.imythu.core.mode.tcc.impl.TestAction.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.imythu.core.HttpServer;
import com.github.imythu.core.HttpServer.Req;
import com.github.imythu.core.HttpServer.Res;
import com.github.imythu.core.TestData;
import jakarta.ws.rs.core.Response.Status;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    private static final HttpServer httpServer = new HttpServer();

    @BeforeAll
    public static void start() {
        httpServer.start(port);
        Function<Req, Res<Object>> handler =
                req -> {
                    TestData map = HttpServer.gson.fromJson(req.getBody(), TestData.class);
                    logger.info("{} received: {}", req.getPath(), map);
                    blockerMap.computeIfPresent(
                            map.id,
                            (s, countDownLatch) -> {
                                countDownLatch.countDown();
                                return countDownLatch;
                            });
                    return new Res<>(
                            Objects.equals(req.getPath(), "/try") && map.id.contains("rollback")
                                    ? Status.CONFLICT
                                    : Status.OK,
                            map);
                };
        httpServer.addJsonHandler("/try", handler);
        httpServer.addJsonHandler("/confirm", handler);
        httpServer.addJsonHandler("/cancel", handler);
    }

    @AfterAll
    public static void stop() {
        httpServer.stop();
    }

    @Test
    public void testCommitTransactionNormally() throws InterruptedException {
        String submitId = "submit" + new Random().nextInt(100);
        CountDownLatch blocker = new CountDownLatch(1);
        logger.info("submitId: {}", submitId);
        blockerMap.put(submitId, blocker);
        assertEquals(
                HttpTccGlobalTxTest.submitSuccess,
                test(submitId, new HttpTccGlobalTx(), blocker, port));
    }

    @Test
    public void testRollbackTransaction() throws InterruptedException {
        String rollbackId = "rollback" + new Random().nextInt(100);
        logger.info("rollbackId: {}", rollbackId);
        CountDownLatch blocker = new CountDownLatch(1);
        blockerMap.put(rollbackId, blocker);
        assertEquals(
                HttpTccGlobalTxTest.rollbackSuccess,
                test(rollbackId, new HttpTccGlobalTx(), blocker, port));
    }
}
