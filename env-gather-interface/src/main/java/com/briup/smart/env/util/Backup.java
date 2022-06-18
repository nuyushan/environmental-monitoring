package com.briup.smart.env.util;

/**
 * Backup接口是物联网数据中心项目备份模块的规范
 */
public interface Backup{
	/**
	 * 在保存数据时,在原来文件的基础上进行追加
	 */
	public static final boolean STORE_APPEND = true;
	
	/**
	 * 在保存数据时,覆盖原来的文件
	 */
	public static final boolean STORE_OVERRIDE = false;
	
	/**
	 * 在读取数据同时,删除备份文件
	 */
	public static final boolean LOAD_REMOVE = true;
	
	/**
	 * 在读取数据同时,不删除备份文件
	 */
	public static final boolean LOAD_UNREMOVE = false;
	
	
	/**
	 * 读取备份文件,返回集合对象
	 */
	public Object load(String fileName,boolean del) throws Exception;
	/**
	 * 将需要备份的集合对象写入到备份文件
	 */
	public void store(String fileName,Object obj,boolean append)throws Exception;
}

