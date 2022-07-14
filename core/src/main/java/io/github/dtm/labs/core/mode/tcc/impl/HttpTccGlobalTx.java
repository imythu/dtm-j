package io.github.dtm.labs.core.mode.tcc.impl;

import io.github.dtm.labs.core.constant.DtmConstant;
import io.github.dtm.labs.core.constant.HttpApis;
import io.github.dtm.labs.core.dtm.req.AbortRequest;
import io.github.dtm.labs.core.dtm.req.PrepareRequest;
import io.github.dtm.labs.core.dtm.req.SubmitRequest;
import io.github.dtm.labs.core.dtm.req.tcc.RegisterBranchRequest;
import io.github.dtm.labs.core.dtm.res.AbortResponse;
import io.github.dtm.labs.core.dtm.res.BaseResponse;
import io.github.dtm.labs.core.dtm.res.NewGidResponse;
import io.github.dtm.labs.core.dtm.res.PrepareResponse;
import io.github.dtm.labs.core.dtm.res.RegisterBranchResponse;
import io.github.dtm.labs.core.dtm.res.SubmitResponse;
import io.github.dtm.labs.core.dtm.utils.HttpClients;
import jakarta.ws.rs.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTccGlobalTx extends AbstractTccGlobalTx {
    private static final Logger logger = LoggerFactory.getLogger(HttpTccGlobalTx.class);

    protected boolean registryBranchTx(RegisterBranchRequest registerBranchRequest) {
        RegisterBranchResponse response =
                HttpClients.request(
                        HttpApis.API_REGISTER_BRANCH,
                        HttpMethod.POST,
                        registerBranchRequest,
                        RegisterBranchResponse.class);
        return isSuccess(response);
    }

    @Override
    protected boolean prepare(PrepareRequest prepareRequest) {
        PrepareResponse response =
                HttpClients.request(
                        HttpApis.API_PREPARE,
                        HttpMethod.POST,
                        prepareRequest,
                        PrepareResponse.class);
        return isSuccess(response);
    }

    protected boolean submit(SubmitRequest submitRequest) {
        SubmitResponse submitResponse =
                HttpClients.request(
                        HttpApis.API_SUBMIT, HttpMethod.POST, submitRequest, SubmitResponse.class);
        return isSuccess(submitResponse);
    }

    @Override
    protected boolean rollback(AbortRequest abortRequest) {
        AbortResponse abortResponse =
                HttpClients.request(
                        HttpApis.API_ABORT, HttpMethod.POST, abortRequest, AbortResponse.class);
        return isSuccess(abortResponse);
    }

    @Override
    protected String newGid() {
        NewGidResponse newGidResponse =
                HttpClients.request(
                        HttpApis.API_NEW_GID, HttpMethod.GET, null, NewGidResponse.class);
        boolean success = isSuccess(newGidResponse);
        if (success) {
            return newGidResponse.getGid();
        }
        throw new RuntimeException(String.format("new gid failed! response: %s", newGidResponse));
    }

    protected static boolean isSuccess(BaseResponse baseResponse) {
        return DtmConstant.RESULT_SUCCESS.equals(baseResponse.getDtmResult());
    }
}
