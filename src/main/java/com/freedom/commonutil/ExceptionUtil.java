package com.freedom.commonutil;

/**
 *@description 获取exception的详细信息
 *@author dingbinthu@163.com
 *@create 2017-05-26, 7:50
 */
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    public static String getTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
