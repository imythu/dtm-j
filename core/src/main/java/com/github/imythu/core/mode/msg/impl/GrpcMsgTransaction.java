package com.github.imythu.core.mode.msg.impl;

import com.github.imythu.core.barrier.BusinessExecutor;
import com.github.imythu.core.barrier.BranchBarrier;
import com.github.imythu.core.exception.DoAndSubmitDbException;
import com.github.imythu.core.exception.DoAndSubmitException;
import com.github.imythu.core.exception.PrepareException;
import com.github.imythu.core.exception.SubmitException;
import com.github.imythu.core.mode.msg.MsgTransaction;
import com.github.imythu.core.mode.msg.entity.Msg;
import java.util.function.Consumer;

/**
 * @author imythu
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
    public void prepare(String queryPrepared) throws PrepareException {}

    @Override
    public void submit() throws SubmitException {}

    @Override
    public void doAndSubmitDb(String queryPrepared, BusinessExecutor businessExecutor)
            throws DoAndSubmitDbException {}

    @Override
    public void doAndSubmit(String queryPrepared, Consumer<BranchBarrier> busiCall)
            throws DoAndSubmitException {}

    @Override
    public void buildCustomOptions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Msg get() {
        return null;
    }
}
