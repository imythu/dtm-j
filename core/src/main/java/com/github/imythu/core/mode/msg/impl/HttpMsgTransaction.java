package com.github.imythu.core.mode.msg.impl;

import com.github.imythu.core.barrier.BusinessExecutor;
import com.github.imythu.core.barrier.BranchBarrier;
import com.github.imythu.core.constant.DtmMethod;
import com.github.imythu.core.exception.DoAndSubmitDbException;
import com.github.imythu.core.exception.DoAndSubmitException;
import com.github.imythu.core.exception.DtmFailureException;
import com.github.imythu.core.exception.PrepareException;
import com.github.imythu.core.exception.SubmitException;
import com.github.imythu.core.mode.msg.MsgTransaction;
import com.github.imythu.core.mode.msg.entity.Msg;
import com.github.imythu.core.utils.DbUtils;
import com.github.imythu.core.utils.JsonUtils;
import com.github.imythu.core.utils.TransBaseUtils;
import jakarta.ws.rs.HttpMethod;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author imythu
 */
public class HttpMsgTransaction implements MsgTransaction<Msg> {
    private final Msg msg;

    public HttpMsgTransaction(Msg msg) {
        Objects.requireNonNull(msg, "msg must not null");
        this.msg = msg;
    }

    @Override
    public Msg add(String action, Object payload) {
        msg.getSteps().add(Collections.singletonMap("action", action));
        msg.getPayloads().add(JsonUtils.toJson(payload));
        return msg;
    }

    @Override
    public Msg setDelay(long delay) {
        msg.setDelay(delay);
        return msg;
    }

    @Override
    public void prepare(String queryPrepared) throws PrepareException {
        msg.setQueryPrepared(queryPrepared);
        TransBaseUtils.transCallDtm(msg, msg, DtmMethod.PREPARE);
    }

    @Override
    public void submit() throws SubmitException {
        buildCustomOptions();
        TransBaseUtils.transCallDtm(msg, msg, DtmMethod.SUBMIT);
    }

    @Override
    public void doAndSubmitDb(String queryPrepared, BusinessExecutor businessExecutor)
            throws DoAndSubmitDbException {
        doAndSubmit(
                queryPrepared,
                branchBarrier -> {
                    try {
                        DbUtils.getConnection().getMetaData().getDatabaseProductName();
                        branchBarrier.call(businessExecutor);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void doAndSubmit(String queryPrepared, Consumer<BranchBarrier> busiCall)
            throws DoAndSubmitException {
        try {
            BranchBarrier branchBarrier =
                    BranchBarrier.from(
                            msg.getTransType(),
                            msg.getGid(),
                            msg.getBranchIDGen().getBranchID(),
                            msg.getOp());
            prepare(queryPrepared);
            Exception busiEx;
            Exception transRequestBranchEx = null;
            TransBaseUtils.transRequestBranch(
                    msg,
                    HttpMethod.GET,
                    null,
                    msg.getBranchIDGen().getBranchID(),
                    msg.getOp(),
                    queryPrepared);
            try {
                busiCall.accept(branchBarrier);
                submit();
            } catch (Exception e) {
                busiEx = e;
                if (!(e instanceof DtmFailureException)) {
                    try {
                        TransBaseUtils.transRequestBranch(
                                msg,
                                HttpMethod.GET,
                                null,
                                msg.getBranchIDGen().getBranchID(),
                                msg.getOp(),
                                queryPrepared);
                    } catch (Exception branchEx) {
                        transRequestBranchEx = branchEx;
                    }
                }
                if (busiEx instanceof DtmFailureException
                        || transRequestBranchEx instanceof DtmFailureException) {
                    TransBaseUtils.transCallDtm(msg, msg, DtmMethod.ABORT);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            throw new DoAndSubmitException(e);
        }
    }

    @Override
    public void buildCustomOptions() {
        Msg msg = get();
        if (msg.getDelay() > 0) {
            msg.setCustomData(JsonUtils.toJson(Collections.singletonMap("delay", msg.getDelay())));
        }
    }

    @Override
    public Msg get() {
        return null;
    }
}
