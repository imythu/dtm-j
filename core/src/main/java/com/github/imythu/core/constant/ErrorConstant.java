package com.github.imythu.core.constant;

import com.github.imythu.core.compatible.golang.error.Errors;
import com.github.imythu.core.compatible.golang.error.Error;
import java.util.Collections;
import java.util.Map;

/**
 * @author imythu
 */
public interface ErrorConstant {

    /** HTTP result of SUCCESS */
    Map<String, Object> MAP_SUCCESS =
            Collections.singletonMap("dtm_result", DtmConstant.RESULT_SUCCESS);

    /** HTTP result of FAILURE */
    Map<String, Object> MAP_FAILURE =
            Collections.singletonMap("dtm_result", DtmConstant.RESULT_FAILURE);

    /** error for returned failure */
    Error ERR_FAILURE = Errors.newError("FAILURE");

    /** error for returned ongoing */
    Error ERR_ONGOING = Errors.newError("ONGOING");

    /**
     * error of DUPLICATED for only msg if QueryPrepared executed before call. then DoAndSubmit
     * return this error
     */
    Error ERR_DUPLICATED = Errors.newError("DUPLICATED");
}
