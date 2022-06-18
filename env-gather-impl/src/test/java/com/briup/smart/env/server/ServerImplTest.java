package com.briup.smart.env.server;

import org.junit.Test;

public class ServerImplTest {

	@Test
	public void test() throws Exception {
		Server server = new ServerImpl();
		server.reciver();
	}
}
