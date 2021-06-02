package com.kyriez;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService{
    private Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(HelloObject hello) {
        logger.info("Received the hello object " + hello.getMessage());
        return "Return hello " + hello.getId();
    }
}
