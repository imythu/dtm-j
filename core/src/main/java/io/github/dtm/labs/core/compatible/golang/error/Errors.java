package io.github.dtm.labs.core.compatible.golang.error;

/**
 * @author myth
 */
public class Errors {
    private Errors() {}

    public static Error newError(String error) {
        return new DefaultError(error);
    }

    static class DefaultError implements Error {
        private final String error;

        public DefaultError(String error) {
            this.error = error;
        }

        @Override
        public String error() {
            return error;
        }
    }
}
