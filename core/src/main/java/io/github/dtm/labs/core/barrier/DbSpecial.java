package io.github.dtm.labs.core.barrier;

import io.github.dtm.labs.core.domain.BarrierDO;

/**
 * db specific operations
 * @author imythu
 */
public interface DbSpecial {
    String getInsertIgnoreSql(BarrierDO barrierDO);
    String getXaSQL(String command, String xid);
}
