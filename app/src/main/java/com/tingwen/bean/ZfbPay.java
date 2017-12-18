package com.tingwen.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/6 0006.
 */
public class ZfbPay  implements Serializable {


    /**
     * status : 1
     * msg : 发起支付成功
     * results : {"response":"alipay_sdk=alipay-sdk-php-20161101&app_id=2017041306684131&biz_content=%7B%22body%22%3A%22%5Cu8d2d%5Cu4e70%5Cu542c%5Cu95fb%5Cu4f1a%5Cu5458%22%2C%22subject%22%3A%22%5Cu8d2d%5Cu4e70%5Cu542c%5Cu95fb%5Cu4f1a%5Cu5458%22%2C%22out_trade_no%22%3A%222017134444m8713t1%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%22100%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fadmin.tingwen.me%2Findex.php%2Fapi%2FAlipay%2Fapp_notify&sign_type=RSA2&timestamp=2017-07-06+13%3A44%3A44&version=1.0&sign=FHERd%2Fszfx%2BQQZowPBk6quFOW69SmZmRRp2hud5Dhha70WfaDAlPDxDoO13uLRNARS1DLNnc8pj0OX0F0D81baEauyDFU1zt4%2BORBVuQGonS0%2BTSGMGJibxv4gS7TT2cgSL6xY220WwHvn%2BrLNmcRdk0IRATaN20SkFPpKmprycN%2Fhq6Dzx8Fgk6wj%2FemeCHNWOVxDwL%2BNuF3rsIoeeNUxjtqEEsBQ7obaNe4BDMwPO70amoqQghs1zw0fzaQe%2FPcbKY%2B%2FIHoh0cvWG3A7tVGqgmwFyecEmXbcwCc%2BSMWEa2eA1iEeMlRH1omVMWa9zG4oS5%2Fblg29YYyMf6lW49AA%3D%3D"}
     */

    private int status;
    private String msg;
    /**
     * response : alipay_sdk=alipay-sdk-php-20161101&app_id=2017041306684131&biz_content=%7B%22body%22%3A%22%5Cu8d2d%5Cu4e70%5Cu542c%5Cu95fb%5Cu4f1a%5Cu5458%22%2C%22subject%22%3A%22%5Cu8d2d%5Cu4e70%5Cu542c%5Cu95fb%5Cu4f1a%5Cu5458%22%2C%22out_trade_no%22%3A%222017134444m8713t1%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%22100%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fadmin.tingwen.me%2Findex.php%2Fapi%2FAlipay%2Fapp_notify&sign_type=RSA2&timestamp=2017-07-06+13%3A44%3A44&version=1.0&sign=FHERd%2Fszfx%2BQQZowPBk6quFOW69SmZmRRp2hud5Dhha70WfaDAlPDxDoO13uLRNARS1DLNnc8pj0OX0F0D81baEauyDFU1zt4%2BORBVuQGonS0%2BTSGMGJibxv4gS7TT2cgSL6xY220WwHvn%2BrLNmcRdk0IRATaN20SkFPpKmprycN%2Fhq6Dzx8Fgk6wj%2FemeCHNWOVxDwL%2BNuF3rsIoeeNUxjtqEEsBQ7obaNe4BDMwPO70amoqQghs1zw0fzaQe%2FPcbKY%2B%2FIHoh0cvWG3A7tVGqgmwFyecEmXbcwCc%2BSMWEa2eA1iEeMlRH1omVMWa9zG4oS5%2Fblg29YYyMf6lW49AA%3D%3D
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
        private String response;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}
