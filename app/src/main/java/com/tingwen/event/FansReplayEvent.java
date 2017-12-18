package com.tingwen.event;

/**
 * Created by Administrator on 2017/9/20 0020.
 */
public class FansReplayEvent {
    private String uid;
    private String id;
    private String to_uid;
    private String userNicename;
    private String userLogin;

    public FansReplayEvent(String uid,String id, String to_uid, String userNicename, String userLogin) {
        this.uid = uid;
        this.id = id;
        this.to_uid = to_uid;
        this.userNicename = userNicename;
        this.userLogin = userLogin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getUserNicename() {
        return userNicename;
    }

    public void setUserNicename(String userNicename) {
        this.userNicename = userNicename;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
