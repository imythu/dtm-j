package io.github.dtm.labs.core.mode.tcc.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.dtm.labs.core.mode.tcc.impl.HttpTccGlobalTxTest.TestData;
import org.junit.jupiter.api.Test;

/**
 * @author zhuhf
 */
class JsonrpcTccGlobalTxTest {
    @Test
    public void testCommitTransactionNormally() {
        assertEquals(
                HttpTccGlobalTxTest.confirmSuccess,
                HttpTccGlobalTxTest.test(true, new TestData("1"), new JsonrpcTccGlobalTx()));
    }

    @Test
    public void testRollbackTransaction() {
        assertEquals(
                HttpTccGlobalTxTest.cancelSuccess,
                HttpTccGlobalTxTest.test(false, new TestData("2"), new JsonrpcTccGlobalTx()));
    }
}
