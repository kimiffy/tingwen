package com.tingwen.bean;

import java.util.List;

/**
 * 新闻分类
 * Created by Administrator on 2017/10/19 0019.
 */
public class CategoryBean {


    /**
     * status : 1
     * msg : 请求成功!
     * results : [{"numberkey":"1","type":"头条","parent":"0"},{"numberkey":"14","type":"时政","parent":"0"},{"numberkey":"8","type":"国际","parent":"0"},{"numberkey":"4","type":"文娱","parent":"0"},{"numberkey":"18","type":"干货","parent":"0"},{"numberkey":"6","type":"财经","parent":"0"},{"numberkey":"7","type":"科技","parent":"0"}]
     */

    private int status;
    private String msg;
    /**
     * numberkey : 1
     * type : 头条
     * parent : 0
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
        private String numberkey;
        private String type;
        private String parent;

        public String getNumberkey() {
            return numberkey;
        }

        public void setNumberkey(String numberkey) {
            this.numberkey = numberkey;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }
    }
}
