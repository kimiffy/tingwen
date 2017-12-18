package com.tingwen.event;

/**
 * 下载界面删除按钮状态变化
 * Created by Administrator on 2017/10/25 0025.
 */
public class DownLoadChangeStateEvent {

    public DownLoadChangeStateEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    int state;

}
