package io.github.dtm.labs.core.exception;

/**
 * @author imythu
 */
public class DoAndSubmitException extends MsgTransactionException {
    public DoAndSubmitException() {
        super();
    }

    public DoAndSubmitException(String message) {
        super(message);
    }

    public DoAndSubmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoAndSubmitException(Throwable cause) {
        super(cause);
    }

    protected DoAndSubmitException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
