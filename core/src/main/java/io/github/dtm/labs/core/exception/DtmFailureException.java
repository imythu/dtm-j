package io.github.dtm.labs.core.exception;

/**
 * error for returned {@link io.github.dtm.labs.core.constant.DtmConstant#RESULT_FAILURE}
 * @author imythu
 */
public class DtmFailureException extends RuntimeException {
    public DtmFailureException() {
        super();
    }

    public DtmFailureException(String message) {
        super(message);
    }

    public DtmFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtmFailureException(Throwable cause) {
        super(cause);
    }

    protected DtmFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
