package com.github.imythu.core.domain;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author imythu
 */
public class BarrierDO {
    private long id;
    private String transType;
    private String gid;
    private String branchId;
    private String op;
    private String barrierId;
    private String reason;
    private Timestamp createTime;
    private Timestamp updateTime;

    public long getId() {
        return id;
    }

    public BarrierDO setId(long id) {
        this.id = id;
        return this;
    }

    public String getTransType() {
        return transType;
    }

    public BarrierDO setTransType(String transType) {
        this.transType = transType;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public BarrierDO setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public String getBranchId() {
        return branchId;
    }

    public BarrierDO setBranchId(String branchId) {
        this.branchId = branchId;
        return this;
    }

    public String getOp() {
        return op;
    }

    public BarrierDO setOp(String op) {
        this.op = op;
        return this;
    }

    public String getBarrierId() {
        return barrierId;
    }

    public BarrierDO setBarrierId(String barrierId) {
        this.barrierId = barrierId;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public BarrierDO setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public BarrierDO setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
        return this;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public BarrierDO setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "BarrierDO{"
                + "id="
                + id
                + ", transType='"
                + transType
                + '\''
                + ", gid='"
                + gid
                + '\''
                + ", branchId='"
                + branchId
                + '\''
                + ", op='"
                + op
                + '\''
                + ", barrierId='"
                + barrierId
                + '\''
                + ", reason='"
                + reason
                + '\''
                + ", createTime="
                + createTime
                + ", updateTime="
                + updateTime
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BarrierDO barrierDo = (BarrierDO) o;

        if (id != barrierDo.id) {
            return false;
        }
        if (!Objects.equals(transType, barrierDo.transType)) {
            return false;
        }
        if (!Objects.equals(gid, barrierDo.gid)) {
            return false;
        }
        if (!Objects.equals(branchId, barrierDo.branchId)) {
            return false;
        }
        if (!Objects.equals(op, barrierDo.op)) {
            return false;
        }
        if (!Objects.equals(barrierId, barrierDo.barrierId)) {
            return false;
        }
        if (!Objects.equals(reason, barrierDo.reason)) {
            return false;
        }
        if (!Objects.equals(createTime, barrierDo.createTime)) {
            return false;
        }
        return Objects.equals(updateTime, barrierDo.updateTime);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (transType != null ? transType.hashCode() : 0);
        result = 31 * result + (gid != null ? gid.hashCode() : 0);
        result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
        result = 31 * result + (op != null ? op.hashCode() : 0);
        result = 31 * result + (barrierId != null ? barrierId.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
