package com.ct.dubbo.lecture.tomrpc.remoting.transport;

import com.ct.dubbo.lecture.tomrpc.common.util.ResponseCallback;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.TomProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        TomProtocol data = (TomProtocol) msg;
        //曾经我们没考虑返回的事情
        ResponseCallback.runCallBack(data);
    }
}