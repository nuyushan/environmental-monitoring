package com.briup.smart.env.client;

import java.util.Collection;

import org.junit.Test;

import com.briup.smart.env.entity.Environment;

public class GatherImplTest {

	@Test
	public void test_copy() throws Exception {
		GatherImpl g = new GatherImpl();
		Student s1 = new Student(1, "tom", 20);
		Student copy = g.copy(s1);
		System.out.println(s1);
		System.out.println(copy);
	}
	
	@Test
	public void test_substring() throws Exception {
		String data = "5d606f7802";
		System.out.println(data.substring(0,4));
		System.out.println(data.substring(4,8));
		System.out.println(Integer.parseInt("a",16));
	}
	
	@Test
	public void test_other() throws Exception {
		Gather g = new GatherImpl();
		Collection<Environment> gather = g.gather();
		System.out.println(gather.size());
	}
	
	@Test
	public void test_other1() throws Exception {
		System.out.println("hello\rworld");
	}
}
class Student{
	private int id;
	private String name;
	private int age;

	public Student() {
		super();
	}

	public Student(int id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}