package com.tingwen.bean;

import com.tingwen.base.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * 关注
 * Created by Administrator on 2017/7/12 0012.
 */
public class FollowBean extends Entity {

    /**
     * status : 1
     * msg : 查询成功!
     * results : [{"id":"143","name":"听闻新歌榜","description":"大田：90后小鲜肉一枚，明明可以靠颜值，我却任性的选择才华！每天一首新歌推荐，咱们来听点不一样的","images":"2016-07-01/crop_57768f34a1d79.JPG","gold":705,"news_num":"258","fan_num":39490}]
     */

    private int status;
    private String msg;
    /**
     * id : 143
     * name : 听闻新歌榜
     * description : 大田：90后小鲜肉一枚，明明可以靠颜值，我却任性的选择才华！每天一首新歌推荐，咱们来听点不一样的
     * images : 2016-07-01/crop_57768f34a1d79.JPG
     * gold : 705
     * news_num : 258
     * fan_num : 39490
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
        private String name;
        private String description;
        private String images;
        private int gold;
        private String news_num;
        private int fan_num;
        private int isFollowed;

        public int getIsFollowed() {
            return isFollowed;
        }

        public void setIsFollowed(int isFollowed) {
            this.isFollowed = isFollowed;
        }

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

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public String getNews_num() {
            return news_num;
        }

        public void setNews_num(String news_num) {
            this.news_num = news_num;
        }

        public int getFan_num() {
            return fan_num;
        }

        public void setFan_num(int fan_num) {
            this.fan_num = fan_num;
        }
    }
}
