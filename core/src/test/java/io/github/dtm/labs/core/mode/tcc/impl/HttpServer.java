package io.github.dtm.labs.core.mode.tcc.impl;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class HttpServer {
    private HttpServer(){}
    private static final Map<String, Consumer<HttpServerExchange>> pathHandlerMap = new HashMap<>();

    public static void start(int port) {
        Undertow.builder()
                .addHttpListener(port, "0.0.0.0")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        String uri = exchange.getRequestURI();
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                        Consumer<HttpServerExchange> consumer = pathHandlerMap.getOrDefault(uri, new Consumer<HttpServerExchange>() {
                            @Override
                            public void accept(HttpServerExchange httpServerExchange) {
                                httpServerExchange.setStatusCode(Response.Status.NOT_FOUND.getStatusCode());
                                httpServerExchange.getResponseSender().send("{\"code\": \"not found\"}");
                            }
                        });
                        consumer.accept(exchange);
                    }
                });
    }

    public static void addHandler(String path, Consumer<HttpServerExchange> handler) {
        pathHandlerMap.put(path, handler);
    }
}
