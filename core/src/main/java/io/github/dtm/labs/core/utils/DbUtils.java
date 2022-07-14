package io.github.dtm.labs.core.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.dtm.labs.core.barrier.DbSpecial;
import io.github.dtm.labs.core.cfg.CfgHolder;
import io.github.dtm.labs.core.cfg.DbProperties;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author imythu
 */
public class DbUtils {
    private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);
    private static volatile DataSource dataSource;

    private static final ConcurrentMap<String, DbSpecial> DB_SPECIAL_MAP =
            new ConcurrentHashMap<>();

    private DbUtils() {}

    public static Connection getConnection() {
        try {
            initDataSource();
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void put(String dbType, DbSpecial dbSpecial) {
        DB_SPECIAL_MAP.put(dbType, dbSpecial);
    }

    public static DbSpecial getDbSpecial(Connection connection) throws SQLException {
        return DB_SPECIAL_MAP.get(connection.getMetaData().getDatabaseProductName());
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
                    Optional<DbConnectionCreator> first =
                            ServiceLoader.load(DbConnectionCreator.class).findFirst();
                    DbProperties dbProperties = CfgHolder.getDtmProperties().getDb();
                    if (first.isPresent()) {
                        DbConnectionCreator connectionCreator = first.get();
                        logger.info(
                                "Use DbConnectionCreator: {}.",
                                connectionCreator.getClass().getName());
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
