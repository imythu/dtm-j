package io.github.dtm.labs.core.dtm.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.dtm.labs.core.constant.DtmConstant;
import io.github.dtm.labs.core.dtm.req.AbortRequest;
import io.github.dtm.labs.core.dtm.req.PrepareRequest;
import io.github.dtm.labs.core.dtm.req.SubmitRequest;
import io.github.dtm.labs.core.dtm.req.tcc.RegisterBranchRequest;
import io.github.dtm.labs.core.dtm.res.AbortResponse;
import io.github.dtm.labs.core.dtm.res.BaseResponse;
import io.github.dtm.labs.core.dtm.res.NewGidResponse;
import io.github.dtm.labs.core.dtm.res.RegisterBranchResponse;
import io.github.dtm.labs.core.dtm.res.SubmitResponse;
import kong.unirest.GenericType;
import kong.unirest.HttpMethod;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private HttpClient() {}

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static <T> T request(String api, HttpMethod method, Object body, Class<T> responseClass) {
        HttpResponse<String> response = getResponse(api, method, body, String.class);
        return gson.fromJson(response.getBody(), responseClass);
    }


    public static <T> T request(String api, HttpMethod method, Object body, GenericType<T> type) {
        HttpResponse<String> response = getResponse(api, method, body, String.class);
        return gson.fromJson(response.getBody(), type.getType());
    }

    public static <T> HttpResponse<T> getResponse(String api, HttpMethod method, Object body, Class<T> responseClass) {
        HttpRequestWithBody httpRequestWithBody = Unirest.request(method.name(), api);
        HttpRequest<?> request;
        if (!HttpMethod.GET.equals(method)) {
            request = httpRequestWithBody.body(gson.toJson(body));
        } else {
            request = httpRequestWithBody;
        }
        return request.asObject(responseClass)
                .ifFailure(res -> logger.error(
                        "request [api: {}, method: {}, headers: {}, body: {}] failed, response: [status: {}, headers: {}, body: {}]",
                        api,
                        method,
                        request.getHeaders(),
                        body,
                        res.getStatus(),
                        res.getHeaders(),
                        res.getBody()))
                .ifSuccess(res -> logger.debug(
                        "request [api: {}, method: {}, headers: {}, body: {}] successful, response: [status: {}, headers: {}, body: {}]",
                        api,
                        method,
                        request.getHeaders(),
                        body,
                        res.getStatus(),
                        res.getHeaders(),
                        res.getBody()));
    }
}
