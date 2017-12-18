package com.tingwen.bean;

/**
 * Created by Administrator on 2017/10/27 0027.
 */
public class TingbiBalanceBean {

    /**
     * status : 1
     * msg : 获取成功!
     * results : {"listen_money":"0.11","id":"18653","user_login":"tw173635","user_pass":"c535018ee946e10adc3949ba59abbe56e057f20f883e89af","user_nicename":"kimiffy","user_phone":"18571622970","user_email":"","user_url":"","avatar":"59dc727d74fc9.jpeg","post_contents":null,"sex":"1","birthday":null,"signature":"安卓开发从业人员","last_login_ip":"27.154.74.111","last_login_time":"2017-10-27 08:57:51","create_time":"2017-03-01 13:48:18","user_activation_key":"","comment_count_anchor":"0","user_status":"1","score":"0","user_type":"2"}
     */

    private int status;
    private String msg;
    /**
     * listen_money : 0.11
     * id : 18653
     * user_login : tw173635
     * user_pass : c535018ee946e10adc3949ba59abbe56e057f20f883e89af
     * user_nicename : kimiffy
     * user_phone : 18571622970
     * user_email :
     * user_url :
     * avatar : 59dc727d74fc9.jpeg
     * post_contents : null
     * sex : 1
     * birthday : null
     * signature : 安卓开发从业人员
     * last_login_ip : 27.154.74.111
     * last_login_time : 2017-10-27 08:57:51
     * create_time : 2017-03-01 13:48:18
     * user_activation_key :
     * comment_count_anchor : 0
     * user_status : 1
     * score : 0
     * user_type : 2
     */

    private ResultsBean results;

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

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String listen_money;
        private String id;
        private String user_login;
        private String user_pass;
        private String user_nicename;
        private String user_phone;
        private String user_email;
        private String user_url;
        private String avatar;
        private String post_contents;
        private String sex;
        private String birthday;
        private String signature;
        private String last_login_ip;
        private String last_login_time;
        private String create_time;
        private String user_activation_key;
        private String comment_count_anchor;
        private String user_status;
        private String score;
        private String user_type;

        public String getListen_money() {
            return listen_money;
        }

        public void setListen_money(String listen_money) {
            this.listen_money = listen_money;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_pass() {
            return user_pass;
        }

        public void setUser_pass(String user_pass) {
            this.user_pass = user_pass;
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

        public String getUser_url() {
            return user_url;
        }

        public void setUser_url(String user_url) {
            this.user_url = user_url;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPost_contents() {
            return post_contents;
        }

        public void setPost_contents(String post_contents) {
            this.post_contents = post_contents;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUser_activation_key() {
            return user_activation_key;
        }

        public void setUser_activation_key(String user_activation_key) {
            this.user_activation_key = user_activation_key;
        }

        public String getComment_count_anchor() {
            return comment_count_anchor;
        }

        public void setComment_count_anchor(String comment_count_anchor) {
            this.comment_count_anchor = comment_count_anchor;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}
