package com.github.imythu.core.dtm.req.tcc;

import com.github.imythu.core.dtm.req.BaseRequest;
import lombok.Data;

@Data
public class RegisterBranchRequest extends BaseRequest {
    private String branchId;
    private String cancel;
    private String confirm;
    private String data;
}
