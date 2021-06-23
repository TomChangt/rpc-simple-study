package com.ct.dubbo.lecture.tomrpc.remoting.transport;

import com.ct.dubbo.lecture.tomrpc.common.util.ByteUtil;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.InvokeMessage;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.TomProtocol;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.TomProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> registryMap;

    public ServerRequestHandler(Map<String, Object> registryMap) {
        this.registryMap = registryMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        TomProtocol data = (TomProtocol) msg;

        //ctx.executor().parent().next().execute(new Runnable() {
        ctx.executor().execute(() -> {

            String serviceName = data.getContent().getServiceName();

            Object provider = registryMap.get(serviceName);
            Object result = null;
            try {
               result = provider.getClass()
                     .getMethod(data.getContent().getMethodName(),data.getContent().getParamTypes())
                     .invoke(provider, data.getContent().getParamValues());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            InvokeMessage content = new InvokeMessage();
            content.setResult(result);
            byte[] contentByte = ByteUtil.toByteArray(content);

            TomProtocolHeader resHeader = new TomProtocolHeader();
            resHeader.setRequestID(data.getHeader().getRequestID());
            resHeader.setDataLen(contentByte.length);

            byte[] headerByte = ByteUtil.toByteArray(resHeader);
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerByte.length + contentByte.length);

            byteBuf.writeBytes(headerByte);
            byteBuf.writeBytes(contentByte);
            ctx.writeAndFlush(byteBuf);
        });




    }

}