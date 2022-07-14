package io.github.dtm.labs.core.enums;

public enum TxType {
    /** tcc 事务 */
    TCC("tcc"),
    /** saga 事务 */
    SAGA("saga"),
    /** xa 事务 */
    XA("xa"),
    /** msg 事务 */
    MSG("msg");
    private final String type;

    TxType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TxType{" + "type='" + type + '\'' + '}';
    }
}
