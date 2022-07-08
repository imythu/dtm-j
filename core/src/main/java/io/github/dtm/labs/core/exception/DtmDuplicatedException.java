package io.github.dtm.labs.core.exception;

import io.github.dtm.labs.core.constant.GlobalTransactionType;

/**
 * error of DUPLICATED for only {@link GlobalTransactionType#MSG}
 * if QueryPrepared executed before call. then DoAndSubmit return this error
 */
public class DtmDuplicatedException extends RuntimeException {
    public DtmDuplicatedException() {
        super();
    }

    public DtmDuplicatedException(String message) {
        super(message);
    }

    public DtmDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtmDuplicatedException(Throwable cause) {
        super(cause);
    }

    protected DtmDuplicatedException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
