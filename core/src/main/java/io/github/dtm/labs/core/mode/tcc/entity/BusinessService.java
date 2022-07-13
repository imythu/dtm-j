package io.github.dtm.labs.core.mode.tcc.entity;

import java.net.http.HttpRequest;
import lombok.Data;

/**
 * @author imythu
 */
@Data
public class BusinessService {
    private HttpRequest tryRequest;
    private String  confirmRequest;
    private String  cancelRequest;
    private String confirmAndCancelRequestData;
}
