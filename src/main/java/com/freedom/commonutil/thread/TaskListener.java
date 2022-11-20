package com.freedom.commonutil.thread;

/**
 *@author dingbinthu@163.com
 *@create 2019-02-27, 23:49
 */
public interface  TaskListener {
    public void onSuccess(TaskHandler handler);
    public void onError(TaskHandler handler);
}
