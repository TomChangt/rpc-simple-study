package com.ct.dubbo.lecture.consumer;

import com.ct.dubbo.lecture.client.business.UserService;
import com.ct.dubbo.lecture.client.dto.UserDTO;
import com.ct.dubbo.lecture.tomrpc.common.RpcConstant;
import com.ct.dubbo.lecture.tomrpc.config.ConsumerApplicationConfig;

/**
 * @author changtong
 * @since 2021/4/22
 */
public class ConsumerApplicationBootstrap {

    public static void main(String[] args) {
        ConsumerApplicationConfig config = new ConsumerApplicationConfig("localhost",
            RpcConstant.DEFAULT_PORT);

        UserService userService = config.get(UserService.class);
        UserDTO user = userService.findUserById(10086l);

        System.out.println(user);

    }
}
