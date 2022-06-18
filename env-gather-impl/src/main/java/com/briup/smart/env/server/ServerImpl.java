package com.briup.smart.env.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;

public class ServerImpl implements Server,PropertiesAware,ConfigurationAware{
	private int serverPort;
	private int stopPort;
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream in;
	private volatile boolean isStop;
	private Log log;
	//需要进行赋值
	private DBStore dbstore;
	private ExecutorService service = Executors.newFixedThreadPool(5);
	
	@Override
	public void reciver() throws Exception {
		try {
			serverSocket = new ServerSocket(serverPort);
			log.info("服务器模块 监听的端口号"+serverPort);
			//开启stopServer()服务器
			//将来如果有连接该服务器的请求后，会自动调用shutdown()方法
			stopServer();
			while(!isStop) {
				log.info("服务器模块 等待客户端连接");
				socket = serverSocket.accept();
				service.execute(()->{
					try {
						System.out.println(Thread.currentThread().getName());
						log.info("服务器模块 接收到客户端连接"+socket);
						in = new ObjectInputStream(socket.getInputStream());
						@SuppressWarnings("unchecked")
						Collection<Environment> c = (Collection<Environment>) in.readObject();
						log.info("服务器模块 数据交给入库模块进行操作");
						dbstore.saveDB(c);
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						if(in!=null) {
							try {
								in.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
			log.info("服务器模块 服务器停止运行");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(socket!=null) {
				socket.close();
			}
			if(serverSocket!=null) {
				serverSocket.close();
			}
		}
	}
	private void stopServer() {
		new Thread() {	
			public void run() {
				//时间点
				ServerSocket serverSocket = null;
				try {
					serverSocket = new ServerSocket(stopPort);
					serverSocket.accept();
					log.info("\t stop服务器调用了shutdown方法");
					ServerImpl.this.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if(serverSocket!=null) {
						try {
							serverSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}				
			};
		}.start();
		log.info("服务器模块 启动stopServer,监听端口："+stopPort);
	}

	@Override
	public void shutdown() throws Exception {
		this.isStop = true;
		service.shutdown();
	}
	@Override
	public void setConfiguration(Configuration config) throws Exception {
		this.log = config.getLogger();	
		this.dbstore = config.getDbStore();
	}
	@Override
	public void init(Properties properties) throws Exception {
		this.serverPort = Integer.valueOf(properties.getProperty("server-port"));		
		this.stopPort = Integer.valueOf(properties.getProperty("server-stop-port"));		
	}
}