package com.tingwen.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.WxPaySuccessEvent;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wxpay_entry);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.e("微信支付完成:"+ "errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            switch (code) {
                case 0:
                    EventBus.getDefault().post(new WxPaySuccessEvent(code));
                    finish();
                    break;
                case -1:
                    EventBus.getDefault().post(new WxPaySuccessEvent(code));
                    finish();
                    break;
                case -2:
                    EventBus.getDefault().post(new WxPaySuccessEvent(code));
                    finish();
                    break;
                default:
                    EventBus.getDefault().post(new WxPaySuccessEvent(code));
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }



    }
}
