package com.kyriez.LoadBanlance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public interface LoadBanlance {
    Instance select(List<Instance> instances);
}
