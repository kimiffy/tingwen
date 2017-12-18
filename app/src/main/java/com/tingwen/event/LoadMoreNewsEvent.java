package com.tingwen.event;

/**
 * 自动加载更多新闻事件
 * Created by Administrator on 2017/11/3 0003.
 */
public class LoadMoreNewsEvent {
    int position;
    String  channel;

    public LoadMoreNewsEvent(int position, String channel) {
        this.position = position;
        this.channel = channel;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
