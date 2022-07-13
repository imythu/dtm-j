package io.github.dtm.labs.core.mode.tcc;

import io.github.dtm.labs.core.exception.PrepareException;
import io.github.dtm.labs.core.mode.tcc.entity.BusinessService;

/**
 * @author imyth
 */
public interface TccGlobalTx {

    /**
     * 注册并调用 try 分支事务。 try 响应码 [200,300) 为成功，否则为失败
     * @param branchTx 分支事务
     * @return 添加成功返回 true，否则返回 false
     */
    boolean tryAndRegistryBranchTx(BusinessService branchTx);


    /**
     * 开启全局事务
     * @throws PrepareException 开启失败抛出异常
     */
    void prepare() throws PrepareException;

    /**
     * 提交全局事务
     * @return 提交成功返回 true，否则返回 false
     */
    boolean submit();

    /**
     * 回滚全局事务
     */
        void rollback();
}
