package io.github.dtm.labs.core.mode.tcc.entity;

import lombok.Data;

/**
 * @author imythu
 */
@Data
public class BusinessService {
    private String tryUrl;
    private String confirmUrl;
    private String cancelUrl;
    private String data;

}
