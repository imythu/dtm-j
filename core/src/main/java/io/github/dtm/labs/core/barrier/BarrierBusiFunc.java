package io.github.dtm.labs.core.barrier;

import io.github.dtm.labs.core.compatible.golang.error.Error;
import io.github.dtm.labs.core.exception.BarrierException;
import io.github.dtm.labs.core.tx.Transaction;

/**
 * type for busi func
 * @author imythu
 */
@FunctionalInterface
public interface BarrierBusiFunc {
    /**
     * type for busi func
     * @param transaction for transactional operations
     * @throws BarrierException BarrierException
     */
    Error doBusi(Transaction transaction) throws BarrierException;
}
