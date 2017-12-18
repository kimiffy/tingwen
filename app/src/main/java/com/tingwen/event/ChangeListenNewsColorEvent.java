package com.tingwen.event;

/**
 * 改变当前正在播放的新闻颜色
 * Created by Administrator on 2017/11/2 0002.
 */
public class ChangeListenNewsColorEvent {

    private String channel;//频道
    private String id;//新闻id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChangeListenNewsColorEvent(String channel, String id) {
        this.channel = channel;
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
