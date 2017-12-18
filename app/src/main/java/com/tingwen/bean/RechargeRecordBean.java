package com.tingwen.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class RechargeRecordBean {


    /**
     * status : 1
     * msg : 获取成功!
     * results : [{"id":"123","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 13:56:01","method":"微信支付"},{"id":"122","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 13:54:38","method":"微信支付"},{"id":"121","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 13:51:39","method":"微信支付"},{"id":"120","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 13:50:48","method":"微信支付"},{"id":"119","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 13:46:09","method":"微信支付"},{"id":"118","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 13:43:18","method":"微信支付"},{"id":"117","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 11:39:54","method":"微信支付"},{"id":"116","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 11:27:33","method":"微信支付"},{"id":"115","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 11:22:46","method":"微信支付"},{"id":"114","user_id":"18653","money":"0.01","type":"1","date":"2017-09-25 11:17:38","method":"微信支付"}]
     */

    private int status;
    private String msg;
    /**
     * id : 123
     * user_id : 18653
     * money : 0.01
     * type : 1
     * date : 2017-09-25 13:56:01
     * method : 微信支付
     */

    private List<ResultsBean> results;

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

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String id;
        private String user_id;
        private String money;
        private String type;
        private String date;
        private String method;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}
