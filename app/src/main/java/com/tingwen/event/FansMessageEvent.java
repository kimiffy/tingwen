package com.tingwen.event;

/**
 * 留言/回复
 * Created by Administrator on 2017/9/20 0020.
 */
public class FansMessageEvent {

    private int type;//0: 自己留言  1:回复别人的留言
    private String content;//留言内容
    private String id;
    private String touid;

    public FansMessageEvent(int type, String content,String id,String touid) {
        this.type = type;
        this.content = content;
        this.id=id;
        this.touid=touid;
    }

    public String getMessage() {
        return content;
    }

    public void setMessage(String message) {
        this.content = content;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

}
