package io.github.dtm.labs.core.constant;

import io.github.dtm.labs.core.cfg.CfgHolder;

/**
 * @author zhuhf
 */
public interface HttpApis {
    String API_PREFIX = "http://"
            + CfgHolder.getDtmProperties().getServer().stream().findAny().orElseThrow() + "/api/dtmsvr";
    String API_NEW_GID = API_PREFIX + "/newGid";
    String API_PREPARE = API_PREFIX + "/prepare";
    String API_SUBMIT = API_PREFIX + "/submit";
    String API_ABORT = API_PREFIX + "/abort";
    String API_REGISTER_BRANCH = API_PREFIX + "/registerBranch";
    String API_QUERY = API_PREFIX + "/query";
    String API_ALL = API_PREFIX + "/all";

}
