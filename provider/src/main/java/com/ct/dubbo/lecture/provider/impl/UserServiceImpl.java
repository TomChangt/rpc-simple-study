package com.ct.dubbo.lecture.provider.impl;

import com.ct.dubbo.lecture.client.business.UserService;
import com.ct.dubbo.lecture.client.dto.UserDTO;
import com.ct.dubbo.lecture.tomrpc.common.TomService;

/**
 * @author changtong
 * @since 2021/4/21
 */
@TomService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Override
    public UserDTO findUserById(Long id) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setAge(20);
        user.setName("tom");
        return user;
    }
}
