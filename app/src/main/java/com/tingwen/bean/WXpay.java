package com.tingwen.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29 0029.
 */
public class WXpay implements Serializable {

    /**
     * status : 1
     * msg : 发起支付成功
     * results : {"appid":"wx53ca9b29537db1d2","timeStamp":"1498714557","partnerid":"1295242101","nonceStr":"umlfbzx6cdnqqph15mr2rseqhw1qz7rz","prepay_id":"wx201706291335578bd87524660948911231","package":"Sign=WXPay","sign":"50B443D6D3EED6477659C2B613BD1934"}
     */

    private int status;
    private String msg;
    /**
     * appid : wx53ca9b29537db1d2
     * timetamp : 1498714557
     * partnerid : 1295242101
     * nonceStr : umlfbzx6cdnqqph15mr2rseqhw1qz7rz
     * prepay_id : wx201706291335578bd87524660948911231
     * package : Sign=WXPay
     * sign : 50B443D6D3EED6477659C2B613BD1934
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

    public static class ResultsBean implements Serializable {
        private String appid;
        private String timestamp;
        private String partnerid;
        private String noncestr;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getTimeStamp() {
            return timestamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timestamp = timeStamp;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getNonceStr() {
            return noncestr;
        }

        public void setNonceStr(String nonceStr) {
            this.noncestr = nonceStr;
        }

        public String getPrepay_id() {
            return prepayid;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepayid = prepay_id;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
