package com.github.imythu.core.barrier.impl;

import com.github.imythu.core.barrier.DbSpecial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author imythu
 */
public abstract class AbstractDbSpecial implements DbSpecial {
    private volatile PreparedStatement preparedStatement;
    private final Object lock = new Object();

    protected PreparedStatement getOrCreate(Connection connection, String sql) throws SQLException {
        if (preparedStatement == null) {
            synchronized (lock) {
                if (preparedStatement == null) {
                    preparedStatement = connection.prepareStatement(sql);
                }
            }
        }
        return preparedStatement;
    }
}
