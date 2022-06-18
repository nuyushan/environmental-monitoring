package com.briup.smart.env;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;

public class ConfigurationImpl implements Configuration{

	private static final Configuration CONFIG = new ConfigurationImpl();
	private static final String XML_CONFIG_PATH = "src/main/resources/conf.xml";
	//负责管理模块对象
	private static Map<String, Object> map = new HashMap<>();
	//负责管理模块中的参数
	private static Properties p = new Properties();
	
	static {
		//解析xml配置文件，将值存放在map和p中
		parseXML(XML_CONFIG_PATH);
		//初始化模块对象以及里面的属性值
		initModule();
	}
	
	private static void parseXML(String xmlConfigPath) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(xmlConfigPath));
			Element rootElement = document.getRootElement();
			Iterator<Element> it = rootElement.elementIterator();
			while(it.hasNext()) {
				Element element = it.next();
				String elementName = element.getName();//gather  ...
				String classAttrValue = element.attribute("class").getValue();//实现类的全限定名
				//注意需要保证每个模块中都必须有一个无参构造器
				Object obj = Class.forName(classAttrValue).newInstance();
				map.put(elementName.toUpperCase(), obj);
				Iterator<Element> childElementIt = element.elementIterator();
				while(childElementIt.hasNext()) {
					Element childElement = childElementIt.next();
					String attrName = childElement.getName();//gather-data-file-path
					String attrValue = childElement.getTextTrim();//src/main/resources/data-file-simple
					p.setProperty(attrName, attrValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initModule() {
		try {
			for(Object module : map.values()) {
				if(module instanceof PropertiesAware) {
					((PropertiesAware) module).init(p);
				}
			}
			for(Object module : map.values()) {
				if(module instanceof ConfigurationAware) {
					((ConfigurationAware) module).setConfiguration(CONFIG);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ConfigurationImpl() {
		if(CONFIG!=null) {
			throw new RuntimeException("请使用getInstance方法创建对象");
		}
	}

	public static Configuration getInstance() {
		return CONFIG;
	}
	
	@Override
	public Log getLogger() throws Exception {
		return (Log) map.get(ModuleName.LOGGER.name());
	}

	@Override
	public Server getServer() throws Exception {
		return (Server) map.get(ModuleName.SERVER.name());
	}

	@Override
	public Client getClient() throws Exception {
		return (Client) map.get(ModuleName.CLIENT.name());
	}

	@Override
	public DBStore getDbStore() throws Exception {
		return (DBStore) map.get(ModuleName.DBSTORE.name());
	}

	@Override
	public Gather getGather() throws Exception {
		return (Gather) map.get(ModuleName.GATHER.name());
	}

	@Override
	public Backup getBackup() throws Exception {
		return (Backup) map.get(ModuleName.BACKUP.name());
	}
	private enum ModuleName{
		GATHER,LOGGER,BACKUP,CLIENT,SERVER,DBSTORE
	}

}