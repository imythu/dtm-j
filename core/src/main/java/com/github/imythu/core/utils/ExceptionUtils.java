package com.github.imythu.core.utils;

import com.github.imythu.core.compatible.golang.ReturnVal;
import com.github.imythu.core.compatible.golang.error.Error;
import com.github.imythu.core.compatible.golang.error.Errors;

/**
 * @author imythu
 */
public class ExceptionUtils {
    private ExceptionUtils() {}

    public static <T> ReturnVal<T> execute(Executor<T> executor) {
        try {
            T t = executor.execute();
            return new ReturnVal<>(t, null);
        } catch (Exception e) {
            return Errors.wrap(e);
        }
    }

    public static Error execute(EmptyReturnValExecutor executor) {
        try {
            executor.execute();
            return null;
        } catch (Exception e) {
            return Errors.newError(e);
        }
    }

    public static interface Executor<T> {
        T execute() throws Exception;
    }

    public static interface EmptyReturnValExecutor {
        void execute() throws Exception;
    }
}
