package com.tingwen.event;

/**
 * Created by Administrator on 2017/9/14 0014.
 */
public class NewsRefreshEvent {
    private int type ;

    public NewsRefreshEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
