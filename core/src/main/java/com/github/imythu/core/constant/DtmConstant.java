package com.github.imythu.core.constant;

/**
 * @author imythu
 */
public interface DtmConstant {
    /** for result of a trans/trans branch Same as HTTP status 409 and GRPC code 10 */
    String RESULT_FAILURE = "FAILURE";
    /** ResultSuccess for result of a trans/trans branch Same as HTTP status 200 and GRPC code 0 */
    String RESULT_SUCCESS = "SUCCESS";
    /** ResultOngoing for result of a trans/trans branch Same as HTTP status 425 and GRPC code 9 */
    String RESULT_ONGOING = "ONGOING";

    /** OpTry branch type for TCC */
    String OP_TRY = "try";
    /** OpConfirm branch type for TCC */
    String OP_CONFIRM = "confirm";
    /** OpCancel branch type for TCC */
    String OP_CANCEL = "cancel";
    /** OpAction branch type for message, SAGA, XA */
    String OP_ACTION = "action";
    /** OpCompensate branch type for SAGA */
    String OP_COMPENSATE = "compensate";
    /** OpCommit branch type for XA */
    String OP_COMMIT = "commit";
    /** OpRollback branch type for XA */
    String OP_ROLLBACK = "rollback";
    /** DBTypeRedis const for driver redis */
    String DB_TYPE_REDIS = "redis";
    /** Jrpc const for json-rpc */
    String JRPC = "json-rpc";
    /** JrpcCodeFailure const for json-rpc failure */
    int JRPC_CODE_FAILURE = -32901;

    /** JrpcCodeOngoing const for json-rpc ongoing */
    int JRPC_CODE_ONGOING = -32902;

    /** MsgDoBranch0 const for DoAndSubmit barrier branch */
    String MSG_DO_BRANCH_0 = "00";
    /** MsgDoBarrier1 const for DoAndSubmit barrier barrierID */
    String MSG_DO_BARRIER_1 = "01";
    /** MsgDoOp const for DoAndSubmit barrier op */
    String MSG_DO_OP = "msg";

    /** XaBarrier1 const for xa barrier id */
    String XA_BARRIER_1 = "01";

    /** ProtocolGRPC const for protocol grpc */
    String PROTOCOL_GRPC = "grpc";
    /** ProtocolHTTP const for protocol http */
    String PROTOCOL_HTTP = "http";

    int HTTP_STATUS_TOO_EARLY = 425;
}
