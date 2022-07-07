package io.github.dtm.labs.core.barrier;

import io.github.dtm.labs.core.exception.BarrierException;
import java.sql.Connection;
import org.hibernate.Transaction;

/**
 * type for busi func
 * @author imythu
 */
@FunctionalInterface
public interface BarrierBusiFunc {
    /**
     * type for busi func
     * @param connection for transactional operations
     * @throws BarrierException BarrierException
     */
    void doBusi(Connection connection) throws BarrierException;
}
