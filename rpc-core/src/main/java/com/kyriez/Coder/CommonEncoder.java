package com.kyriez.Coder;

import com.kyriez.Serializer.CommonSerializer;
import com.kyriez.entity.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class CommonEncoder extends MessageToByteEncoder {

    private final Logger logger = LoggerFactory.getLogger(CommonEncoder.class);
    private final CommonSerializer serializer;
    private final Class<?> clazz;


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        if(clazz.isInstance(obj)){
            //  将对象转化成byte
            byte[] body = serializer.serialize(obj);
            //  读取消息长度 4bytes
            int dataLength = body.length;

            //  写入消息对应的字节数组长度数据(32位数字) 占据4bytes在ByteBuf头部
            byteBuf.writeInt(dataLength);
            byteBuf.writeBytes(body);


        }
    }
}
