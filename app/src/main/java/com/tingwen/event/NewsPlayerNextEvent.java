package com.tingwen.event;

/**
 * 切换下一条新闻
 * Created by Administrator on 2017/8/26 0026.
 */
public class NewsPlayerNextEvent {
    int position;

    public NewsPlayerNextEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
