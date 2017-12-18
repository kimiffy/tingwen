package com.tingwen.event;

/**
 * 验证码是否正确
 * Created by Administrator on 2017/7/26 0026.
 */
public class RegisterCodeEvent {
    private boolean isVcodeRight;
    private String phoneNum;
    private String codeNum;


    public RegisterCodeEvent(boolean isVcodeRight, String phone, String code) {
        this.isVcodeRight = isVcodeRight;
        this.phoneNum=phone;
        this.codeNum=code;
    }

    public String getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(String codeNum) {
        this.codeNum = codeNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean isVcodeRight() {
        return isVcodeRight;
    }

    public void setVcodeRight(boolean vcodeRight) {
        isVcodeRight = vcodeRight;
    }
}
