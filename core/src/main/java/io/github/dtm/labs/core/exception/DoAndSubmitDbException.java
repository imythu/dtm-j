package io.github.dtm.labs.core.exception;

/**
 * @author imythu
 */
public class DoAndSubmitDbException extends MsgTransactionException {
    public DoAndSubmitDbException() {
        super();
    }

    public DoAndSubmitDbException(String message) {
        super(message);
    }

    public DoAndSubmitDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoAndSubmitDbException(Throwable cause) {
        super(cause);
    }

    protected DoAndSubmitDbException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
