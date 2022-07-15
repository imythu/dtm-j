package com.github.imythu.core.mode.tcc.entity;

import com.github.imythu.core.mode.tcc.TccGlobalTx;
import com.github.imythu.core.mode.tcc.impl.HttpTccGlobalTx;

/**
 * @author imyth
 */
public class TccTxUtils {
    private TccTxUtils() {}

    public static TccGlobalTx openHttpTx() {
        return new HttpTccGlobalTx();
    }
}
