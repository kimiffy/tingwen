package com.tingwen.event;

/**
 * 打赏成功事件
 * Created by Administrator on 2017/10/27 0027.
 */
public class ShangSuccessEvent {
    int type;//1 新闻详情里的打赏  2 节目详情的打赏
    String money;

    public ShangSuccessEvent(int type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public ShangSuccessEvent(int type, String money) {
        this.type = type;
        this.money = money;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
