package io.github.dtm.labs.core.cfg;

public class DbProperties {

    /** JDBC URL of the database. */
    private String url;

    /** Login username of the database. */
    private String username;

    /** Login password of the database. */
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DbProperties{"
                + "url='"
                + url
                + '\''
                + ", username='"
                + username
                + '\''
                + ", password='"
                + password
                + '\''
                + '}';
    }
}
