package io.github.dtm.labs.core.mode.tcc.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.github.dtm.labs.core.mode.tcc.TccGlobalTx;
import io.github.dtm.labs.core.mode.tcc.entity.BusinessService;
import io.github.dtm.labs.core.mode.tcc.entity.HttpRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import kong.unirest.HttpMethod;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhuhf
 */
class HttpTccGlobalTxTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpTccGlobalTxTest.class);
    static final String confirmSuccess = "confirmSuccess";
    static final String cancelSuccess = "cancelSuccess";
    public static final int port = 12345;
    private static String apiPrefix = "http://host.docker.internal:" + port;
    static HttpServer httpServer;
    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private static ConcurrentMap<String, String> result = new ConcurrentHashMap<>(2);

    @Test
    public void testCommitTransactionNormally() {
        listening();
        assertEquals(confirmSuccess, test(true, new TestData("1"), new HttpTccGlobalTx()));
        httpServer.stop(5);
    }

    @Test
    public void testRollbackTransaction() {
        listening();
        assertEquals(cancelSuccess, test(false, new TestData("2"), new HttpTccGlobalTx()));
        httpServer.stop(5);
    }

    static String test(boolean submit, TestData testData, TccGlobalTx tx) {
        tx.prepare();
        String bodyStr = gson.toJson(testData);
        byte[] body = bodyStr.getBytes(StandardCharsets.UTF_8);
        boolean tryAndRegistryBranchTx = tx.tryAndRegistryBranchTx(new BusinessService()
                .setTryRequest(
                        HttpRequest.build(apiPrefix + "/try", HttpMethod.POST.name(), Collections.emptyMap(), body))
                .setCancelRequest(apiPrefix + "/cancel")
                .setConfirmRequest(apiPrefix + "/confirm")
                .setConfirmAndCancelRequestData(bodyStr));
        if (!tryAndRegistryBranchTx) {
            httpServer.stop(0);
            throw new RuntimeException();
        }
        if (submit) {
            if (!tx.submit()) {
                httpServer.stop(200);
                throw new RuntimeException("submit failed");
            }
        } else {
            tx.rollback();
        }
        try {
            return CompletableFuture.supplyAsync(() -> {
                        while (result.get(testData.id) == null) {}
                        return result.get(testData.id);
                    })
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    static void listening() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext("/try", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    Map<String, Object> json = gson.fromJson(
                            new String(exchange.getRequestBody().readAllBytes()),
                            new TypeToken<Map<String, Object>>() {}.getType());
                    logger.info("try with gid: {}", json);
                    byte[] response = "{\"success\": true}".getBytes();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.close();
                }
            });
            httpServer.createContext("/confirm", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    TestData json =
                            gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), TestData.class);
                    logger.info("confirm with gid: {}", json);
                    byte[] response = "{\"success\": true}".getBytes();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.close();
                    result.put(json.id, confirmSuccess);
                }
            });
            httpServer.createContext("/cancel", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    TestData json =
                            gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), TestData.class);
                    logger.info("cancel with gid: {}", json);
                    byte[] response = "{\"success\": true}".getBytes();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.close();
                    result.put(json.id, cancelSuccess);
                }
            });
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class TestData {

        private String id;

        public TestData(String id) {
            this.id = id;
        }
    }
}
