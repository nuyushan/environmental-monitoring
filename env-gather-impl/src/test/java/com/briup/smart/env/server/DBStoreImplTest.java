package com.briup.smart.env.server;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Test;

public class DBStoreImplTest {
	@SuppressWarnings("deprecation")
	@Test
	public void test() throws Exception{
		Timestamp time = new Timestamp(1516323596029L);
		int date = time.getDate();
		System.out.println(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		int i = calendar.get(Calendar.DAY_OF_MONTH);//19
		System.out.println(i);
	}
}
