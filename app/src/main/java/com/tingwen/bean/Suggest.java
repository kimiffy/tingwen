package com.tingwen.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/1 0001.
 */
public class Suggest implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTuid() {
        return tuid;
    }

    public void setTuid(String tuid) {
        this.tuid = tuid;
    }

    public String getTo_comment_id() {
        return to_comment_id;
    }

    public void setTo_comment_id(String to_comment_id) {
        this.to_comment_id = to_comment_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public CommentUser getUser() {
        return user;
    }

    public void setUser(CommentUser user) {
        this.user = user;
    }

    public CommentUser getTo_user() {
        return to_user;
    }

    public void setTo_user(CommentUser to_user) {
        this.to_user = to_user;
    }

    private String id;
    private String uid;
    private String comment;
    private String images;
    private String status;
    private String tuid;
    private String to_comment_id;
    private String create_time;
    private CommentUser user;
    private CommentUser to_user;

}
