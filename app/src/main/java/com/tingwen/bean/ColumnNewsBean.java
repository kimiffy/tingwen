package com.tingwen.bean;

import com.tingwen.base.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * 专栏bean
 * Created by Administrator on 2017/7/11 0011.
 */
public class ColumnNewsBean  extends Entity {

    /**
     * status : 1
     * msg : 获取栏目新闻成功!
     * results : [{"id":"74807","tuiid":"0","post_author":"32","post_news":"157","author":"0","post_keywords":"时政","post_date":"2017-07-11 09:23:37","post_times":"","post_content":"","post_title":"【央广评论】鼓浪屿申遗成功入选世界文化遗产名录","post_lai":"央广评论","post_mp":"http://mp3.tingwen.me/data/upload/mp3/59642880717e4.mp3","post_time":"155000","post_size":"3725559","post_excerpt":"鼓浪屿申遗成功入选世界文化遗产名录","post_status":"1","comment_status":"1","post_modified":"2017-07-11 09:19:35","post_content_filtered":null,"post_parent":"0","post_type":null,"post_mime_type":"","comment_count":"0","smeta":"{\"thumb\":\"http:\\/\\/picture.tingwen.me\\/Uploads\\/2017-07-11\\/crop_5964285bda655.jpg\"}","post_hits":"29","post_like":"0","istop":"0","recommended":"0","toutiao":"头条","zhuti":"0","url":"","listeningrate":"1674","praisenums":"50","praisenum":"165","gold":"0","post_act":{"id":"157","name":"央广评论","description":"倾听中国的声音，中央人民广播电台中国之声频率王牌评论。","images":"2016-08-09/crop_57a97c887f147.jpg","fan_num":145145,"message_num":"9","is_fan":0},"is_collection":0}]
     */

    private int status;
    private String msg;
    /**
     * id : 74807
     * tuiid : 0
     * post_author : 32
     * post_news : 157
     * author : 0
     * post_keywords : 时政
     * post_date : 2017-07-11 09:23:37
     * post_times :
     * post_content :
     * post_title : 【央广评论】鼓浪屿申遗成功入选世界文化遗产名录
     * post_lai : 央广评论
     * post_mp : http://mp3.tingwen.me/data/upload/mp3/59642880717e4.mp3
     * post_time : 155000
     * post_size : 3725559
     * post_excerpt : 鼓浪屿申遗成功入选世界文化遗产名录
     * post_status : 1
     * comment_status : 1
     * post_modified : 2017-07-11 09:19:35
     * post_content_filtered : null
     * post_parent : 0
     * post_type : null
     * post_mime_type :
     * comment_count : 0
     * smeta : {"thumb":"http:\/\/picture.tingwen.me\/Uploads\/2017-07-11\/crop_5964285bda655.jpg"}
     * post_hits : 29
     * post_like : 0
     * istop : 0
     * recommended : 0
     * toutiao : 头条
     * zhuti : 0
     * url :
     * listeningrate : 1674
     * praisenums : 50
     * praisenum : 165
     * gold : 0
     * post_act : {"id":"157","name":"央广评论","description":"倾听中国的声音，中央人民广播电台中国之声频率王牌评论。","images":"2016-08-09/crop_57a97c887f147.jpg","fan_num":145145,"message_num":"9","is_fan":0}
     * is_collection : 0
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
        private String tuiid;
        private String post_author;
        private String post_news;
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
        private String gold;
        /**
         * id : 157
         * name : 央广评论
         * description : 倾听中国的声音，中央人民广播电台中国之声频率王牌评论。
         * images : 2016-08-09/crop_57a97c887f147.jpg
         * fan_num : 145145
         * message_num : 9
         * is_fan : 0
         */

        private PostActBean post_act;
        private int is_collection;

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

        public String getPost_news() {
            return post_news;
        }

        public void setPost_news(String post_news) {
            this.post_news = post_news;
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

        public Object getPost_content_filtered() {
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

        public Object getPost_type() {
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

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public PostActBean getPost_act() {
            return post_act;
        }

        public void setPost_act(PostActBean post_act) {
            this.post_act = post_act;
        }

        public int getIs_collection() {
            return is_collection;
        }

        public void setIs_collection(int is_collection) {
            this.is_collection = is_collection;
        }

        public static class PostActBean implements Serializable{
            private String id;
            private String name;
            private String description;
            private String images;
            private int fan_num;
            private String message_num;
            private int is_fan;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public int getIs_fan() {
                return is_fan;
            }

            public void setIs_fan(int is_fan) {
                this.is_fan = is_fan;
            }
        }
    }
}
