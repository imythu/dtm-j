package io.github.dtm.labs.core.dtm.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
public class SubmitRequest extends BaseRequest{
    /**
     * 指定整个事务会分成多个步骤，每个步骤的正向操作 url 为 action
     */
    private List<Map<String, String>> steps;
    /**
     * 表示每个步骤中进行http请求时的body
     */
    private List<String> payloads;
}
