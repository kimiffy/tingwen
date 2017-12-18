package com.tingwen.bean;

import java.io.Serializable;

/**
 * 新闻   统一类型的新闻bean
 * Created by Administrator on 2017/8/1 0001.
 */


public class NewsBean implements Serializable {

    public String post_news;//主播ID
    public String term_id = "";//频道id
    public String term_name;//频道名
    public String id; //新闻id
    public String post_id;
    public String post_author;
    public String author;
    public String post_keywords;
    public String post_date;
    public String post_content;
    public String post_title;
    public String post_lai;
    public String post_mp;
    public String post_time;
    public String post_excerpt;
    public String post_content_filtered;
    public String post_parent;
    public String post_type;
    public String post_mime_type;
    public String comment_count;
    public String smeta;
    public String post_hits;
    public String post_like;
    public String istop;
    public String recommended;
    public String toutiao;
    public String user_login;
    public String comment_list;
    public String downloadLink;
    public String simpleImage;
    public String praisenum;
    public String post_size;
    public String url;
    public String post_act;




    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect;//是否选中

    public String getPost_news() {
        return post_news;
    }

    public void setPost_news(String post_news) {
        this.post_news = post_news;
    }

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

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
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

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }

    public String getPost_content_filtered() {
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

    public String getPost_type() {
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

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getComment_list() {
        return comment_list;
    }

    public void setComment_list(String comment_list) {
        this.comment_list = comment_list;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getSimpleImage() {
        return simpleImage;
    }

    public void setSimpleImage(String simpleImage) {
        this.simpleImage = simpleImage;
    }

    public String getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(String praisenum) {
        this.praisenum = praisenum;
    }

    public String getPost_size() {
        return post_size;
    }

    public void setPost_size(String post_size) {
        this.post_size = post_size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPost_act() {
        return post_act;
    }

    public void setPost_act(String post_act) {
        this.post_act = post_act;
    }


}
