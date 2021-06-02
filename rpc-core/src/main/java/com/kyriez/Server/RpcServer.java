package com.kyriez.Server;

import com.kyriez.Registry.DefaultServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RpcServer {
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private final DefaultServiceRegistry serviceRegistry;

    public RpcServer(DefaultServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,workingQueue, threadFactory);
    }

    /**
     * 对外暴露接口
     */
    public void register(Object service, int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("客户端连接！Ip为：" + socket.getInetAddress());

                threadPool.execute(new WorkerThread(socket, service));

            }
        }catch (Exception e){
            logger.error("Server "+e);
        }
    }
}
