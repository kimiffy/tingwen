package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 广告
 * Created by Administrator on 2017/8/3 0003.
 */
public class FastNewsADBean implements Serializable{

    /**
     * msg : 83b753d66210529f
     * results : [{"cate":"1","id":"1","picture":{"thumb":"http://admin.tingwen.me/Uploads/2017-03-21/crop_58d0820ebcd2d.jpg"},"post_list":{"author":"0","comment_count":"7","comment_status":"1","gold":"0","id":"66921","istop":"0","listeningrate":"14472","post_act":{"description":"4e0d505a72ec5bb665b095fbff0c53ea670972ec727989c270b93002","fan_num":"1484","id":"87","images":"2017-02-23/crop_58ae9623c0f92.jpg","is_fan":0,"message_num":"7","name":"738b65008bf459294e0b"},"post_author":"32","post_content":"","post_date":"2017-03-21 09:30:40","post_excerpt":"4e0d505a72ec5bb665b095fbff0c53ea670972ec727989c270b93002","post_hits":"44","post_keywords":"65f6653f","post_lai":"738b65008bf459294e0b","post_like":"0","post_mime_type":"","post_modified":"2017-03-21 09:29:46","post_mp":"http://admin.tingwen.me/data/upload/mp3/58d0823668fb1.mp3","post_news":"87","post_parent":"0","post_size":"10173973","post_status":"1","post_time":"423000","post_times":"","post_title":"3010738b65008bf459294e0b301120175e74367082165e52014201473af4fdd90e85bf9184e2a57ce5e025c555f0076847a7a6c148d2891cf4e13987977635bdf","praisenum":"139","praisenums":"50","recommended":"0","smeta":{"thumb":"http://admin.tingwen.me/Uploads/2017-03-21/crop_58d0820ebcd2d.jpg"},"term_id":"14","term_name":"65f6653","toutiao":"59346761","tuiid":"0","url":"","zhuti":"0"},"url":"66921"},{"cate":"3","description":"4e0a5e02516c53f8768475358bdd9500552e79d88bc0","id":"20","is_free":"0","picture":{"thumb":"http://admin.tingwen.me/Uploads/2017-04-07/crop_58e7948bcff1f.jpg"},"url":"259"}]
     * status : 1
     */

