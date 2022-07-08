package io.github.dtm.labs.core.barrier.impl;

import io.github.dtm.labs.core.barrier.DbSpecial;
import io.github.dtm.labs.core.constant.DbTypeConstant;
import io.github.dtm.labs.core.domain.BarrierDO;
import io.github.dtm.labs.core.utils.DbUtils;
import java.sql.Connection;

/**
 * @author zhuhf
 */
public class PostgreSQLSpecial extends AbstractDbSpecial {
    static {
        PostgreSQLSpecial special = new PostgreSQLSpecial();
        DbUtils.put(special.dbName(), special);
    }

    private static final String TEMPLATE_SQL = "insert into barrier (trans_type, gid, branch_id, op, barrier_id, reason) values (?,?,?,?,?,?)"
            + " on conflict ON CONSTRAINT uniq_barrier do nothing";

    @Override
    public int executeInsertIgnoreSql(Connection connection, BarrierDO barrierDO) {

    }

    @Override
    public String getXaSql(String command, String xid) {
        return null;
    }

    @Override
    public String dbName() {
        return DbTypeConstant.PostgreSQL;
    }
}
