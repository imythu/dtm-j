package io.github.dtm.labs.core.mode.msg;

import io.github.dtm.labs.core.barrier.BarrierBusiFunc;
import io.github.dtm.labs.core.barrier.BranchBarrier;
import io.github.dtm.labs.core.exception.DoAndSubmitDbException;
import io.github.dtm.labs.core.exception.DoAndSubmitException;
import io.github.dtm.labs.core.exception.PrepareException;
import io.github.dtm.labs.core.exception.SubmitException;
import io.github.dtm.labs.core.mode.msg.entity.Msg;
import io.github.dtm.labs.core.utils.JsonUtils;
import org.hibernate.Session;

import java.sql.Connection;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * @author imythu
 */
public interface MsgTransaction<T extends Msg> {
    /**
     * add a new step
     *
     * @param action
     * @param payload
     * @return
     */
    T add(String action, Object payload);

    /**
     * delay call branch, unit second
     *
     * @param delay
     * @return
     */
    T setDelay(long delay);

    /**
     * prepare the msg, msg will later be submitted
     *
     * @param queryPrepared
     * @throws PrepareException while failed
     */
    void prepare(String queryPrepared) throws PrepareException;

    /**
     * submit the msg
     *
     * @throws SubmitException
     */
    void submit() throws SubmitException;

    /**
     * short method for Do on db type. please see {@link #doAndSubmit}
     * @param queryPrepared
     * @param session
     * @param barrierBusiFunc
     * @throws DoAndSubmitDbException
     */
    void doAndSubmitDb(String queryPrepared, Session session, BarrierBusiFunc barrierBusiFunc) throws DoAndSubmitDbException;

    /**
     * one method for the entire prepare->busi->submit
     * the error returned by busiCall will be returned
     * if busiCall return ErrFailure, then abort is called directly
     * if busiCall return not nil error other than ErrFailure, then DoAndSubmit will call queryPrepared to get the result
     * @param queryPrepared
     * @param busiCall
     * @throws DoAndSubmitException
     */
    void doAndSubmit(String queryPrepared, Consumer<BranchBarrier> busiCall) throws DoAndSubmitException;

    /**
     * add custom options to the request context
     */
    void buildCustomOptions();

    /**
     * Returns the <strong>msg</strong>> instance bound to this action method
     *
     * @return T
     */
    T get();
}
