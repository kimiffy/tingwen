package com.tingwen.event;

/**
 * 新的意见反馈消息
 * Created by Administrator on 2017/11/9 0009.
 */
public class NewFeedBackMessageEvent {
    int num;

    public NewFeedBackMessageEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
