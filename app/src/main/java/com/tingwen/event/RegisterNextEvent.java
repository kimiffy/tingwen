package com.tingwen.event;

/**
 * 注册 下一步事件
 * Created by Administrator on 2017/7/26 0026.
 */
public class RegisterNextEvent {

    private int message;//1 当前处于填写验证码  2 当前处于填写密码

    public RegisterNextEvent(int i) {
        this.message = i;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
