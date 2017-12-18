package com.tingwen.bean;

/**
 * 版本信息
 * Created by Administrator on 2017/10/17 0017.
 */
public class VersionBean {


    /**
     * versionName : 听闻7.3
     * versionInfo : 1.新增会员功能，购买会员可收听更多资讯哟 2.优化播放器功能,播放更流畅3.修复已知的bug
     * version : 56
     * apkUrl : http://www.tingwen.me/tingwen.apk
     * apkSize : 14592567
     * ios_version : 7.1
     */

    private String versionName;
    private String versionInfo;
    private int version;
    private String apkUrl;
    private String apkSize;
    private double ios_version;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public double getIos_version() {
        return ios_version;
    }

    public void setIos_version(double ios_version) {
        this.ios_version = ios_version;
    }
}
