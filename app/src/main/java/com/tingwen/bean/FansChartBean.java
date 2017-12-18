package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 粉丝榜(赞赏榜)
 * Created by Administrator on 2017/8/8 0008.
 */
public class FansChartBean implements Serializable {


    /**
     * status : 1
     * msg : 榜单获取成功!
     * results : {"rank":0,"list":[{"user_id":"10563","date":"1491560414","user_nicename":"sq660589","user_avatar":"http://q.qlogo.cn/qqapp/1105760468/298AAC3908DBF68983D9A9308B34133B/100","total":"500.00","listen_money":"5.00","gold":"0","experience":"52","message":null,"level":"4"}]}
     */

    private int status;
    private String msg;
    /**
     * rank : 0
     * list : [{"user_id":"10563","date":"1491560414","user_nicename":"sq660589","user_avatar":"http://q.qlogo.cn/qqapp/1105760468/298AAC3908DBF68983D9A9308B34133B/100","total":"500.00","listen_money":"5.00","gold":"0","experience":"52","message":null,"level":"4"}]
     */

    private ResultsBean results;

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

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public static class ResultsBean implements Serializable{
        private int rank;
        /**
         * user_id : 10563
         * date : 1491560414
         * user_nicename : sq660589
         * user_avatar : http://q.qlogo.cn/qqapp/1105760468/298AAC3908DBF68983D9A9308B34133B/100
         * total : 500.00
         * listen_money : 5.00
         * gold : 0
         * experience : 52
         * message : null
         * level : 4
         */

        private List<ListBean> list;

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable {
            private String user_id;
            private String date;
            private String user_nicename;
            private String user_avatar;
            private String total;
            private String listen_money;
            private String gold;
            private String experience;
            private String message;
            private String level;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getUser_nicename() {
                return user_nicename;
            }

            public void setUser_nicename(String user_nicename) {
                this.user_nicename = user_nicename;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getListen_money() {
                return listen_money;
            }

            public void setListen_money(String listen_money) {
                this.listen_money = listen_money;
            }

            public String getGold() {
                return gold;
            }

            public void setGold(String gold) {
                this.gold = gold;
            }

            public String getExperience() {
                return experience;
            }

            public void setExperience(String experience) {
                this.experience = experience;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }
        }
    }
}
