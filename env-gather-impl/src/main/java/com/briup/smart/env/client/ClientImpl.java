package com.briup.smart.env.client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;

public class ClientImpl implements Client,PropertiesAware,ConfigurationAware{
	private String serverIp;
	private int serverPort;
	private Socket socket;
	private ObjectOutputStream out;
	private Log log;
	@Override
	public void send(Collection<Environment> c) throws Exception {
		try {
			if(c == null || c.size() == 0) {
				log.warn("客户端模块 拿到的数据集合为空 客户端模块退出");
				return;
			}
			socket = new Socket(serverIp, serverPort);
			log.info("客户端模块 连接服务器成功，ip"+serverIp+",prot="+serverPort);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(c);
			out.flush();
			log.info("客户端模块 发送数据成功，共发送"+c.size()+"条");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				out.close();
			}
			
			if(socket!=null) {
				socket.close();
			}
		}
	}
	@Override
	public void setConfiguration(Configuration config) throws Exception {
		this.log = config.getLogger();
	}
	@Override
	public void init(Properties properties) throws Exception {
		this.serverIp = properties.getProperty("client-server-ip");
		this.serverPort = Integer.valueOf(properties.getProperty("client-port"));
	}
}