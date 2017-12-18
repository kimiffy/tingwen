package com.tingwen.event;

/**
 * 切换上一条新闻
 * Created by Administrator on 2017/8/26 0026.
 */
public class NewsPlayerPreviousEvent {
    int position;

    public NewsPlayerPreviousEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
