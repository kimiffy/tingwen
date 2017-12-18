package com.tingwen.event;

/**
 * 微信登录成功
 * Created by Administrator on 2017/9/21 0021.
 */
public class WxLoginSuccessEvent {
    private String code;

    public WxLoginSuccessEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
