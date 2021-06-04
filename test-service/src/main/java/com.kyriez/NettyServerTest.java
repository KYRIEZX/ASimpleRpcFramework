package com.kyriez;

import com.kyriez.Registry.DefaultServiceRegistry;
import com.kyriez.Registry.ServiceRegistry;
import com.kyriez.Server.Netty.NettyServer;

public class NettyServerTest {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
