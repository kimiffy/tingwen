package com.tingwen.event;

/**
 * 刷新课堂试听UI事件
 * Created by Administrator on 2017/10/12 0012.
 */
public class ClassListenEvent {
    int state;//1 播放  2停止播放    3显示底部暂停按钮  4显示底部播放按钮
    String mp3;//当前播放的Mp3

    public ClassListenEvent(int state, String mp3) {
        this.state = state;
        this.mp3 = mp3;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }
}
