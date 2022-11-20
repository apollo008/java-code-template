package com.freedom.commonutil;


import org.apache.commons.lang.StringUtils;

/**
 *@description 字符串类使用操作封装整个类
 *@author dingbinthu@163.com
 *@create 2019-01-26, 22:12
 */
public class MyStringUtil {

    public static double  getSimilarity(String str1,  String str2) {
        int dist = StringUtils.getLevenshteinDistance(str1, str2);
        if (dist > Math.max(str1.length(), str2.length())) {
            dist = Math.max(str1.length(), str2.length());
        }
        double diff = dist * 1.0d / Math.max(str1.length(), str2.length());
        return 1.0f - diff;
    }
}
