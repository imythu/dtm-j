package io.github.dtm.labs.core.barrier;

import com.google.common.base.Strings;
import io.github.dtm.labs.core.compatible.golang.ReturnVal;
import io.github.dtm.labs.core.compatible.golang.error.Error;
import io.github.dtm.labs.core.constant.DtmConstant;
import io.github.dtm.labs.core.constant.ErrorConstant;
import io.github.dtm.labs.core.domain.BarrierDO;
import io.github.dtm.labs.core.tx.Transaction;
import io.github.dtm.labs.core.utils.DbUtils;
import io.github.dtm.labs.core.utils.ExceptionUtils;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author imythu
 */
public class BranchBarrier {
    private static final Logger logger = LoggerFactory.getLogger(BranchBarrier.class);
    private String transType;
    private String gid;
    private String branchID;
    private String op;
    private int barrierID;

    public static BranchBarrier from(String transType, String gid, String branchId, String op) {
        if (Strings.isNullOrEmpty(transType)
                || Strings.isNullOrEmpty(gid)
                || Strings.isNullOrEmpty(branchId)
                || Strings.isNullOrEmpty(op)) {
            throw new IllegalArgumentException("param cannot is null or empty");
        }
        BranchBarrier branchBarrier = new BranchBarrier();
        branchBarrier.setTransType(transType);
        branchBarrier.setGid(gid);
        branchBarrier.setBranchID(branchId);
        branchBarrier.setOp(op);
        return branchBarrier;
    }

    public String getTransType() {
        return transType;
    }

    public BranchBarrier setTransType(String transType) {
        this.transType = transType;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public BranchBarrier setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public String getBranchID() {
        return branchID;
    }

    public BranchBarrier setBranchID(String branchID) {
        this.branchID = branchID;
        return this;
    }

    public String getOp() {
        return op;
    }

    public BranchBarrier setOp(String op) {
        this.op = op;
        return this;
    }

    public int getBarrierID() {
        return barrierID;
    }

    public BranchBarrier setBarrierID(int barrierID) {
        this.barrierID = barrierID;
        return this;
    }

    /** the same as {@link #call(Connection, BarrierBusiFunc)} */
    public Error callWithDb(Connection connection, BarrierBusiFunc barrierBusiFunc) {
        Error error = ExceptionUtils.execute(() -> connection.setAutoCommit(false));
        if (error == null) {
            error = call(connection, barrierBusiFunc);
        }
        return error;
    }

    /**
     * see detail description in <a
     * href="https://en.dtm.pub/practice/barrier.html">https://en.dtm.pub/practice/barrier.html</a>
     *
     * @param connection local transaction connection
     * @param barrierBusiFunc busi func
     */
    public Error call(Connection connection, BarrierBusiFunc barrierBusiFunc) {
        barrierID++;
        String bid = String.format("%02d", barrierID);
        String originOp = null;
        if (DtmConstant.OP_CANCEL.equals(op)) {
            originOp = DtmConstant.OP_TRY;
        } else if (DtmConstant.OP_COMPENSATE.equals(op)) {
            originOp = DtmConstant.OP_ACTION;
        }

        String finalOriginOp = originOp;
        ReturnVal<Integer> originVal =
                ExceptionUtils.execute(
                        () ->
                                DbUtils.getDbSpecial(connection)
                                        .executeInsertIgnoreSql(
                                                connection,
                                                new BarrierDO()
                                                        .setTransType(transType)
                                                        .setGid(gid)
                                                        .setBranchId(branchID)
                                                        .setOp(finalOriginOp)
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
                                                        .setBranchId(branchID)
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
        boolean isCancelOrComPensateOp =
                DtmConstant.OP_CANCEL.equals(op) || DtmConstant.OP_COMPENSATE.equals(op);
        if (isCancelOrComPensateOp && originAffected > 0) {
            currentAffected = 0;
        }
        if (rerr == null) {
            rerr = barrierBusiFunc.doBusi(new Transaction(connection));
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
                + branchID
                + '\''
                + ", op='"
                + op
                + '\''
                + ", barrierID="
                + barrierID
                + '}';
    }
}
