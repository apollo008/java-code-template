package com.freedom.commonutil;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *@description Date相关计算逻辑封装整个类
 *@author dingbinthu@163.com
 *@create 2019-01-26, 16:37
 */
public class MyDateUtils {

    /**
     * 比较两个日期Date2比Date1(年月日)多的天数,(只考虑天数不考虑时间)
     * 例如:2017-01-25 23:59:59与 2017-01-26 00:00:00   返回1
     * 2017-01-25 与 2017-01-25   返回0
     * 2017-01-28 与 2017-01-25   返回-3
     * @author terry.peng
     */
    public static int differDay(Date Date1,Date Date2) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.clear();
        calendar.setTime(Date1);
        int day1 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
        int year1 = calendar.get(java.util.Calendar.YEAR);
        calendar.setTime(Date2);
        int day2 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
        int year2 = calendar.get(java.util.Calendar.YEAR);
        if (year1 == year2) {//同一年
            return day2 - day1;
        } else if (year1 < year2) {//Date1<Date2
            int days = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {//闰年
                    days += 366;
                } else {
                    days += 365;
                }
            }
            return days + (day2 - day1);
        } else {//Date1>Date2
            int days = 0;
            for (int i = year2; i < year1; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    days += 366;
                } else {
                    days += 365;
                }
            }
            return 0 - days + (day2 - day1);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(differDay(new SimpleDateFormat("yyyyMMdd").parse("20170317"), new SimpleDateFormat("yyyyMMdd").parse("20170328")));
    }
}
