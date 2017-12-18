package com.tingwen.event;

/**
 * 已经阅读了新消息
 * Created by Administrator on 2017/11/9 0009.
 */
public class HasReadMessageEvent {
    int type;//消息类别 0 :听友圈 1: 意见反馈

    public HasReadMessageEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
