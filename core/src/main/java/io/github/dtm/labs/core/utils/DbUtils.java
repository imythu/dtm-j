package io.github.dtm.labs.core.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.dtm.labs.core.cfg.CfgHolder;
import io.github.dtm.labs.core.cfg.DbProperties;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ServiceLoader;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author imythu
 */
public class DbUtils {
    private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);
    private static volatile DataSource dataSource;

    private DbUtils() {}

    public static Connection getConnection() {
        try {
            initDataSource();
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface DbConnectionCreator {
        DataSource create(DbProperties dbProperties);
    }

    static class DefaultDbConnectionCreator implements DbConnectionCreator {
        @Override
        public DataSource create(DbProperties dbProperties) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbProperties.getUrl());
            config.setUsername(dbProperties.getUsername());
            config.setPassword(dbProperties.getPassword());
            return new HikariDataSource(config);
        }
    }

    private static void initDataSource() {
        if (dataSource == null) {
            synchronized (DbUtils.class) {
                if (dataSource == null) {
                    Optional<DbConnectionCreator> first = ServiceLoader.load(DbConnectionCreator.class)
                            .findFirst();
                    DbProperties dbProperties = CfgHolder.getDtmProperties().getDb();
                    if (first.isPresent()) {
                        DbConnectionCreator connectionCreator = first.get();
                        logger.info("Use DbConnectionCreator: {}.", connectionCreator.getClass().getName());
                        dataSource = connectionCreator.create(dbProperties);
                    } else {
                        logger.info("Use the default DbConnectionCreator.");
                        dataSource = new DefaultDbConnectionCreator().create(dbProperties);
                    }
                }
            }
        }
    }

}
