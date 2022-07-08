package io.github.dtm.labs.core.mode.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imythu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TransBase extends TransOptions {
    /**
     * NOTE: unique in storage, can customize the generation rules instead of using server-side generation, it will help with the tracking
     */
    private String gid;

    private String transType;
    private transient String dtm;
    /**
     * nosql data persistence
     */
    private String customData;
    /**
     * use in MSG/SAGA
     */
    private List<Map<String, String>> steps = new ArrayList<>(0);
    /**
     * used in MSG/SAGA
     */
    private List<String> payloads = new ArrayList<>(0);

    private byte[][] binPayloads;
    /**
     * used in XA/TCC
     */
    private transient BranchIDGen branchIDGen;
    /**
     * used in XA/TCC
     */
    private transient String op;
    /**
     * used in MSG
     */
    private String queryPrepared;

    private String protocol;
}
