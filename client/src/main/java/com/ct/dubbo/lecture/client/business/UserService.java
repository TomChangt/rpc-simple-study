package com.ct.dubbo.lecture.client.business;

import com.ct.dubbo.lecture.client.dto.UserDTO;

/**
 * @author changtong
 * @since 2021/4/21
 */
public interface UserService {

    UserDTO findUserById(Long id);

}
