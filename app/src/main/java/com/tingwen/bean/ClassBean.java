package com.tingwen.bean;

import com.tingwen.base.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * 课堂bean
 * Created by Administrator on 2017/7/10 0010.
 */
public class ClassBean extends Entity {


    /**
     * status : 1
     * msg : 获取课堂列表成功!
     * results : [{"id":"247","name":"2017创业投资、职场晋升必读的58本书","description":"魏雪漫 资深讲书老师\r\n中国好声音实力派歌手","images":"2017-03-21/crop_58d1144b8157a.jpg","price":"99.00","sprice":"2610.00","fan_num":660,"message_num":"21","is_fan":"0","is_free":"1"}]
     */

    private int status;
    private String msg;
    /**
     * id : 247
     * name : 2017创业投资、职场晋升必读的58本书
     * description : 魏雪漫 资深讲书老师
     中国好声音实力派歌手
     * images : 2017-03-21/crop_58d1144b8157a.jpg
     * price : 99.00
     * sprice : 2610.00
     * fan_num : 660
     * message_num : 21
     * is_fan : 0
     * is_free : 1
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
        private String price;
        private String sprice;
        private int fan_num;
        private String message_num;
        private String is_fan;
        private String is_free;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSprice() {
            return sprice;
        }

        public void setSprice(String sprice) {
            this.sprice = sprice;
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

        public String getIs_free() {
            return is_free;
        }

        public void setIs_free(String is_free) {
            this.is_free = is_free;
        }
    }
}
