package com.tingwen.bean;

/**
 * Created by Administrator on 2017/9/1 0001.
 */
public class ImageBean {

    private String imagePath;
    private int width;
    private int height;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImagePath() {

        return imagePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
