package com.tingwen.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/1 0001.
 */
public class CommentUser implements Serializable {
    private String id;
    private String user_nicename;
    private String sex;
    private String signature;
    private String avatar;
    private String fan_num;
    private String guan_num;
    private String user_login;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFan_num() {
        return fan_num;
    }

    public void setFan_num(String fan_num) {
        this.fan_num = fan_num;
    }

    public String getGuan_num() {
        return guan_num;
    }

    public void setGuan_num(String guan_num) {
        this.guan_num = guan_num;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }
}
