package com.github.imythu.core.barrier.exception;

import com.github.imythu.core.barrier.enums.BarrierErrorEnum;

/**
 * @author zhuhf
 */
public class BarrierException extends Exception {
    private BarrierErrorEnum errorEnum;
    public BarrierException(BarrierErrorEnum errorEnum) {
        super(errorEnum.toString());
        this.errorEnum = errorEnum;
    }

    public BarrierException(String message) {
        super(message);
    }
}
