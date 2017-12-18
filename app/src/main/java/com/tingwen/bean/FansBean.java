package com.tingwen.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/26 0026.
 */
public class FansBean {

    /**
     * status : 1
     * msg : 请求成功!
     * results : [{"uid":"26774","created":"2017-05-18 14:32:03","friend_id":"18653","user_login":"tw1493948466266632","user_nicename":"从头","user_phone":null,"user_email":"","avatar":"590bd85c6b51f.jpeg","sex":"0","signature":null}]
     */

    private int status;
    private String msg;
    /**
     * uid : 26774
     * created : 2017-05-18 14:32:03
     * friend_id : 18653
     * user_login : tw1493948466266632
     * user_nicename : 从头
     * user_phone : null
     * user_email :
     * avatar : 590bd85c6b51f.jpeg
     * sex : 0
     * signature : null
     */

    private List<ResultsBean> results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String uid;
        private String created;
        private String friend_id;
        private String user_login;
        private String user_nicename;
        private String user_phone;
        private String user_email;
        private String avatar;
        private String sex;
        private String signature;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(String friend_id) {
            this.friend_id = friend_id;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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
    }
}
