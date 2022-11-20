package com.freedom.demoapp.subdemoapp.main;
import java.util.ArrayList;
import java.util.List;
/**
 * @description 研报数据库column信息表达类
 * @author dingbinthu@163.com
 * @date 一月 24 2021, 21:10
 */
public enum RptparseDbColumn {
    ID("id",1),                         //id
    TYPE("type",2),                     //研报类型
    FULLLINK("fulllink",3),             //原文链接
    URL("url",4),                       //PDF url
    SOURCE("source",5),                 //来源
    TITLE("title",6),                   //标题
    ORGNIZATION("orgnization",7),       //机构
    REPORTDATE("reportdate",8),         //发布日期
    AUTHORS("authors",9),               //作者
    UPDATETIME("updatetime",10),        //更新时间
    DOWNLOADEDFILEPATH("downloadedfilepath",11),//下载PDF文件存放路径名

    PARSE_UPDATETIME("parse_updatetime",12),  //解析PDF动作的发生时间

    PARSE_TYPE("parse_type",13),             //解析PDF出的类型
    PARSE_ORGNIZATION("parse_orgnization",14), //解析PDF出的机构
    PARSE_TITLE("parse_title",15),            //解析PDF出的标题
    PARSE_REPORTDATE("parse_reportdate",16), // 解析PDF出的发布日期
    PARSE_AUTHORS("parse_authors",17),      //解析PDF出的分析师
    PARSE_PAGECOUNT("parse_pagecount",18),  //解析PDF的页码
    PARSE_KEYPOINT("parse_keypoint",19),    //解析PDF的要点
    PARSE_CONTENT("parse_content",20),      //解析PDF的正文
    PARSE_CHART("parse_chart",21),          //解析PDF的图表
    PARSE_TYPE_NOTE("parse_type_note",22),  //解析PDF的类型note
    PARSE_ORGNIZATION_NOTE("parse_orgnization_note",23), //解析PDF的机构note
    PARSE_TITLE_NOTE("parse_title_note",24),             //解析PDF的title note
    PARSE_REPORTDATE_NOTE("parse_reportdate_note",25),   //解析PDF的发布日期note
    PARSE_AUTHORS_NOTE("parse_authors_note",26),         //解析PDF的分析师note
    PARSE_PAGECOUNT_NOTE("parse_pagecount_note",27),     //解析PDF的pageCount Note
    PARSE_KEYPOINT_NOTE("parse_keypoint_note",28),       //解析PDF的要点note
    PARSE_CONTENT_NOTE("parse_content_note",29),         //解析PDF的正文note
    PARSE_CHART_NOTE("parse_chart_note",30);             //解析PDF的图表note

    //所有column的名字集合
    public static String[] COLUMN_NAMES = RptparseDbColumn.getAllNames();
    public static String[] PARSED_COLUMN_NAMES_BUT_PARSE_UPDATETIME = RptparseDbColumn.getAllParsedNamesButParseUpdatetime();
    //column名称
    private String name;
    //次序
    private int  index;

    RptparseDbColumn(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String[] getAllNames() {
        List<String> namesList = new ArrayList<>();
        for(RptparseDbColumn name: RptparseDbColumn.values() ) {
            namesList.add(name.getName());
        }
        return namesList.toArray(new String[0]);
    }

    public static String[] getAllParsedNamesButParseUpdatetime() {
        List<String> namesList = new ArrayList<>();
        for(RptparseDbColumn name: RptparseDbColumn.values() ) {
            if (name.getName().startsWith("parse_") && !name.getName().equals(PARSE_UPDATETIME.getName())) {
                namesList.add(name.getName());
            }
        }
        return namesList.toArray(new String[0]);
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
