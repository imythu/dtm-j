package com.github.imythu.core.cfg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imythu
 */
public class DtmProperties {
    private List<String> httpServer = new ArrayList<>(0);
    private List<String> grpcServer = new ArrayList<>(0);
    private DbProperties db;

    public List<String> getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(List<String> httpServer) {
        this.httpServer = httpServer;
    }

    public List<String> getGrpcServer() {
        return grpcServer;
    }

    public void setGrpcServer(List<String> grpcServer) {
        this.grpcServer = grpcServer;
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
                "httpServer=" + httpServer +
                ", grpcServer=" + grpcServer +
                ", db=" + db +
                '}';
    }
}
