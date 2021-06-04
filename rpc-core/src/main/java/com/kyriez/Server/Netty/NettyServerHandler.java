package com.kyriez.Server.Netty;

import com.kyriez.Registry.DefaultServiceRegistry;
import com.kyriez.Server.RequestHandler;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler = new RequestHandler();
    private static DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        try {
            logger.info("服务器接收到了请求：{}", request);

            String interfaceName = request.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(request, service);
            ChannelFuture channelFuture = ctx.writeAndFlush(RpcResponse.success(result));
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(request);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("调用时错误：");
        cause.printStackTrace();
        ctx.close();
    }
}
