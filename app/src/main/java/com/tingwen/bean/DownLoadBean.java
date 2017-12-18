package com.tingwen.bean;

import java.io.Serializable;

/**
 * 下载的实体
 * Created by Administrator on 2017/10/23 0023.
 */
public class DownLoadBean implements Serializable {

    private String id; //新闻id
    private String post_date;//新闻日期
    private String post_title;//新闻标题
    private String post_lai;//新闻来源
    private String post_mp;//新闻下载地址
    private String post_time;//新闻时长
    private String post_size;//新闻大小
    private String post_excerpt;//新闻内容
    private String smeta;//新闻图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
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

    public String getSmeta() {
        return smeta;
    }

    public void setSmeta(String smeta) {
        this.smeta = smeta;
    }
}
