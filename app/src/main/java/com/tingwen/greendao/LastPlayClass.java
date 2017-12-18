package com.tingwen.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 课堂上一次播放
 * Created by Administrator on 2017/12/12 0012.
 */
@Entity
public class LastPlayClass {
    @Id
    public String id; //课堂id
    public String position;//所在列表的位置
    public String time;//已播放时长;
    @Generated(hash = 854588985)
    public LastPlayClass(String id, String position, String time) {
        this.id = id;
        this.position = position;
        this.time = time;
    }
    @Generated(hash = 1568918545)
    public LastPlayClass() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}
