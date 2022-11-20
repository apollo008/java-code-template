package com.freedom.demoapp.subdemoapp.main;

import cn.itcast.jdbc.TxQueryRunner;
import com.freedom.commonutil.ExceptionUtil;
import com.freedom.commonutil.thread.TaskHandler;
import com.freedom.demoapp.util.RptparserConfigMgr;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *@description   研报解析处理器类，负责一批PDF文件的解析，解析完成后一次性批量更新到数据库上，批量更新才提高了更新数据库的效率。
 *@author dingbinthu@163.com
 *@create 2021-01-24, 20:51
 */
public class RptparseProcessor extends TaskHandler {
    static {
        //加载研报解析配置各参数
        RptparserConfigMgr.config();
    }

    public static Logger                      logger  = LoggerFactory.getLogger(RptparseProcessor.class);
    //数据库操作辅助类
    private QueryRunner                       qr      = new TxQueryRunner();

    public String                              dbName; //数据库库名
    public String                              tblName;//数据库表名
    private List<RptparseOnedbrecordHandler>   handlerList = new ArrayList<>(); //handler列表，每个handler负责1篇文档，即唯一一个PDF文件的解析

    public RptparseProcessor(String dbName, String tblName, List<RptparseOnedbrecordHandler> handlerList) {
        super();
        this.dbName = dbName;
        this.tblName = tblName;
        this.handlerList = handlerList;
    }

    /**
     * 方法描述:   genRptparseProcessor  根据指定数据库名、表名 和一批数据库PDF文件记录，生成对应的若干条onedbrecordHandler
     * 作    者：  dingbinthu@163.com
     * 日    期：  2021/2/14 17:08
     * @param     dbName  数据库名
     * @param     tblName  数据表名
     * @param     dbrecordMapList 数据库拉取的若干条PDF文件记录
     * @throw
     * @return    含若干onedbrecordHandler的RptparseProcessor对象
     */
    public static RptparseProcessor genRptparseProcessor(String dbName,String tblName,List<Map<String,Object>> dbrecordMapList) {
        List<RptparseOnedbrecordHandler> retList = new ArrayList<>();
        if (StringUtils.isBlank(dbName) || StringUtils.isBlank(tblName) || dbrecordMapList == null || dbrecordMapList.isEmpty()) {
            ;
        }
        else {
            for(Map<String,Object> m : dbrecordMapList) {
                if (null == m || m.isEmpty()) continue;
                String downloadfilePath = (String)m.get(RptparseDbColumn.DOWNLOADEDFILEPATH.getName());
                if (StringUtils.isBlank(downloadfilePath) || !downloadfilePath.endsWith(".pdf")) {
                    logger.error("!!!===== Omit invalid downloadfilepath: " + downloadfilePath);
                    continue;
                }
                retList.add(new RptparseOnedbrecordHandler(new RptparseOnedbrecord(dbName,tblName,m)));
            }
        }
        return new RptparseProcessor(dbName,tblName,retList);
    }

    public List<RptparseOnedbrecordHandler> getHandlerList() {
        return handlerList;
    }

    //该批次PDF文件记录解析完毕后，批量更新数据库，将解析结果写入数据库
    public void batchUpdateDb() throws Exception {
        if (handlerList.isEmpty()) return;

        int paramsCnt = RptparseDbColumn.PARSED_COLUMN_NAMES_BUT_PARSE_UPDATETIME.length;
        List<String> tmpList = new ArrayList<>();
        for(int i = 0; i < paramsCnt; ++i){
            tmpList.add("%s=?");
        }
        tmpList.add(RptparseDbColumn.PARSE_UPDATETIME.getName() + "=?");

        String sql = "UPDATE " + dbName + "." + tblName + " set " + String.format(StringUtils.join(tmpList,","),RptparseDbColumn.PARSED_COLUMN_NAMES_BUT_PARSE_UPDATETIME)
                +" WHERE " + RptparseDbColumn.ID.getName() + "=?";
        Object[][] params = new Object[handlerList.size()][];
        for(int i = 0; i < handlerList.size(); ++i) {
            List<Object> paramsList = new ArrayList<>();
            for(String columnName : RptparseDbColumn.PARSED_COLUMN_NAMES_BUT_PARSE_UPDATETIME ) {
                Object val = handlerList.get(i).getRecord().dbrecordMap.get(columnName);
                paramsList.add(val);
            }
            paramsList.add(new Date());
            paramsList.add(handlerList.get(i).getRecord().dbrecordMap.get(RptparseDbColumn.ID));
            params[i] = paramsList.toArray();
        }

        qr.batch(sql,params);
    }

    //主接口 批量解析数据库记录的PDF文件，完成后批量更新数据库
    public void run(){
        //遍历每个onedbrecordHandler
        //1个onedbrecordHandler对象处理一篇文档
        for(RptparseOnedbrecordHandler handler : handlerList) {
            try {
                //调用handler的run()函数解析一篇PDF文件
                handler.run();
            }
            catch (Exception ex) {
                logger.error(ExceptionUtil.getTrace(ex));
            }
        }
        try {
            batchUpdateDb();
        }
        catch (Exception ex1) {
            logger.error(ExceptionUtil.getTrace(ex1));
        }
        finally {
            for(RptparseOnedbrecordHandler handler : handlerList) {
               handler.destroy();
            }
            handlerList.clear();
            handlerList = null;
        }
    }
}
