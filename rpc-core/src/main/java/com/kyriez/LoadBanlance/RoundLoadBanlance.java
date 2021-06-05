package com.kyriez.LoadBanlance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public class RoundLoadBanlance implements LoadBanlance {
    private int index = 0;
    @Override
    public Instance select(List<Instance> instances) {
        if(index >= instances.size()) index %= instances.size();
        return instances.get(index++);
    }
}
