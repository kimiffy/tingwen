package com.tingwen.event;

/**
 * 批量下载
 * Created by Administrator on 2017/10/25 0025.
 */
public class BatchDownloadEditEvent {
    int state;//0 普通  1 编辑

    public BatchDownloadEditEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
