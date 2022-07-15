package com.github.imythu.core.dtm.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClients {
    private static final Logger logger = LoggerFactory.getLogger(HttpClients.class);

    private HttpClients() {}

    private static final Gson gson =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
    private static final HttpClient client = HttpClient.newBuilder().build();

    public static <T> T request(String api, String method, Object body, Class<T> responseClass) {
        HttpResponse<String> response = getResponse(api, method, body);
        return gson.fromJson(response.body(), responseClass);
    }

    public static <T> T request(String api, String method, Object body, TypeToken<T> typeToken) {
        HttpResponse<String> response = getResponse(api, method, body);
        return gson.fromJson(response.body(), typeToken.getType());
    }

    public static HttpResponse<String> getResponse(String api, String method, Object body) {
        return getResponse(api, method, body, Collections.emptyMap());
    }

    public static HttpResponse<String> getResponse(
            String api, String method, Object body, Map<String, List<String>> headers) {
        HttpRequest.Builder builder =
                HttpRequest.newBuilder()
                        .uri(URI.create(api))
                        .method(method, HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .version(HttpClient.Version.HTTP_1_1);
        headers.forEach(
                (name, value) -> {
                    builder.header(name, String.join(",", value));
                });
        HttpRequest httpRequest = builder.build();
        return getResponse(httpRequest);
    }

    public static HttpResponse<String> getResponse(HttpRequest httpRequest) {
        String api = httpRequest.uri().toString();
        String method = httpRequest.method();
        String body = httpRequest.bodyPublisher().orElse(BodyPublishers.noBody()).toString();
        try {
            HttpResponse<String> response =
                    client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (200 <= statusCode && statusCode < 300) {
                logger.debug(
                        "request [api: {}, method: {}, headers: {}, body: {}] successful, response: [status: {}, headers: {}, body: {}]",
                        api,
                        method,
                        httpRequest.headers(),
                        body,
                        response.statusCode(),
                        response.headers(),
                        response.body());
            } else {
                logger.error(
                        "request [api: {}, method: {}, headers: {}, body: {}] failed, response: [status: {}, headers: {}, body: {}]",
                        api,
                        method,
                        httpRequest.headers(),
                        body,
                        response.statusCode(),
                        response.headers(),
                        response.body());
            }
            return response;
        } catch (IOException | InterruptedException e) {
            logger.error(
                    "request [api: {}, method: {}, headers: {}, body: {}] failed",
                    api,
                    method,
                    httpRequest.headers(),
                    body,
                    e);
            throw new RuntimeException(e);
        }
    }
}
