package com.kyriez.Coder;

import com.kyriez.Serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
public class CommonDecoder extends ByteToMessageDecoder {
    private final Logger logger = LoggerFactory.getLogger(CommonEncoder.class);
    private final CommonSerializer serializer;
    private final Class<?> clazz;
    private static final int BODY_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        if(byteBuf.readableBytes() >= 4){
            //标记当前readIndex位置
            byteBuf.markReaderIndex();
            //读取数组长度数据
            int dataLength = byteBuf.readInt();
            System.out.println("Decode dataLength:"+dataLength);
            if(dataLength < 0){
                logger.error("Data length is invalid");
                return;
            }
            //如果可读字节数小于消息长度， 说明消息不完整， 重置readIndex
            if(byteBuf.readableBytes() < dataLength){
                byteBuf.resetReaderIndex();
                return;
            }

            byte[] body = new byte[dataLength];
            byteBuf.readBytes(body);
            System.out.println("********************************************");
            Object obj = serializer.deserialize(body, clazz);
            out.add(obj);
            logger.info("Successfully decode the ByteBuf to Object");

        }
    }
}