    private String msg;
    private int status;
    private List<ResultsBean> results;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Serializable {
        /**
         * cate : 1
         * id : 1
         * picture : {"thumb":"http://admin.tingwen.me/Uploads/2017-03-21/crop_58d0820ebcd2d.jpg"}
         * post_list : {"author":"0","comment_count":"7","comment_status":"1","gold":"0","id":"66921","istop":"0","listeningrate":"14472","post_act":{"description":"4e0d505a72ec5bb665b095fbff0c53ea670972ec727989c270b93002","fan_num":"1484","id":"87","images":"2017-02-23/crop_58ae9623c0f92.jpg","is_fan":0,"message_num":"7","name":"738b65008bf459294e0b"},"post_author":"32","post_content":"","post_date":"2017-03-21 09:30:40","post_excerpt":"4e0d505a72ec5bb665b095fbff0c53ea670972ec727989c270b93002","post_hits":"44","post_keywords":"65f6653f","post_lai":"738b65008bf459294e0b","post_like":"0","post_mime_type":"","post_modified":"2017-03-21 09:29:46","post_mp":"http://admin.tingwen.me/data/upload/mp3/58d0823668fb1.mp3","post_news":"87","post_parent":"0","post_size":"10173973","post_status":"1","post_time":"423000","post_times":"","post_title":"3010738b65008bf459294e0b301120175e74367082165e52014201473af4fdd90e85bf9184e2a57ce5e025c555f0076847a7a6c148d2891cf4e13987977635bdf","praisenum":"139","praisenums":"50","recommended":"0","smeta":{"thumb":"http://admin.tingwen.me/Uploads/2017-03-21/crop_58d0820ebcd2d.jpg"},"term_id":"14","term_name":"65f6653","toutiao":"59346761","tuiid":"0","url":"","zhuti":"0"}
         * url : 66921
         * description : 4e0a5e02516c53f8768475358bdd9500552e79d88bc0
         * is_free : 0
         */

        private String cate;
        private String id;
        private String picture;
        private PostListBean post_list;
        private String url;
        private String description;
        private String is_free;
        private String des;
        private String name;
        private String images;
        private int fan_num;
        private String message_num;
        private String is_fan;

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getFan_num() {
            return fan_num;
        }

        public void setFan_num(int fan_num) {
            this.fan_num = fan_num;
        }

        public String getMessage_num() {
            return message_num;
        }

        public void setMessage_num(String message_num) {
            this.message_num = message_num;
        }

        public String getIs_fan() {
            return is_fan;
        }

        public void setIs_fan(String is_fan) {
            this.is_fan = is_fan;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public PostListBean getPost_list() {
            return post_list;
        }

        public void setPost_list(PostListBean post_list) {
            this.post_list = post_list;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIs_free() {
            return is_free;
        }

        public void setIs_free(String is_free) {
            this.is_free = is_free;
        }



        public static class PostListBean implements Serializable{
            /**
             * author : 0
             * comment_count : 7
             * comment_status : 1
             * gold : 0
             * id : 66921
             * istop : 0
             * listeningrate : 14472
             * post_act : {"description":"4e0d505a72ec5bb665b095fbff0c53ea670972ec727989c270b93002","fan_num":"1484","id":"87","images":"2017-02-23/crop_58ae9623c0f92.jpg","is_fan":0,"message_num":"7","name":"738b65008bf459294e0b"}
             * post_author : 32
             * post_content :
             * post_date : 2017-03-21 09:30:40
             * post_excerpt : 4e0d505a72ec5bb665b095fbff0c53ea670972ec727989c270b93002
             * post_hits : 44
             * post_keywords : 65f6653f
             * post_lai : 738b65008bf459294e0b
             * post_like : 0
             * post_mime_type :
             * post_modified : 2017-03-21 09:29:46
             * post_mp : http://admin.tingwen.me/data/upload/mp3/58d0823668fb1.mp3
             * post_news : 87
             * post_parent : 0
             * post_size : 10173973
             * post_status : 1
             * post_time : 423000
             * post_times :
             * post_title : 3010738b65008bf459294e0b301120175e74367082165e52014201473af4fdd90e85bf9184e2a57ce5e025c555f0076847a7a6c148d2891cf4e13987977635bdf
             * praisenum : 139
             * praisenums : 50
             * recommended : 0
             * smeta : {"thumb":"http://admin.tingwen.me/Uploads/2017-03-21/crop_58d0820ebcd2d.jpg"}
             * term_id : 14
             * term_name : 65f6653
             * toutiao : 59346761
             * tuiid : 0
             * url :
             * zhuti : 0
             */

            private String author;
            private String comment_count;
            private String comment_status;
            private String gold;
            private String id;
            private String istop;
            private String listeningrate;
            private PostActBean post_act;
            private String post_author;
            private String post_content;
            private String post_date;
            private String post_excerpt;
            private String post_hits;
            private String post_keywords;
            private String post_lai;
            private String post_like;
            private String post_mime_type;
            private String post_modified;
            private String post_mp;
            private String post_news;
            private String post_parent;
            private String post_size;
            private String post_status;
            private String post_time;
            private String post_times;
            private String post_title;
            private String praisenum;
            private String praisenums;
            private String recommended;
            private String smeta;
            private String term_id;
            private String term_name;
            private String toutiao;
            private String tuiid;
            private String url;
            private String zhuti;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getComment_count() {
                return comment_count;
            }

            public void setComment_count(String comment_count) {
                this.comment_count = comment_count;
            }

            public String getComment_status() {
                return comment_status;
            }

            public void setComment_status(String comment_status) {
                this.comment_status = comment_status;
            }

            public String getGold() {
                return gold;
            }

            public void setGold(String gold) {
                this.gold = gold;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIstop() {
                return istop;
            }

            public void setIstop(String istop) {
                this.istop = istop;
            }

            public String getListeningrate() {
                return listeningrate;
            }

            public void setListeningrate(String listeningrate) {
                this.listeningrate = listeningrate;
            }

            public PostActBean getPost_act() {
                return post_act;
            }

            public void setPost_act(PostActBean post_act) {
                this.post_act = post_act;
            }

            public String getPost_author() {
                return post_author;
            }

            public void setPost_author(String post_author) {
                this.post_author = post_author;
            }

            public String getPost_content() {
                return post_content;
            }

            public void setPost_content(String post_content) {
                this.post_content = post_content;
            }

            public String getPost_date() {
                return post_date;
            }

            public void setPost_date(String post_date) {
                this.post_date = post_date;
            }

            public String getPost_excerpt() {
                return post_excerpt;
            }

            public void setPost_excerpt(String post_excerpt) {
                this.post_excerpt = post_excerpt;
            }

            public String getPost_hits() {
                return post_hits;
            }

            public void setPost_hits(String post_hits) {
                this.post_hits = post_hits;
            }

            public String getPost_keywords() {
                return post_keywords;
            }

            public void setPost_keywords(String post_keywords) {
                this.post_keywords = post_keywords;
            }

            public String getPost_lai() {
                return post_lai;
            }

            public void setPost_lai(String post_lai) {
                this.post_lai = post_lai;
            }

            public String getPost_like() {
                return post_like;
            }

            public void setPost_like(String post_like) {
                this.post_like = post_like;
            }

            public String getPost_mime_type() {
                return post_mime_type;
            }

            public void setPost_mime_type(String post_mime_type) {
                this.post_mime_type = post_mime_type;
            }

            public String getPost_modified() {
                return post_modified;
            }

            public void setPost_modified(String post_modified) {
                this.post_modified = post_modified;
            }

            public String getPost_mp() {
                return post_mp;
            }

            public void setPost_mp(String post_mp) {
                this.post_mp = post_mp;
            }

            public String getPost_news() {
                return post_news;
            }

            public void setPost_news(String post_news) {
                this.post_news = post_news;
            }

            public String getPost_parent() {
                return post_parent;
            }

            public void setPost_parent(String post_parent) {
                this.post_parent = post_parent;
            }

            public String getPost_size() {
                return post_size;
            }

            public void setPost_size(String post_size) {
                this.post_size = post_size;
            }

            public String getPost_status() {
                return post_status;
            }

            public void setPost_status(String post_status) {
                this.post_status = post_status;
            }

            public String getPost_time() {
                return post_time;
            }

            public void setPost_time(String post_time) {
                this.post_time = post_time;
            }

            public String getPost_times() {
                return post_times;
            }

            public void setPost_times(String post_times) {
                this.post_times = post_times;
            }

            public String getPost_title() {
                return post_title;
            }

            public void setPost_title(String post_title) {
                this.post_title = post_title;
            }

            public String getPraisenum() {
                return praisenum;
            }

            public void setPraisenum(String praisenum) {
                this.praisenum = praisenum;
            }

            public String getPraisenums() {
                return praisenums;
            }

            public void setPraisenums(String praisenums) {
                this.praisenums = praisenums;
            }

            public String getRecommended() {
                return recommended;
            }

            public void setRecommended(String recommended) {
                this.recommended = recommended;
            }

            public String getSmeta() {
                return smeta;
            }

            public void setSmeta(String smeta) {
                this.smeta = smeta;
            }

            public String getTerm_id() {
                return term_id;
            }

            public void setTerm_id(String term_id) {
                this.term_id = term_id;
            }

            public String getTerm_name() {
                return term_name;
            }

            public void setTerm_name(String term_name) {
                this.term_name = term_name;
            }

            public String getToutiao() {
                return toutiao;
            }

            public void setToutiao(String toutiao) {
                this.toutiao = toutiao;
            }

            public String getTuiid() {
                return tuiid;
            }

            public void setTuiid(String tuiid) {
                this.tuiid = tuiid;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getZhuti() {
                return zhuti;
            }

            public void setZhuti(String zhuti) {
                this.zhuti = zhuti;
            }

            public static class PostActBean implements Serializable{
                /**
                 * description : 4e0d505a72ec5bb665b095fbff0c53ea670972ec727989c270b93002
                 * fan_num : 1484
                 * id : 87
                 * images : 2017-02-23/crop_58ae9623c0f92.jpg
                 * is_fan : 0
                 * message_num : 7
                 * name : 738b65008bf459294e0b
                 */

                private String description;
                private String fan_num;
                private String id;
                private String images;
                private int is_fan;
                private String message_num;
                private String name;

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getFan_num() {
                    return fan_num;
                }

                public void setFan_num(String fan_num) {
                    this.fan_num = fan_num;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getImages() {
                    return images;
                }

                public void setImages(String images) {
                    this.images = images;
                }

                public int getIs_fan() {
                    return is_fan;
                }

                public void setIs_fan(int is_fan) {
                    this.is_fan = is_fan;
                }

                public String getMessage_num() {
                    return message_num;
                }

                public void setMessage_num(String message_num) {
                    this.message_num = message_num;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

        }
    }
}
