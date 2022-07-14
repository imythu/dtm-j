package io.github.dtm.labs.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class HttpServer {
    private final Map<String, Consumer<HttpServerExchange>> pathHandlerMap = new HashMap<>();
    private final Map<String, Function<Req, Res<Object>>> pathJsonHandlerMap = new HashMap<>();
    public static final Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

    private Undertow undertow;

    public void start(int port) {
        undertow =
                Undertow.builder()
                        .addHttpListener(port, "0.0.0.0")
                        .setHandler(
                                new BlockingHandler(
                                        exchange -> {
                                            String uri = exchange.getRequestURI();

                                            Function<Req, Res<Object>> function =
                                                    pathJsonHandlerMap.get(uri);
                                            if (function != null) {
                                                exchange.getResponseHeaders()
                                                        .put(
                                                                Headers.CONTENT_TYPE,
                                                                MediaType.APPLICATION_JSON);
                                                exchange.getRequestReceiver()
                                                        .receiveFullString(
                                                                (exchange1, message) -> {
                                                                    Res<Object> res =
                                                                            function.apply(
                                                                                    new Req(
                                                                                            uri,
                                                                                            message));
                                                                    exchange1.setStatusCode(
                                                                            res.status
                                                                                    .getStatusCode());
                                                                    exchange1
                                                                            .getResponseSender()
                                                                            .send(
                                                                                    gson.toJson(
                                                                                            res.data));
                                                                });
                                                return;
                                            }
                                            Consumer<HttpServerExchange> consumer =
                                                    pathHandlerMap.getOrDefault(
                                                            uri,
                                                            new Consumer<HttpServerExchange>() {
                                                                @Override
                                                                public void accept(
                                                                        HttpServerExchange
                                                                                httpServerExchange) {
                                                                    httpServerExchange
                                                                            .getResponseHeaders()
                                                                            .put(
                                                                                    Headers
                                                                                            .CONTENT_TYPE,
                                                                                    MediaType
                                                                                            .APPLICATION_JSON);
                                                                    httpServerExchange
                                                                            .setStatusCode(
                                                                                    Status.NOT_FOUND
                                                                                            .getStatusCode());
                                                                    httpServerExchange
                                                                            .getResponseSender()
                                                                            .send(
                                                                                    "{\"code\": \"not found\"}");
                                                                }
                                                            });
                                            consumer.accept(exchange);
                                        }))
                        .build();
        undertow.start();
        Runtime.getRuntime().addShutdownHook(new Thread(undertow::stop));
    }

    public void addHandler(String path, Consumer<HttpServerExchange> handler) {
        pathHandlerMap.put(path, handler);
    }

    public void addJsonHandler(String path, Function<Req, Res<Object>> handler) {
        pathJsonHandlerMap.put(path, handler);
    }

    public void stop() {
        undertow.stop();
    }

    public static class Res<T> {
        private final Response.Status status;
        private final T data;

        public Res(Status status, T data) {
            this.status = status;
            this.data = data;
        }
    }

    public static class Req {
        private final String path;
        private final String body;

        public Req(String path, String body) {
            this.path = path;
            this.body = body;
        }

        public String getPath() {
            return path;
        }

        public String getBody() {
            return body;
        }
    }
}
