package com.briup.smart.env.util;

/**
 * Log接口是物联网数据中心项目日志模块的规范
 * 日志模块将日志信息划分为五种级别,不同情况可以使用不同级别的日志进行记录
 */
public interface Log{
	//记录debug级别的日志
	public void debug(String message);
	//记录info级别的日志
	public void info(String message);
	//记录warn级别的日志
	public void warn(String message);
	//记录error级别的日志
	public void error(String message);
	//记录fatal级别的日志
	public void fatal(String message);
}
