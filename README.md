# ASimpleRpcFramework
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

一套简易RPC框架，技术栈用到了Netty（用于网络传输，也实现了原生Socket版本）、Kyro（序列化）和Nacos（注册中心）。

### 什么是RPC？

>RPC(Remote Procedure Call) 即远程调用，通过名字我们就能看出RPC关注的是远程调用而非本地调用。

### 为什么要用RPC?

>因为两个服务器上的方法并不在同一个内存空间，所以需要网络编程的方法才能调用。

### RPC能做什么？
>假设A、B两个服务部署在两个不同的机器上，如果A想调用服务B就可以使用RPC。简单来说，RPC的出现就是为了使调用远程方法像调用本地方法一样简单！

## 基本架构
![系统架构](./rpc-framework/pic1.png)



