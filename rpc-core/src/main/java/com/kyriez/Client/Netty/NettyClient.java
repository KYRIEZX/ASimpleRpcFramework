package com.kyriez.Client.Netty;

import com.kyriez.Client.Client;
import com.kyriez.Coder.CommonDecoder;
import com.kyriez.Coder.CommonEncoder;
import com.kyriez.Serializer.KryoSerializer;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class NettyClient implements Client {

    private final static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private final String host;
    private final int port;
    private static final Bootstrap bootstrap;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static{
        bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        KryoSerializer serializer = new KryoSerializer();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder(serializer, RpcResponse.class));
                        pipeline.addLast(new CommonEncoder(serializer, RpcRequest.class));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object SendRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            logger.info("Client connect {}", host + ":" + port);
            Channel channel = future.channel();
            if(channel != null){
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()){
                        logger.info(String.format("Client send the Message: %s", rpcRequest.toString()));
                    }else{
                        logger.error("发送信息时发生错误", future1.cause());
                    }
                });
                //阻塞等待直到channel关闭
                channel.closeFuture().sync();
                //将服务器返回的数据返回
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            logger.error("连接时出现异常", e);
        }
        return null;
    }
}
