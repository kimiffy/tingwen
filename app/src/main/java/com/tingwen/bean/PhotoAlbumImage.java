package com.tingwen.bean;

/**
 * Created by Administrator on 2016/1/13 0013.
 */
public class PhotoAlbumImage {
    private String imagePath;
    private String id;
    public boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {

        return imagePath;
    }
}
