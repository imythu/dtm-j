package com.github.imythu.core.barrier.enums;

/**
 * @author zhuhf
 */
public enum BarrierErrorEnum {
    /** error for returned failure */
    FAILURE("FAILURE"),
    /** error for returned ongoing */
    ONGOING("ONGOING"),

    /**
     * error of DUPLICATED for only msg if QueryPrepared executed before call. then return this
     * error
     */
    DUPLICATED("DUPLICATED");
    private final String code;

    BarrierErrorEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "BarrierErrorEnum{" + "code='" + code + '\'' + "} " + super.toString();
    }
}
