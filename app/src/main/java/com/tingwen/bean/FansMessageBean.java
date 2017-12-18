package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10 0010.
 */
public class FansMessageBean implements Serializable {


    /**
     * status : 1
     * msg : 请求成功!
     * results : [{"id":"1120","post_table":"act","post_id":"161","url":"index.php?g=&amp;m=article&amp;a=index&amp;id=161","uid":"48667","to_uid":"0","full_name":"Viceroy","email":"","createtime":"2017-08-10 08:21:43","content":"大美女","type":"1","parentid":"0","path":null,"praisenum":"0","status":"1","avatar":"http://q.qlogo.cn/qqapp/1101441286/C3417F620FD049F6AABF5CE84CF56DE4/100","user_login":"tw1502323080429138","user_nicename":"Viceroy","sex":"0","signature":null,"to_avatar":null,"to_user_login":null,"to_user_nicename":null,"to_sex":null,"to_signature":null,"emoji":"大美女","praiseFlag":1}]
     */

    private int status;
    private String msg;
    /**
     * id : 1120
     * post_table : act
     * post_id : 161
     * url : index.php?g=&amp;m=article&amp;a=index&amp;id=161
     * uid : 48667
     * to_uid : 0
     * full_name : Viceroy
     * email :
     * createtime : 2017-08-10 08:21:43
     * content : 大美女
     * type : 1
     * parentid : 0
     * path : null
     * praisenum : 0
     * status : 1
     * avatar : http://q.qlogo.cn/qqapp/1101441286/C3417F620FD049F6AABF5CE84CF56DE4/100
     * user_login : tw1502323080429138
     * user_nicename : Viceroy
     * sex : 0
     * signature : null
     * to_avatar : null
     * to_user_login : null
     * to_user_nicename : null
     * to_sex : null
     * to_signature : null
     * emoji : 大美女
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

    public static class ResultsBean implements Serializable{
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
        public int endIndex;
        private int praiseFlag;



        public int getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

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

        public Object getPath() {
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
