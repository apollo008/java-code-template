package com.freedom.demoapp.subdemoapp.main;

import cn.itcast.jdbc.TxQueryRunner;
import com.freedom.commonutil.ExceptionUtil;
import com.freedom.demoapp.util.RptparserConfigMgr;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Map;
/**
 *@description    研报1个PDF文件解析解决器类
 *@author dingbinthu@163.com
 *@create 2021-01-24, 19:11
 */
public class RptparseOnedocHandler {
    static {
        //加载研报解析配置各参数
        RptparserConfigMgr.config();
    }

    protected Logger                  logger  = LoggerFactory.getLogger(getClass());
    //数据库操作辅助类
    protected QueryRunner             qr      = new TxQueryRunner();

    //PDF文件全路径
    private  String                   docFilePath;
    //数据库记录类对象
    private   RptparseOnedbrecord      record;
    public RptparseOnedocHandler(String docFilePath,RptparseOnedbrecord record) {
        this.record      = record;
    }

    private void parse() throws Exception {
        Map<String, Object> recordMap = this.record.getDbrecordMap();
        recordMap.put(RptparseDbColumn.PARSE_ORGNIZATION.getName(),"something");
        recordMap.put(RptparseDbColumn.PARSE_ORGNIZATION_NOTE.getName(),"{}");

        recordMap.put(RptparseDbColumn.PARSE_PAGECOUNT.getName(),0);
        recordMap.put(RptparseDbColumn.PARSE_REPORTDATE.getName(),new Date());
        recordMap.put(RptparseDbColumn.PARSE_REPORTDATE_NOTE.getName(),"{}");

        recordMap.put(RptparseDbColumn.PARSE_TITLE.getName(),"something");
        recordMap.put(RptparseDbColumn.PARSE_TITLE_NOTE.getName(),"{}");

        recordMap.put(RptparseDbColumn.PARSE_AUTHORS.getName(),"something");
        recordMap.put(RptparseDbColumn.PARSE_AUTHORS_NOTE.getName(),"{}");

        recordMap.put(RptparseDbColumn.PARSE_TYPE.getName(),"something");
        recordMap.put(RptparseDbColumn.PARSE_TYPE_NOTE.getName(),"{}");

        recordMap.put(RptparseDbColumn.PARSE_KEYPOINT.getName(),"{}");
        recordMap.put(RptparseDbColumn.PARSE_KEYPOINT_NOTE.getName(),"{}");

        recordMap.put(RptparseDbColumn.PARSE_CONTENT.getName(),"something");
        recordMap.put(RptparseDbColumn.PARSE_CONTENT_NOTE.getName(),"{}");

        recordMap.put(RptparseDbColumn.PARSE_CHART.getName(),"{}");
        recordMap.put(RptparseDbColumn.PARSE_CHART_NOTE.getName(),"{}");
    }

    public void run() throws Exception {
        try {
            parse();
            RptparseOnedbrecordHandler.succCount.incrementAndGet();
        }
        catch (Exception ex) {
            RptparseOnedbrecordHandler.failCount.incrementAndGet();
            throw new Exception(ExceptionUtil.getTrace(ex) + " When handling: " + docFilePath);
        }
    }
}
