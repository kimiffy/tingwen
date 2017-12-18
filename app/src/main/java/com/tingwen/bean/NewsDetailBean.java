package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻详情
 * Created by Administrator on 2017/8/25 0025.
 */
public class NewsDetailBean implements Serializable{


    /**
     * status : 1
     * msg : 请求成功!
     * results : {"post_id":"77223","post_news":"307","post_title":"原\"贾跃亭团队\"解体 乐视移动总裁阿木离职","post_lai":"腾讯科技","post_mp":"http://admin.tingwen.me/data/upload/mp3/599fbde36bc73.mp3","post_time":"89000","post_size":"1425059","post_excerpt":"  自贾跃亭辞去乐视网董事长兼CEO之后，乐视高层大洗牌。继乐视金融总裁杨新军、乐视金融副总裁兼CTO丁晓强之后，贾跃亭的另一位\u201c老搭档\u201d乐视移动总裁阿木也宣布离职。\r\n\r\n  乐视移动总裁阿木宣布卸任一切职务。这也意味着，\u201c贾跃亭团队\u201d正式解体，由孙宏斌带领的新一轮乐视高管接盘。\r\n\r\n  资料显示，2015年1月阿木加盟乐视，出任乐视控股战略规划与管理部副总裁暨总裁办主任，全面负责乐视集团全球战略规划及战略运营管理等工作;2017年4月，阿木升任乐视移动CEO，负责乐视移动整体的日常经营及团队管理，向乐视生态全球CEO贾跃亭汇报，同时冯幸不再担任总裁。据悉，乐视最初的\u201c生态化反\u201d与\u201c七大子生态\u201d等基本上都是由阿木提出并传播。\r\n\r\n  日前，乐视新任董事长孙宏斌、新CEO梁军(现任乐视网法人代表)均透露，未来乐视将以家庭互联网娱乐为主，主要发展电视、影视业务，聚焦大屏生态、分众自制、内容开放，继续推进开放生态战略。\r\n\r\n\r\n（来源：腾讯科技）","post_modified":"2017-08-25 14:03:26","comment_count":"0","smeta":"{\"thumb\":\"http:\\/\\/admin.tingwen.me\\/Uploads\\/2017-08-25\\/crop_599fbdb224aef.jpg\"}","gold":"0","is_collection":0,"act":{"id":"307","name":"欣然","description":"科技新闻、时政新闻主播","images":"2017-07-18/crop_596daf2ef1711.jpg","fan_num":"2035","message_num":"2","is_fan":"0"},"reward_num":"1","reward":[{"money":"10.00","date":"1502372276","user_id":"48799","user_login":"tw1502371807329956","user_nicename":"宇泽","avatar":"598c645ff3ac4.jpg"}]}
     */

    private int status;
    private String msg;
    /**
     * post_id : 77223
     * post_news : 307
     * post_title : 原"贾跃亭团队"解体 乐视移动总裁阿木离职
     * post_lai : 腾讯科技
     * post_mp : http://admin.tingwen.me/data/upload/mp3/599fbde36bc73.mp3
     * post_time : 89000
     * post_size : 1425059
     * post_excerpt :   自贾跃亭辞去乐视网董事长兼CEO之后，乐视高层大洗牌。继乐视金融总裁杨新军、乐视金融副总裁兼CTO丁晓强之后，贾跃亭的另一位“老搭档”乐视移动总裁阿木也宣布离职。

     乐视移动总裁阿木宣布卸任一切职务。这也意味着，“贾跃亭团队”正式解体，由孙宏斌带领的新一轮乐视高管接盘。

     资料显示，2015年1月阿木加盟乐视，出任乐视控股战略规划与管理部副总裁暨总裁办主任，全面负责乐视集团全球战略规划及战略运营管理等工作;2017年4月，阿木升任乐视移动CEO，负责乐视移动整体的日常经营及团队管理，向乐视生态全球CEO贾跃亭汇报，同时冯幸不再担任总裁。据悉，乐视最初的“生态化反”与“七大子生态”等基本上都是由阿木提出并传播。

     日前，乐视新任董事长孙宏斌、新CEO梁军(现任乐视网法人代表)均透露，未来乐视将以家庭互联网娱乐为主，主要发展电视、影视业务，聚焦大屏生态、分众自制、内容开放，继续推进开放生态战略。


     （来源：腾讯科技）
     * post_modified : 2017-08-25 14:03:26
     * comment_count : 0
     * smeta : {"thumb":"http:\/\/admin.tingwen.me\/Uploads\/2017-08-25\/crop_599fbdb224aef.jpg"}
     * gold : 0
     * is_collection : 0
     * act : {"id":"307","name":"欣然","description":"科技新闻、时政新闻主播","images":"2017-07-18/crop_596daf2ef1711.jpg","fan_num":"2035","message_num":"2","is_fan":"0"}
     * reward_num : 1
     * reward : [{"money":"10.00","date":"1502372276","user_id":"48799","user_login":"tw1502371807329956","user_nicename":"宇泽","avatar":"598c645ff3ac4.jpg"}]
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

    public static class ResultsBean implements Serializable{
        private String post_id;
        private String post_news;
        private String post_title;
        private String post_lai;
        private String post_mp;
        private String post_time;
        private String post_size;
        private String post_excerpt;
        private String post_modified;
        private String comment_count;
        private String smeta;
        private String gold;
        private int is_collection;
        /**
         * id : 307
         * name : 欣然
         * description : 科技新闻、时政新闻主播
         * images : 2017-07-18/crop_596daf2ef1711.jpg
         * fan_num : 2035
         * message_num : 2
         * is_fan : 0
         */

        private ActBean act;
        private String reward_num;
        /**
         * money : 10.00
         * date : 1502372276
         * user_id : 48799
         * user_login : tw1502371807329956
         * user_nicename : 宇泽
         * avatar : 598c645ff3ac4.jpg
         */

        private List<RewardBean> reward;

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getPost_news() {
            return post_news;
        }

        public void setPost_news(String post_news) {
            this.post_news = post_news;
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

        public String getPost_modified() {
            return post_modified;
        }

        public void setPost_modified(String post_modified) {
            this.post_modified = post_modified;
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

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public int getIs_collection() {
            return is_collection;
        }

        public void setIs_collection(int is_collection) {
            this.is_collection = is_collection;
        }

        public ActBean getAct() {
            return act;
        }

        public void setAct(ActBean act) {
            this.act = act;
        }

        public String getReward_num() {
            return reward_num;
        }

        public void setReward_num(String reward_num) {
            this.reward_num = reward_num;
        }

        public List<RewardBean> getReward() {
            return reward;
        }

        public void setReward(List<RewardBean> reward) {
            this.reward = reward;
        }

        public static class ActBean implements Serializable{
            private String id;
            private String name;
            private String description;
            private String images;
            private String fan_num;
            private String message_num;
            private String is_fan;

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

            public String getFan_num() {
                return fan_num;
            }

            public void setFan_num(String fan_num) {
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
        }

        public static class RewardBean implements Serializable{
            private String money;
            private String date;
            private String user_id;
            private String user_login;
            private String user_nicename;
            private String avatar;

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
