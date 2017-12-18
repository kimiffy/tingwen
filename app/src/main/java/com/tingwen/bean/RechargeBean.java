package com.tingwen.bean;

import java.util.List;

/**
 * 听币充值
 * Created by Administrator on 2017/11/7 0007.
 */
public class RechargeBean {


    /**
     * status : 1
     * msg : 获取成功!
     * results : [{"id":"1","money":"5","zs_experience":"0","zs_money":"0","status":"1"},{"id":"2","money":"10","zs_experience":"100","zs_money":"0","status":"1"},{"id":"3","money":"20","zs_experience":"300","zs_money":"8","status":"1"},{"id":"4","money":"50","zs_experience":"1000","zs_money":"30","status":"1"},{"id":"5","money":"100","zs_experience":"3000","zs_money":"60","status":"1"},{"id":"6","money":"500","zs_experience":"15000","zs_money":"200","status":"1"}]
     */

    private int status;
    private String msg;
    /**
     * id : 1
     * money : 5
     * zs_experience : 0
     * zs_money : 0
     * status : 1
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
        private String money;
        private String zs_experience;
        private String zs_money;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getZs_experience() {
            return zs_experience;
        }

        public void setZs_experience(String zs_experience) {
            this.zs_experience = zs_experience;
        }

        public String getZs_money() {
            return zs_money;
        }

        public void setZs_money(String zs_money) {
            this.zs_money = zs_money;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
