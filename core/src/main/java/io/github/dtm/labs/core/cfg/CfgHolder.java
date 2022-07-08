package io.github.dtm.labs.core.cfg;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * @author imythu
 */
public class CfgHolder {
    private static final Logger logger = LoggerFactory.getLogger(CfgHolder.class);
    private static volatile DtmProperties dtmProperties;
    public static final String DEFAULT_CONFIG_FILE_NAME = "dtm.yaml";

    private CfgHolder() {}

    public static DtmProperties getDtmProperties() {
        read();
        return dtmProperties;
    }

    private static void read() {
        if (dtmProperties == null) {
            synchronized (CfgHolder.class) {
                if (dtmProperties == null) {
                    Optional<CfgReader> first =
                            ServiceLoader.load(CfgReader.class).findFirst();
                    if (first.isPresent()) {
                        CfgReader cfgReader = first.get();
                        logger.info("Use CfgReader: {}.", cfgReader.getClass().getName());
                        dtmProperties = cfgReader.read();
                    } else {
                        logger.info("CfgReader is not implemented, reads configuration from classpath:dtm.yaml.");
                        InputStream inputStream = CfgHolder.class.getResourceAsStream(DEFAULT_CONFIG_FILE_NAME);
                        if (inputStream == null) {
                            throw new RuntimeException("dtm.yaml does not exist.");
                        }
                        dtmProperties = new Yaml().loadAs(new InputStreamReader(inputStream), DtmProperties.class);
                    }
                }
            }
        }
    }
}
