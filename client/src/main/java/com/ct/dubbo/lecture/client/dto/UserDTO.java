package com.ct.dubbo.lecture.client.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author changtong
 * @since 2021/4/21
 */
@Data
public class UserDTO implements Serializable {

    private Long id;

    private String name;

    private Integer age;


}
