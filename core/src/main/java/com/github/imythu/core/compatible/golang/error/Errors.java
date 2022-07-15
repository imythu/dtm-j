package com.github.imythu.core.compatible.golang.error;

import com.github.imythu.core.compatible.golang.ReturnVal;

/**
 * @author imythu
 */
public class Errors {
    private Errors() {}

    public static <T> ReturnVal<T> wrap(String error) {
        return new ReturnVal<>(null, new StringError(error));
    }

    public static <T> ReturnVal<T> wrap(Exception e) {
        return new ReturnVal<>(null, new ExceptionError(e));
    }

    public static Error newError(String error) {
        return new StringError(error);
    }

    public static Error newError(Exception e) {
        return new ExceptionError(e);
    }

    public static class StringError implements Error {
        private final String error;

        public StringError(String error) {
            this.error = error;
        }

        @Override
        public String error() {
            return error;
        }
    }

    public static class ExceptionError implements Error {
        private final Exception exception;

        ExceptionError(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String error() {
            return exception.getMessage();
        }

        public Exception getException() {
            return exception;
        }
    }
}
