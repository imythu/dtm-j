package com.github.imythu.core.cfg;

/**
 * config reader.
 *
 * @author imythu
 */
@FunctionalInterface
public interface CfgReader {

    /**
     * read {@link DtmProperties} from somewhere
     *
     * @return {@link DtmProperties}
     */
    DtmProperties read();
}
