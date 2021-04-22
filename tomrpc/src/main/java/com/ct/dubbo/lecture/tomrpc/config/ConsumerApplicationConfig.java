package com.ct.dubbo.lecture.tomrpc.config;

import com.ct.dubbo.lecture.tomrpc.rpc.proxy.TomProtocolProxy;
import java.net.InetSocketAddress;

/**
 * @author changtong
 * @since 2021/4/22
 */
public class ConsumerApplicationConfig {

    private TomProtocolProxy protocolProxy;

    //直连先写死
    private String host;

    //直连先写死
    private int port;

    private InetSocketAddress directServiceAddress;

    public ConsumerApplicationConfig(String host, int port) {
        this.host = host;
        this.port = port;
        directServiceAddress = new InetSocketAddress(host,port);
        protocolProxy = new TomProtocolProxy();
    }

    public <T>T get(Class<T>  interfaceInfo){
        return protocolProxy.proxyGet(directServiceAddress,interfaceInfo);
    }

}
