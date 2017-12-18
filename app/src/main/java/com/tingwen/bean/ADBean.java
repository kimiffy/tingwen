package com.tingwen.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/23 0023.
 */
public class ADBean {


    /**
     * status : 1
     * msg : 查询成功!
     * results : [{"ad_id":"52","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167b7933609.jpg","status":"1","url_name":"IOS960:640"},{"ad_id":"53","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167b9f0c403.jpg","status":"1","url_name":"IOS1136:640"},{"ad_id":"54","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167bb3911e4.jpg","status":"1","url_name":"IOS1334:750"},{"ad_id":"55","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167bc6c81c7.jpg","status":"1","url_name":"IOS1920:1080"},{"ad_id":"57","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167c238fd94.jpg","status":"1","url_name":"Android720:1230"},{"ad_id":"58","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167c3944bb7.jpg","status":"1","url_name":"Android1080:1620"},{"ad_id":"59","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167c499b1d2.jpg","status":"1","url_name":"Android1080:1762"},{"ad_id":"60","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167c5c880b4.jpg","status":"1","url_name":"Android1080:1870"},{"ad_id":"61","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167c8f13631.jpg","status":"1","url_name":"Android1536:2048"},{"ad_id":"62","ad_name":"https://ccact.nainiuxianjin.com/#!/success?act=164","ad_content":"/data/upload/5a167c6d8f3b3.jpg","status":"1","url_name":"Android1600:2510"}]
     */

    private int status;
    private String msg;
    /**
     * ad_id : 52
     * ad_name : https://ccact.nainiuxianjin.com/#!/success?act=164
     * ad_content : /data/upload/5a167b7933609.jpg
     * status : 1
     * url_name : IOS960:640
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
        private String ad_id;
        private String ad_name;
        private String ad_content;
        private String status;
        private String url_name;

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getAd_name() {
            return ad_name;
        }

        public void setAd_name(String ad_name) {
            this.ad_name = ad_name;
        }

        public String getAd_content() {
            return ad_content;
        }

        public void setAd_content(String ad_content) {
            this.ad_content = ad_content;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl_name() {
            return url_name;
        }

        public void setUrl_name(String url_name) {
            this.url_name = url_name;
        }
    }
}
