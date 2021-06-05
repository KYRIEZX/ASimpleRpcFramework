package com.kyriez.provider;

public interface ServiceProvider {

    <T> void addServiceProvider(T service);

    Object getServiceProvider(String ServiceName);
}
