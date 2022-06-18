package com.briup.smart.env.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import com.briup.smart.env.support.PropertiesAware;

public class BackupImpl implements Backup,PropertiesAware{
	private String basePath = "src/main/resources/backup";

	@Override
	public Object load(String fileName, boolean del) throws Exception {
		ObjectInputStream in = null;
		Object obj = null;
		try {
			String filePath = basePath+"/"+fileName;
			File file = new File(filePath);
			if(!file.exists()) {
				return null;
			}
			in = new ObjectInputStream(new FileInputStream(file));
			obj = in.readObject();
			if(del) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in!=null) {
				in.close();
			}
		}
		return obj;
	}

	@Override
	public void store(String fileName, Object obj, boolean append) throws Exception {
		ObjectOutputStream out = null;
		try {
			String filePath = basePath+"/"+fileName;
			File file = new File(filePath);
			//判断文件所在的目录是否存在，如果不存在，就创建目录
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
			out = new ObjectOutputStream(new FileOutputStream(file,append));
			out.writeObject(obj);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out!=null) {
				out.close();
			}
		}
	}

	@Override
	public void init(Properties properties) throws Exception {
		this.basePath = properties.getProperty("backup-file-path");
	}
}