package com.github.imythu.core.jsonrpc;

import java.io.Serializable;

/**
 * json rpc 2.0 protocol request
 *
 * @author imythu
 */
public class JsonrpcRequest<T> implements Serializable {

    private final String jsonrpc = "2.0";
    private String method;
    private String id;
    private T params;

    private JsonrpcRequest(String method, String id, T params) {
        this.method = method;
        this.id = id;
        this.params = params;
    }

    private JsonrpcRequest() {}

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @Override
    public String toString() {
        return "Request{"
                + "jsonrpc='"
                + jsonrpc
                + '\''
                + ", method='"
                + method
                + '\''
                + ", id='"
                + id
                + '\''
                + ", params="
                + params
                + '}';
    }

    public static class Builder<T> {
        private String method;
        private String id;
        private T params;

        private Builder() {}

        public Builder<T> setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder<T> setId(String id) {
            this.id = id;
            return this;
        }

        public Builder<T> setParams(T params) {
            this.params = params;
            return this;
        }

        public JsonrpcRequest<T> build() {
            return new JsonrpcRequest<>(method, id, params);
        }

        @Override
        public String toString() {
            return "Builder{"
                    + "method='"
                    + method
                    + '\''
                    + ", id='"
                    + id
                    + '\''
                    + ", params="
                    + params
                    + '}';
        }
    }
}
