package com.github.imythu.core.jsonrpc;

import lombok.Data;

/**
 * @author zhuhf
 */
@Data
public class JsonrpcError {
    private Integer code;
    private String message;
    private Object data;
}
