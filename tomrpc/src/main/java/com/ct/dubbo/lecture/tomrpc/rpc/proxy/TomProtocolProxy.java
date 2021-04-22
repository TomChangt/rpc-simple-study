package com.ct.dubbo.lecture.tomrpc.rpc.proxy;

import com.ct.dubbo.lecture.tomrpc.common.util.ByteUtil;
import com.ct.dubbo.lecture.tomrpc.common.util.ResponseCallback;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.InvokeMessage;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.TomProtocolHeader;
import com.ct.dubbo.lecture.tomrpc.remoting.transport.ClientFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author changtong
 * @since 2021/4/9
 */
public class TomProtocolProxy {

    public <T>T proxyGet(InetSocketAddress address,Class<T>  interfaceInfo){

        ClassLoader loader = interfaceInfo.getClassLoader();

        Class<?>[] methodInfo = {interfaceInfo};

         return (T) Proxy.newProxyInstance(loader, methodInfo, (proxy, method, args) -> {
            //如何设计我们的consumer对于provider的调用过程
            //1，调用 服务，方法，参数  ==》 封装成message  [content]
            Class<?>[] parameterTypes = method.getParameterTypes();
            InvokeMessage content = new InvokeMessage();
            content.setParamValues(args);
            content.setServiceName(interfaceInfo.getName());
            content.setMethodName(method.getName());
            content.setParamTypes(parameterTypes);
            byte[] msgBody = ByteUtil.toByteArray(content);
            //2，requestID+message  ，本地要缓存
            //协议：【header<>】【msgBody】
            TomProtocolHeader header = createHeader(msgBody);

            //解决数据decode问题
            //TODO：Server：： dispatcher  Executor
            byte[] msgHeader =  ByteUtil.toByteArray(header);

            //3，连接池：：取得连接
            ClientFactory factory = ClientFactory.getFactory();
            NioSocketChannel clientChannel = factory.getClient(address);
            //获取连接过程中： 开始-创建，过程-直接取

            //4，发送--> 走IO  out -->走Netty（event 驱动）

            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);

            long id = header.getRequestID();
            CompletableFuture<Object> res = new CompletableFuture<>();
            ResponseCallback.addCallBack(id, res);
            byteBuf.writeBytes(msgHeader);
            byteBuf.writeBytes(msgBody);
            ChannelFuture channelFuture = clientChannel.writeAndFlush(byteBuf);
            channelFuture.sync();  //io是双向的，你看似有个sync，她仅代表out
             Object rs = res.get(); //阻塞的
             if(rs == null){
                 throw new Exception("调用服务出错........");
             }
             return rs;
        });
    }

    private TomProtocolHeader createHeader(byte[] msg){
        TomProtocolHeader header = new TomProtocolHeader();
        long requestID =  Math.abs(UUID.randomUUID().getLeastSignificantBits());
        header.setDataLen(msg.length);
        header.setRequestID(requestID);
        return header;
    }
}
