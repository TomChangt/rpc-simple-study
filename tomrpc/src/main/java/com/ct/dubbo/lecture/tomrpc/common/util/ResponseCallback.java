package com.ct.dubbo.lecture.tomrpc.common.util;


import com.ct.dubbo.lecture.tomrpc.remoting.protocol.TomProtocol;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author changtong
 * @since 2021/4/9
 */
public class ResponseCallback {

    static ConcurrentHashMap<Long, CompletableFuture> mapping = new ConcurrentHashMap<>();

    public static void  addCallBack(long requestID,CompletableFuture cb){
        mapping.putIfAbsent(requestID,cb);
    }

    public static void runCallBack(TomProtocol msg){
        CompletableFuture cf = mapping.get(msg.getHeader().getRequestID());
        cf.complete(msg.getContent().getResult());
        removeCB(msg.getHeader().getRequestID());
    }

    private static void removeCB(long requestID) {
        mapping.remove(requestID);
    }
}
