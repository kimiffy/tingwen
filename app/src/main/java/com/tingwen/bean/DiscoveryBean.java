package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */
public class DiscoveryBean implements Serializable {

    /**
     * status : 1
     * msg :
     * results : [{"id":28,"type":"听闻课堂","data":[{"id":"247","name":"2017创业投资、职场晋升必读的58本书","description":"魏雪漫 资深讲书老师\r\n中国好声音实力派歌手","images":"2017-03-21/crop_58d1144b8157a.jpg","list_tpl":null,"one_tpl":null,"listorder":"21","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"99.00","sprice":"2610.00","fan_num":715,"message_num":"21","is_fan":0,"is_free":"1"},{"id":"250","name":"如何运用九型人格来管理团队","description":"李太林导师\r\n中国宗师级人力资源大师\r\n人资网特聘为首席指导师","images":"2017-03-21/crop_58d113ea6cacb.jpg","list_tpl":null,"one_tpl":null,"listorder":"16","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"99.00","sprice":"3500.00","fan_num":165,"message_num":"16","is_fan":0,"is_free":"1"},{"id":"253","name":"如何创办一家价值30亿的媒体公司","description":"虞锋 云锋基金董事长\r\n聚众、分众传媒前总裁\r\n最大的楼宇电视广告媒体","images":"2017-03-21/crop_58d114859e020.jpg","list_tpl":null,"one_tpl":null,"listorder":"7","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"59.00","sprice":"3000.00","fan_num":110,"message_num":"7","is_fan":0,"is_free":"0"}]}]
     */

    private int status;
    private String msg;
    /**
     * id : 28
     * type : 听闻课堂
     * data : [{"id":"247","name":"2017创业投资、职场晋升必读的58本书","description":"魏雪漫 资深讲书老师\r\n中国好声音实力派歌手","images":"2017-03-21/crop_58d1144b8157a.jpg","list_tpl":null,"one_tpl":null,"listorder":"21","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"99.00","sprice":"2610.00","fan_num":715,"message_num":"21","is_fan":0,"is_free":"1"},{"id":"250","name":"如何运用九型人格来管理团队","description":"李太林导师\r\n中国宗师级人力资源大师\r\n人资网特聘为首席指导师","images":"2017-03-21/crop_58d113ea6cacb.jpg","list_tpl":null,"one_tpl":null,"listorder":"16","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"99.00","sprice":"3500.00","fan_num":165,"message_num":"16","is_fan":0,"is_free":"1"},{"id":"253","name":"如何创办一家价值30亿的媒体公司","description":"虞锋 云锋基金董事长\r\n聚众、分众传媒前总裁\r\n最大的楼宇电视广告媒体","images":"2017-03-21/crop_58d114859e020.jpg","list_tpl":null,"one_tpl":null,"listorder":"7","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"59.00","sprice":"3000.00","fan_num":110,"message_num":"7","is_fan":0,"is_free":"0"}]
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

    public static class ResultsBean implements Serializable {
        private int id;
        private String type;

        /**
         * id : 247
         * name : 2017创业投资、职场晋升必读的58本书
         * description : 魏雪漫 资深讲书老师
         * 中国好声音实力派歌手
         * images : 2017-03-21/crop_58d1144b8157a.jpg
         * list_tpl : null
         * one_tpl : null
         * listorder : 21
         * status : 1
         * post_content :
         * comment_count_act : 0
         * act_type : 1
         * term_id : 0
         * author : 1
         * price : 99.00
         * sprice : 2610.00
         * fan_num : 715
         * message_num : 21
         * is_fan : 0
         * is_free : 1
         */

        private List<DataBean> data;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            private String id;
            private String name;
            private String description;
            private String images;
            private String list_tpl;
            private String one_tpl;
            private String listorder;
            private String status;
            private String post_content;
            private String comment_count_act;
            private String act_type;
            private String term_id;
            private String author;
            private String price;
            private String sprice;
            private int fan_num;
            private String message_num;
            private int is_fan;
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

            public Object getList_tpl() {
                return list_tpl;
            }

            public void setList_tpl(String list_tpl) {
                this.list_tpl = list_tpl;
            }

            public Object getOne_tpl() {
                return one_tpl;
            }

            public void setOne_tpl(String one_tpl) {
                this.one_tpl = one_tpl;
            }

            public String getListorder() {
                return listorder;
            }

            public void setListorder(String listorder) {
                this.listorder = listorder;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPost_content() {
                return post_content;
            }

            public void setPost_content(String post_content) {
                this.post_content = post_content;
            }

            public String getComment_count_act() {
                return comment_count_act;
            }

            public void setComment_count_act(String comment_count_act) {
                this.comment_count_act = comment_count_act;
            }

            public String getAct_type() {
                return act_type;
            }

            public void setAct_type(String act_type) {
                this.act_type = act_type;
            }

            public String getTerm_id() {
                return term_id;
            }

            public void setTerm_id(String term_id) {
                this.term_id = term_id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
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

            public int getIs_fan() {
                return is_fan;
            }

            public void setIs_fan(int is_fan) {
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

    @Override
    public String toString() {
        return "DiscoveryBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", results=" + results +
                '}';
    }
}
