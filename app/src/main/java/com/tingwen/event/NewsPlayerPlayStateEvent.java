package com.tingwen.event;

/**
 * 播放或者暂停事件
 * Created by Administrator on 2017/8/26 0026.
 */
public class NewsPlayerPlayStateEvent {
    int state;//1 播放  2暂停

    public NewsPlayerPlayStateEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
