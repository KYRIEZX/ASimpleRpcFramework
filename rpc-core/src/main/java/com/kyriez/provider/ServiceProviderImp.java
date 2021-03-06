package com.kyriez.provider;

import com.kyriez.enumeration.RpcError;
import com.kyriez.exeption.RpcExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviderImp implements ServiceProvider{
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImp.class);
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        Class<?> [] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new RpcExeption(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> interFace: interfaces){
            serviceMap.put(interFace.getCanonicalName(), service); // 某个接口由某服务提供
        }
        logger.info("向接口: {} 注册服务：{}", interfaces, serviceName);
    }

    @Override
    public Object getServiceProvider(String ServiceName) {
        Object service = serviceMap.get(ServiceName);
        if(service == null){
            throw new RpcExeption(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
