package com.ct.dubbo.lecture.tomrpc.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author changtong
 * @since 2021/4/9
 */
public final class ByteUtil {

    private ByteUtil() { }

   private static ByteArrayOutputStream out = new ByteArrayOutputStream();

    public synchronized static byte[] toByteArray(Object msg){
        out.reset();
        ObjectOutputStream oout = null;
        byte[] msgBody = null;
        try {
            oout = new ObjectOutputStream(out);
            oout.writeObject(msg);
            msgBody= out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msgBody;
    }
}
