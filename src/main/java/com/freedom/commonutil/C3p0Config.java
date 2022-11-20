package com.freedom.commonutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *@description C3p0数据库连接池配置类
 *@author dingbinthu@163.com
 *@create 2018-12-03, 20:18
 */
public class C3p0Config {
    public static String                  s_c3p0ConfigFilePath;   //c3p0 配置文件路径
    public static Logger                  logger  = LoggerFactory.getLogger(C3p0Config.class);

    public static void config(String configFilePath) throws Exception {
        s_c3p0ConfigFilePath = configFilePath;
        if (!new File(s_c3p0ConfigFilePath).exists()) {
            throw new Exception("c3p0 configuration file: " + s_c3p0ConfigFilePath + " does not exist!");
        }
        logger.error("=====Use c3p0 configuration file path is:\t" + configFilePath);
        System.setProperty("com.mchange.v2.c3p0.cfg.xml",configFilePath);
    }
}
