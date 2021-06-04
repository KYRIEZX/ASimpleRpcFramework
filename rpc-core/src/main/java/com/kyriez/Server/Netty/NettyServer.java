package com.kyriez.Server.Netty;

import com.kyriez.Coder.CommonDecoder;
import com.kyriez.Coder.CommonEncoder;
import com.kyriez.Serializer.KryoSerializer;
import com.kyriez.Server.Server;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Override
    public void start(int port){

        /**
         * bossGroup 只处理连接请求， 业务处理交给workerGroup
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * 创建服务器端的启动对象，配置参数
             */
            ServerBootstrap bootstrap = new ServerBootstrap();
            KryoSerializer serializer = new KryoSerializer();
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel 作为通道的实现
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立较为频繁，
                    //服务器处理创建新连接较慢，可适当调大该参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE,true)//是否开启TCP底层心跳机制
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childOption(ChannelOption.TCP_NODELAY, true)//TCP 默认开启Nagle算法 该算法作用是尽可能快速的发送大数据块，
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer, RpcResponse.class));
                            pipeline.addLast(new CommonDecoder(serializer, RpcRequest.class));

                            pipeline.addLast(new NettyServerHandler());

                        }
                    });
            //绑定端口并启动服务器
            ChannelFuture future = bootstrap.bind(port).sync();
            //对通道关闭事件进行监听
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器发生错误", e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
