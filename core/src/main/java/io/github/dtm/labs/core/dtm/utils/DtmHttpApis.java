package io.github.dtm.labs.core.dtm.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.dtm.labs.core.cfg.CfgHolder;
import io.github.dtm.labs.core.constant.DtmConstant;
import io.github.dtm.labs.core.dtm.req.AbortRequest;
import io.github.dtm.labs.core.dtm.req.PrepareRequest;
import io.github.dtm.labs.core.dtm.req.tcc.RegisterBranchRequest;
import io.github.dtm.labs.core.dtm.req.SubmitRequest;
import io.github.dtm.labs.core.dtm.res.*;
import kong.unirest.GenericType;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

public class DtmHttpApis {
    private DtmHttpApis(){}
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    public static final String apiPrefix = "http://" + CfgHolder.getDtmProperties().getDtmServer().stream().findAny().orElseThrow() + "/api/dtmsvr";
    public static final String newGid = apiPrefix + "/newGid";
    public static final String prepare  = apiPrefix + "/prepare";
    public static final String submit   = apiPrefix + "/submit";
    public static final String abort    = apiPrefix + "/abort";
    public static final String registerBranch    = apiPrefix + "/registerBranch";
    public static final String query     = apiPrefix + "/query";
    public static final String all     = apiPrefix + "/all";

    static {
        Unirest.config().setObjectMapper(
                new ObjectMapper() {
                    @Override
                    public <T> T readValue(String s, Class<T> aClass) {
                        return gson.fromJson(s, new TypeToken<T>(){}.getType());
                    }

                    @Override
                    public String writeValue(Object o) {
                        return gson.toJson(o);
                    }
                }
        );
    }

    public static String newGid() {
        NewGidResponse newGidResponse = Unirest.get(newGid)
                .asObject(NewGidResponse.class)
                .getBody();
        boolean success = isSuccess(newGidResponse);
        if (!success) {
            throw new RuntimeException(String.format("new gid failed! response: %s", newGidResponse));
        }
        return newGidResponse.getGid();
    }

    public static boolean prepare(PrepareRequest request) {
        BaseResponse response = post(request, prepare);
        return isSuccess(response);
    }

    public static boolean submit(SubmitRequest submitRequest) {
        SubmitResponse submitResponse = post(submitRequest, submit);
        return isSuccess(submitResponse);
    }

    public static boolean abort(AbortRequest abortRequest) {
        AbortResponse abortResponse = post(abortRequest, abort);
        return isSuccess(abortResponse);
    }

    public static boolean registerBranch(RegisterBranchRequest request) {
        RegisterBranchResponse response = post(request, registerBranch);
        return isSuccess(response);
    }

    public static boolean isSuccess(BaseResponse baseResponse) {
        return DtmConstant.RESULT_SUCCESS.equals(baseResponse.getDtmResult());
    }

    private static <T> T post(Object body, String api) {
        return Unirest.post(api)
                .body(body)
                .asObject(new GenericType<T>() {
                }).getBody();
    }
}
