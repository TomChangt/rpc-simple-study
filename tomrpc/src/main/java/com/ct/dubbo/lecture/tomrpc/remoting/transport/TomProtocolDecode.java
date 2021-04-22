package com.ct.dubbo.lecture.tomrpc.remoting.transport;


import com.ct.dubbo.lecture.tomrpc.remoting.protocol.InvokeMessage;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.TomProtocol;
import com.ct.dubbo.lecture.tomrpc.remoting.protocol.TomProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author changtong
 * @since 2021/4/9
 */
public class TomProtocolDecode extends ByteToMessageDecoder {

    /**
     * head 的大小是固定的。。是用java序列化后算出来了。如果换序列化方式或者更改head的包名长度大小也会改变
     */
    private static final int HEAD_LENGTH = 122;


    //父类里一定有channelread{  前老的拼buf  decode（）；剩余留存 ;对out遍历 } -> bytebuf
    //因为你偷懒，自己能不能实现！
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {

        while(buf.readableBytes() >= HEAD_LENGTH) {
            byte[] bytes = new byte[HEAD_LENGTH];
            buf.getBytes(buf.readerIndex(),bytes);  //从哪里读取，读多少，但是readindex不变
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream oin = new ObjectInputStream(in);
            TomProtocolHeader header = (TomProtocolHeader) oin.readObject();


            //DECODE在2个方向都使用
            //通信的协议
            if(buf.readableBytes() - HEAD_LENGTH >= header.getDataLen()){
                //处理指针
                buf.readBytes(HEAD_LENGTH);  //移动指针到body开始的位置
                byte[] data = new byte[(int)header.getDataLen()];
                buf.readBytes(data);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                InvokeMessage content = (InvokeMessage) objectInputStream.readObject();
                out.add(new TomProtocol(header,content));
            }else{
                break;
            }


        }

    }
}
