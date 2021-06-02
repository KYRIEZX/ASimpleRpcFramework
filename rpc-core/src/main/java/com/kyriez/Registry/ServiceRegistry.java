package com.kyriez.Registry;

public interface ServiceRegistry {

    <T> void register(T service);
    Object getService(String ServiceName);
}
