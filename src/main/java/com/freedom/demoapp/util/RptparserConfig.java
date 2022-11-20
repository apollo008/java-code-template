package com.freedom.demoapp.util;

import cn.itcast.jdbc.TxQueryRunner;
import com.freedom.demoapp.subdemoapp.main.RptparseDbColumn;
import com.freedom.commonutil.ExceptionUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 *@ 研报解析配置参数信息表达类
 *@author dingbinthu@163.com
 *@create 2021-01-23, 11:34
 */
public class RptparserConfig {
    public static String                  s_configFilePath; // 配置文件路径
    public static Properties              s_configProps;// 配置参数properties

    public static String                  s_dbName;  //需要解析的研报数据存放之 数据库名
    public static String[]                s_tableNameArr;//需要解析的研报数据存放之 数据库表名，可能多张表

    public static String                  s_rptRootDir;//研报pdf文件存放根目录，以下是dbName/tableName/研报pdf文件名
    public static String                  s_parseResultRootDir;//研报pdf文件解析结果存放根目录
    public static Integer                 s_parseDbrecordcountOnetime = 100;//一次从数据库拉取多少个研报pdf文件解析

    public static Integer                 s_parseThreadNum = 6;//线程数

    public static Logger                  logger  = LoggerFactory.getLogger(RptparserConfig.class);
    //数据库操作辅助类
    private  static QueryRunner           qr = new TxQueryRunner();

    public static void config(String configFilePath) throws Exception {
        s_configFilePath = configFilePath;
        if (!new File(s_configFilePath).exists()) {
            throw new Exception("rptparser configuration file: " + s_configFilePath + " does not exist!");
        }
        logger.error("=====Use rptparser configuration file path is:\t" + configFilePath);

        s_configProps = new Properties();
        InputStream is = new FileInputStream(new File(s_configFilePath));
        s_configProps.load(is);

        checkValid();
    }

    //检查配置参数是否提供齐全或合法
    public static void checkValid() throws Exception {
        s_dbName = (String)s_configProps.get("db-name");
        if (StringUtils.isBlank(s_dbName)) {
            throw new Exception("db-name can not be empty!");
        }
        String tblNames = (String)s_configProps.get("table-name");
        if (StringUtils.isBlank(tblNames)) {
            throw new Exception("table-name can not be empty!");
        }
        s_tableNameArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(tblNames,",");
        if (null == s_tableNameArr || s_tableNameArr.length == 0 ) {
            throw new Exception("table-name can not be empty!");
        }

        for (String curTblName : s_tableNameArr) {
            if (StringUtils.isBlank(curTblName)) {
                throw new Exception("invalid table-name,please check!");
            }
            String sql = "SELECT count(1) as count FROM information_schema.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? ;";
            Object[] params = {s_dbName,curTblName} ;
            Map m =qr.query(sql, new MapHandler(),params);
            if (null==m || (Long)m.get("count") < 1) {
                throw new Exception("dbname: " + s_dbName + " ,tablename: " + curTblName + " does not exist,please check!");
            }
            sql = "select COLUMN_NAME from information_schema.COLUMNS where TABLE_SCHEMA = ? AND TABLE_NAME = ?; ";
            List<Map<String,Object>> mList = qr.query(sql, new MapListHandler(),params);
            if (null==mList || mList.isEmpty()) {
                throw new Exception("dbname: " + s_dbName + " ,tablename: " + curTblName + " has no columns definitions,please check!");
            }
            else {
                Set<String> columnsSet = new HashSet<>();
                for(Map m1 : mList) { columnsSet.addAll(m1.values()); }

                //注意，解析程序能跑的前提是，数据库表中对应的条目中必须含有指定的parsed_开头的多个列，用以
                //存放pdf解析结果。
                List<String> lackedColumnNamesList = new ArrayList<>();
                for(String cname: RptparseDbColumn.COLUMN_NAMES) {
                    if (!columnsSet.contains(cname)) { lackedColumnNamesList.add(cname); }
                }
                if (!lackedColumnNamesList.isEmpty()) {
                    throw new Exception("dbname: " + s_dbName + " ,tablename: " + curTblName + " lacks columns definitions: ["
                            + StringUtils.join(lackedColumnNamesList,",")
                            +"],please check!");
                }
            }
        }

        s_rptRootDir = (String)s_configProps.get("report-files-root-directory");
        if (StringUtils.isBlank(s_rptRootDir)) {
            throw new Exception("report-files-root-directory can not be empty!");
        }
        File rptRootDir1 = new File(s_rptRootDir);
        if (!rptRootDir1.exists() || !rptRootDir1.isDirectory()) {
            throw new Exception("report-files-root-directory does not exist or is not directory,please check!");
        }

        s_parseResultRootDir = (String)s_configProps.get("parsed-result-root-directory");
        if (StringUtils.isBlank(s_parseResultRootDir)) {
            throw new Exception("parsed-result-root-directory can not be empty!");
        }
        File parseResultRootDir1  = new File(s_parseResultRootDir);
        if (parseResultRootDir1.exists() && ( !parseResultRootDir1.isDirectory()  || !parseResultRootDir1.canWrite() ) ) {
            throw new Exception("parsed-result-root-directory is not directory or is unwritable!");
        }

        try {
            s_parseDbrecordcountOnetime = Integer.valueOf((String)s_configProps.get("parsed-dbrecords-count-onetime"));
        }
        catch (Exception ex) {
           logger.error("Encountered error when load config option:parsed-db-records-count: [" + ExceptionUtil.getTrace(ex) + "], used default value 100.");
        }

        try {
            s_parseThreadNum = Integer.valueOf((String)s_configProps.get("parsed-thread-count"));
        }
        catch (Exception ex) {
            logger.error("Encountered error when load config option:parsed-thread-count: [" + ExceptionUtil.getTrace(ex) + "], used default value 6.");
        }
    }
}
