package com.github.imythu.core.mode.tcc.impl;

import com.github.imythu.core.constant.DtmConstant;
import com.github.imythu.core.dtm.req.AbortRequest;
import com.github.imythu.core.dtm.req.PrepareRequest;
import com.github.imythu.core.dtm.req.SubmitRequest;
import com.github.imythu.core.dtm.req.tcc.RegisterBranchRequest;
import com.github.imythu.core.mode.tcc.impl.grpc.MultiAddressNameResolverProvider;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import io.grpc.StatusRuntimeException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhuhf
 */
public class GrpcTccGlobalTx extends AbstractTccGlobalTx {
    private static final Logger logger = LoggerFactory.getLogger(GrpcTccGlobalTx.class);
    private static volatile DtmGrpc.DtmBlockingStub dtmServiceStub;

    public GrpcTccGlobalTx() {
        if (dtmServiceStub == null) {
            synchronized (GrpcTccGlobalTx.class) {
                if (dtmServiceStub == null) {
                    String scheme = "listserver";
                    NameResolverRegistry.getDefaultRegistry()
                            .register(new MultiAddressNameResolverProvider());
                    ManagedChannel channel =
                            ManagedChannelBuilder.forTarget(
                                            MultiAddressNameResolverProvider.MULTI_ADDRESS_SCHEME
                                                    + "://multiaddress")
                                    .usePlaintext()
                                    .enableRetry()
                                    .build();
                    dtmServiceStub = DtmGrpc.newBlockingStub(channel);
                }
            }
        }
    }

    @Override
    protected boolean registryBranchTx(RegisterBranchRequest registerBranchRequest) {
        Map<String, String> opUrlMap = new HashMap<>(2);
        opUrlMap.put(DtmConstant.OP_CANCEL, registerBranchRequest.getCancel());
        opUrlMap.put(DtmConstant.OP_CONFIRM, registerBranchRequest.getConfirm());
        return reqBool(
                () ->
                        dtmServiceStub.registerBranch(
                                DtmBranchRequest.newBuilder()
                                        .setGid(registerBranchRequest.getGid())
                                        .setTransType(registerBranchRequest.getTransType())
                                        .setBranchID(registerBranchRequest.getBranchId())
                                        .putAllData(opUrlMap)
                                        .setBusiPayload(
                                                ByteString.copyFrom(
                                                        registerBranchRequest
                                                                .getData()
                                                                .getBytes(StandardCharsets.UTF_8)))
                                        .build()));
    }

    @Override
    protected boolean prepare(PrepareRequest prepareRequest) {

        return reqBool(
                () -> {
                    DtmRequest dtmRequest =
                            DtmRequest.newBuilder()
                                    .setGid(prepareRequest.getGid())
                                    .setTransType(prepareRequest.getTransType())
                                    .build();
                    return dtmServiceStub.prepare(dtmRequest);
                });
    }

    @Override
    protected boolean submit(SubmitRequest submitRequest) {
        return reqBool(
                () ->
                        dtmServiceStub.submit(
                                DtmRequest.newBuilder()
                                        .setGid(submitRequest.getGid())
                                        .setTransType(submitRequest.getTransType())
                                        .build()));
    }

    @Override
    protected boolean rollback(AbortRequest abortRequest) {
        return reqBool(
                () ->
                        dtmServiceStub.abort(
                                DtmRequest.newBuilder()
                                        .setGid(abortRequest.getGid())
                                        .setTransType(abortRequest.getTransType())
                                        .build()));
    }

    @Override
    protected String newGid() {
        DtmGidReply dtmGidReply = dtmServiceStub.newGid(Empty.newBuilder().build());
        return dtmGidReply.getGid();
    }

    private static boolean reqBool(Executor<Empty> executor) {
        try {
            Empty empty = executor.execute();
            return true;
        } catch (StatusRuntimeException e) {
            return false;
        }
    }

    @FunctionalInterface
    private interface Executor<T> {

        /**
         * execute
         *
         * @return ignored
         */
        Empty execute();
    }
}
