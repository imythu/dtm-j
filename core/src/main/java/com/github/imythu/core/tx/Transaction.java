package com.github.imythu.core.tx;

import com.github.imythu.core.barrier.DefaultTransactionImpl;
import com.github.imythu.core.utils.DbUtils;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author imythu
 */
public interface Transaction {

    static Transaction getDefaultTransaction() {
        return new DefaultTransactionImpl(DbUtils.getConnection());
    }

    void begin() throws SQLException;

    void rollback() throws SQLException;

    void commit() throws SQLException;

    Connection getConnection();
}
