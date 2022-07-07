package io.github.dtm.labs.core.mode.msg.entity;

import io.github.dtm.labs.core.mode.base.TransBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imythu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Msg extends TransBase {
    private long delay;
}
