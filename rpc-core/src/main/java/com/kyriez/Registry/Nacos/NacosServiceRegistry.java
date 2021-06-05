package com.kyriez.Registry.Nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kyriez.LoadBanlance.LoadBanlance;
import com.kyriez.LoadBanlance.RandomLoadBanlance;
import com.kyriez.LoadBanlance.RoundLoadBanlance;
import com.kyriez.Registry.ServiceRegistry;
import com.kyriez.enumeration.RpcError;
import com.kyriez.exeption.RpcExeption;
import com.kyriez.utils.NacosUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.rmi.Naming;
import java.util.List;


public class NacosServiceRegistry implements ServiceRegistry {

    private final static Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    private static final NamingService namingService;

    private final LoadBanlance loadBanlance;

    static{
        namingService = NacosUtils.getNamingService();
    }

    public NacosServiceRegistry(LoadBanlance loadBanlance){
        if(loadBanlance == null) this.loadBanlance = new RandomLoadBanlance();
        else this.loadBanlance = loadBanlance;
    }


    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try{
            NacosUtils.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册时发生错误",e);
            throw new RpcExeption(RpcError.FAILED_TO_REGISTER);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try{


            List<Instance> instances = NacosUtils.getAllInstances(serviceName);
            Instance instance = loadBanlance.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务发生错误", e);
            throw new RpcExeption(RpcError.SERVICE_NOT_FOUND);
        }
    }
}
