package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/31 0031.
 */
public class ListenCircleBean implements Serializable{

    private int status;
    private String msg;
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
        private String post_id;
        private String uid;
        private String to_uid;
        private String createtime;
        private String comment;
        private String parentid;
        private String praisenum;
        private String status;
        private String timages;
        private String mp3_url;
        private String play_time;
        private UserBean user;
        private int flag;//判断是否是长文字
        private PostBean post;
        private int zan;

        private List<ChildCommentBean> child_comment;




        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getUid() {
            return uid;
        }
        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
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

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public PostBean getPost() {
            return post;
        }

        public void setPost(PostBean post) {
            this.post = post;
        }

        public int getZan() {
            return zan;
        }

        public void setZan(int zan) {
            this.zan = zan;
        }

        public List<ChildCommentBean> getChild_comment() {
            return child_comment;
        }

        public void setChild_comment(List<ChildCommentBean> child_comment) {
            this.child_comment = child_comment;
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

        public static class PostBean implements Serializable{

            private String id;
            private String tuiid;
            private String post_author;
            private String author;
            private String post_keywords;
            private String post_date;
            private String post_times;
            private String post_content;
            private String post_title;
            private String post_lai;
            private String post_mp;
            private String post_time;
            private String post_size;
            private String post_excerpt;
            private String post_status;
            private String comment_status;
            private String post_modified;
            private String post_content_filtered;
            private String post_parent;
            private String post_type;
            private String post_mime_type;
            private String comment_count;
            private String smeta;
            private String post_hits;
            private String post_like;
            private String istop;
            private String recommended;
            private String toutiao;
            private String zhuti;
            private String url;
            private String listeningrate;
            private String praisenums;
            private String praisenum;
            private String simpleImage;
            private String post_news;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTuiid() {
                return tuiid;
            }

            public void setTuiid(String tuiid) {
                this.tuiid = tuiid;
            }

            public String getPost_author() {
                return post_author;
            }

            public void setPost_author(String post_author) {
                this.post_author = post_author;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getPost_keywords() {
                return post_keywords;
            }

            public void setPost_keywords(String post_keywords) {
                this.post_keywords = post_keywords;
            }

            public String getPost_date() {
                return post_date;
            }

            public void setPost_date(String post_date) {
                this.post_date = post_date;
            }

            public String getPost_times() {
                return post_times;
            }

            public void setPost_times(String post_times) {
                this.post_times = post_times;
            }

            public String getPost_content() {
                return post_content;
            }

            public void setPost_content(String post_content) {
                this.post_content = post_content;
            }

            public String getPost_title() {
                return post_title;
            }

            public void setPost_title(String post_title) {
                this.post_title = post_title;
            }

            public String getPost_lai() {
                return post_lai;
            }

            public void setPost_lai(String post_lai) {
                this.post_lai = post_lai;
            }

            public String getPost_mp() {
                return post_mp;
            }

            public void setPost_mp(String post_mp) {
                this.post_mp = post_mp;
            }

            public String getPost_time() {
                return post_time;
            }

            public void setPost_time(String post_time) {
                this.post_time = post_time;
            }

            public String getPost_size() {
                return post_size;
            }

            public void setPost_size(String post_size) {
                this.post_size = post_size;
            }

            public String getPost_excerpt() {
                return post_excerpt;
            }

            public void setPost_excerpt(String post_excerpt) {
                this.post_excerpt = post_excerpt;
            }

            public String getPost_status() {
                return post_status;
            }

            public void setPost_status(String post_status) {
                this.post_status = post_status;
            }

            public String getComment_status() {
                return comment_status;
            }

            public void setComment_status(String comment_status) {
                this.comment_status = comment_status;
            }

            public String getPost_modified() {
                return post_modified;
            }

            public void setPost_modified(String post_modified) {
                this.post_modified = post_modified;
            }

            public String getPost_content_filtered() {
                return post_content_filtered;
            }

            public void setPost_content_filtered(String post_content_filtered) {
                this.post_content_filtered = post_content_filtered;
            }

            public String getPost_parent() {
                return post_parent;
            }

            public void setPost_parent(String post_parent) {
                this.post_parent = post_parent;
            }

            public String getPost_type() {
                return post_type;
            }

            public void setPost_type(String post_type) {
                this.post_type = post_type;
            }

            public String getPost_mime_type() {
                return post_mime_type;
            }

            public void setPost_mime_type(String post_mime_type) {
                this.post_mime_type = post_mime_type;
            }

            public String getComment_count() {
                return comment_count;
            }

            public void setComment_count(String comment_count) {
                this.comment_count = comment_count;
            }

            public String getSmeta() {
                return smeta;
            }

            public void setSmeta(String smeta) {
                this.smeta = smeta;
            }

            public String getPost_hits() {
                return post_hits;
            }

            public void setPost_hits(String post_hits) {
                this.post_hits = post_hits;
            }

            public String getPost_like() {
                return post_like;
            }

            public void setPost_like(String post_like) {
                this.post_like = post_like;
            }

            public String getIstop() {
                return istop;
            }

            public void setIstop(String istop) {
                this.istop = istop;
            }

            public String getRecommended() {
                return recommended;
            }

            public void setRecommended(String recommended) {
                this.recommended = recommended;
            }

            public String getToutiao() {
                return toutiao;
            }

            public void setToutiao(String toutiao) {
                this.toutiao = toutiao;
            }

            public String getZhuti() {
                return zhuti;
            }

            public void setZhuti(String zhuti) {
                this.zhuti = zhuti;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getListeningrate() {
                return listeningrate;
            }

            public void setListeningrate(String listeningrate) {
                this.listeningrate = listeningrate;
            }

            public String getPraisenums() {
                return praisenums;
            }

            public void setPraisenums(String praisenums) {
                this.praisenums = praisenums;
            }

            public String getPraisenum() {
                return praisenum;
            }

            public void setPraisenum(String praisenum) {
                this.praisenum = praisenum;
            }

            public String getSimpleImage() {
                return simpleImage;
            }

            public void setSimpleImage(String simpleImage) {
                this.simpleImage = simpleImage;
            }

            public String getPost_news() {
                return post_news;
            }

            public void setPost_news(String post_news) {
                this.post_news = post_news;
            }
        }

        public static class ChildCommentBean implements Serializable {
            private String id;
            private String post_id;
            private String uid;
            private String to_uid;
            private String createtime;
            private String comment;
            private String parentid;
            private String praisenum;
            private String status;
            /**
             * id : 20481
             * user_login : tw490344
             * user_pass :
             * user_nicename : sq543380
             * user_phone : null
             * user_email :
             * user_url :
             * avatar : http://q.qlogo.cn/qqapp/1101441286/63CA3ED1ACA833A00F5DBEC4B55EEA87/100
             * post_contents : null
             * sex : 0
             * birthday : null
             * signature : null
             * last_login_ip : 115.201.117.13
             * last_login_time : 2017-03-14 06:39:47
             * create_time : 2017-03-14 06:39:47
             * user_activation_key :
             * comment_count_anchor : 0
             * user_status : 1
             * score : 0
             * user_type : 2
             */

            private UserBean user;
            /**
             * fan_num : 0
             * guan_num : 0
             */

            private ToUserBean to_user;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPost_id() {
                return post_id;
            }

            public void setPost_id(String post_id) {
                this.post_id = post_id;
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

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getParentid() {
                return parentid;
            }

            public void setParentid(String parentid) {
                this.parentid = parentid;
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

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public ToUserBean getTo_user() {
                return to_user;
            }

            public void setTo_user(ToUserBean to_user) {
                this.to_user = to_user;
            }

            public static class UserBean implements Serializable{
                private String id;
                private String user_login;
                private String user_pass;
                private String user_nicename;
                private Object user_phone;
                private String user_email;
                private String user_url;
                private String avatar;
                private Object post_contents;
                private String sex;
                private Object birthday;
                private Object signature;
                private String last_login_ip;
                private String last_login_time;
                private String create_time;
                private String user_activation_key;
                private String comment_count_anchor;
                private String user_status;
                private String score;
                private String user_type;

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

                public Object getUser_phone() {
                    return user_phone;
                }

                public void setUser_phone(Object user_phone) {
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

                public Object getSignature() {
                    return signature;
                }

                public void setSignature(Object signature) {
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

            public static class ToUserBean implements Serializable {
                private String fan_num;
                private String guan_num;
                private String id;
                private String user_nicename;
                private String sex;
                private String signature;
                private String avatar;
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

                public String getUser_login() {
                    return user_login;
                }

                public void setUser_login(String user_login) {
                    this.user_login = user_login;
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
}
