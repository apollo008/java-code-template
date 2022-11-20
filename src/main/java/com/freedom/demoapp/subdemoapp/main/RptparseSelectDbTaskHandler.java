package com.freedom.demoapp.subdemoapp.main;

import cn.itcast.jdbc.TxQueryRunner;
import com.freedom.commonutil.ExceptionUtil;
import com.freedom.commonutil.thread.TaskHandler;
import com.freedom.demoapp.util.RptparserConfig;
import com.freedom.demoapp.util.RptparserConfigMgr;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 *@author dingbinthu@163.com
 *@create 2021-02-28, 3:50
 */
public class RptparseSelectDbTaskHandler extends TaskHandler {
    static {
        //加载研报解析配置各参数
        RptparserConfigMgr.config();
    }


    protected Logger                  logger  = LoggerFactory.getLogger(getClass());
    //数据库操作辅助类
    protected QueryRunner             qr      = new TxQueryRunner();

    protected String                  dbName  = RptparserConfig.s_dbName;
    protected List<String>            tblNamesList = new ArrayList<>(Arrays.asList(RptparserConfig.s_tableNameArr));

    protected BlockingQueue<TaskHandler> handlersQueue;
    protected String                    selectCode;
    protected List<String>             idsList; //如果不为空，表示只拉取数据库中指定这些id的记录
    protected String                   tblName;


    public RptparseSelectDbTaskHandler(BlockingQueue<TaskHandler> handlersQueue,String selectCode) {
        super();
        this.handlersQueue = handlersQueue;
        this.selectCode = selectCode;
    }

    public RptparseSelectDbTaskHandler(BlockingQueue<TaskHandler> handlersQueue,String selectCode,
                                       String tblName, List<String> idsList) {
        super();
        this.handlersQueue = handlersQueue;
        this.selectCode = selectCode;
        this.idsList = idsList;
        this.tblName = tblName;
    }

    @Override
    public void run() {
        if (null == idsList || idsList.isEmpty()) {
            selectDbData();
        }
        else {
            selectDbDataByIdsList();
        }
    }



    public void batchUpdateDbSelectCode(String tblName,List<Map<String,Object>> dbrecordMapList) throws Exception {
        if (null == dbrecordMapList || dbrecordMapList.isEmpty()) {
            return;
        }
        String sql = "UPDATE " + dbName + "." + tblName + " set select_code=?  WHERE " + RptparseDbColumn.ID.getName() + "=?";
        Object[][] params = new Object[dbrecordMapList.size()][];

        for(int i = 0; i < dbrecordMapList.size(); ++i) {
            List<Object> paramsList = new ArrayList<>();
            paramsList.add(selectCode);
            paramsList.add(dbrecordMapList.get(i).get(RptparseDbColumn.ID));
            params[i] = paramsList.toArray();
        }

        qr.batch(sql,params);
    }


    public void selectDbDataByIdsList() {
        if (null == idsList || idsList.isEmpty() || StringUtils.isBlank(tblName)) return;

        Object[] selectParams = new Object[]{selectCode};

        while (!idsList.isEmpty()) {
            List<String> curIdsList = new LinkedList<>();
            while (curIdsList.size() < RptparserConfig.s_parseDbrecordcountOnetime && !idsList.isEmpty()) {
                String tmpId = idsList.remove(0);
                curIdsList.add(tmpId);
            }

            String sql = String.format("SELECT * FROM %s.%s WHERE id in ('%s') AND ( select_code is NULL OR select_code != ? ) ",
                    dbName, tblName, StringUtils.join(curIdsList, "','"));

            //一次拉取指定数量的数据库记录
            try {
                //查询数据库，一次拉取指定数量的记录，
                List<Map<String, Object>> dbrecordMapList = qr.query(sql, new MapListHandler(), selectParams);
                if (null == dbrecordMapList || dbrecordMapList.isEmpty()) {
                    continue;
                }
                batchUpdateDbSelectCode(tblName, dbrecordMapList);

                //以一次拉取的若干数量的数据库记录 构造RptparseProcessor对象，调用其run()函数解析PDF文件
                RptparseProcessor processor = RptparseProcessor.genRptparseProcessor(dbName, tblName, dbrecordMapList);
                if (!processor.getHandlerList().isEmpty()) {
                    handlersQueue.add(processor);
                }
            } catch (Exception ex1) {
                logger.error(ExceptionUtil.getTrace(ex1));
            }
        }
    }


   public void selectDbData() {
       Object[] selectParams = new Object[]{selectCode, RptparserConfig.s_parseDbrecordcountOnetime };
       while(!tblNamesList.isEmpty()) {
           logger.warn("new round in selectDbdata, and tblanemsList is:[" + StringUtils.join(tblNamesList.toArray(),",") + "] and handlesQueue.size=" + handlersQueue.size());
           try {
               if (handlersQueue.size() <= RptparserConfig.s_parseThreadNum + 5) {
                   //遍历每个数据库table
                   for(String tblName :tblNamesList) {
                       logger.warn("select pdf info from table: " + tblName + " ,while tablnames is: " + StringUtils.join(tblNamesList.toArray(),",") + " and handlesQueue.size=" + handlersQueue.size());
                       String sql = String.format("SELECT * FROM %s.%s WHERE parse_updatetime is null AND ( select_code is NULL OR select_code != ? ) ORDER BY reportdate DESC limit ?", dbName,tblName);
                       //一次拉取指定数量的数据库记录
                       try {
                           //查询数据库，一次拉取指定数量的记录，
                           List<Map<String,Object>> dbrecordMapList = qr.query(sql,new MapListHandler(),selectParams);
                           if (null == dbrecordMapList || dbrecordMapList.isEmpty()) {
                               tblNamesList.remove(tblName);
                               break;
                           }
                           batchUpdateDbSelectCode(tblName,dbrecordMapList);
                           logger.warn("finished selecting pdf " + dbrecordMapList.size() +" info from table: " + tblName + " ,while tablnames is: " + StringUtils.join(tblNamesList.toArray(),",") + " and handlesQueue.size=" + handlersQueue.size());


                           //以一次拉取的若干数量的数据库记录 构造RptparseProcessor对象，调用其run()函数解析PDF文件
                           RptparseProcessor  processor  =  RptparseProcessor.genRptparseProcessor(dbName,tblName,dbrecordMapList);
                           if (!processor.getHandlerList().isEmpty()) {
                               handlersQueue.add(processor);
                           }
                       }
                       catch (Exception ex1) {
                           logger.error(ExceptionUtil.getTrace(ex1));
                       }
                   }
               }
               else {
                   Thread.sleep(100);
               }
           }
           catch (Exception ex2) {
               logger.error(ExceptionUtil.getTrace(ex2));
           }
       }
   }
}
