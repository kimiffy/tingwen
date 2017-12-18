package com.tingwen.bean;

import java.io.Serializable;

/**
 * 验证码
 * Created by Administrator on 2017/7/26 0026.
 */
public class CodeBean implements Serializable{

    /**
     * status : 1
     * msg : 验证码发送成功!
     * results : 774368
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
