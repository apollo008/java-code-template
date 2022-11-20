package com.freedom.commonutil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *@author dingbinthu@163.com
 *@create 2018-11-30, 10:50
 */
public class FileUtils {
    /**
     * 修正路径，将 \\ 或 / 等替换为 File.separator
     * @param path 待修正的路径
     * @return 修正后的路径
     */
    public static String path(String path){
        String p = StringUtils.replace(path, "\\", "/");
        p = StringUtils.join(StringUtils.split(p, "/"), "/");
        if (!StringUtils.startsWithAny(p, "/") && StringUtils.startsWithAny(path, "\\", "/")){
            p += "/";
        }
        if (!StringUtils.endsWithAny(p, "/") && StringUtils.endsWithAny(path, "\\", "/")){
            p = p + "/";
        }
        if (path != null && path.startsWith("/")){
            p = "/" + p; // linux下路径
        }
        return p;
    }


    public static List<String> listFiles(File file, FileFilter ff, Boolean recursive) {
        List<String> retList = new ArrayList<String>();
        if (!recursive ) {
            File[] subFilesArr =  file.listFiles(ff);
            if (null == subFilesArr || subFilesArr.length == 0) {
                return retList;
            }
            for(File subF : subFilesArr) {
                retList.add(subF.getAbsolutePath());
            }
        }
        else {
            File[] subFilesArr =  file.listFiles((FileFilter)null);
            if (null != subFilesArr) {
                for(File subF : subFilesArr) {
                    if (subF.isDirectory()) {
                        List<String> subRetList = listFiles(subF,ff,true);
                        retList.addAll(subRetList);
                    }
                    else if(ff.accept(subF)){
                        retList.add(subF.getAbsolutePath());
                    }
                }
            }
        }
        return retList;
    }
}
