package com.briup.smart.env.support;

import java.util.Properties;

/**
 * 如果某模块A， 需要传入外部数据properteis(key-value形式)进行初始化赋值
 * 那么这个模块A，就需要实现这个PropertiesAware接口
 * 
 * 例如读取xml文件中的数据，并传入模块中进行初始化赋值
 */
public interface PropertiesAware{
	//通过init方法对当前模块进行初始化赋值
	public void init(Properties properties)throws Exception;
}
