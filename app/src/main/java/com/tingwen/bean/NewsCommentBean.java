package com.tingwen.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/26 0026.
 */
public class NewsCommentBean {

    /**
     * status : 1
     * msg : 请求成功!
     * results : [{"id":"186515","post_table":"posts","post_id":"77267","url":"index.php?g=&amp;m=article&amp;a=index&amp;id=77267","uid":"18653","to_uid":"0","full_name":"kimiffy","email":"","createtime":"2017-08-26 14:00:40","content":"还是美帝厉害","type":"1","parentid":"0","path":"0-186515","praisenum":"0","status":"1","timages":"","mp3_url":"","play_time":"0","avatar":"58be80544917a.jpeg","user_login":"tw173635","user_nicename":"kimiffy","sex":"1","signature":"Android开发从业人员","to_avatar":null,"to_user_login":null,"to_user_nicename":null,"to_sex":null,"to_signature":null,"emoji":"还是美帝厉害","praiseFlag":1}]
     */

    private int status;
    private String msg;
    /**
     * id : 186515
     * post_table : posts
     * post_id : 77267
     * url : index.php?g=&amp;m=article&amp;a=index&amp;id=77267
     * uid : 18653
     * to_uid : 0
     * full_name : kimiffy
     * email :
     * createtime : 2017-08-26 14:00:40
     * content : 还是美帝厉害
     * type : 1
     * parentid : 0
     * path : 0-186515
     * praisenum : 0
     * status : 1
     * timages :
     * mp3_url :
     * play_time : 0
     * avatar : 58be80544917a.jpeg
     * user_login : tw173635
     * user_nicename : kimiffy
     * sex : 1
     * signature : Android开发从业人员
     * to_avatar : null
     * to_user_login : null
     * to_user_nicename : null
     * to_sex : null
     * to_signature : null
     * emoji : 还是美帝厉害
     * praiseFlag : 1
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
        private String id;
        private String post_table;
        private String post_id;
        private String url;
        private String uid;
        private String to_uid;
        private String full_name;
        private String email;
        private String createtime;
        private String content;
        private String type;
        private String parentid;
        private String path;
        private String praisenum;
        private String status;
        private String timages;
        private String mp3_url;
        private String play_time;
        private String avatar;
        private String user_login;
        private String user_nicename;
        private String sex;
        private String signature;
        private String to_avatar;
        private String to_user_login;
        private String to_user_nicename;
        private String to_sex;
        private String to_signature;
        private String emoji;
        private int praiseFlag;

        public int getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

        public int endIndex;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPost_table() {
            return post_table;
        }

        public void setPost_table(String post_table) {
            this.post_table = post_table;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(String to_uid) {
            this.to_uid = to_uid;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPraisenum() {
            return praisenum;
        }

        public void setPraisenum(String praisenum) {
            this.praisenum = praisenum;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTimages() {
            return timages;
        }

        public void setTimages(String timages) {
            this.timages = timages;
        }

        public String getMp3_url() {
            return mp3_url;
        }

        public void setMp3_url(String mp3_url) {
            this.mp3_url = mp3_url;
        }

        public String getPlay_time() {
            return play_time;
        }

        public void setPlay_time(String play_time) {
            this.play_time = play_time;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getTo_avatar() {
            return to_avatar;
        }

        public void setTo_avatar(String to_avatar) {
            this.to_avatar = to_avatar;
        }

        public String getTo_user_login() {
            return to_user_login;
        }

        public void setTo_user_login(String to_user_login) {
            this.to_user_login = to_user_login;
        }

        public String getTo_user_nicename() {
            return to_user_nicename;
        }

        public void setTo_user_nicename(String to_user_nicename) {
            this.to_user_nicename = to_user_nicename;
        }

        public String getTo_sex() {
            return to_sex;
        }

        public void setTo_sex(String to_sex) {
            this.to_sex = to_sex;
        }

        public String getTo_signature() {
            return to_signature;
        }

        public void setTo_signature(String to_signature) {
            this.to_signature = to_signature;
        }

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
        }

        public int getPraiseFlag() {
            return praiseFlag;
        }

        public void setPraiseFlag(int praiseFlag) {
            this.praiseFlag = praiseFlag;
        }
    }
}
