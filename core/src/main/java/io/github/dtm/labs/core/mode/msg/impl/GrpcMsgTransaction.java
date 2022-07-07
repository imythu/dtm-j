package io.github.dtm.labs.core.mode.msg.impl;

import io.github.dtm.labs.core.barrier.BarrierBusiFunc;
import io.github.dtm.labs.core.barrier.BranchBarrier;
import io.github.dtm.labs.core.exception.DoAndSubmitDbException;
import io.github.dtm.labs.core.exception.DoAndSubmitException;
import io.github.dtm.labs.core.exception.PrepareException;
import io.github.dtm.labs.core.exception.SubmitException;
import io.github.dtm.labs.core.mode.msg.MsgTransaction;
import io.github.dtm.labs.core.mode.msg.entity.Msg;
import org.hibernate.Session;

import javax.naming.OperationNotSupportedException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author myth
 */
public class GrpcMsgTransaction implements MsgTransaction<Msg> {
    @Override
    public Msg add(String action, Object payload) {
        return null;
    }

    @Override
    public Msg setDelay(long delay) {
        return null;
    }

    @Override
    public void prepare(String queryPrepared) throws PrepareException {

    }

    @Override
    public void submit() throws SubmitException {

    }

    @Override
    public void doAndSubmitDb(String queryPrepared, Session session, BarrierBusiFunc barrierBusiFunc) throws DoAndSubmitDbException {

    }

    @Override
    public void doAndSubmit(String queryPrepared, Consumer<BranchBarrier> busiCall) throws DoAndSubmitException {

    }

    @Override
    public void buildCustomOptions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Msg get() {
        return null;
    }
}
