package io.github.dtm.labs.core.cfg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imythu
 */
public class DtmProperties {
    private List<String> dtmServer = new ArrayList<>(0);
    private DbProperties db;

    public List<String> getDtmServer() {
        return dtmServer;
    }

    public DtmProperties setDtmServer(List<String> dtmServer) {
        this.dtmServer = dtmServer;
        return this;
    }

    public DbProperties getDb() {
        return db;
    }

    public DtmProperties setDb(DbProperties db) {
        this.db = db;
        return this;
    }

    @Override
    public String toString() {
        return "DtmProperties{" +
                "dtmServer=" + dtmServer +
                ", db=" + db +
                '}';
    }
}
