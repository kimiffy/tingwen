package com.tingwen.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tingwen.R;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.WxLoginSuccessEvent;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI wxapi;

    enum SHARE_TYPE {Type_WXSceneSession, Type_WXSceneTimeline}
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        wxapi = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, false);
        wxapi.handleIntent(getIntent(), this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wxentry, menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法 用不上 但必须重写出来
    @Override
    public void onReq(BaseReq arg0) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {

       Logger.e("微信回调结果:"+resp.errCode);

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                switch (resp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        //登录回调,处理登录成功的逻辑
                        Logger.e("微信登录成功回调");
                        String code = ((SendAuth.Resp) resp).code;
                        EventBus.getDefault().post(new WxLoginSuccessEvent(code));
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        //分享回调,处理分享成功后的逻辑
                        Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                        break;
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                switch (resp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        Toast.makeText(WXEntryActivity.this, "登录取消", Toast.LENGTH_SHORT).show();
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        Toast.makeText(WXEntryActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:

                Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();

                break;

            default:

                break;
        }
        this.finish();
    }


}
