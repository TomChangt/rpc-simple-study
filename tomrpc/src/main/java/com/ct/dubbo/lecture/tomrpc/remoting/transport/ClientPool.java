package com.ct.dubbo.lecture.tomrpc.remoting.transport;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author changtong
 * @since 2021/4/8
 */
public class ClientPool {
    NioSocketChannel[] clients;
    Object[] lock;

    ClientPool(int size){
        clients = new NioSocketChannel[size];//init  连接都是空的
        lock = new Object[size]; //锁是可以初始化的
        for(int i = 0;i< size;i++){
            lock[i] = new Object();
        }
    }
}
