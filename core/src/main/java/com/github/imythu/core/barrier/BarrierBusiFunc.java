package com.github.imythu.core.barrier;

import com.github.imythu.core.compatible.golang.error.Error;
import com.github.imythu.core.exception.BarrierException;
import com.github.imythu.core.tx.Transaction;

/**
 * type for busi func
 *
 * @author imythu
 */
@FunctionalInterface
public interface BarrierBusiFunc {
    /**
     * type for busi func
     *
     * @param transaction for transactional operations
     * @throws BarrierException BarrierException
     */
    Error doBusi(Transaction transaction) throws BarrierException;
}
