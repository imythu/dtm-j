package io.github.dtm.labs.core.constant;

import com.mysql.cj.jdbc.DatabaseMetaData;
import org.postgresql.jdbc.PgDatabaseMetaData;

/**
 * @author zhuhf
 */
public interface DbTypeConstant {

    /**
     * @see DatabaseMetaData#getDatabaseProductName()
     */
    String MySQL = "MySQL";
    /**
     * @see PgDatabaseMetaData#getDatabaseProductName()
     */
    String PostgreSQL = "PostgreSQL";
}
