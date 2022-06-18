package com.briup.smart.env.main;

import java.util.Collection;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.ConfigurationImpl;
import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.entity.Environment;

//客户端入口类
public class ClientMain {
	public static void main(String[] args) throws Exception {
		Configuration config = ConfigurationImpl.getInstance();
		Gather gather = config.getGather();
		Client client = config.getClient();
		Collection<Environment> c = gather.gather();
		client.send(c);
	}
}
