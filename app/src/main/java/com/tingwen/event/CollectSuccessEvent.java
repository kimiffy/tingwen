package com.tingwen.event;

/**
 * 收藏成功
 * Created by Administrator on 2017/9/19 0019.
 */
public class CollectSuccessEvent {
   int type; //1 收藏成功

    public CollectSuccessEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
