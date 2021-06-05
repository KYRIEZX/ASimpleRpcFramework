package com.kyriez.Client.Netty;

import com.kyriez.Client.Client;
import com.kyriez.Coder.CommonDecoder;
import com.kyriez.Coder.CommonEncoder;
import com.kyriez.Registry.Nacos.NacosServiceRegistry;
import com.kyriez.Registry.ServiceRegistry;
import com.kyriez.Serializer.CommonSerializer;
import com.kyriez.Serializer.KryoSerializer;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import com.kyriez.utils.RpcMessageChecker;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class NettyClient implements Client {

    private final static Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final Bootstrap bootstrap;
    private final ServiceRegistry serviceRegistry;

    private static CommonSerializer serializer;

    public NettyClient() {
        this.serviceRegistry = new NacosServiceRegistry(null);
    }

    static{
        bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        serializer = new KryoSerializer();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public Object SendRequest(RpcRequest rpcRequest) {
        AtomicReference<Object> result = new AtomicReference<>(null);
        try{
            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(bootstrap, inetSocketAddress, serializer);
            if(channel.isActive()){
                channel.writeAndFlush(rpcRequest).addListener(future1 ->{
                   if(future1.isSuccess()){
                       logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                   }else{
                       logger.error("发送消息时有错误发生: ", future1.cause());
                   }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                result.set(rpcResponse.getData());
            }else {
                System.exit(0);
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生：", e);
        }
        return result.get();
    }
}
