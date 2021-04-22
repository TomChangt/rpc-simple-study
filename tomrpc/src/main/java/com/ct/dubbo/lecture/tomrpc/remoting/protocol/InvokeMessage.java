package com.ct.dubbo.lecture.tomrpc.remoting.protocol;

import java.io.Serializable;
import lombok.Data;

/**
 * @author changtong
 * @since 2021/4/21
 */
@Data
public class InvokeMessage  implements Serializable {

    // 服务名称
    private String serviceName;

    // 方法名
    private String methodName;

    // 参数列表
    private Class<?>[] paramTypes;

    // 方法参数值
    private Object[] paramValues;

    // 调用结果
    private Object result;

}
