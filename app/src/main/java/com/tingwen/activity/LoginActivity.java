package com.tingwen.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.User;
import com.tingwen.bean.WeiBoBean;
import com.tingwen.bean.WxLoginBean;
import com.tingwen.bean.WxUserInfo;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.LoginSuccessEvent;
import com.tingwen.event.WxLoginSuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * Created by Administrator on 2017/7/25 0025.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.tv_login)
    RoundTextView tvLogin;
    @BindView(R.id.ll_weixin_login)
    LinearLayout llWeixinLogin;
    @BindView(R.id.ll_qq_login)
    LinearLayout llQqLogin;
    @BindView(R.id.ll_weibo_login)
    LinearLayout llWeiboLogin;
    @BindView(R.id.tv_forget_psw)
    TextView tvForgetPsw;

    //登录相关

    private String token;
    private String expires;
    private String openId;
    private LogInListener logInListener;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        String from = getIntent().getStringExtra("from");//从哪个界面跳转过来的

        if (!TextUtils.isEmpty(from) && from.equals("audition")) {//试听
            new MaterialDialog.Builder(this)
                    .title("温馨提示")
                    .content("登录后才可以购买哦~")
                    .contentColorRes(R.color.text_black)
                    .positiveText("知道了")
                    .build().show();

        }
    }

    @Override
    protected void initUI() {
        super.initUI();
        TouchUtil.setTouchDelegate(ivLeft, 50);
        TouchUtil.setTouchDelegate(tvRight, 50);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.ivLeft, R.id.tv_right, R.id.tv_login, R.id.ll_weixin_login, R.id.ll_qq_login, R.id.ll_weibo_login, R.id.tv_forget_psw})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tv_right:
                LauncherHelper.getInstance().launcherActivity(this, RegisterActivity.class);
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.ll_weixin_login:
                wxLogin();
                break;
            case R.id.ll_qq_login:
                qqLogin();
                break;
            case R.id.ll_weibo_login:
                wbLogin();
                break;
            case R.id.tv_forget_psw:
                LauncherHelper.getInstance().launcherActivity(this, ForgetPswActivity.class);
                break;
            default:
                break;
        }
    }


    //微信登录相关
    private SendAuth.Req req;
    private IWXAPI api;

    /**
     * 微信登录
     */
    private void wxLogin() {

        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, false);
        }
        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, "还没有安装微信哦~", Toast.LENGTH_SHORT).show();
            return;
        }
        api.registerApp(AppConfig.WX_APP_ID);//注册到微信
        req = new SendAuth.Req();//授权
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void WXLoginSuccessEvent(WxLoginSuccessEvent event) {
        String code = event.getCode();
        if (!TextUtils.isEmpty(code)) {
            getWeixinAcces(code);
        }
    }

    /**
     * 获取微信接口的accessToken
     *
     * @param code
     */
    private void getWeixinAcces(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=";
        StringBuilder builder = new StringBuilder();
        builder.append(url).append(AppConfig.WX_APP_ID).append("&secret=").append(AppConfig.WX_APP_SECRET)
                .append("&code=").append(code).append("&grant_type=authorization_code");
        url = builder.toString();
        OkGo.<WxLoginBean>post(url).tag(this).execute(new SimpleJsonCallback<WxLoginBean>(WxLoginBean.class) {
            @Override
            public void onSuccess(Response<WxLoginBean> response) {
                WxLoginBean bean = response.body();
                token = bean.getAccess_token();
                expires = bean.getExpires_in() + "";
                openId = bean.getUnionid();
                if (!TextUtils.isEmpty(token)) {
                    getWeixinUserInfo(token, openId);
                }
            }
        });

    }


    /**
     * 获取微信的用户信息
     *
     * @param accessToken
     */
    private void getWeixinUserInfo(String accessToken, String openId) {
        String uri = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
        OkGo.<WxUserInfo>post(uri).tag(this).execute(new SimpleJsonCallback<WxUserInfo>(WxUserInfo.class) {
            @Override
            public void onSuccess(Response<WxUserInfo> response) {
                WxUserInfo bean = response.body();
                String head = bean.getHeadimgurl();
                String name = bean.getNickname();
                otherLogin(name, head, "3");
            }
        });


    }


    //腾讯登录相关
    private Tencent mTencent;
    private UserInfo mInfo;

    /**
     * QQ登录
     */
    private void qqLogin() {

        if (!NetUtil.isHasNetAvailable(this)) {
            ToastUtils.showBottomToast("请检查网络");
            return;
        }
        mTencent = Tencent.createInstance(AppConfig.APP_ID, getApplicationContext());
        logInListener = new LogInListener();
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", logInListener);
        } else {
            mTencent.logout(this);
        }


    }

    //获取openid和token
    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {

        }
    }


    /**
     * 获取QQ的用户信息
     */
    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject) response;

                    try {
                        String name = json.getString("nickname");
                        String head = json.getString("figureurl_qq_2");
                        if (head == null || head.equals("")) {
                            head = json.getString("figureurl_qq_1");
                        }
                        Logger.e(head);
                        otherLogin(name, head, "1");

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
            Toast.makeText(this, "登陆出错了", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }


    //微博相关
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    /**
     * 微博登录
     */
    private void wbLogin() {
        AuthInfo mAuthInfo = new AuthInfo(this, AppConfig.WB_APP_KEY, AppConfig.WB_REDIRECT_URL, AppConfig.WB_SCOPE);
        WbSdk.install(this, mAuthInfo);

        mSsoHandler = new SsoHandler(LoginActivity.this);
        mSsoHandler.authorize(new SelfWbAuthListener());


    }


    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
//                        updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                        getWbUserInfo();
                    }
                }
            });
        }

        @Override
        public void cancel() {
            ToastUtils.showBottomToast("取消授权!");
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            ToastUtils.showBottomToast(errorMessage.getErrorMessage());
        }
    }

    /**
     * 获取微博用户信息
     */
    private void getWbUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", mAccessToken.getToken());
        map.put("uid", mAccessToken.getUid());
        OkGo.<String>get("https://api.weibo.com/2/users/show.json")
                .params(map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        WeiBoBean weiBoBean = new Gson().fromJson(response.body(), WeiBoBean.class);
                        openId = weiBoBean.getId();
                        String name = weiBoBean.getName();
                        String head = weiBoBean.getAvatar_hd();
                        expires = String.valueOf(mAccessToken.getExpiresTime());
                        token = mAccessToken.getToken();
                        otherLogin(name, head, "2");
                    }
                });

    }


    /**
     * 手机登录
     */
    private void login() {
        String phone = etPhone.getText().toString().trim();
        String psd = etPsd.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showBottomToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(psd)) {
            ToastUtils.showBottomToast("请输入密码");
            return;
        }

        KeyBoardUtils.closeKeyboard(etPsd);

        try {
            String accountCode = LoginUtil.encode(LoginUtil.AESCODE, phone.getBytes());
            String psdCode = LoginUtil.encode(LoginUtil.AESCODE, psd.getBytes());
            OkGo.<User>post(UrlProvider.LOGIN)
                    .params("accessToken", accountCode)
                    .params("password", psdCode)
                    .tag(this)
                    .execute(new SimpleJsonCallback<User>(User.class) {
                                 @Override
                                 public void onSuccess(Response<User> response) {
                                     int code = response.body().getStatus();
                                     if (code == 1) {
                                         AppSpUtil.getInstance().saveUserInfo(response.body().toString());
                                         EventBus.getDefault().post(new LoginSuccessEvent(AppConfig.LOGIN_TYPE_TINGWEN));
                                         AppSpUtil.getInstance().saveTingbi(response.body().getResults().getListen_money());
                                         AppSpUtil.getInstance().saveJingbi(response.body().getResults().getGold());
                                         ToastUtils.showBottomToast("登录成功!");
                                         finish();
                                     } else {
                                         ToastUtils.showBottomToast(response.body().getMsg());
                                     }

                                 }

                                 @Override
                                 public void onError(Response<User> response) {
                                     super.onError(response);
                                     ToastUtils.showBottomToast("登录失败!");
                                 }
                             }
                    );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 第三方登录
     *
     * @param type
     */
    private void otherLogin(String name, String head, final String type) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("head", head);
        map.put("type", type);
        map.put("openid", openId);
        map.put("access_token", token);
        map.put("expires_in", expires);
        OkGo.<User>post(UrlProvider.OTHER_LOGIN).params(map, true).tag(this).execute(new SimpleJsonCallback<User>(User.class) {
            @Override
            public void onSuccess(Response<User> response) {

                int status = response.body().getStatus();
                if (status == 1) {
                    User.ResultsBean bean = response.body().getResults();
                    if (bean.getUser_status().equals("0")) {
                        ToastUtils.showBottomToast("您的账号已被冻结!");
                        AppSpUtil.getInstance().deleteUserInfo();
                        return;
                    }

                    if (!bean.getAvatar().equals("") && bean.getAvatar().contains("http")) {
                        bean.setAvatar(UrlProvider.URL_IMAGE_USER + bean.getAvatar());
                    }
                    AppSpUtil.getInstance().saveUserInfo(response.body().toString());
                    AppSpUtil.getInstance().saveTingbi(response.body().getResults().getListen_money());
                    AppSpUtil.getInstance().saveJingbi(response.body().getResults().getGold());
                    EventBus.getDefault().post(new LoginSuccessEvent(type));
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                }

            }
        });

    }


    private class LogInListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            //设置openid和token，否则获取不到下面的信息
            initOpenidAndToken(jsonObject);
            //获取QQ用户的各信息
            updateUserInfo();
        }

        @Override
        public void onError(UiError uiError) {


        }

        @Override
        public void onCancel() {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, logInListener);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(this);

    }
}
