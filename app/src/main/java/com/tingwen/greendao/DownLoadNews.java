package com.tingwen.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/10/23 0023.
 */
@Entity
public class DownLoadNews {

    @Id
    public String id; //新闻id
    private String post_date;//新闻日期
    private String post_title;//新闻标题
    private String post_lai;//新闻来源
    private String post_mp;//新闻下载地址
    private String post_time;//新闻时长
    private String post_size;//新闻大小
    private String post_excerpt;//新闻内容
    private String smeta;//新闻图片
//    private String post_news;//主播ID
//    private String term_id = "";//频道id
//    private String term_name;//频道名
//    private String post_id;
//    private String post_author;
//    private String author;
//    private String post_keywords;
//    private String post_content;
//    private String post_content_filtered;
//    private String post_parent;
//    private String post_type;
//    private String post_mime_type;
//    private String comment_count;
//    private String post_hits;
//    private String post_like;
//    private String istop;
//    private String recommended;
//    private String toutiao;
//    private String user_login;
//    private String comment_list;
//    private String downloadLink;
//    private String simpleImage;
//    private String praisenum;
//    private String url;
//    private String post_act;


    @Generated(hash = 1920782137)
    public DownLoadNews(String id, String post_date, String post_title,
            String post_lai, String post_mp, String post_time, String post_size,
            String post_excerpt, String smeta) {
        this.id = id;
        this.post_date = post_date;
        this.post_title = post_title;
        this.post_lai = post_lai;
        this.post_mp = post_mp;
        this.post_time = post_time;
        this.post_size = post_size;
        this.post_excerpt = post_excerpt;
        this.smeta = smeta;
    }
    @Generated(hash = 1062283595)
    public DownLoadNews() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPost_date() {
        return this.post_date;
    }
    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
    public String getPost_title() {
        return this.post_title;
    }
    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }
    public String getPost_lai() {
        return this.post_lai;
    }
    public void setPost_lai(String post_lai) {
        this.post_lai = post_lai;
    }
    public String getPost_mp() {
        return this.post_mp;
    }
    public void setPost_mp(String post_mp) {
        this.post_mp = post_mp;
    }
    public String getPost_time() {
        return this.post_time;
    }
    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }
    public String getPost_size() {
        return this.post_size;
    }
    public void setPost_size(String post_size) {
        this.post_size = post_size;
    }
    public String getPost_excerpt() {
        return this.post_excerpt;
    }
    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }
    public String getSmeta() {
        return this.smeta;
    }
    public void setSmeta(String smeta) {
        this.smeta = smeta;
    }
}
