package com.briup.smart.env.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;

public class GatherImpl implements Gather, PropertiesAware, ConfigurationAware {
	private String dataFilePath;
	private String fileLengthBak;
	private Backup backup;
	private Log log;
	@Override
	public Collection<Environment> gather() throws Exception {
		/*
		 *需求分析
		 *1、读文件 io流 一行一行读 
		 *每行数据如下：
		 *100|101|2|16|1|3|5d606f7802|1|1516323596029
		 * 0   1  2  3 4 5   6        7  8
		 *按照｜进行分割
		 *封装 Environment对象
		 *1、温度和湿度   16   2个env对象
		 *温度  5d60  -》 十进制 -》公式(data*(0.00268127F))-46.85F
		 *湿度  6f78  --》十进制 ---》(data*0.00190735F)-6
		 *2、 二氧化碳 1280  前4位--》十进制 1个env对象
		 *3、光照强度  256  前4位--》十进制  1个env对象  
		 *数据存到集合中，最终把集合返回      
		 */	
		List<Environment> list = new ArrayList<>();
		BufferedReader in = null;
		try {
			File file = new File(dataFilePath);
			in = new BufferedReader(new FileReader(file));
			Long len = (Long) backup.load(fileLengthBak, Backup.LOAD_REMOVE);
			log.debug("采集模块 读取备份数据：上次已经读取的字节数：" + (len == null ? 0 : len));
			if (len != null) {
				in.skip(len);
				log.debug("采集模块 已经跳过的字节数：" + len);
			}
			// 拿到文件的长度 刚好是字节数
			long fileLength = file.length();
			backup.store(fileLengthBak, fileLength, Backup.STORE_OVERRIDE);
			log.debug("采集模块 备份本次读取的字节数为：" + fileLength);
			String line = null;
			Environment env = null;
			Environment envCopy = null;
			int data = -1;
			log.info("采集模块 开始采集数据......");
			while ((line = in.readLine()) != null) {
				// 100|101|2|16|1|3|5d606f7802|1|1516323596029
				String[] arr = line.split("[|]");
				if (arr.length != 9) {
					// TODO 记录当前格式有问题的数据
					log.warn("采集模块 读取到有问题的数据");
					continue;
				} else {
					env = new Environment();
					// 发送端id
					env.setSrcId(arr[0]);
					// 树莓派系统id
					env.setDesId(arr[1]);
					// 实验箱区域模块id(1-8)
					env.setDevId(arr[2]);
					// 传感器个数
					env.setCount(Integer.valueOf(arr[4]));
					// 发送指令标号 3表示接收数据 16表示发送命令
					env.setCmd(arr[5]);
					// 状态 默认1表示成功
					env.setStatus(Integer.valueOf(arr[7]));
					env.setGather_date(new Timestamp(Long.valueOf(arr[8])));

					switch (arr[3]) {
					case "16":
						env.setName("温度");
						data = this.getData(arr[6]);
						env.setData((data * (0.00268127F)) - 46.85F);

						envCopy = copy(env);
						envCopy.setName("湿度");
						data = this.getData(arr[6], 2);
						envCopy.setData((data * 0.00190735F) - 6);

						list.add(env);
						list.add(envCopy);
						break;

					case "256":
						env.setName("光照强度");
						data = getData(arr[6]);
						env.setData(data);
						list.add(env);
						break;
					case "1280":
						env.setName("二氧化碳");
						data = getData(arr[6]);
						env.setData(data);
						list.add(env);
						break;
					default:
						break;
					}
				}
			}
			log.info("采集模块 采集数据结束");
			log.debug("采集模块 本次共采集到" + list.size() + "条");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return list;
	}

	public <T> T copy(T t) throws Exception {
		@SuppressWarnings("unchecked")
		Class<T> c = (Class<T>) t.getClass();
		T copy = c.newInstance();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			if (Modifier.isFinal(f.getModifiers())) {
				continue;
			}
			f.setAccessible(true);
			Object value = f.get(t);
			f.set(copy, value);
		}
		return copy;
	}

	/**
	 * 从16进制的字符串中，获取具体的int数据，并且可以指定第几个数据
	 * 
	 * @param data  16进制的字符串
	 * @param index 第几个数据
	 * @return 数据
	 */
	private int getData(String data, int index) {
		if (index == 1) {
			data = data.substring(0, 4);
		} else if (index == 2) {
			data = data.substring(4, 8);
		} else {
			throw new RuntimeException("index的值只能为1和2");
		}
		return Integer.parseInt(data, 16);
	}

	/**
	 * 返回16进制的字符串中默认的第一个数据
	 * 
	 */
	private int getData(String data) {
		return getData(data, 1);
	}
//	
//	public void init(Properties p) {
//		this.dataFilePath = p.getProperty("gather-data-file-path");
//		this.fileLengthBak = p.getProperty("gather-file-length-bak");
//	}

	@Override
	public void init(Properties p) throws Exception {
		this.dataFilePath = p.getProperty("gather-data-file-path");
		this.fileLengthBak = p.getProperty("gather-file-length-bak");
	}

	@Override
	public void setConfiguration(Configuration conf) throws Exception {
		this.backup = conf.getBackup();
		this.log = conf.getLogger();
	}

//	public void setConfig(Configuration conf) {
//		this.backup = conf.getBackup();
//		this.log = conf.getLogger();
//	}

}