package com.tingwen.bean;

/**
 * Created by Administrator on 2017/11/15 0015.
 */
public class PushNewsBean {


    /**
     * status : 1
     * msg : 请求成功!
     * results : {"term_id":"14","term_name":"时政","id":"80971","tuiid":"0","post_author":"892","post_news":"56","author":"0","post_keywords":"发改委,国有企业,第三批,时政","post_date":"2017-11-15 14:47:49","post_times":"","post_content":"","post_title":"发改委：已确定将31家国有企业纳入第三批混改试点","post_lai":"新华网","post_mp":"http://mp3.tingwen.me/data/upload/mp3/5a0be313be548.mp3","post_time":"90000","post_size":"2183553","post_excerpt":"国家发展改革委于2017年11月15日(周三)上午9：30，在中配楼三层大会议室召开定时定主题新闻发布会，发布宏观经济运行情况并回应热点问题，请相关负责同志出席发布会，并回答记者提问。\r\n\r\n国家发改委政策研究室副主任兼新闻发言人孟玮介绍，目前的最新进展是：两批19家中央企业试点的重点任务正在逐步落地，试点先行先试、示范带动的作用正在逐步显现，超过三分之一的试点企业已基本完成引入投资者、设立新公司、重构公司治理机制、建立内部激励机制等工作，其他企业也正在按照试点方案加快落实。总的看，试点企业通过混改，有三个\u201c明显\u201d成效，投资实力明显增强，杠杆率明显降低，经营状况明显改善。特别是中国联通、东航物流等混改试点方案落地实施，改革力度大，市场反映积极，营造了良好的社会氛围。\r\n\r\n前不久，国务院国企改革领导小组审议通过了第三批试点名单，已确定将31家国有企业纳入第三批试点范围，其中既有中央企业，也有地方国有企业。目前，我们正在抓紧指导试点企业制定实施方案。从时间进度看，混合所有制改革工作进度，符合国务院国有企业改革领导小组部署的工作计划安排。\r\n\r\n（来源：新华网）","post_status":"1","comment_status":"1","post_modified":"2017-11-15 14:47:08","post_content_filtered":null,"post_parent":"0","post_type":null,"post_mime_type":"","comment_count":"0","smeta":"{\"thumb\":\"http:\\/\\/picture.tingwen.me\\/Uploads\\/2017-11-15\\/crop_5a0be2f0878b1.jpg\"}","post_hits":"5","post_like":"0","istop":"0","recommended":"0","toutiao":"头条","zhuti":"0","url":"","listeningrate":"243","praisenums":"50","praisenum":"125","gold":"0","source":"2","item_id":"0","user_login":"gaoming"}
     */

    private int status;
    private String msg;
    /**
     * term_id : 14
     * term_name : 时政
     * id : 80971
     * tuiid : 0
     * post_author : 892
     * post_news : 56
     * author : 0
     * post_keywords : 发改委,国有企业,第三批,时政
     * post_date : 2017-11-15 14:47:49
     * post_times :
     * post_content :
     * post_title : 发改委：已确定将31家国有企业纳入第三批混改试点
     * post_lai : 新华网
     * post_mp : http://mp3.tingwen.me/data/upload/mp3/5a0be313be548.mp3
     * post_time : 90000
     * post_size : 2183553
     * post_excerpt : 国家发展改革委于2017年11月15日(周三)上午9：30，在中配楼三层大会议室召开定时定主题新闻发布会，发布宏观经济运行情况并回应热点问题，请相关负责同志出席发布会，并回答记者提问。

     国家发改委政策研究室副主任兼新闻发言人孟玮介绍，目前的最新进展是：两批19家中央企业试点的重点任务正在逐步落地，试点先行先试、示范带动的作用正在逐步显现，超过三分之一的试点企业已基本完成引入投资者、设立新公司、重构公司治理机制、建立内部激励机制等工作，其他企业也正在按照试点方案加快落实。总的看，试点企业通过混改，有三个“明显”成效，投资实力明显增强，杠杆率明显降低，经营状况明显改善。特别是中国联通、东航物流等混改试点方案落地实施，改革力度大，市场反映积极，营造了良好的社会氛围。

     前不久，国务院国企改革领导小组审议通过了第三批试点名单，已确定将31家国有企业纳入第三批试点范围，其中既有中央企业，也有地方国有企业。目前，我们正在抓紧指导试点企业制定实施方案。从时间进度看，混合所有制改革工作进度，符合国务院国有企业改革领导小组部署的工作计划安排。

     （来源：新华网）
     * post_status : 1
     * comment_status : 1
     * post_modified : 2017-11-15 14:47:08
     * post_content_filtered : null
     * post_parent : 0
     * post_type : null
     * post_mime_type :
     * comment_count : 0
     * smeta : {"thumb":"http:\/\/picture.tingwen.me\/Uploads\/2017-11-15\/crop_5a0be2f0878b1.jpg"}
     * post_hits : 5
     * post_like : 0
     * istop : 0
     * recommended : 0
     * toutiao : 头条
     * zhuti : 0
     * url :
     * listeningrate : 243
     * praisenums : 50
     * praisenum : 125
     * gold : 0
     * source : 2
     * item_id : 0
     * user_login : gaoming
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

    public static class ResultsBean {
        private String term_id;
        private String term_name;
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
        private Object post_content_filtered;
        private String post_parent;
        private Object post_type;
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
        private String source;
        private String item_id;
        private String user_login;

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

        public void setPost_content_filtered(Object post_content_filtered) {
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

        public void setPost_type(Object post_type) {
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

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }
    }
}
