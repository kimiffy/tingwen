package com.tingwen.bean;

/**
 * 只有状态和msg的bean
 * Created by Administrator on 2017/8/1 0001.
 */
public class SimpleMsgBean {

    /**
     * status : 1
     * msg : 取消收藏!
     */

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
