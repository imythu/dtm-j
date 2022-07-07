package io.github.dtm.labs.core.constant;

/**
 * for jsonrpc: method name
 * for http: api pathtop
 * @author imythu
 */
public interface DtmMethod {
    String NEW_GID = "newGid";
    String PREPARE = "prepare";
    String SUBMIT = "submit";
    String ABORT = "abort";
    String REGISTER_BRANCH = "registerBranch";
}
