package com.briup.smart.env.client;

import java.util.Collection;

import com.briup.smart.env.entity.Environment;


/**
 * Gather接口是物联网数据中心项目采集模块的规范
 * 该模块对物联网数据中心项目环境信息进行采集
 * 将采集的数据封装成Collection<Environment>集合
 */
public interface Gather{
	public Collection<Environment> gather()throws Exception;
}
