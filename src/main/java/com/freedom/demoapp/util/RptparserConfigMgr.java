package com.freedom.demoapp.util;

import com.freedom.commonutil.C3p0Config;
import com.freedom.commonutil.Log4jConfig;
import com.freedom.commonutil.PathUtils;

/**
 *@description 研报解析配置管理器 ，加载log、c3p0数据库连接池和其它 static配置参数
 *@author dingbinthu@163.com
 *@create 2021-01-23, 11:50
 */
public class RptparserConfigMgr {
    static  {
        try{
            //加载log4j配置
            String configPath = PathUtils.getProjRoleDirPath(RptparserConfigMgr.class,"config");
            String log4jFilePath = configPath + "/" +  "log4j.properties";
            Log4jConfig.load(log4jFilePath);

            //加载c3p0数据库连接池配置信息
            String c3p0ConfigFilePath = configPath + "/" +  "c3p0-config.xml";
            C3p0Config.config(c3p0ConfigFilePath);

            //加载rptparser.properties配置与解析pdf相关的需要的配置
            String rptparserConfigFilePath = configPath + "/" + "demoapp.properties";
            RptparserConfig.config(rptparserConfigFilePath);

            //加载静态目录下文件，如股票信息名录和证券公司 期货公司等信息名录
            String staticConfigDirPath = configPath + "/" + "static";
            RptparserStaticFilesConfig.config(staticConfigDirPath);
        }
        catch (Exception ex) {
            throw  new RuntimeException(ex);
        }

    }

    public static void config() {}
}
