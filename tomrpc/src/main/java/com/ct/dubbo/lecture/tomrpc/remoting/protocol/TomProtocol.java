package com.ct.dubbo.lecture.tomrpc.remoting.protocol;

import java.io.Serializable;
import lombok.Data;

/**
 * @author changtong
 * @since 2021/4/21
 */
@Data
public class TomProtocol implements Serializable {

    public TomProtocol(TomProtocolHeader header, InvokeMessage content) {
        this.header = header;
        this.content = content;
    }

    private TomProtocolHeader header;

    private InvokeMessage content;

}
