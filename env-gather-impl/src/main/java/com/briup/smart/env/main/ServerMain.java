package com.briup.smart.env.main;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.ConfigurationImpl;
import com.briup.smart.env.server.Server;

//服务器入口类
public class ServerMain {
	public static void main(String[] args) throws Exception {
		Configuration config = ConfigurationImpl.getInstance();
		Server server = config.getServer();
		server.reciver();
	}
}
