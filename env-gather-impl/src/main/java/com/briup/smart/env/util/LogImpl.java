package com.briup.smart.env.util;

import org.apache.log4j.Logger;

public class LogImpl implements Log{
	private static final Logger LOGGER = Logger.getRootLogger();
	
	@Override
	public void debug(String message) {
		LOGGER.debug(message);
	}

	@Override
	public void info(String message) {
		LOGGER.info(message);
	}

	@Override
	public void warn(String message) {
		LOGGER.warn(message);
	}

	@Override
	public void error(String message) {
		LOGGER.error(message);
	}

	@Override
	public void fatal(String message) {
		LOGGER.fatal(message);
	}
}