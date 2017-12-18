package com.tingwen.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public class SearchPartBean {

    /**
     * status : 1
     * msg : 查询节目信息成功!
     * results : {"act":[{"id":"269","name":"案例1000","description":"正和岛旗下控股子公司正和岛智库，致力于打造中国第一生态案例平台\u201d，专注于为企业提供商业案例输出、输入服务，以助力企业整合产业生态资源，提升经营管理为核心价值；并链接产业上下游资源，实现精准对接。\u201c商业案例1000\u201d通过征集企业案例，将最具代表性的企业核心价值点梳理呈现，成为更多企业可借鉴学习的商界经典。","images":"2017-04-22/crop_58fb698744eba.jpg","list_tpl":null,"one_tpl":null,"listorder":"0","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"0.00","sprice":"0.00","gold":6606,"news_num":"61","fan_num":16775,"message_num":"0","is_fan":0}],"lesson":[{"id":"260","name":"10步成为巴菲特式投资天才","description":"刘建位 复旦大学国际金融硕士\r\n上海社会科学院经济学博士\r\n巴菲特价值投资研究与传播者","images":"2017-04-13/crop_58ef1953ef484.jpg","list_tpl":null,"one_tpl":null,"listorder":"14","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"99.00","sprice":"5800.00","gold":0,"is_free":"0","news_num":"10","fan_num":165,"message_num":"14","is_fan":0},{"id":"264","name":"如何成为价值10亿的职业经理人","description":"唐骏 前微软中国公司总裁\r\n前盛大网络公司董事兼顾问\r\n前新华都集团总裁兼CEO","images":"2017-04-17/crop_58f4713f00565.jpg","list_tpl":null,"one_tpl":null,"listorder":"3","status":"1","post_content":"","comment_count_act":"0","act_type":"1","term_id":"0","author":"1","price":"69.00","sprice":"4600.00","gold":40,"is_free":"0","news_num":"9","fan_num":110,"message_num":"3","is_fan":0}]}
     */

    private int status;
    private String msg;
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

    public static class ResultsBean {
        /**
         * id : 269
         * name : 案例1000
         * description : 正和岛旗下控股子公司正和岛智库，致力于打造中国第一生态案例平台”，专注于为企业提供商业案例输出、输入服务，以助力企业整合产业生态资源，提升经营管理为核心价值；并链接产业上下游资源，实现精准对接。“商业案例1000”通过征集企业案例，将最具代表性的企业核心价值点梳理呈现，成为更多企业可借鉴学习的商界经典。
         * images : 2017-04-22/crop_58fb698744eba.jpg
         * list_tpl : null
         * one_tpl : null
         * listorder : 0
         * status : 1
         * post_content :
         * comment_count_act : 0
         * act_type : 1
         * term_id : 0
         * author : 1
         * price : 0.00
         * sprice : 0.00
         * gold : 6606
         * news_num : 61
         * fan_num : 16775
         * message_num : 0
         * is_fan : 0
         */

        private List<ActBean> act;
        /**
         * id : 260
         * name : 10步成为巴菲特式投资天才
         * description : 刘建位 复旦大学国际金融硕士
         上海社会科学院经济学博士
         巴菲特价值投资研究与传播者
         * images : 2017-04-13/crop_58ef1953ef484.jpg
         * list_tpl : null
         * one_tpl : null
         * listorder : 14
         * status : 1
         * post_content :
         * comment_count_act : 0
         * act_type : 1
         * term_id : 0
         * author : 1
         * price : 99.00
         * sprice : 5800.00
         * gold : 0
         * is_free : 0
         * news_num : 10
         * fan_num : 165
         * message_num : 14
         * is_fan : 0
         */

        private List<LessonBean> lesson;

        public List<ActBean> getAct() {
            return act;
        }

        public void setAct(List<ActBean> act) {
            this.act = act;
        }

        public List<LessonBean> getLesson() {
            return lesson;
        }

        public void setLesson(List<LessonBean> lesson) {
            this.lesson = lesson;
        }

        public static class ActBean {
            private String id;
            private String name;
            private String description;
            private String images;
            private Object list_tpl;
            private Object one_tpl;
            private String listorder;
            private String status;
            private String post_content;
            private String comment_count_act;
            private String act_type;
            private String term_id;
            private String author;
            private String price;
            private String sprice;
            private int gold;
            private String news_num;
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

            public Object getList_tpl() {
                return list_tpl;
            }

            public void setList_tpl(Object list_tpl) {
                this.list_tpl = list_tpl;
            }

            public Object getOne_tpl() {
                return one_tpl;
            }

            public void setOne_tpl(Object one_tpl) {
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

        public static class LessonBean {
            private String id;
            private String name;
            private String description;
            private String images;
            private Object list_tpl;
            private Object one_tpl;
            private String listorder;
            private String status;
            private String post_content;
            private String comment_count_act;
            private String act_type;
            private String term_id;
            private String author;
            private String price;
            private String sprice;
            private int gold;
            private String is_free;
            private String news_num;
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

            public Object getList_tpl() {
                return list_tpl;
            }

            public void setList_tpl(Object list_tpl) {
                this.list_tpl = list_tpl;
            }

            public Object getOne_tpl() {
                return one_tpl;
            }

            public void setOne_tpl(Object one_tpl) {
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

            public int getGold() {
                return gold;
            }

            public void setGold(int gold) {
                this.gold = gold;
            }

            public String getIs_free() {
                return is_free;
            }

            public void setIs_free(String is_free) {
                this.is_free = is_free;
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
