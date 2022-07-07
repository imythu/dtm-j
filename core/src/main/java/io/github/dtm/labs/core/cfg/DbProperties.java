package io.github.dtm.labs.core.cfg;

public class DbProperties {

    /**
     * JDBC URL of the database.
     */
    private String url;

    /**
     * Login username of the database.
     */
    private String username;

    /**
     * Login password of the database.
     */
    private String password;

    public String getUrl() {
        return url;
    }

    public DbProperties setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DbProperties setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DbProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "DbProperties{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
