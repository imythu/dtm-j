package com.github.imythu.core.barrier;

import com.github.imythu.core.domain.BarrierDO;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * db specific operations
 *
 * @author imythu
 */
public interface DbSpecial {
    int executeInsertIgnoreSql(Connection connection, BarrierDO barrierDO) throws SQLException;

    String getXaSql(String command, String xid);

    String dbName();
}
