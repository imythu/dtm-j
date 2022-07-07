package io.github.dtm.labs.core.exception;

public class SubmitException extends MsgTransactionException{
    public SubmitException() {
        super();
    }

    public SubmitException(String message) {
        super(message);
    }

    public SubmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubmitException(Throwable cause) {
        super(cause);
    }

    protected SubmitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
