package com.kyriez.Server.Netty;

import com.kyriez.Registry.DefaultServiceProvider;
import com.kyriez.Registry.ServiceRegistry;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import com.kyriez.handler.RequestHandler;
import com.kyriez.utils.ThreadPoolFactory;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private static final ExecutorService threadPool;

    static{
        requestHandler = new RequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        threadPool.execute(()->{
            try{
                logger.info("服务器接收到请求: {}", request);
                Object result = requestHandler.handle(request);
                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, request.getRequestId()));
                future.addListener(ChannelFutureListener.CLOSE);
            }finally {
                ReferenceCountUtil.release(request);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("调用时错误：");
        cause.printStackTrace();
        ctx.close();
    }
}
