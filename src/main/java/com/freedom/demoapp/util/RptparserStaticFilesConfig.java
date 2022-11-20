package com.freedom.demoapp.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @description static静态文件如证券公司名录、沪深两市股票代码信息、期货公司等文件配置
 * @author dingbinthu@163.com
 * @create 2021-01-25, 14:04
 */
public class RptparserStaticFilesConfig {
    public static Logger                  logger  = LoggerFactory.getLogger(RptparserConfig.class);
    public static List<StockCompanyInfo>  s_stockCompanyInfos = new ArrayList<>();  //证券公司信息

    public static void config(String staticDir) throws Exception {
        File staticDir1 = new File(staticDir);
        if (!staticDir1.exists() || !staticDir1.isDirectory()) {
            throw new Exception("static directory: " + staticDir + " does not exist or is not directory!");
        }
        logger.error("=====Use static directory is:\t" + staticDir);
        loadStaticFiles(staticDir);
    }

    /**
     * 方法描述:   loadStaticFiles   加载静态目录配置文件
     * 作    者：  dingbinthu@163.com
     * 日    期：  2019/2/14 14:34
     * @param     staticDir static目录路径
     * @return
     */
    private static void loadStaticFiles(String staticDir) throws Exception {
        loadStaticStockCompanyFiles(staticDir);
    }

    /**
     * 方法描述:   loadStaticStockCompanyFiles   加载静态证券公司信息
     * 作    者：  dingbinthu@163.com
     * 日    期：  2019/2/14 14:35
     * @param     staticDir 静态配置文件目录
     * @throw
     * @return
     */
    private static void loadStaticStockCompanyFiles(String staticDir) throws Exception {
        String fileName = "stock_company.txt";
        String filePath = staticDir + "/" + fileName;

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = null;
        while(null != (line = br.readLine())) {
            if (StringUtils.isBlank(line)) continue;
            if (line.startsWith("#")) continue;
            String[] arr = StringUtils.splitByWholeSeparator(line,",");
            String name = arr[0].trim();
            String area = arr[1].trim();
            String shortName = arr[2].trim();
            if (!StringUtils.isBlank(name) && !StringUtils.isBlank(area) && !StringUtils.isBlank(shortName)) {
                s_stockCompanyInfos.add(new StockCompanyInfo(name,area,shortName));
            }
        }
    }
}

