package com.tingwen.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/25 0025.
 */
public class User implements Serializable {

    /**
     * status : 1
     * msg : 登录成功
     * results : {"id":"18653","user_login":"tw173635","user_pass":"c535018ee946e10adc3949ba59abbe56e057f20f883e89af","user_nicename":"kimiffy","user_phone":"18571622970","user_email":"","user_url":"","avatar":"58be80544917a.jpeg","post_contents":null,"sex":"1","birthday":null,"signature":"Android开发从业人员","last_login_ip":"27.154.74.38","last_login_time":"2017-07-25 15:44:54","create_time":"2017-03-01 13:48:18","user_activation_key":"","comment_count_anchor":"0","user_status":"1","score":"0","user_type":"2","listen_money":"0.00","gold":"221.00","experience":"669","level":"11","sign":"0","is_record":0,"member_type":0,"is_stop":1}
     */

    private int status;
    private String msg;
    /**
     * id : 18653
     * user_login : tw173635
     * user_pass : c535018ee946e10adc3949ba59abbe56e057f20f883e89af
     * user_nicename : kimiffy
     * user_phone : 18571622970
     * user_email :
     * user_url :
     * avatar : 58be80544917a.jpeg
     * post_contents : null
     * sex : 1
     * birthday : null
     * signature : Android开发从业人员
     * last_login_ip : 27.154.74.38
     * last_login_time : 2017-07-25 15:44:54
     * create_time : 2017-03-01 13:48:18
     * user_activation_key :
     * comment_count_anchor : 0
     * user_status : 1
     * score : 0
     * user_type : 2
     * listen_money : 0.00
     * gold : 221.00
     * experience : 669
     * level : 11
     * sign : 0
     * is_record : 0
     * member_type : 0
     * is_stop : 1
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

    public static class ResultsBean implements Serializable {
        private String id;
        private String user_login;
        private String user_pass;
        private String user_nicename;
        private String user_phone;
        private String user_email;
        private String user_url;
        private String avatar;
        private Object post_contents;
        private String sex;
        private Object birthday;
        private String signature;
        private String last_login_ip;
        private String last_login_time;
        private String create_time;
        private String user_activation_key;
        private String comment_count_anchor;
        private String user_status;
        private String score;
        private String user_type;
        private String listen_money;
        private String gold;
        private String experience;
        private String level;
        private String sign;
        public int member_type;//会员类型  默认 0 普通1 超级2
        public int is_stop;//非会员今日是否听完, 0 未听完 1已听完
        public int is_record;//购买课堂后提交用户信息,是否已经填写 0没填写 1已经填写


        public String guan_num;
        public String fan_num;
        public int fansNum;
        public int attentionsNum;

        private int login_type;//登录类型


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

        public Object getPost_contents() {
            return post_contents;
        }

        public void setPost_contents(Object post_contents) {
            this.post_contents = post_contents;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
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

        public String getListen_money() {
            return listen_money;
        }

        public void setListen_money(String listen_money) {
            this.listen_money = listen_money;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getIs_record() {
            return is_record;
        }

        public void setIs_record(int is_record) {
            this.is_record = is_record;
        }

        public int getMember_type() {
            return member_type;
        }

        public void setMember_type(int member_type) {
            this.member_type = member_type;
        }

        public int getIs_stop() {
            return is_stop;
        }

        public void setIs_stop(int is_stop) {
            this.is_stop = is_stop;
        }

        public int getLogin_type() {
            return login_type;
        }

        public void setLogin_type(int login_type) {
            this.login_type = login_type;
        }
        public String getGuan_num() {
            return guan_num;
        }

        public void setGuan_num(String guan_num) {
            this.guan_num = guan_num;
        }

        public String getFan_num() {
            return fan_num;
        }

        public void setFan_num(String fan_num) {
            this.fan_num = fan_num;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        public int getAttentionsNum() {
            return attentionsNum;
        }

        public void setAttentionsNum(int attentionsNum) {
            this.attentionsNum = attentionsNum;
        }



        @Override
        public String toString() {
            return "{" +
                    "id='" + id + '\'' +
                    ", user_login='" + user_login + '\'' +
                    ", user_pass='" + user_pass + '\'' +
                    ", user_nicename='" + user_nicename + '\'' +
                    ", user_phone='" + user_phone + '\'' +
                    ", user_email='" + user_email + '\'' +
                    ", user_url='" + user_url + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", post_contents=" + post_contents +
                    ", sex='" + sex + '\'' +
                    ", birthday=" + birthday +
                    ", signature='" + signature + '\'' +
                    ", last_login_ip='" + last_login_ip + '\'' +
                    ", last_login_time='" + last_login_time + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", user_activation_key='" + user_activation_key + '\'' +
                    ", comment_count_anchor='" + comment_count_anchor + '\'' +
                    ", user_status='" + user_status + '\'' +
                    ", score='" + score + '\'' +
                    ", user_type='" + user_type + '\'' +
                    ", listen_money='" + listen_money + '\'' +
                    ", gold='" + gold + '\'' +
                    ", experience='" + experience + '\'' +
                    ", level='" + level + '\'' +
                    ", sign='" + sign + '\'' +
                    ", is_record=" + is_record +
                    ", member_type=" + member_type +
                    ", is_stop=" + is_stop +
                    ", login_type=" + login_type +
                    ", guan_num=" + guan_num +
                    ", fan_num=" + fan_num +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", results=" + results.toString() +
                '}';
    }
}
