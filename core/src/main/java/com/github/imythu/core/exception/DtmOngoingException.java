package com.github.imythu.core.exception;

import com.github.imythu.core.constant.DtmConstant;

/**
 * error for returned {@link DtmConstant#RESULT_ONGOING}
 *
 * @author imythu
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

    protected DtmOngoingException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
