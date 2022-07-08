package io.github.dtm.labs.core.utils;

import com.google.common.base.Strings;
import io.github.dtm.labs.core.constant.DtmConstant;
import io.github.dtm.labs.core.constant.GlobalTransactionType;
import io.github.dtm.labs.core.exception.DtmFailureException;
import io.github.dtm.labs.core.exception.DtmOngoingException;
import io.github.dtm.labs.core.exception.PrepareException;
import io.github.dtm.labs.core.jsonrpc.Request;
import io.github.dtm.labs.core.mode.base.TransBase;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kong.unirest.HttpResponse;
import kong.unirest.JsonObjectMapper;
import kong.unirest.Unirest;

/**
 * @author imythu
 */
public class TransBaseUtils {

    private TransBaseUtils() {}

    static {
        Unirest.config()
                .setObjectMapper(new JsonObjectMapper())
                .setDefaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }

    /**
     * call dtm
     */
    public static void transCallDtm(TransBase t, Object body, String methodOrPath) {
        try {
            if (t.getRequestTimeout() != 0) {
                Unirest.config().connectTimeout((int) TimeUnit.SECONDS.toMillis(t.getRequestTimeout()));
            }
            if (DtmConstant.JRPC.equals(t.getProtocol())) {
                HttpResponse<String> response = Unirest.post(t.getDtm())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .body(JsonUtils.toJson(Request.<Object>builder()
                                .setId("no-use")
                                .setMethod(methodOrPath)
                                .setParams(body)
                                .build()))
                        .asString();
                String jsonStr = response.getBody();
                Map<String, Object> result = JsonUtils.toObj(jsonStr);
                if (response.getStatus() != Response.Status.OK.getStatusCode() || result.get("error") != null) {
                    throw new PrepareException(jsonStr);
                }
                return;
            }
            HttpResponse<String> response = Unirest.post(String.format("%s/%s", t.getDtm(), methodOrPath))
                    .body(body)
                    .asString();
            String bodyStr = response.getBody();
            if (response.getStatus() != Response.Status.OK.getStatusCode()
                    || bodyStr.contains(DtmConstant.RESULT_FAILURE)) {
                throw new PrepareException(bodyStr);
            }
        } catch (Exception e) {
            throw new PrepareException(e);
        }
    }

    /**
     * request branch result
     */
    public static HttpResponse<String> transRequestBranch(
            TransBase t, String method, Object body, String branchId, String op, String url) {
        if (Strings.isNullOrEmpty(url)) {
            throw new IllegalArgumentException("url cannot is null or empty");
        }
        Map<String, Object> query = new HashMap<>(6);
        query.put("dtm", t.getDtm());
        query.put("gid", t.getGid());
        query.put("branch_id", t.getBranchIDGen().getBranchID());
        query.put("trans_type", t.getTransType());
        query.put("op", t.getOp());
        if (GlobalTransactionType.XA.equals(t.getTransType())) {
            query.put("phase2_url", url);
        }
        HttpResponse<String> response = Unirest.request(method, url)
                .body(body)
                .queryString(query)
                .headers(t.getBranchHeaders())
                .asString();
        transformAndThrowException(response);
        return response;
    }

    private static void transformAndThrowException(HttpResponse<String> response) {
        int status = response.getStatus();
        String str = response.getBody();
        if (status == DtmConstant.HTTP_STATUS_TOO_EARLY || str.contains(DtmConstant.RESULT_ONGOING)) {
            throw new DtmOngoingException(String.format("%s. %s", str, DtmConstant.RESULT_ONGOING));
        } else if (Response.Status.CONFLICT.getStatusCode() == status || str.contains(DtmConstant.RESULT_FAILURE)) {
            throw new DtmFailureException(String.format("%s. %s", str, DtmConstant.RESULT_FAILURE));
        } else if (status != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException(str);
        }
    }
}
