package com.tingwen.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
@Entity
public class ListenedNews {
    @Id
    public String id; //新闻id

    @Generated(hash = 585186980)
    public ListenedNews(String id) {
        this.id = id;
    }

    @Generated(hash = 1971353931)
    public ListenedNews() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
