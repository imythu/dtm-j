package com.github.imythu.core.jsonrpc;

import lombok.Data;

/**
 * @author zhuhf
 */
@Data
public class JsonrpcResponse<T> {
    private final String jsonrpc = "2.0";
    private JsonrpcError error;
    private String id;
    private T result;
}
