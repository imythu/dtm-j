package com.github.imythu.core.mode.tcc.impl.grpc;

import io.grpc.NameResolver;
import io.grpc.NameResolver.Args;
import io.grpc.NameResolverProvider;
import java.net.URI;

/**
 * @author zhuhf
 */
public class MultiAddressNameResolverProvider extends NameResolverProvider {
    public static final String MULTI_ADDRESS_SCHEME = "multiaddress";

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        // default 5
        return 5;
    }

    @Override
    public NameResolver newNameResolver(URI targetUri, Args args) {
        return new MultiAddressNameResolver(targetUri);
    }

    @Override
    public String getDefaultScheme() {
        return MULTI_ADDRESS_SCHEME;
    }
}
