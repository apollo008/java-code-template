package com.freedom.demoapp.subdemoapp.main;

import com.freedom.demoapp.util.RptparserConfigMgr;
/**
 *@author dingbinthu@163.com
 *@create 2021-01-24, 19:15
 */
public class RptParseApp {
    static {
        //加载解析配置管理器
        RptparserConfigMgr.config();
    }

    /*证券研报解析程序 主入口*/
    public static void main(String[] args) {
        //创建一个研报解析调度器 RptparseDispatcher 并run()运行
        new RptparseDispatcher().run();
    }
}
