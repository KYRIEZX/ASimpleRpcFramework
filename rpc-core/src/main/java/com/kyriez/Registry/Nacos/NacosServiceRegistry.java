package com.kyriez.Registry.Nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kyriez.Registry.ServiceRegistry;
import com.kyriez.enumeration.RpcError;
import com.kyriez.exeption.RpcExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.rmi.Naming;
import java.util.List;

public class NacosServiceRegistry implements ServiceRegistry {

    private final static Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    private static final String SERVER_ADDRESS = "127.0.0.1:8848";
    private static final NamingService namingService;

    static{
        try{
            namingService = NamingFactory.createNamingService(SERVER_ADDRESS);
        } catch (NacosException e) {
            logger.error("连接到NACOS时发生错误", e);
            throw new RpcExeption(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }


    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try{
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册时发生错误",e);
            throw new RpcExeption(RpcError.FAILED_TO_REGISTER);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try{
            List<Instance> instances = namingService.getAllInstances(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务发生错误", e);
            throw new RpcExeption(RpcError.SERVICE_NOT_FOUND);
        }
    }
}
