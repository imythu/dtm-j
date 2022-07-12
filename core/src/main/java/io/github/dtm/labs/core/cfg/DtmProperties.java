package io.github.dtm.labs.core.cfg;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author imythu
 */
public class DtmProperties {
    private List<String> server = new ArrayList<>(0);
    private DbProperties db;

    public List<String> getServer() {
        return server;
    }

    public void setServer(List<String> server) {
        this.server = server;
    }

    public DbProperties getDb() {
        return db;
    }

    public void setDb(DbProperties db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return "DtmProperties{" +
                "server=" + server +
                ", db=" + db +
                '}';
    }
}
