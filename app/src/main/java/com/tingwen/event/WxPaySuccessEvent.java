package com.tingwen.event;

/**
 * 微信支付成功
 * Created by Administrator on 2017/9/21 0021.
 */
public class WxPaySuccessEvent {

    private int type;
    public WxPaySuccessEvent( int type) {

        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
