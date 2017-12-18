package com.tingwen.event;

/**
 * 已经下载的新闻, 编辑模式
 * Created by Administrator on 2017/10/25 0025.
 */
public class DownloadEditEvent {
    int state;//0 普通  1 编辑

    public DownloadEditEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
