package io.github.dtm.labs.core.compatible.golang.error;

/**
 * The error interface type is the conventional interface for representing an error condition,
 * with the null value representing no error.
 * @author imythu
 */
@FunctionalInterface
public interface Error {
    /**
     * @return error
     */
    String error();
}
