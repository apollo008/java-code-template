package com.freedom.commonutil;

import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *@description log日志打印模块配置类
 *@author dingbinthu@163.com
 *@create 2018-12-03, 18:45
 */
public class Log4jConfig {
    public static String          s_log4jFilePath;  //log4j配置文件路径
    public static boolean         s_isReload = true;   //是否支持配置文件信息动态热加载
    public static int             s_reloadInterval = 60000;  //配置文件动态热加载间隔时间


    public static void load(String log4jFilePath) throws Exception {
        s_log4jFilePath = log4jFilePath;
        if (!new File(s_log4jFilePath).exists()) {
            throw new Exception("log4j configure file: " + log4jFilePath + " does not exist!");
        }
        System.out.println("=====Use log4j configure file path:\t" + log4jFilePath );
        if (s_isReload) {
            // 间隔特定时间，检测文件是否修改，自动重新读取配置
            PropertyConfigurator.configureAndWatch(s_log4jFilePath,s_reloadInterval);
        }
        else {
            InputStream is =new BufferedInputStream(new FileInputStream(log4jFilePath));
            PropertyConfigurator.configure(is);
        }
    }
}
