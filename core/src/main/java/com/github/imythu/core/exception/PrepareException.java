package com.github.imythu.core.exception;

/**
 * <strong>msg</strong> prepare exception
 *
 * @author imythu
 */
public class PrepareException extends MsgTransactionException {
    public PrepareException() {
        super();
    }

    public PrepareException(String message) {
        super(message);
    }

    public PrepareException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrepareException(Throwable cause) {
        super(cause);
    }

    protected PrepareException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
