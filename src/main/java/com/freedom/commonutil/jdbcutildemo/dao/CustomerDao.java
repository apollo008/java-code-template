package com.freedom.commonutil.jdbcutildemo.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.freedom.commonutil.jdbcutildemo.domain.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 *dao层  
 *    对数据库的操作 
 * @author ZHAOYUQIANG
 *
 */
public class CustomerDao {
	/*
	 * QueryRunner类是已经写好的类，里面有对数据库的操作方法，如query、update、delete等
	 *    TxQueryRunner继承了QueryRunner类，添加了Dao层对con的关闭
	 */
	private QueryRunner qr = new TxQueryRunner();
	/**
	 * 添加新客户
	 * @param cstm
	 * @throws SQLException
	 */
	public void add(Customer cstm) throws SQLException {
		/*
		 * 1. 创建TxQueryRunner对象 
		 * 2. 准备SQL模版     
		 * 3. 将参数存入参数数组 
		 * 4. 调用TxQueryRunner类中update方法  进行插入操作
		 */

		String sql ="insert into jdbcutildemo.t_customer values(?,?,?)";
		Object[] params = {cstm.getUsername(),
				cstm.getAge(),
				cstm.getBalance()};
		qr.update(sql, params);
	}

	/**
	 * 添加新客户
	 *   批处理
	 * @param cstm,num是批量插入记录的行数
	 * @throws SQLException
	 */
	public void batchAdd(Customer cstm , int num) throws SQLException {
    /*
     * 1. 创建TxQueryRunner对象
     * 2. 准备SQL模版
     * 3. 将参数存入参数数组
     * 4. 调用TxQueryRunner类中batch方法  进行插入操作
     */
		String sql ="insert into jdbcutildemo.t_customer values(?,?,?)";
		Object[][] params = new Object[num][];
		for(int i=0;i<params.length;i++){
			params[i]=new Object[]{cstm.getUsername()+i,
					cstm.getAge()+i,
					cstm.getBalance()};
		}
//        Object[] params = {cstm.getUsername(),
//                           cstm.getAge(),
//                           cstm.getBalance()};
		qr.batch(sql, params);
	}



	/**
	 * 转账
	 * @param nameA
	 * @param d
	 * @throws SQLException
	 */
	public void transfer(String name, double money) throws SQLException {
		/*
		 * 1. 创建TxQueryRunner对象 
		 * 2. 准备SQL模版     
		 * 3. 将参数存入参数数组 
		 * 4. 调用TxQueryRunner类中update方法  进行更改操作
		 */
		String sql = "update jdbcutildemo.t_customer set balance=balance+? where username=?";
		Object[] params = {money,name};
		qr.update(sql,params);
	}
	/**
	 * 查询  （按照username查询） 
	 * 结果集处理器1.  BeanHandler演示  查询结果是一个记录 
	 *      结果集处理器把返回的查询结果封装成JavaBean
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Customer query01(String name) throws SQLException {
		/*
		 * 1. 创建TxQueryRunner对象 
		 * 2. 准备SQL模版     
		 * 3. 将参数存入参数数组 
		 * 4. 调用query（sql,BeanHandler,params）,并且把查询结果封装到Customer对象中
		 * 5. 返回对象
		 */
		String sql ="select * from jdbcutildemo.t_customer where username=?";
		Object[] params = {name} ;
		Customer cstm =qr.query(sql,
				new BeanHandler<Customer>(Customer.class),params);
		return cstm ;
	}
	/**
	 * 查询  （查询所有用户，查询结果返回的是多个记录）
	 *  结果集处理器2.  BeanListHandler演示  
	 *     使用封装好的结果集处理器把返回的结果一条封装成一个Customer对象，多个对象构成List集合
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public List<Customer> queryAll02() throws SQLException {
		/*
		 * 1. 创建TxQueryRunner对象 
		 * 2. 准备SQL模版     
		 * 3. 将参数存入参数数组 
		 * 4. 调用query（sql,BeanListHandler）,
		 *        并且把查询结果一条记录封装到一个对象中，多个对象构成一个List集合
		 * 5. 返回对象集合
		 */
		String sql ="select * from jdbcutildemo.t_customer ";
		List<Customer> cstmList =  qr.query(sql,
				new BeanListHandler<Customer>(Customer.class));
		return cstmList ;
	}
	/**
	 * 查询  按照username查询 （查询结果是一个记录）
	 *  结果集处理器3.   MapHandler演示  
	 *     使用封装好的结果集处理器把返回的一条记录封装成一个Map
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Map query03(String name) throws SQLException {
		/*
		 * 1. 创建TxQueryRunner对象 
		 * 2. 准备SQL模版     
		 * 3. 将参数存入参数数组 
		 * 4. 调用query（sql,Map,params）,并且把查询结果封装到一个map中
		 * 5. 返回Map对象
		 */
		String sql ="select * from jdbcutildemo.t_customer where username=?";
		Object[] params = {name} ;
		Map cstmMap =qr.query(sql,
				new MapHandler(),params);
		return cstmMap ;
	}
	/**
	 * 查询   所有记录 （查询结果返回的是多个记录）
	 * 结果集处理器4.   MapListHandler演示  
	 *   使用封装好的结果集处理器把返回的结果一条封装成一个Map，多个Map构成List集合
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String,Object>> queryAll04() throws SQLException {
		/*
		 * 1. 创建TxQueryRunner对象 
		 * 2. 准备SQL模版     
		 * 3. 将参数存入参数数组 
		 * 4. 调用query（sql,MapListHandler）,并且把查询结果一条封装到一个map，多个map构成一个List集合
		 * 5. 返回MapList对象
		 */
		String sql ="select * from jdbcutildemo.t_customer";
		List<Map<String,Object>> cstmMapList =qr.query(sql,
				new MapListHandler());
		return cstmMapList ;
	}
	/**
	 * 查询 所有记录的行数    （返回的是一个数）
	 *
	 * 结果集处理器5.   ScalarHandler演示  
	 *
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Number queryAllNum() throws SQLException {
		/*
		 * 1. 创建TxQueryRunner对象 
		 * 2. 准备SQL模版     
		 * 3. 将参数存入参数数组 
		 * 4. 调用query（sql,ScalarHandler）
		 *       查询的结果先放在Number中，转成int
		 * 5. 返回Number对象
		 */
		String sql ="select count(*) from jdbcutildemo.t_customer";
		Number cnt =(Number)qr.query(sql,
				new ScalarHandler());
		int c = cnt.intValue();
//		long c= cnt.longValue();
		return c;
	}
}
