package io.github.dtm.labs.core.exception;

/**
 * error for returned {@link io.github.dtm.labs.core.constant.DtmConstant#RESULT_ONGOING}
 * @author myth
 */
public class DtmOngoingException extends RuntimeException {
    public DtmOngoingException() {
        super();
    }

    public DtmOngoingException(String message) {
        super(message);
    }

    public DtmOngoingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtmOngoingException(Throwable cause) {
        super(cause);
    }

    protected DtmOngoingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
