package com.ct.dubbo.lecture.tomrpc.config;

import com.ct.dubbo.lecture.tomrpc.common.RpcConstant;
import com.ct.dubbo.lecture.tomrpc.common.TomService;
import com.ct.dubbo.lecture.tomrpc.remoting.transport.ServerRequestHandler;
import com.ct.dubbo.lecture.tomrpc.remoting.transport.TomProtocolDecode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author changtong
 * @since 2021/4/21
 */
public class ProviderApplicationConfig {

    private String host;

    private int port;

    private String providerPackage;

    public ProviderApplicationConfig(String host,String providerPackage) {
        this(host, RpcConstant.DEFAULT_PORT,providerPackage);
    }

    public ProviderApplicationConfig(String host,int port, String providerPackage) {
        this.host = host;
        this.port = port;
        this.providerPackage = providerPackage;
    }

    /**
     * 注册的服务名
     */
    private Set<String> serviceClassNames = new HashSet();

    /**
     * 注册的服务
     */
    private Map<String, Object> registryMap = new HashMap();


    public void publish() throws Exception{
        scanProviderPackage(providerPackage);
        register();
        run();
    }

    private void register() throws Exception{
        if (serviceClassNames.size() == 0) {
            return;
        }
        for (String className : serviceClassNames) {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(TomService.class)) {
                TomService annotation = clazz.getAnnotation(TomService.class);
                String interfaceName = annotation.interfaceClass().getName();
                registryMap.put(interfaceName, clazz.newInstance());
            }
        }
    }

    private void run(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker =  new NioEventLoopGroup();
        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture bind = sbs.group(boss, worker)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new TomProtocolDecode());
                    p.addLast(new ServerRequestHandler(registryMap));
                }
            }).bind(new InetSocketAddress(host, port));

        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private  void scanProviderPackage(String providerPackage){

        URL resource = this.getClass().getClassLoader()
            .getResource(providerPackage.replace(".", "/"));
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {

            if (file.isDirectory()) {
                scanProviderPackage(providerPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")){
                String fileName = file.getName().replace(".class", "");
                serviceClassNames.add(providerPackage + "." + fileName);
            }
        }

    }


}
