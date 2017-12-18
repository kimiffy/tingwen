package com.tingwen.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class PhotoUpImageBucket {
    private ArrayList<PhotoAlbumImage> list;
    private int count;
    private String name;
    public boolean isSelected = false;
    public boolean isAllImage = false;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setList(ArrayList<PhotoAlbumImage> list) {
        this.list = list;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public List<PhotoAlbumImage> getList() {
        return list;
    }

    public int getCount() {
        return count;
    }
}
