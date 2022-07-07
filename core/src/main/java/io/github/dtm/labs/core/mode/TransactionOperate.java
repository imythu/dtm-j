package io.github.dtm.labs.core.mode;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author myth
 */
public class TransactionOperate {
    private final Connection connection;

    public TransactionOperate(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }


    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }
}
