package com.tingwen.event;

/**
 * 主界面播放器控制事件(旋转/停止旋转)
 * Created by Administrator on 2017/10/13 0013.
 */
public class PlayerBarEvent {

    int state;//1  旋转 2 停止

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public PlayerBarEvent(int state) {

        this.state = state;
    }
}
