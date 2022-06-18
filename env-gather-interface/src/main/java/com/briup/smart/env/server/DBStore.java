package com.briup.smart.env.server;

import java.util.Collection;

import com.briup.smart.env.entity.Environment;

/**
 * DBStore接口是物联网数据中心项目入库模块的规范
 * 该模块负责对Environment集合进行持久化操作
 */
public interface DBStore{
	//1-31 几号的数据 怎么通过时间戳来获取到具体的几号数据
	//几号数据-->插入到几号表中
	//前5条数据-->3 使用的是同一个sql语句      
	//后10条都是7号的，要切换sql语句
	//要用什么样的处理方式
	public void saveDB(Collection<Environment> c)throws Exception;
}
