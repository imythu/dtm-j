package com.github.imythu.core.mode.base;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author imythu
 */
@Data
public class TransOptions {
    private boolean waitResult;
    /** for trans type: xa, tcc, unit: second */
    private int timeoutToFail;
    /** for global trans resets request timeout, unit: second */
    private int requestTimeout;
    /** for trans type: msg saga xa tcc, unit: second */
    private int retryInterval;
    /** for inherit the specified gin context headers */
    private List<String> passthroughHeaders;
    /** custom branch headers, dtm server => service api */
    private Map<String, String> branchHeaders;
    /** for trans type: saga msg */
    private boolean concurrent;
}
