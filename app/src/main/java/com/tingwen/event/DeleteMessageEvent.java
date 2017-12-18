package com.tingwen.event;

/**
 * 删除自己的留言
 * Created by Administrator on 2017/9/20 0020.
 */
public class DeleteMessageEvent {
   private  String id;

    public DeleteMessageEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
