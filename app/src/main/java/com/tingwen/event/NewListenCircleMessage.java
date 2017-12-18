package com.tingwen.event;

/**
 * 新的听友圈关于自己评论消息
 * Created by Administrator on 2017/11/10 0010.
 */
public class NewListenCircleMessage {
    int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public NewListenCircleMessage(int num) {

        this.num = num;
    }
}
