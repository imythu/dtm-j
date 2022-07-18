package com.github.imythu.core.barrier;

import com.github.imythu.core.compatible.golang.ReturnVal;
import com.github.imythu.core.compatible.golang.error.Error;
import com.github.imythu.core.constant.DtmConstant;
import com.github.imythu.core.constant.ErrorConstant;
import com.github.imythu.core.domain.BarrierDO;
import com.github.imythu.core.tx.Transaction;
import com.github.imythu.core.utils.DbUtils;
import com.github.imythu.core.utils.ExceptionUtils;
import com.google.common.base.Strings;
import java.sql.Connection;
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

    public static BranchBarrier from(String transType, String gid, String branchId, String op) {
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
        return branchBarrier;
    }

    /** the same as {@link #call(Connection, BusinessExecutor)} */
    public Error callWithDb(Connection connection, BusinessExecutor businessExecutor) {
        Error error = ExceptionUtils.execute(() -> connection.setAutoCommit(false));
        if (error == null) {
            error = call(connection, businessExecutor);
        }
        return error;
    }

    /**
     * see detail description in <a
     * href="https://en.dtm.pub/practice/barrier.html">https://en.dtm.pub/practice/barrier.html</a>
     *
     * @param connection local transaction connection
     * @param businessExecutor busi func
     */
    public Error call(Connection connection, BusinessExecutor businessExecutor) {
        barrierId++;
        String bid = String.format("%02d", barrierId);
        String originOp = opMap.get(op);

        ReturnVal<Integer> originVal =
                ExceptionUtils.execute(
                        () ->
                                DbUtils.getDbSpecial(connection)
                                        .executeInsertIgnoreSql(
                                                connection,
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
                                DbUtils.getDbSpecial(connection)
                                        .executeInsertIgnoreSql(
                                                connection,
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
            return ErrorConstant.ERR_DUPLICATED;
        }

        if (rerr == null) {
            rerr = oerr;
        }
        // null compensate
        boolean judgeOp =
                DtmConstant.OP_CANCEL.equals(this.op) || DtmConstant.OP_COMPENSATE.equals(this.op) || DtmConstant.OP_ROLLBACK.equals(
                        this.op);
        if (judgeOp && originAffected > 0) {
            // repeated request or dangled request
            currentAffected = 0;
            return rerr;
        }
        if (rerr == null) {
            rerr = businessExecutor.doBusi(new Transaction(connection));
        }
        return rerr;
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
