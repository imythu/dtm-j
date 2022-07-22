package com.github.imythu.core.barrier;

import com.github.imythu.core.barrier.enums.BarrierErrorEnum;
import com.github.imythu.core.barrier.exception.BarrierException;
import com.github.imythu.core.compatible.golang.ReturnVal;
import com.github.imythu.core.compatible.golang.error.Error;
import com.github.imythu.core.constant.DtmConstant;
import com.github.imythu.core.constant.ErrorConstant;
import com.github.imythu.core.domain.BarrierDO;
import com.github.imythu.core.tx.Transaction;
import com.github.imythu.core.utils.DbUtils;
import com.github.imythu.core.utils.ExceptionUtils;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author imythu
 */
public class BranchBarrier {
    private static final Logger logger = LoggerFactory.getLogger(BranchBarrier.class);
    private static final Map<String, String> opMap = new HashMap<>(3);

    static {
        // tcc
        opMap.put(DtmConstant.OP_CANCEL, DtmConstant.OP_TRY);
        // saga
        opMap.put(DtmConstant.OP_COMPENSATE, DtmConstant.OP_ACTION);
        // workflow
        opMap.put(DtmConstant.OP_ROLLBACK, DtmConstant.OP_ACTION);
    }

    private String transType;
    private String gid;
    private String branchId;
    private String op;
    private int barrierId;

    private Transaction transaction;

    public static BranchBarrier from(
            String transType, String gid, String branchId, String op, Transaction transaction) {
        if (Strings.isNullOrEmpty(transType)
                || Strings.isNullOrEmpty(gid)
                || Strings.isNullOrEmpty(branchId)
                || Strings.isNullOrEmpty(op)) {
            throw new IllegalArgumentException("param cannot is null or empty");
        }
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.transType = transType;
        branchBarrier.gid = gid;
        branchBarrier.branchId = branchId;
        branchBarrier.op = op;
        branchBarrier.transaction = transaction;
        return branchBarrier;
    }

    public static BranchBarrier from(String transType, String gid, String branchId, String op) {
        return from(transType, gid, branchId, op, Transaction.getDefaultTransaction());
    }

    /**
     * see detail description in <a
     * href="https://en.dtm.pub/practice/barrier.html">https://en.dtm.pub/practice/barrier.html</a>
     *
     * @param businessExecutor businessExecutor
     */
    public void call(BusinessExecutor businessExecutor) throws Exception {
        transaction.begin();
        try {
            callInternal(businessExecutor);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    private void callInternal(BusinessExecutor businessExecutor) throws BarrierException {
        barrierId++;
        String bid = String.format("%02d", barrierId);
        String originOp = opMap.get(op);

        ReturnVal<Integer> originVal =
                ExceptionUtils.execute(
                        () ->
                                DbUtils.getDbSpecial(transaction.getConnection())
                                        .executeInsertIgnoreSql(
                                                transaction.getConnection(),
                                                new BarrierDO()
                                                        .setTransType(transType)
                                                        .setGid(gid)
                                                        .setBranchId(branchId)
                                                        .setOp(originOp)
                                                        .setBarrierId(bid)
                                                        .setReason(op)));
        ReturnVal<Integer> currentVal =
                ExceptionUtils.execute(
                        () ->
                                DbUtils.getDbSpecial(transaction.getConnection())
                                        .executeInsertIgnoreSql(
                                                transaction.getConnection(),
                                                new BarrierDO()
                                                        .setTransType(transType)
                                                        .setGid(gid)
                                                        .setBranchId(branchId)
                                                        .setOp(op)
                                                        .setBarrierId(bid)
                                                        .setReason(op)));
        int originAffected = originVal.getValue();
        Error oerr = originVal.getErr();

        int currentAffected = currentVal.getValue();
        Error rerr = currentVal.getErr();

        logger.debug("originAffected: {} currentAffected: {}", originAffected, currentAffected);

        // for msg's DoAndSubmit, repeated insert should be rejected.
        if (rerr == null && DtmConstant.MSG_DO_OP.equals(op) && currentAffected == 0) {
            throw new BarrierException(BarrierErrorEnum.DUPLICATED);
        }

        if (rerr == null) {
            rerr = oerr;
        }
        // null compensate
        boolean judgeOp =
                DtmConstant.OP_CANCEL.equals(this.op)
                        || DtmConstant.OP_COMPENSATE.equals(this.op)
                        || DtmConstant.OP_ROLLBACK.equals(this.op);
        if (judgeOp && originAffected > 0) {
            // repeated request or dangled request
            currentAffected = 0;
            throw new BarrierException(oerr.error());
        }
        if (rerr == null) {
            rerr = businessExecutor.execute(transaction);
        }
        if (rerr != null) {
            throw new BarrierException(rerr.error());
        }
    }

    @Override
    public String toString() {
        return "BranchBarrier{"
                + "transType='"
                + transType
                + '\''
                + ", gid='"
                + gid
                + '\''
                + ", branchID='"
                + branchId
                + '\''
                + ", op='"
                + op
                + '\''
                + ", barrierID="
                + barrierId
                + '}';
    }
}
