package com.github.imythu.core.exception;

/**
 * @author imythu
 */
public abstract class MsgTransactionException extends RuntimeException {
    public MsgTransactionException() {
        super();
    }

    public MsgTransactionException(String message) {
        super(message);
    }

    public MsgTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MsgTransactionException(Throwable cause) {
        super(cause);
    }

    protected MsgTransactionException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
