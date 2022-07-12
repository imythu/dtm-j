package io.github.dtm.labs.core.mode.msg.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.github.dtm.labs.core.cfg.CfgHolder;
import io.github.dtm.labs.core.exception.SubmitException;
import io.github.dtm.labs.core.mode.msg.entity.Msg;
import org.junit.jupiter.api.Test;

/**
 * @author imythu
 */
class HttpMsgTransactionTest {

    @Test
    public void test() {
        Msg msg = new Msg();
        msg.setGid("3489dghffsda");
        HttpMsgTransaction msgTransaction = new HttpMsgTransaction(msg);
        msgTransaction.add("action", msg);
        msgTransaction.prepare("");
        try {
            msgTransaction.submit();
        } catch (SubmitException e) {
            throw new RuntimeException(e);
        }
    }

}
