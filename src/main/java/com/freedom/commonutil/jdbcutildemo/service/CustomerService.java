package com.freedom.commonutil.jdbcutildemo.service;

import cn.itcast.jdbc.JdbcUtils;
import com.freedom.commonutil.jdbcutildemo.dao.CustomerDao;
import com.freedom.commonutil.jdbcutildemo.domain.Customer;

import java.sql.SQLException;
import java.util.List;


/**
 * service 层   处理业务
 * @功能  1. 增加新客户
 *       2. 转账操作  需要事务
 *       3. 查询  BeanHandler(根据客户的username)
 *       4. 查询  BeanListHandler 
 *       7. 查询  ScalarHandler
 *  关于Map的结果集处理器不常用，就不再演示
 * @author ZHAOYUQIANG
 *
 */
public class CustomerService {
	/*
	 * 依赖CustomerDao
	 */
	CustomerDao cstmDao = new CustomerDao();

	/**
	 * 增加新客户
	 * @param cstm
	 * @throws SQLException 
	 */
	public void add(Customer cstm) throws SQLException{
		cstmDao.add(cstm);
	}

	/**
	 * 批量增加新客户
	 * @param cstm
	 * @throws SQLException
	 */
	public void batchAdd(Customer cstm,int num) throws SQLException{
		cstmDao.batchAdd(cstm,num);
	}

	/**
     * 转账
     *   A用户转给B用户money钱
     * @param nameA
     * @param nameB
     * @param money
     * @throws Exception 
     */
	public void transfer(String nameA,String nameB,double money) throws Exception{
		try{
			/*
			 * 1. 事务1 ： 开启事务
			 * 2. 处理业务
			 * 3. 事务2： 提交事务
			 * 4. 事务3 ：回滚事务
			 */
			JdbcUtils.beginTransaction();
			cstmDao.transfer(nameA,-money);
			cstmDao.transfer(nameB,money);
			JdbcUtils.commitTransaction();
		}catch(Exception e){
			try{
				JdbcUtils.rollbackTransaction();
			}catch(SQLException e1){		
			}
			throw e;
		}		
	}
	/**
	 * 查询  BeanHandler(根据客户的username)
	 * @param name
	 * @return
	 * @throws SQLException 
	 */
	public Customer query(String name) throws SQLException{
		Customer cstm = cstmDao.query01(name);
		return cstm ;
		
	}
	/**
	 * 查询   BeanListHandler 
	 * @return
	 * @throws SQLException
	 */
	public List<Customer> queryAll() throws SQLException{
		List<Customer> cstmList= cstmDao.queryAll02();
		return cstmList;
	} 
	/**
	 * 查询  ScalarHandler
	 * @return
	 * @throws SQLException
	 */
	public Number queryAllNum() throws SQLException{
		Number cnt= cstmDao.queryAllNum();
		return cnt ;
	}
	
}
