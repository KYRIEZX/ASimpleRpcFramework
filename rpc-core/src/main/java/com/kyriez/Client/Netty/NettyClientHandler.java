package com.kyriez.Client.Netty;

import com.kyriez.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
        try {
            logger.info(String.format("Client received the message: %s", rpcResponse.toString()));
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcResponse.getRequestId());
            ctx.channel().attr(key).set(rpcResponse);
            ctx.close();
        } finally {
            ReferenceCountUtil.release(rpcResponse);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
