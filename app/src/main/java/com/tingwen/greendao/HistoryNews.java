package com.tingwen.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 播放历史
 * Created by Administrator on 2017/12/8 0008.
 */
@Entity
public class HistoryNews {
    @Id
    public String id; //新闻id
    public String time;//播放历史时长
    public String totaltime; //音频总时长
    @Generated(hash = 137019220)
    public HistoryNews(String id, String time, String totaltime) {
        this.id = id;
        this.time = time;
        this.totaltime = totaltime;
    }
    @Generated(hash = 836431513)
    public HistoryNews() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTotaltime() {
        return this.totaltime;
    }
    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }
    
}
