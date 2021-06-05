package com.kyriez.Server.Netty;

import com.kyriez.Client.Netty.ShutDownHook;
import com.kyriez.Coder.CommonDecoder;
import com.kyriez.Coder.CommonEncoder;
import com.kyriez.Registry.Nacos.NacosServiceRegistry;
import com.kyriez.Registry.ServiceRegistry;
import com.kyriez.Serializer.CommonSerializer;
import com.kyriez.Serializer.KryoSerializer;
import com.kyriez.Server.Server;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import com.kyriez.provider.ServiceProvider;
import com.kyriez.provider.ServiceProviderImp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


public class NettyServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private static int port;
    private static String host;
    private final ServiceProvider serviceProvider;
    private final ServiceRegistry serviceRegistry;

    private CommonSerializer serializer;

    public NettyServer(String host, int port){
        this.host = host;
        this.port = port;
        serviceProvider = new ServiceProviderImp();
        serviceRegistry = new NacosServiceRegistry(null);
    }

    public <T> void publishService(Object service, Class<T> serviceClass){
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        logger.info("注册服务：{}在:{}", service.toString(), host+":"+port);
    }


    @Override
    public void start(){

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
            serializer = new KryoSerializer();
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
            ChannelFuture future = bootstrap.bind(host, port).sync();
            //添加注销用的钩子
            ShutDownHook.getShutDownHook().addClearAllHook();
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
