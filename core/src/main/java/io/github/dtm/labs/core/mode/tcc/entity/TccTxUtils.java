package io.github.dtm.labs.core.mode.tcc.entity;

import io.github.dtm.labs.core.mode.tcc.TccGlobalTx;
import io.github.dtm.labs.core.mode.tcc.impl.HttpTccGlobalTx;

/**
 * @author imyth
 */
public class TccTxUtils {
    private TccTxUtils() {}

    public static TccGlobalTx openHttpTx() {
        return new HttpTccGlobalTx();
    }
}
