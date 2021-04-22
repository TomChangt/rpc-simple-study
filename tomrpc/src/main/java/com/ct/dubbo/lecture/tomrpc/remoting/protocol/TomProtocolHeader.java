package com.ct.dubbo.lecture.tomrpc.remoting.protocol;

import java.io.Serializable;
import lombok.Data;

/**
 * @author changtong
 * @since 2021/4/9
 */
@Data
public class TomProtocolHeader implements Serializable {

    /**
     * 请求id
     */
    long requestID;
    /**
     * 数据长度
     */
    long dataLen;
}
