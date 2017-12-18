package com.tingwen.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class ConsumeBean {


    /**
     * status : 1
     * msg : 获取成功!
     * results : [{"id":"617","user_id":"18653","money":"0.01","type":"2","act_id":"0","date":"1506592933","name":null,"message":null},{"id":"616","user_id":"18653","money":"59.00","type":"3","act_id":"0","date":"1506592906","name":null,"message":null},{"id":"614","user_id":"18653","money":"0.01","type":"2","act_id":"0","date":"1506592142","name":null,"message":null},{"id":"613","user_id":"18653","money":"79.00","type":"3","act_id":"0","date":"1506592095","name":null,"message":null},{"id":"610","user_id":"18653","money":"0.01","type":"2","act_id":"0","date":"1506591964","name":null,"message":null},{"id":"608","user_id":"18653","money":"39.00","type":"3","act_id":"0","date":"1506591939","name":null,"message":null},{"id":"606","user_id":"18653","money":"99.00","type":"3","act_id":"0","date":"1506591497","name":null,"message":null},{"id":"602","user_id":"18653","money":"0.01","type":"2","act_id":"0","date":"1506588243","name":null,"message":null},{"id":"599","user_id":"18653","money":"59.00","type":"3","act_id":"0","date":"1506587922","name":null,"message":null},{"id":"596","user_id":"18653","money":"9.90","type":"3","act_id":"0","date":"1506587740","name":null,"message":null}]
     */

    private int status;
    private String msg;
    /**
     * id : 617
     * user_id : 18653
     * money : 0.01
     * type : 2
     * act_id : 0
     * date : 1506592933
     * name : null
     * message : null
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
        private String act_id;
        private String date;
        private String name;
        private String message;

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

        public String getAct_id() {
            return act_id;
        }

        public void setAct_id(String act_id) {
            this.act_id = act_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
