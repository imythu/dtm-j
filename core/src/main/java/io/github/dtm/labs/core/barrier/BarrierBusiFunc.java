package io.github.dtm.labs.core.barrier;

import io.github.dtm.labs.core.exception.BarrierException;
import org.hibernate.Transaction;

/**
 * type for busi func
 * @author myth
 */
@FunctionalInterface
public interface BarrierBusiFunc {
    /**
     * type for busi func
     * @param transaction for transactional operations
     * @throws BarrierException BarrierException
     */
    void doBusi(Transaction transaction) throws BarrierException;
}