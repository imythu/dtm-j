package com.github.imythu.core.dtm.req.tcc;

import com.github.imythu.core.dtm.req.BaseRequest;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author imythu
 */
@Data
public class PrepareRequest extends BaseRequest {
    /** 指定整个事务会分成多个步骤，每个步骤的正向操作 url 为 action */
    private List<Map<String, String>> steps;
    /** 表示每个步骤中进行http请求时的body */
    private List<String> payloads;
    /** 指定事务消息超时后进行查询的url */
    private String queryPrepared;
}
