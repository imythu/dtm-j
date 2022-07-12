package io.github.dtm.labs.core.tx;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author imythu
 */
public class Transaction {
    private final Connection connection;

    public Transaction(Connection connection) {
        this.connection = connection;
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void commit() throws SQLException {
        connection.commit();
    }
}
