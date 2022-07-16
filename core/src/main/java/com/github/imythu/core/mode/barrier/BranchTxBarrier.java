package com.github.imythu.core.mode.barrier;

import com.github.imythu.core.exception.BarrierException;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 子事务屏障
 * @see <a href="https://dtm.pub/practice/barrier.html#%E5%AD%90%E4%BA%8B%E5%8A%A1%E5%B1%8F%E9%9A%9C">异常与子事务屏障</a>
 * @author imythu
 */
public class BranchTxBarrier {
    private static void call() throws BarrierException {

    }
    public static<T> void callWithDb(TxOp txOp, Supplier<T> businessAction) throws BarrierException {


    }

    

}
