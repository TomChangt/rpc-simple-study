package com.ct.dubbo.lecture.provider;

import com.ct.dubbo.lecture.tomrpc.config.ProviderApplicationConfig;

/**
 * @author changtong
 * @since 2021/4/21
 */
public class ApplicationBootstrap {

    public static void main(String[] args)throws Exception {
        ProviderApplicationConfig config
            = new ProviderApplicationConfig("127.0.0.1","com.ct.dubbo.lecture.provider");
        config.publish();




    }

}
