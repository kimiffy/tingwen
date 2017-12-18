package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 意见反馈中 和自己有关的消息
 * Created by Administrator on 2017/11/8 0008.
 */
public class FeedBackMessageBean implements Serializable {


    /**
     * status : 1
     * msg :
     * results : [{"id":"792","uid":"26779","comment":"不可以吗？","images":"","tuid":"18653","to_comment_id":"786","create_time":"1508308643","status":"1","is_comment":"1","user":{"id":"26779","user_nicename":"ios","user_login":"tw1493951584274536","sex":"1","signature":"该用户没有什么想说的","avatar":"59127a436af41.jpeg","fan_num":"1","guan_num":"0"}},{"id":"720","uid":"35847","comment":"到哪里下拉刷新","images":"","tuid":"18653","to_comment_id":"717","create_time":"1497531997","status":"1","is_comment":"1","user":{"id":"35847","user_nicename":"小蔡","user_login":"tw1497452058732025","sex":"0","signature":null,"avatar":"http://wx.qlogo.cn/mmopen/Q3auHgzwzM71H5EouFQzPq0Sdib31HLcWwrhgCPyQsxib54qGwFupnV5DZ4V4m6qR5x5zjGKFPrKxlwq7ucVWOn4RLooOnXSnSVcRiaQ5bcoiaM/0","fan_num":"0","guan_num":"0"}},{"id":"719","uid":"35847","comment":"谢谢你","images":"","tuid":"18653","to_comment_id":"717","create_time":"1497531816","status":"1","is_comment":"1","user":{"id":"35847","user_nicename":"小蔡","user_login":"tw1497452058732025","sex":"0","signature":null,"avatar":"http://wx.qlogo.cn/mmopen/Q3auHgzwzM71H5EouFQzPq0Sdib31HLcWwrhgCPyQsxib54qGwFupnV5DZ4V4m6qR5x5zjGKFPrKxlwq7ucVWOn4RLooOnXSnSVcRiaQ5bcoiaM/0","fan_num":"0","guan_num":"0"}},{"id":"687","uid":"30178","comment":"安卓","images":"","tuid":"18653","to_comment_id":"680","create_time":"1494909208","status":"1","is_comment":"1","user":{"id":"30178","user_nicename":"Imperfectmistake","user_login":"tw1494733639285430","sex":"0","signature":null,"avatar":"http://wx.qlogo.cn/mmopen/yictHsalOeDqMmV2qSXkEIVls0E9WQLWLOEyPaUb9K2oR73iag7KJgrCsJYvwibJDRZkQuJWRoibsDV7M2rDmKHyUFrPJ9tCXAOf/0","fan_num":"0","guan_num":"0"}},{"id":"684","uid":"26994","comment":"是的，乐视手机","images":"","tuid":"18653","to_comment_id":"675","create_time":"1494849820","status":"1","is_comment":"0","user":{"id":"26994","user_nicename":"张朝斌13689554203","user_login":"tw1494066555682067","sex":"0","signature":null,"avatar":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLA8IVvzzE7lOoAFsgic7UQRcT1CAyV2Hvx6xvnXRicgRZka3E0gjD1y0O6SJA0k7MWn32JoG6o9NMdQ/0","fan_num":"0","guan_num":"1"}}]
     */

    private int status;
    private String msg;
    /**
     * id : 792
     * uid : 26779
     * comment : 不可以吗？
     * images :
     * tuid : 18653
     * to_comment_id : 786
     * create_time : 1508308643
     * status : 1
     * is_comment : 1
     * user : {"id":"26779","user_nicename":"ios","user_login":"tw1493951584274536","sex":"1","signature":"该用户没有什么想说的","avatar":"59127a436af41.jpeg","fan_num":"1","guan_num":"0"}
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
        private String uid;
        private String comment;
        private String images;
        private String tuid;
        private String to_comment_id;
        private String create_time;
        private String status;
        private String is_comment;
        /**
         * id : 26779
         * user_nicename : ios
         * user_login : tw1493951584274536
         * sex : 1
         * signature : 该用户没有什么想说的
         * avatar : 59127a436af41.jpeg
         * fan_num : 1
         * guan_num : 0
         */

        private UserBean user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getTuid() {
            return tuid;
        }

        public void setTuid(String tuid) {
            this.tuid = tuid;
        }

        public String getTo_comment_id() {
            return to_comment_id;
        }

        public void setTo_comment_id(String to_comment_id) {
            this.to_comment_id = to_comment_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIs_comment() {
            return is_comment;
        }

        public void setIs_comment(String is_comment) {
            this.is_comment = is_comment;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements Serializable{
            private String id;
            private String user_nicename;
            private String user_login;
            private String sex;
            private String signature;
            private String avatar;
            private String fan_num;
            private String guan_num;

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

            public String getUser_login() {
                return user_login;
            }

            public void setUser_login(String user_login) {
                this.user_login = user_login;
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
        }
    }
}
