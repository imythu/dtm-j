package com.github.imythu.core.compatible.golang;

import com.github.imythu.core.compatible.golang.error.Error;

/**
 * @author imythu
 */
public class ReturnVal<T> {
    public T value;
    public Error err;

    public ReturnVal(T value, Error err) {
        this.value = value;
        this.err = err;
    }

    public T getValue() {
        return value;
    }

    public ReturnVal<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public Error getErr() {
        return err;
    }

    public ReturnVal<T> setErr(Error err) {
        this.err = err;
        return this;
    }

    @Override
    public String toString() {
        return "ReturnVal{" + "value=" + value + ", err=" + err + '}';
    }
}
