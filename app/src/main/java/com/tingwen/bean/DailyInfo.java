package com.tingwen.bean;

/**
 * 每日限制
 * Created by Administrator on 2017/10/30 0030.
 */
public class DailyInfo {
    /**
     * results : {"num":10,"date":1497856267}
     * status : 1
     * msg : 获取成功
     */
    private ResultsEntity results;
    private int status;
    private String msg;

    public void setResults(ResultsEntity results) {
        this.results = results;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultsEntity getResults() {
        return results;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public class ResultsEntity {
        /**
         * num : 10
         * date : 1497856267
         */
        private int num;
        private String date;

        public void setNum(int num) {
            this.num = num;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getNum() {
            return num;
        }

        public String getDate() {
            return date;
        }
    }
}
