package com.github.imythu.core.mode.barrier;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author imythu
 */
public class TxOp {
    private final Connection connection;

    public TxOp(Connection connection) throws SQLException {
        this.connection = connection;
    }

    public void beginTx() {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }
}
