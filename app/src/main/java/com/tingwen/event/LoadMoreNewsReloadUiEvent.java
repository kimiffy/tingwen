package com.tingwen.event;

/**
 * 自动加载更多后, 重新刷新新闻详情页
 * Created by Administrator on 2017/11/3 0003.
 */
public class LoadMoreNewsReloadUiEvent {
    int position;
    String channel;

    public LoadMoreNewsReloadUiEvent(int position, String channel) {
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
