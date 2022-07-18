package com.github.imythu.core.exception;

/**
 * @author imythu
 */
public class BarrierException extends RuntimeException {
    public BarrierException() {
        super();
    }

    public BarrierException(String message) {
        super(message);
    }

    public BarrierException(String message, Throwable cause) {
        super(message, cause);
    }

    public BarrierException(Throwable cause) {
        super(cause);
    }

    protected BarrierException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
