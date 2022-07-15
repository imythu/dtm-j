package com.github.imythu.core.mode.tcc.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author imythu
 */
public class HttpRequest {
    private String url;
    private String method;
    private Map<String, List<String>> listHeaders;
    private byte[] body;

    private HttpRequest() {}

    public static HttpRequest build(
            String url, String method, Map<String, String> headers, byte[] body) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.url = url;
        httpRequest.method = method;
        httpRequest.listHeaders = new HashMap<>(headers.size());
        headers.forEach(
                (key, value) -> httpRequest.listHeaders.put(key, Lists.newArrayList(value)));
        httpRequest.body = body;
        return httpRequest;
    }

    public static HttpRequest buildWithListHeaders(
            String url, Map<String, List<String>> headers, byte[] body) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.url = url;
        httpRequest.listHeaders = new HashMap<>(headers.size());
        httpRequest.listHeaders = Maps.newHashMap(headers);
        httpRequest.body = body;
        return httpRequest;
    }

    public HttpRequest addHeaders(Map<String, String> headers) {
        headers.forEach(this::addHeader);
        return this;
    }

    public HttpRequest addListHeaders(Map<String, List<String>> headers) {
        headers.forEach(this::addHeader);
        return this;
    }

    public HttpRequest addHeader(String name, String value) {
        this.listHeaders.compute(
                name,
                (s, strings) -> {
                    if (strings == null) {
                        strings = Lists.newArrayList(value);
                    } else {
                        strings.add(value);
                    }
                    return strings;
                });
        return this;
    }

    public HttpRequest addHeader(String name, List<String> value) {
        this.listHeaders.compute(
                name,
                (s, strings) -> {
                    if (strings == null) {
                        strings = Lists.newArrayList(value);
                    } else {
                        strings.addAll(value);
                    }
                    return strings;
                });
        return this;
    }

    public HttpRequest setHeaders(Map<String, String> headers) {
        headers.forEach(this::setHeader);
        return this;
    }

    public HttpRequest setListHeaders(Map<String, List<String>> headers) {
        headers.forEach(this::setHeader);
        return this;
    }

    public HttpRequest setHeader(String name, String value) {
        this.listHeaders.put(name, Lists.newArrayList(value));
        return this;
    }

    public HttpRequest setHeader(String name, List<String> value) {
        this.listHeaders.put(name, value);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, List<String>> getListHeaders() {
        return listHeaders;
    }

    public byte[] getBody() {
        return body;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "HttpRequest{"
                + "url='"
                + url
                + '\''
                + ", method='"
                + method
                + '\''
                + ", listHeaders="
                + listHeaders
                + ", body="
                + Arrays.toString(body)
                + '}';
    }
}
