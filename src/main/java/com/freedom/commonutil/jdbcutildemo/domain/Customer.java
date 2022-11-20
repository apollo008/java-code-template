package com.freedom.commonutil.jdbcutildemo.domain;



/*
CREATE TABLE jdbcutildemo.t_customer (
username VARCHAR(50) DEFAULT NULL,
age INT(11) DEFAULT NULL,
balance DOUBLE(20,5) DEFAULT NULL
);
 */


/**
 * 实体类
 *    变量名字最好与数据库中对应字段名一样，便于封装操作
 * @author ZHAOYUQIANG
 *
 */
public class Customer {
	private String username ; //用户
	private int age ;  //年龄
	private double balance ; //资金
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Customer [username=" + username + ", age=" + age + ", balance="
				+ balance + "]";
	}
	public Customer(String username, int age, double balance) {
		super();
		this.username = username;
		this.age = age;
		this.balance = balance;
	}
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
