package com.tingwen.event;

/**
 * 登录成功事件
 * Created by Administrator on 2017/8/22 0022.
 */
public class LoginSuccessEvent {
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LoginSuccessEvent(String type) {

        this.type = type;
    }
}
