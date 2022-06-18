package com.briup.smart.env.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.JdbcUtil;
import com.briup.smart.env.util.Log;

public class DBStoreImpl implements DBStore,PropertiesAware,ConfigurationAware{
	private Log log ;
	private int batchsize ;

	@Override
	public void saveDB(Collection<Environment> c) throws Exception {
		/*
		 * 需求分析：
		 * 集合中拿出Environment对象，对象中的属性值插入到数据库中
		 * 
		 * 每条数据都是1-31号之间的
		 * 需要根绝数据是几号的，那么就插入到第几张表中
		 * 
		 * 因为需要大量同构的insert语句，所以可以使用ps
		 * 因为处理数据比较多，所以也可以使用批处理
		 */
		if(c == null || c.size() == 0) {
			log.warn("入库模块 拿到的数据集合为空 入库模块退出");
			return;
		}
		Connection conn =null;
		PreparedStatement ps = null;
		Set<Integer> set = new HashSet<>();
		try {
			log.debug("入库模块 获取数据连接对象");
			conn = JdbcUtil.getConn();
			log.debug("入库模块 设置事务为手动提交");
			conn.setAutoCommit(false);
			String sql = "";
			//判断已经到了第几条数据
			int index = 0;
			//当前数据的日期
			int current_dayOfMonth = -1;
			//上一条数据的日期
			int last_dayOfMonth = -1;
			log.info("入库模块 开始处理数据");
			for(Environment env:c) {
				Timestamp timestamp = env.getGather_date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(timestamp);
				current_dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				set.add(current_dayOfMonth);
				/*
				 * 第一次执行循环 --if(last_dayOfMonth == -1)
				 * 创建了一个sql语句以及ps这个对象
				 * 第二次执行循环
				 * 1、我这次还是和上一次的表相同，但是数据不同  可以使用同一个ps
				 * 2、表不同，数据不同 新建sql语句，新建一个ps对象
				 */
				if(last_dayOfMonth == -1) {
					sql = "insert into E_DETAIL_"+current_dayOfMonth+"(name,srcId,desId,devId,sersorAddress,count,cmd,status,data,gather_date) values(?,?,?,?,?,?,?,?,?,?)";
					ps = conn.prepareStatement(sql);
				}else {
					if(current_dayOfMonth!=last_dayOfMonth) {
						ps.executeBatch();
						index = 0;
						ps.close();
						sql = "insert into E_DETAIL_"+current_dayOfMonth+"(name,srcId,desId,devId,sersorAddress,count,cmd,status,data,gather_date) values(?,?,?,?,?,?,?,?,?,?)";
						ps = conn.prepareStatement(sql);		
					}
				}
				ps.setString(1, env.getName());
				ps.setString(2, env.getSrcId());
				ps.setString(3, env.getDesId());
				ps.setString(4, env.getDevId());
				ps.setString(5, env.getSersorAddress());
				ps.setInt(6, env.getCount());
				ps.setString(7, env.getCmd());
				ps.setInt(8, env.getStatus());
				ps.setFloat(9, env.getData());
				ps.setTimestamp(10, env.getGather_date());
				
				ps.addBatch();
				index++;
				
				if(index%batchsize==0) {
					ps.executeBatch();
					index = 0;
				}
				//循环结束 这一次的就变成了上一次的
				last_dayOfMonth = current_dayOfMonth;//19
			}
			ps.executeBatch();
			log.info("入库模块 数据处理完成");
			log.debug("入库模块 共处理数据"+c.size()+"条");
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps);
		}
		log.debug("入库模块 入库数据分布的表为："+set);
		
	}

	@Override
	public void setConfiguration(Configuration config) throws Exception {
		this.log = config.getLogger();				
	}

	@Override
	public void init(Properties properties) throws Exception {
		this.batchsize = Integer.valueOf(properties.getProperty("dbStore-batch-size"));				
	}
}