package com.freedom.demoapp.subdemoapp.main;

import com.freedom.demoapp.util.RptparserConfigMgr;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
/**
 *@description 研报解析调试类，用于给定一个mysql 研报记录的id list的文件，然后去解析这些id集合的研报，解析结果仍旧存入mysql数据库。
 *@author dingbinthu@163.com
 *@create 2021-01-24, 19:15
 */
public class RptParseDebug {
    static {
        //加载解析配置管理器
        RptparserConfigMgr.config();
    }

    private static Logger                            logger  = LoggerFactory.getLogger(RptParseDebug.class);
    /*证券研报解析程序 主入口*/
    public static void main(String[] args) throws Exception {
        if (null == args || args.length < 2) {
            logger.error("\n\nUsage:\t\tjava -cp xxxx.jar tableName  report-database-ids-file-path!\n");
            System.exit(-1);
        }

        //读取研报记录数据库主键id集合的文件
        String tblName = args[0];
        if (StringUtils.isBlank(tblName)) {
            logger.error("tableName can not be empty!");
            System.exit(-1);
        }

        BufferedReader br = new BufferedReader(new FileReader(args[1]));
        String line = null;
        List<String> idsList = new LinkedList<>();
        while(null != (line = br.readLine())) {
            line = StringUtils.trimToEmpty(line);
            if (StringUtils.isBlank(line)) continue;
            if (line.startsWith("#")) continue;
            idsList.add(line);
        }

        if (idsList.isEmpty()) {
            logger.error("report database ids-file content can not be empty!");
            System.exit(-1);
        }

        logger.error("\n\nTotally " + idsList.size() + " report database record will be parsed:\n\n");
        //创建一个研报解析调度器 RptparseDispatcher 并run()运行
        new RptparseDispatcher().runIdsList(tblName,idsList);
    }
}
