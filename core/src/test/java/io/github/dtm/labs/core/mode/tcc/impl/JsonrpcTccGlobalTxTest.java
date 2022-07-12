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
        HttpTccGlobalTxTest.listening();
        assertEquals(
                HttpTccGlobalTxTest.confirmSuccess,
                HttpTccGlobalTxTest.test(true, new TestData("1"), new JsonrpcTccGlobalTx()));
        HttpTccGlobalTxTest.httpServer.stop(5);
    }

    @Test
    public void testRollbackTransaction() {
        HttpTccGlobalTxTest.listening();
        assertEquals(
                HttpTccGlobalTxTest.cancelSuccess,
                HttpTccGlobalTxTest.test(false, new TestData("2"), new JsonrpcTccGlobalTx()));
        HttpTccGlobalTxTest.httpServer.stop(5);
    }
}
