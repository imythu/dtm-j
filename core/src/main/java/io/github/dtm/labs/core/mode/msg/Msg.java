package io.github.dtm.labs.core.mode.msg;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.dtm.labs.core.mode.base.TransBase;

/**
 * @author imythu
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Msg extends TransBase {
    private long delay;
}
