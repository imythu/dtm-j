package com.github.imythu.core.mode.tcc.impl;

import com.github.imythu.core.HttpServer;
import com.github.imythu.core.mode.tcc.TccGlobalTx;
import com.github.imythu.core.mode.tcc.entity.BusinessService;
import com.github.imythu.core.TestData;
import jakarta.ws.rs.HttpMethod;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhuhf
 */
public class TestAction {
    private TestAction() {}

    public static String test(String id, TccGlobalTx tccGlobalTx, CountDownLatch blocker, int port)
            throws InterruptedException {
        tccGlobalTx.prepare();
        String prefix = "http://host.docker.internal:" + port;
        String body = HttpServer.gson.toJson(new TestData(id));
        boolean tryAndRegistryBranchTx =
                tccGlobalTx.tryAndRegistryBranchTx(
                        new BusinessService()
                                .setConfirmRequest(prefix + "/confirm")
                                .setCancelRequest(prefix + "/cancel")
                                .setTryRequest(
                                        HttpRequest.newBuilder()
                                                .uri(URI.create(prefix + "/try"))
                                                .method(
                                                        HttpMethod.POST,
                                                        BodyPublishers.ofString(body))
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
