package com.tingwen.event;

/**
 * 下载成功事件
 * Created by Administrator on 2017/10/24 0024.
 */
public class DownLoadFinishEvent {
    String id; //新闻id

    public DownLoadFinishEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
