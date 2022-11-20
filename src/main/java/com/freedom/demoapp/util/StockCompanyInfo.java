package com.freedom.demoapp.util;


/**
 * @description 证券公司名称信息类
 * @author dingbinthu@163.com
 * @create 2021-01-25, 14:22
 */
public class StockCompanyInfo {
    String name;   //名称
    String area;   //辖区
    String shortName;  //简称

    public StockCompanyInfo( String name,String area, String shortName) {
        this.name = name;
        this.area = area;
        this.shortName = shortName;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
