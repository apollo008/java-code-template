package com.freedom.demoapp.subdemoapp.main;
import cn.itcast.jdbc.TxQueryRunner;
import com.freedom.demoapp.util.RptparserConfig;
import com.freedom.demoapp.util.RptparserConfigMgr;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *@description 研报文档解析数据库记录解决器 类
 *@author dingbinthu@163.com
 *@create 2021-01-24, 19:12
 */
public class RptparseOnedbrecordHandler {
    static {
        //加载研报解析配置各参数
        RptparserConfigMgr.config();
    }
    public static AtomicInteger          succCount = new AtomicInteger(0);
    public static AtomicInteger          failCount = new AtomicInteger(0);

    private Logger                            logger  = LoggerFactory.getLogger(getClass());
    //数据库操作辅助类
    private QueryRunner                       qr      = new TxQueryRunner();
    //数据库记录类
    private RptparseOnedbrecord               record;

    public RptparseOnedbrecordHandler(RptparseOnedbrecord record) {
        this.record = record;
    }

    //解析一条数据库记录
    public void run() throws Exception {
        //纯PDF文件名
        String pureFileName = (String)record.getDbrecordMap().get(RptparseDbColumn.DOWNLOADEDFILEPATH.getName());
        //PDF存储全路径
        String pdfFilePath = RptparserConfig.s_rptRootDir + "/" + record.getDbName()
                + "/" + record.getTblName() + "/" + pureFileName;
        logger.error("handling pdf file:[" + pdfFilePath + "]");
        //构造RptparseOnedocHandler类,并调用run()函数解析该PDF文件
        new RptparseOnedocHandler(pdfFilePath,record).run();
        logger.error("FINISHED handling pdf file:[" + pdfFilePath + "]");
    }
    public RptparseOnedbrecord getRecord() {
        return record;
    }
    public void setRecord(RptparseOnedbrecord record) {
        this.record = record;
    }
    public void destroy() {
        record.dbrecordMap.clear();
        record.dbrecordMap = null;
    }
}
