package com.github.imythu.core.utils;

import com.github.imythu.core.constant.DtmConstant;
import com.github.imythu.core.constant.GlobalTransactionType;
import com.github.imythu.core.dtm.utils.HttpClients;
import com.github.imythu.core.exception.DtmFailureException;
import com.github.imythu.core.exception.DtmOngoingException;
import com.github.imythu.core.exception.PrepareException;
import com.github.imythu.core.mode.base.TransBase;
import com.google.common.base.Strings;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author imythu
 */
public class TransBaseUtils {

    private TransBaseUtils() {}

    /** call dtm */
    public static void transCallDtm(TransBase t, Object body, String methodOrPath) {
        try {
            if (DtmConstant.JRPC.equals(t.getProtocol())) {
                HttpResponse<String> response =
                        HttpClients.getResponse(t.getDtm(), HttpMethod.POST, body);
                String jsonStr = response.body();
                Map<String, Object> result = JsonUtils.toObj(jsonStr);
                if (response.statusCode() != Response.Status.OK.getStatusCode()
                        || result.get("error") != null) {
                    throw new PrepareException(jsonStr);
                }
                return;
            }
            HttpResponse<String> response =
                    HttpClients.getResponse(
                            String.format("%s/%s", t.getDtm(), methodOrPath),
                            HttpMethod.POST,
                            body);
            String bodyStr = response.body();
            if (response.statusCode() != Response.Status.OK.getStatusCode()
                    || bodyStr.contains(DtmConstant.RESULT_FAILURE)) {
                throw new PrepareException(bodyStr);
            }
        } catch (Exception e) {
            throw new PrepareException(e);
        }
    }

    /** request branch result */
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
        HttpResponse<String> response = HttpClients.getResponse(url, method, body);
        transformAndThrowException(response);
        return response;
    }

    private static void transformAndThrowException(HttpResponse<String> response) {
        int status = response.statusCode();
        String str = response.body();
        if (status == DtmConstant.HTTP_STATUS_TOO_EARLY
                || str.contains(DtmConstant.RESULT_ONGOING)) {
            throw new DtmOngoingException(String.format("%s. %s", str, DtmConstant.RESULT_ONGOING));
        } else if (Response.Status.CONFLICT.getStatusCode() == status
                || str.contains(DtmConstant.RESULT_FAILURE)) {
            throw new DtmFailureException(String.format("%s. %s", str, DtmConstant.RESULT_FAILURE));
        } else if (status != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException(str);
        }
    }
}
