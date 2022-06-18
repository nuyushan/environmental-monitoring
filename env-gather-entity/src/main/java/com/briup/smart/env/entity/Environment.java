package com.briup.smart.env.entity;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 环境存储实体类,包括环境种类(温度,湿度,二氧化碳,光照强度)
 */
public class Environment implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//环境种类名称 温度 湿度 二氧化碳 光照强度
	private String name;
	//发送端id
	private String srcId;
	//树莓派系统id
	private String desId;
	//实验箱区域模块id(1-8)
	private String devId;
	//模块上传感器地址
	private String sersorAddress;
	//传感器个数
	private int count;
	//发送指令标号 3表示接收数据 16表示发送命令
	private String cmd;
	//状态 默认1表示成功
	private int status;
	//环境值
	private float data;
	//采集时间
	private Timestamp gather_date;
	
	public Environment() {}
	
	public Environment(String name, String srcId, String desId, String devId,
			String sersorAddress, int count, String cmd, int status,
			float data, Timestamp gather_date) {
		this.name = name;
		this.srcId = srcId;
		this.desId = desId;
		this.devId = devId;
		this.sersorAddress = sersorAddress;
		this.count = count;
		this.cmd = cmd;
		this.status = status;
		this.data = data;
		this.gather_date = gather_date;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getDesId() {
		return desId;
	}
	public void setDesId(String desId) {
		this.desId = desId;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getSersorAddress() {
		return sersorAddress;
	}
	public void setSersorAddress(String sersorAddress) {
		this.sersorAddress = sersorAddress;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public float getData() {
		return data;
	}
	public void setData(float data) {
		this.data = data;
	}
	public Timestamp getGather_date() {
		return gather_date;
	}
	public void setGather_date(Timestamp gather_date) {
		this.gather_date = gather_date;
	}
	@Override
	public String toString() {
		return "Environment [name=" + name + ", srcId=" + srcId + ", desId="
				+ desId + ", devId=" + devId + ", sersorAddress="
				+ sersorAddress + ", count=" + count + ", cmd=" + cmd
				+ ", status=" + status + ", data=" + data + ", gather_date="
				+ gather_date + "]";
	}
}





