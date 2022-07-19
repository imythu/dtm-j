package com.github.imythu.core.mode.tcc.impl;

import com.github.imythu.core.constant.DtmConstant;
import com.github.imythu.core.constant.HttpApis;
import com.github.imythu.core.dtm.utils.HttpClients;
import com.github.imythu.core.dtm.req.tcc.AbortRequest;
import com.github.imythu.core.dtm.req.tcc.PrepareRequest;
import com.github.imythu.core.dtm.req.tcc.SubmitRequest;
import com.github.imythu.core.dtm.req.tcc.RegisterBranchRequest;
import com.github.imythu.core.dtm.res.tcc.AbortResponse;
import com.github.imythu.core.dtm.res.BaseResponse;
import com.github.imythu.core.dtm.res.NewGidResponse;
import com.github.imythu.core.dtm.res.tcc.PrepareResponse;
import com.github.imythu.core.dtm.res.tcc.RegisterBranchResponse;
import com.github.imythu.core.dtm.res.tcc.SubmitResponse;
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
