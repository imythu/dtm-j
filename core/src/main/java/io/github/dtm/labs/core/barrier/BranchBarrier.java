package io.github.dtm.labs.core.barrier;

import com.google.common.base.Strings;
import io.github.dtm.labs.core.constant.DtmConstant;
import java.sql.PreparedStatement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.Service;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author imythu
 */
public class BranchBarrier {
    private String transType;
    private String gid;
    private String branchID;
    private String op;
    private int barrierID;
    /**
     * {@link io.github.dtm.labs.core.constant.DtmConstant#DB_TYPE_MYSQL} or
     * {@link io.github.dtm.labs.core.constant.DtmConstant#DB_TYPE_POSTGRES}
     */
    private String dbType;
    private String barrierTableName;

    public static BranchBarrier from(String transType, String gid, String branchId, String op) {
        if (Strings.isNullOrEmpty(transType) || Strings.isNullOrEmpty(gid) || Strings.isNullOrEmpty(branchId) || Strings.isNullOrEmpty(op)) {
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

    public String getDbType() {
        return dbType;
    }

    public BranchBarrier setDbType(String dbType) {
        this.dbType = dbType;
        return this;
    }

    public String getBarrierTableName() {
        return barrierTableName;
    }

    public BranchBarrier setBarrierTableName(String barrierTableName) {
        this.barrierTableName = barrierTableName;
        return this;
    }

    @Override
    public String toString() {
        return "BranchBarrier{" +
                "transType='" + transType + '\'' +
                ", gid='" + gid + '\'' +
                ", branchID='" + branchID + '\'' +
                ", op='" + op + '\'' +
                ", barrierID=" + barrierID +
                ", dbType='" + dbType + '\'' +
                ", barrierTableName='" + barrierTableName + '\'' +
                '}';
    }

    /**
     * the same as {@link #call(Connection, BarrierBusiFunc)}
     */
    public void callWithDb(Connection connection, BarrierBusiFunc barrierBusiFunc) throws SQLException {
                call(connection, barrierBusiFunc);


    }

    /**
     * see detail description in <a href="https://en.dtm.pub/practice/barrier.html">https://en.dtm.pub/practice/barrier.html</a>
     * @param connection local transaction connection
     * @param barrierBusiFunc busi func
     * @throws SQLException
     */
    public void call(Connection connection, BarrierBusiFunc barrierBusiFunc) throws SQLException {

        try (connection) {
            barrierID++;
            String bid = String.format("%02d", barrierID);
            String originOp = null;
            if (DtmConstant.OP_CANCEL.equals(op)) {
                originOp = DtmConstant.OP_TRY;
            } else if (DtmConstant.OP_COMPENSATE.equals(op)) {
                originOp = DtmConstant.OP_ACTION;
            }

            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement();
            statement.executeUpdate();
            int originAffected;
        } catch (Exception e) {
        }

    }
}
