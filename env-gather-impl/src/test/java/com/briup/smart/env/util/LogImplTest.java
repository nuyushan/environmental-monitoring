package com.briup.smart.env.util;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LogImplTest {
	private static final Logger LOGGER = Logger.getRootLogger();

	@Test
	public void test() {
		LOGGER.debug("debuge级别日志测试.....");
		LOGGER.info("info级别日志测试.....");
		LOGGER.warn("warn级别日志测试.....");
		LOGGER.error("error级别日志测试.....");
	}
}