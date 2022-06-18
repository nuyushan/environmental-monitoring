package com.briup.smart.env.util;

import java.io.Serializable;

import org.junit.Test;

public class BackupImplTest {

	@Test
	public void test_store() {
		Backup backup = new BackupImpl();
		String fileName = "user.backup";
		Object data = new User("tom") ;
		try {
			backup.store(fileName , data , Backup.STORE_OVERRIDE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_load() {
		Backup backup = new BackupImpl();
		String fileName = "user.backup";
		try {
			Object obj = backup.load(fileName, Backup.LOAD_REMOVE);
			System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class User implements Serializable{

		private static final long serialVersionUID = 1L;
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public User(String name) {
			super();
			this.name = name;
		}
		@Override
		public String toString() {
			return "User [name=" + name + "]";
		}
	}
}