package com.github.imythu.core.mode.tcc.impl.grpc;

import com.github.imythu.core.cfg.CfgHolder;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
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
                        .flatMap(
                                s -> {
                                    String[] hostAndPort = s.split(":");
                                    int port = Integer.parseInt(hostAndPort[1]);
                                    try {
                                        return resolveAddress(hostAndPort[0]).stream()
                                                .map(
                                                        inetAddress ->
                                                                new EquivalentAddressGroup(
                                                                        InetSocketAddress
                                                                                .createUnresolved(
                                                                                        inetAddress
                                                                                                .getHostAddress(),
                                                                                        port)));
                                    } catch (UnknownHostException e) {
                                        throw new RuntimeException(e);
                                    }
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

    public List<InetAddress> resolveAddress(String host) throws UnknownHostException {
        return List.of(InetAddress.getAllByName(host));
    }
}
