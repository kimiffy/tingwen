package com.tingwen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31 0031.
 */
public class VipBean implements Serializable{


    /**
     * status : 1
     * msg : 获取成功
     * results : {"user":{"id":"8713","avatar":"58b3e3f530a42.jpg","user_nicename":"szy"},"end_date":"2017-08-08","is_member":"1","memprcie":[{"monthes":"1","price":"19","type":"1"},{"monthes":"6","price":"59","type":"1"},{"monthes":"12","price":"99","type":"1"},{"monthes":"12","price":"1980","type":"2"}]}
     */

    private int status;
    private String msg;
    /**
     * user : {"id":"8713","avatar":"58b3e3f530a42.jpg","user_nicename":"szy"}
     * end_date : 2017-08-08
     * is_member : 1
     * memprcie : [{"monthes":"1","price":"19","type":"1"},{"monthes":"6","price":"59","type":"1"},{"monthes":"12","price":"99","type":"1"},{"monthes":"12","price":"1980","type":"2"}]
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
        /**
         * id : 8713
         * avatar : 58b3e3f530a42.jpg
         * user_nicename : szy
         */

        private UserBean user;
        private String end_date;
        private String is_member;

        public int getMember_type() {
            return member_type;
        }

        public void setMember_type(int member_type) {
            this.member_type = member_type;
        }

        private int member_type;
        /**
         * monthes : 1
         * price : 19
         * type : 1
         */

        private List<MemprcieBean> memprcie;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getIs_member() {
            return is_member;
        }

        public void setIs_member(String is_member) {
            this.is_member = is_member;
        }

        public List<MemprcieBean> getMemprcie() {
            return memprcie;
        }

        public void setMemprcie(List<MemprcieBean> memprcie) {
            this.memprcie = memprcie;
        }

        public static class UserBean {
            private String id;
            private String avatar;
            private String user_nicename;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUser_nicename() {
                return user_nicename;
            }

            public void setUser_nicename(String user_nicename) {
                this.user_nicename = user_nicename;
            }
        }

        public static class MemprcieBean implements Serializable{
            private String monthes;
            private String price;
            private String type;

            public String getMonthes() {
                return monthes;
            }

            public void setMonthes(String monthes) {
                this.monthes = monthes;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
