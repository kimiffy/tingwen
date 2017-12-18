package com.tingwen.bean;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public class UpImageBean {

    /**
     * status : 1
     * msg : 上传图片成功!
     * results : data/upload/feedback/59b6092a02faf.jpg
     */

    private int status;
    private String msg;
    private String results;

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

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
