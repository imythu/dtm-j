package com.github.imythu.core.barrier;

import com.github.imythu.core.tx.Transaction;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhuhf
 */
public class DefaultTransactionImpl implements Transaction {

    private final Connection connection;

    public DefaultTransactionImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
