package com.freedom.demoapp.subdemoapp.main;

import cn.itcast.jdbc.TxQueryRunner;
import com.freedom.commonutil.ExceptionUtil;
import com.freedom.commonutil.thread.TaskDispatcher;
import com.freedom.commonutil.thread.TaskHandler;
import com.freedom.demoapp.util.RptparserConfig;
import com.freedom.demoapp.util.RptparserConfigMgr;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *@description 研报解析调度器类,负责   1.从数据库拉取研报PDF文件和相关信息，一次拉取指定数量的PDF文件记录。
 *                                 2.一次拉取产生的指定数量记录集合 构造一个RptparseProcessor对象，调用该对象的run()解析研报各部分要素
 *                                 3.解析结束时打印解析过程中的统计信息，成功多少，失败多少，占比多少
 *@author dingbinthu@163.com
 *@create 2021-01-24, 19:13
 */
public class RptparseDispatcher {
    static {
        //加载研报解析配置各参数
        RptparserConfigMgr.config();
    }

    protected Logger                  logger  = LoggerFactory.getLogger(getClass());
    //数据库操作辅助类
    protected QueryRunner             qr      = new TxQueryRunner();
    protected String                  uuid    = UUID.randomUUID().toString();
    public RptparseDispatcher() {
    }
    //打印解析过程各要素成功失败数量统计信息
    public void printStatisInfos() {
        logger.error("=====It finally finised with succ/fail count:[{}/{}]=========",
                RptparseOnedbrecordHandler.succCount.get(),
                RptparseOnedbrecordHandler.failCount.get());
    }
    public void runIdsList(String tblName,List<String> idsList) {
        try {
            logger.error("------------Begin RptparseDispatcher::run() with uuid: " + uuid + " ------------");

            BlockingQueue<TaskHandler> handlersQueue = new LinkedBlockingDeque<>();
            handlersQueue.add(new RptparseSelectDbTaskHandler(handlersQueue,uuid,tblName,idsList));
            TaskDispatcher.create()
                    .setUUID(uuid)
                    .schedulerQueue(handlersQueue)
                    .thread(RptparserConfig.s_parseThreadNum)
                    .run();
        }
        catch (Exception ex) {
            logger.error(ExceptionUtil.getTrace(ex));
        }
        finally {
            //打印解析统计信息
            printStatisInfos();

            logger.error("------------End RptparseDispatcher::run() with uuid: " + uuid + " ------------");
        }
    }
    public void run() {
        try {
            logger.error("------------Begin RptparseDispatcher::run() with uuid: " + uuid + " ------------");

            BlockingQueue<TaskHandler> handlersQueue = new LinkedBlockingDeque<>();
            handlersQueue.add(new RptparseSelectDbTaskHandler(handlersQueue,uuid));
            TaskDispatcher.create()
                    .setUUID(uuid)
                    .schedulerQueue(handlersQueue)
                    .thread(RptparserConfig.s_parseThreadNum)
                    .run();
        }
        catch (Exception ex) {
            logger.error(ExceptionUtil.getTrace(ex));
        }
        finally {
            //打印解析统计信息
            printStatisInfos();
            logger.error("------------End RptparseDispatcher::run() with uuid: " + uuid + " ------------");
        }
    }
}
