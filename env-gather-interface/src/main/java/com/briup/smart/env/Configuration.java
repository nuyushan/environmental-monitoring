package com.briup.smart.env;

import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;

/**
 * Configuration接口提供了配置模块的规范
 * 配置模块 需要对其他模块进行创建并初始化赋值(init)
 * 配置模块 还提供了获取其他模块对象的方法 
 */
public interface Configuration{
	//获取日志模块的实例
	public Log getLogger()throws Exception;
	//获取服务器端的实例
	public Server getServer()throws Exception;
	//获取客户端的实例
	public Client getClient()throws Exception;
	//获取入库模块的实例
	public DBStore getDbStore()throws Exception;
	//获取采集模块的实例
	public Gather getGather()throws Exception;
	//获取备份模块的实例
	public Backup getBackup()throws Exception;
}
