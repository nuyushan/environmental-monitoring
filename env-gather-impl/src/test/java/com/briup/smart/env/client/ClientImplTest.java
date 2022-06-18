package com.briup.smart.env.client;

import java.util.Collection;

import org.junit.Test;

import com.briup.smart.env.entity.Environment;

public class ClientImplTest {

	@Test
	public void test() throws Exception {
		Client client = new ClientImpl();
		Gather gather = new GatherImpl();
		Collection<Environment> coll = gather.gather();
		System.out.println("发送的数据总条数:"+coll.size());
		client.send(coll);
	}
}
