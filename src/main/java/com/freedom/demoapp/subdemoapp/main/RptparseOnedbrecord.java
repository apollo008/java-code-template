package com.freedom.demoapp.subdemoapp.main;
import java.util.Map;
/**
 *@description 数据库记录类
 *@author dingbinthu@163.com
 *@create 2021-01-24, 20:27
 */
public class RptparseOnedbrecord {
    public String                           dbName; //库名
    public String                           tblName; //表名
    public Map<String,Object>               dbrecordMap; //数据库记录1条
    public RptparseOnedbrecord(String dbName, String tblName, Map<String,Object> dbrecordMap) {
        this.dbName = dbName;
        this.tblName = tblName;
        this.dbrecordMap = dbrecordMap;
    }
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public Map<String, Object> getDbrecordMap() {
        return dbrecordMap;
    }
    public void setDbrecordMap(Map<String, Object> dbrecordMap) {
        this.dbrecordMap = dbrecordMap;
    }
    public String getTblName() {
        return tblName;
    }
    public void setTblName(String tblName) {
        this.tblName = tblName;
    }
}
