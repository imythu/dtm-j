package com.github.imythu.core.mode.tcc.impl.grpc;

import com.github.imythu.core.cfg.CfgHolder;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuhf
 */
public class RandomNameResolver extends NameResolver {
    private final URI uri;

    public RandomNameResolver(URI uri) {
        this.uri = uri;
    }

    @Override
    public void start(Listener2 listener) {
        List<EquivalentAddressGroup> collect =
                CfgHolder.getDtmProperties().getGrpcServer().stream()
                        .map(
                                s -> {
                                    String[] hostAndPort = s.split(":");
                                    return new EquivalentAddressGroup(
                                            InetSocketAddress.createUnresolved(
                                                    hostAndPort[0],
                                                    Integer.parseInt(hostAndPort[1])));
                                })
                        .collect(Collectors.toList());
        listener.onResult(
                ResolutionResult.newBuilder()
                        .setAddresses(collect)
                        .setAttributes(Attributes.EMPTY)
                        .build());
    }

    @Override
    public String getServiceAuthority() {
        return uri.getAuthority();
    }

    @Override
    public void shutdown() {}
}
