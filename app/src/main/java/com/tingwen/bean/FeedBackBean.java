package com.tingwen.bean;

import java.util.List;

/**
 * 意见反馈
 * Created by Administrator on 2017/10/17 0017.
 */
public class FeedBackBean {
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

    public static class ResultsBean {
        private String id;
        private String uid;
        private String comment;
        private String images;
        private String tuid;
        private String to_comment_id;
        private String create_time;
        private String status;
        private String is_comment;
        private UserBean user;
        private int is_zan;
        private int zan_num;


        private List<ChildCommentBean> child_comment;

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

        public int getIs_zan() {
            return is_zan;
        }

        public void setIs_zan(int is_zan) {
            this.is_zan = is_zan;
        }

        public int getZan_num() {
            return zan_num;
        }

        public void setZan_num(int zan_num) {
            this.zan_num = zan_num;
        }

        public List<ChildCommentBean> getChild_comment() {
            return child_comment;
        }

        public void setChild_comment(List<ChildCommentBean> child_comment) {
            this.child_comment = child_comment;
        }

        public static class UserBean {
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

        public static class ChildCommentBean {
            private String id;
            private String uid;
            private String comment;
            private String images;
            private String tuid;
            private String to_comment_id;
            private String create_time;
            private String status;
            private String is_comment;
            private UserBean user;
            private ToUserBean to_user;



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

            public ToUserBean getTo_user() {
                return to_user;
            }

            public void setTo_user(ToUserBean to_user) {
                this.to_user = to_user;
            }

            public static class UserBean {
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

            public static class ToUserBean {
                private int fan_num;
                private int guan_num;
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



                public int getFan_num() {
                    return fan_num;
                }

                public void setFan_num(int fan_num) {
                    this.fan_num = fan_num;
                }

                public int getGuan_num() {
                    return guan_num;
                }

                public void setGuan_num(int guan_num) {
                    this.guan_num = guan_num;
                }
            }
        }
    }
}
