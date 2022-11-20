package com.freedom.commonutil.jdbcutildemo.Test;

import com.freedom.commonutil.jdbcutildemo.domain.Customer;
import com.freedom.commonutil.jdbcutildemo.service.CustomerService;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * 测试类
 * @功能 1. 测试添加
 *      2. 测试查找(条件查找、全部查找、查找所有记录行数)
 *      3. 测试转账(事务)
 * @author ZHAOYUQIANG
 *
 */
public class TestDemo {
	/*
	 * 依赖Service
	 */
	CustomerService cstmService = new CustomerService();
	/**
	 * 测试   添加新客户
	 * @throws SQLException
	 */
	@Test
	public void test1() throws SQLException{
		Customer cstm1 = new Customer("王五",18,1000.000);
		Customer cstm2 = new Customer("赵六",98,1000.000);
		cstmService.add(cstm1);
		cstmService.add(cstm2);
	}
	/**
	 * 测试  转账
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		cstmService.transfer("王五", "赵六", 100.00);
	}
	/**
	 * 测试 条件查询
	 * @throws SQLException
	 */
	@Test
	public void test3() throws SQLException{
		Customer cstm = cstmService.query("王五");
		System.out.println(cstm.toString());

	}
	/**
	 * 测试 全部查询
	 * @throws SQLException
	 */
	@Test
	public void test4() throws SQLException{
		List<Customer> cstmList = cstmService.queryAll();
		for(int i =0 ;i<cstmList.size();i++){
			System.out.println(cstmList.get(i).toString());
		}

	}
	/**
	 * 测试 查询记录数
	 * @throws SQLException
	 */
	@Test
	public void test5() throws SQLException{
		Number num = cstmService.queryAllNum();
		System.out.println(num);

	}

	/**
	 * 测试   添加新客户
	 * @throws SQLException
	 */
	@Test
	public void test6() throws SQLException{
		Customer cstm1 = new Customer("张三",38,10000.000);
		cstmService.batchAdd(cstm1,1000000);
	}
}
