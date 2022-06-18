package com.briup.smart.env.util;

//通过数据源进行相同代码的封装
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class JdbcUtil {
	private static DataSource dataSource;
	static {
		Properties pro = new Properties();
		InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("druid.properties");
		try {
			pro.load(is);
			dataSource = DruidDataSourceFactory.createDataSource(pro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 获取数据库连接对象
	public static Connection getConn() throws SQLException {
		return dataSource.getConnection();
	}
	// 执行sql
	public static void executeSql(String sql) {
		Connection conn = null;
		Statement st = null;
		try {
			conn = getConn();
			st = conn.createStatement();
			int num = st.executeUpdate(sql);
			System.out.println("sql语句执行完成");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, st);
		}
	}
	// 执行查询语句的时候，由于每个select语句最终产生的结果集是不同的，所以结果集怎么处理是需要
	// 执行查询语句的这个人自己将处理过程传递
	public static <T> List<T> executeSelect(String sql, Function<ResultSet, List<T>> fun) {
		Connection conn = null;
		Statement st = null;
		ResultSet set = null;
		List<T> t = null;
		try {
			conn = getConn();
			st = conn.createStatement();
			set = st.executeQuery(sql);
			System.out.println("------------");
			t = fun.apply(set);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	public static void close(Connection conn, Statement st) {
		close(null, conn, st);
	}
	// 关闭资源
	public static void close(ResultSet set, Connection conn, Statement st) {
		if (set != null) {
			try {
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
