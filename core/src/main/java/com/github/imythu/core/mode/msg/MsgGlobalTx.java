package com.github.imythu.core.mode.msg;


import com.github.imythu.core.exception.PrepareException;

public interface MsgGlobalTx {
    /**
     * 开启全局事务
     *
     * @throws PrepareException 开启失败抛出异常
     */
    void prepare() throws PrepareException;

    void addStep(String url, String body);

    boolean submit();
    boolean doAndSubmitDB();


}
