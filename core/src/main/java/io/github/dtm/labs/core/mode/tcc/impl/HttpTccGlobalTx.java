package io.github.dtm.labs.core.mode.tcc.impl;

import com.google.common.base.MoreObjects;
import io.github.dtm.labs.core.dtm.req.PrepareRequest;
import io.github.dtm.labs.core.dtm.req.tcc.RegisterBranchRequest;
import io.github.dtm.labs.core.dtm.utils.DtmHttpApis;
import io.github.dtm.labs.core.enums.TxType;
import io.github.dtm.labs.core.mode.tcc.TccGlobalTx;
import io.github.dtm.labs.core.mode.tcc.entity.BusinessService;
import kong.unirest.HttpResponse;
import kong.unirest.RawResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class HttpTccGlobalTx implements TccGlobalTx {
    private static final Logger logger = LoggerFactory.getLogger(HttpTccGlobalTx.class);
    private Runnable callback;

    private AtomicInteger branchId = new AtomicInteger(1);

    @Override
    public void addBranchTxList(List<BusinessService> branchTxList) {}

    @Override
    public boolean addBranchTx(BusinessService branchTx) {
        String gid = DtmHttpApis.newGid();
        PrepareRequest prepareRequest = new PrepareRequest();
        prepareRequest.setGid(gid);
        boolean result = false;
        boolean prepare = DtmHttpApis.prepare(prepareRequest);
        if (prepare) {
            RegisterBranchRequest registerBranchRequest = new RegisterBranchRequest();
            registerBranchRequest
                    .setConfirm(branchTx.getConfirmUrl())
                    .setData(branchTx.getData())
                    .setCancel(branchTx.getCancelUrl())
                    .setBranchId(gid + branchId.getAndIncrement())
                    .setGid(gid)
                    .setTransType(TxType.TCC.getType());
            boolean registerBranch = DtmHttpApis.registerBranch(registerBranchRequest);
            if (!registerBranch) {
                return false;
            }
                    Unirest.post(branchTx.getTryUrl()).body(branchTx.getData())
                            .asObject(Object.class).ifSuccess(new Consumer<HttpResponse<Object>>() {
                                @Override
                                public void accept(HttpResponse<Object> res) {
                                    Unirest.post(branchTx.getConfirmUrl())
                                            .body(branchTx.getData())
                                            .asObject(Object.class)
                                            .ifSuccess(new Consumer<HttpResponse<Object>>() {
                                                @Override
                                                public void accept(HttpResponse<Object> objectHttpResponse) {

                                                }
                                            })
                                }
                            })

        }
        return result;
    }

    @Override
    public void callback(Runnable runnable) {
        this.callback = runnable;
    }

    @Override
    public boolean start() {
        return false;
    }

    private String newSubBranchId() {}
}
