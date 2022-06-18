package com.briup.smart.env.server;

/**
 * Server接口是物联网数据中心项目网络模块(服务端)的规范
 * 该模板负责与客户端进行通信接收信息
 * 并且利用DBStore模块的功能将接收的数据进行持久化操作
 */
public interface Server{
	public void reciver()throws Exception;
	public void shutdown()throws Exception;
}
