package com.tingwen.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */
public class WxUserInfo {


    /**
     * openid : onjqeuAafW3DRD5lganauY1T6U3g
     * nickname : KiMiffy
     * sex : 1
     * language : zh_CN
     * city : Xiamen
     * province : Fujian
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epHCI7bRP1yUpClooT2kib7xnklOnibyVo7jX5xUJ2DduNsmJeicqesxZbpVViaLDMnajnN48QFZ6AOmA/0
     * privilege : []
     * unionid : oPvgSt9LmRP4Ag6HXK6gPDJvYhGU
     */

    private String openid;
    private String nickname;
    private String sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}
