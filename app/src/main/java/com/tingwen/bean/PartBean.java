package com.tingwen.bean;

import java.io.Serializable;

/**
 *  统一的节目(主播)
 * Created by Administrator on 2017/8/7 0007.
 */
public class PartBean implements Serializable {

    private String id;
    private String friend_id;
    private String name;
    private String description;
    private String images;
    private String status;
    private String post_content;
    private String message_num;
    private String fan_num;
    private String gold;
    private String news_num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
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

    public String getMessage_num() {
        return message_num;
    }

    public void setMessage_num(String message_num) {
        this.message_num = message_num;
    }

    public String getFan_num() {
        return fan_num;
    }

    public void setFan_num(String fan_num) {
        this.fan_num = fan_num;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getNews_num() {
        return news_num;
    }

    public void setNews_num(String news_num) {
        this.news_num = news_num;
    }
}
