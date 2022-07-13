package io.github.dtm.labs.core.mode.tcc.impl;

import io.github.dtm.labs.core.dtm.req.AbortRequest;
import io.github.dtm.labs.core.dtm.req.PrepareRequest;
import io.github.dtm.labs.core.dtm.req.SubmitRequest;
import io.github.dtm.labs.core.dtm.req.tcc.RegisterBranchRequest;
import io.github.dtm.labs.core.dtm.utils.HttpClients;
import io.github.dtm.labs.core.enums.TxType;
import io.github.dtm.labs.core.exception.PrepareException;
import io.github.dtm.labs.core.mode.tcc.TccGlobalTx;
import io.github.dtm.labs.core.mode.tcc.entity.BusinessService;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTccGlobalTx implements TccGlobalTx {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AtomicInteger branchId = new AtomicInteger(1);

    private String gid;

    private boolean prepared = false;

    /**
     * 向 dtm 注册分支事务
     * @param registerBranchRequest request body
     * @return 成功返回 true，否则返回 false
     */
    protected abstract boolean registryBranchTx(RegisterBranchRequest registerBranchRequest);

    /**
     * 向 dtm prepare 事务
     * @param prepareRequest request body
     * @return 成功返回 true，否则返回 false
     */
    protected abstract boolean prepare(PrepareRequest prepareRequest);

    /**
     * 向 dtm 提交事务
     * @param submitRequest request body
     * @return 成功返回 true，否则返回 false
     */
    protected abstract boolean submit(SubmitRequest submitRequest);

    /**
     * 向 dtm 回滚事务
     * @param abortRequest
     * @return 成功返回 true，否则返回 false
     */
    protected abstract boolean rollback(AbortRequest abortRequest);

    /**
     * 向 dtm 请求 gid
     * @return gid
     */
    protected abstract String newGid();

    @Override
    public boolean tryAndRegistryBranchTx(BusinessService branchTx) {
        if (!prepared) {
            throw new RuntimeException("The start() method must be called before registering a branch transaction");
        }

        RegisterBranchRequest registerBranchRequest = new RegisterBranchRequest();
        registerBranchRequest
                .setConfirm(branchTx.getConfirmRequest())
                .setData(branchTx.getConfirmAndCancelRequestData())
                .setCancel(branchTx.getCancelRequest())
                .setBranchId(gid + branchId.getAndIncrement())
                .setGid(gid)
                .setTransType(TxType.TCC.getType());
        boolean registerBranch = registryBranchTx(registerBranchRequest);
        if (!registerBranch) {
            return false;
        }

        try {
            HttpResponse<String> tryRes = HttpClients.getResponse(branchTx.getTryRequest());
            if (200 <= tryRes.statusCode() && tryRes.statusCode() < 300) {
                return true;
            }
        } catch (Exception ignored) {
        }
        logger.error("branch transaction failed! call rollback!");
        AbortRequest abortRequest = new AbortRequest();
        abortRequest.setGid(gid);
        abortRequest.setTransType(TxType.TCC.getType());
        rollback();
        return false;
    }

    @Override
    public void prepare() throws PrepareException {
        String gid = newGid();
        this.gid = gid;
        PrepareRequest prepareRequest = new PrepareRequest();
        prepareRequest.setGid(gid).setTransType(TxType.TCC.getType());
        prepared = prepare(prepareRequest);
        if (!prepared) {
            throw new PrepareException();
        }
    }

    public boolean submit() {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setGid(gid).setTransType(TxType.TCC.getType());

        return submit(submitRequest);
    }

    @Override
    public void rollback() {
        AbortRequest abortRequest = new AbortRequest();
        abortRequest.setGid(gid).setTransType(TxType.TCC.getType());

        boolean rollback = rollback(abortRequest);
        logger.info("abort tcc transaction [gid: {}] request result: [success: {}]", gid, rollback);
    }
}
