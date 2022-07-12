package io.github.dtm.labs.core.mode.tcc;

import io.github.dtm.labs.core.mode.tcc.entity.BusinessService;

import java.util.List;

/**
 * @author imyth
 */
public interface TccGlobalTx {
    /**
     * 添加分支事务
     * @param branchTxList 分支事务列表
     */
    void addBranchTxList(List<BusinessService> branchTxList);

    /**
     * 添加分支事务
     *
     * @param branchTx 分支事务
     * @return
     */
    boolean addBranchTx(BusinessService branchTx);

    void callback(Runnable runnable);

    boolean start();
}
