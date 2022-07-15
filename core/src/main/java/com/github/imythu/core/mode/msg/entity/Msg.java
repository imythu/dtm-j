package com.github.imythu.core.mode.msg.entity;

import com.github.imythu.core.mode.base.TransBase;
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
