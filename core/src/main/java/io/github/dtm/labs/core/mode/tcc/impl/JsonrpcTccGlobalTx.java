package io.github.dtm.labs.core.mode.tcc.impl;

import com.google.gson.reflect.TypeToken;
import io.github.dtm.labs.core.cfg.CfgHolder;
import io.github.dtm.labs.core.constant.DtmConstant;
import io.github.dtm.labs.core.constant.JsonrpcMethods;
import io.github.dtm.labs.core.dtm.req.AbortRequest;
import io.github.dtm.labs.core.dtm.req.PrepareRequest;
import io.github.dtm.labs.core.dtm.req.SubmitRequest;
import io.github.dtm.labs.core.dtm.req.tcc.RegisterBranchRequest;
import io.github.dtm.labs.core.dtm.res.AbortResponse;
import io.github.dtm.labs.core.dtm.res.NewGidResponse;
import io.github.dtm.labs.core.dtm.res.PrepareResponse;
import io.github.dtm.labs.core.dtm.res.RegisterBranchResponse;
import io.github.dtm.labs.core.dtm.res.SubmitResponse;
import io.github.dtm.labs.core.dtm.utils.HttpClients;
import io.github.dtm.labs.core.jsonrpc.JsonrpcRequest;
import io.github.dtm.labs.core.jsonrpc.JsonrpcResponse;
import jakarta.ws.rs.HttpMethod;
import java.util.Objects;
import java.util.Random;

/**
 * @author zhuhf
 */
public class JsonrpcTccGlobalTx extends AbstractTccGlobalTx {
    public static final String API_JSONRPC =
            "http://"
                    + CfgHolder.getDtmProperties().getHttpServer().stream().findAny().orElseThrow()
                    + "/api/json-rpc";

    @Override
    protected boolean registryBranchTx(RegisterBranchRequest registerBranchRequest) {

        String id = genId();
        JsonrpcResponse<RegisterBranchResponse> response =
                HttpClients.request(
                        API_JSONRPC,
                        HttpMethod.POST,
                        JsonrpcRequest.<RegisterBranchRequest>builder()
                                .setId(id)
                                .setMethod(JsonrpcMethods.REGISTER_BRANCH)
                                .setParams(registerBranchRequest)
                                .build(),
                        new TypeToken<>() {});
        return isSuccess(response, id);
    }

    @Override
    protected boolean prepare(PrepareRequest prepareRequest) {
        String id = genId();
        JsonrpcResponse<PrepareResponse> response =
                HttpClients.request(
                        API_JSONRPC,
                        HttpMethod.POST,
                        JsonrpcRequest.<PrepareRequest>builder()
                                .setId(id)
                                .setMethod(JsonrpcMethods.PREPARE)
                                .setParams(prepareRequest)
                                .build(),
                        new TypeToken<>() {});
        return isSuccess(response, id);
    }

    @Override
    protected boolean submit(SubmitRequest submitRequest) {
        String id = genId();
        JsonrpcResponse<SubmitResponse> response =
                HttpClients.request(
                        API_JSONRPC,
                        HttpMethod.POST,
                        JsonrpcRequest.<SubmitRequest>builder()
                                .setId(id)
                                .setParams(submitRequest)
                                .setMethod(JsonrpcMethods.SUBMIT)
                                .build(),
                        new TypeToken<>() {});
        return isSuccess(response, id);
    }

    @Override
    protected boolean rollback(AbortRequest abortRequest) {
        String id = genId();
        JsonrpcResponse<AbortResponse> response =
                HttpClients.request(
                        API_JSONRPC,
                        HttpMethod.POST,
                        JsonrpcRequest.<AbortRequest>builder()
                                .setId(id)
                                .setParams(abortRequest)
                                .setMethod(JsonrpcMethods.ABORT)
                                .build(),
                        new TypeToken<>() {});
        return isSuccess(response, id);
    }

    @Override
    protected String newGid() {
        String id = genId();
        JsonrpcResponse<NewGidResponse> response =
                HttpClients.request(
                        API_JSONRPC,
                        HttpMethod.POST,
                        JsonrpcRequest.<Void>builder()
                                .setId(id)
                                .setMethod(JsonrpcMethods.NEW_GID)
                                .build(),
                        new TypeToken<>() {});
        if (isSuccess(response, id)) {
            return response.getResult().getGid();
        }

        throw new RuntimeException(String.format("new gid failed! response: %s", response));
    }

    private static <T> boolean isSuccess(JsonrpcResponse<T> jsonrpcResponse, String id) {
        return Objects.equals(id, jsonrpcResponse.getId())
                && (jsonrpcResponse.getError() == null
                        || DtmConstant.JRPC_CODE_ONGOING == jsonrpcResponse.getError().getCode());
    }

    private static String genId() {
        return String.valueOf(new Random().nextInt(1000));
    }
}
