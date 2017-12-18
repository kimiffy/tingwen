package com.tingwen.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.flyco.roundview.RoundTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.bean.TingbiBalanceBean;
import com.tingwen.bean.WXpay;
import com.tingwen.bean.ZfbPay;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.RechargeSuccessEvent;
import com.tingwen.event.ShangSuccessEvent;
import com.tingwen.event.WxPaySuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.ColorPhrase;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.PayResult;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付
 * Created by Administrator on 2017/10/27 0027.
 */
public class PayActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.iv_ting)
    ImageView ivTing;
    @BindView(R.id.tv_go_recharge)
    RoundTextView tvGoRecharge;
    @BindView(R.id.rl_ting)
    RelativeLayout rlTing;
    @BindView(R.id.iv_zfb)
    ImageView ivZfb;
    @BindView(R.id.rl_zfb)
    RelativeLayout rlZfb;
    @BindView(R.id.iv_wx)
    ImageView ivWx;
    @BindView(R.id.rl_wx)
    RelativeLayout rlWx;
    @BindView(R.id.tv_go_pay)
    RoundTextView tvGoPay;

    private float money;//传递过来的消费金额
    private String listen_money;//听币余额
    private int type = -1;//选择的支付类型  0 听币 1支付宝 2 微信
    private Boolean isTingReady = false;//听币支付是否可用
    private String actId;
    private IWXAPI api;
    private int formType;
    private String payMoney;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitivity_pay;
    }


    @Override
    protected void initData() {
        super.initData();
        money = getIntent().getFloatExtra("money", 0);
        actId = getIntent().getStringExtra("act_id");
        formType = getIntent().getIntExtra("type", 1);
        Logger.e("打赏金额:" + money + " ID: " + actId);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, true);
        api.registerApp(AppConfig.WX_APP_ID);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void initUI() {
        super.initUI();
        getBalance();
        String s = "本次消费 < " + money + " > 听币";
        CharSequence format = ColorPhrase.from(s).withSeparator("<>").innerColor(Color.parseColor("#f67825")).format();
        tvPayMoney.setText(format);
        tvBalance.setText("账户余额:");
    }


    @OnClick({R.id.ivLeft, R.id.tv_go_recharge, R.id.rl_ting, R.id.rl_zfb, R.id.rl_wx, R.id.tv_go_pay})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tv_go_recharge:
                LauncherHelper.getInstance().launcherActivity(this, RechargeActivity.class);
                break;
            case R.id.rl_ting:
                if (isTingReady) {
                    setUnselect();
                    type = 0;
                    ivTing.setImageResource(R.drawable.icon_select);
                }
                break;
            case R.id.rl_zfb:
                setUnselect();
                type = 1;
                ivZfb.setImageResource(R.drawable.icon_select);
                break;
            case R.id.rl_wx:
                setUnselect();
                type = 2;
                ivWx.setImageResource(R.drawable.icon_select);
                break;
            case R.id.tv_go_pay:
                goPay();
                break;
            default:
                break;
        }
    }


    public void setUnselect() {
        ivTing.setImageResource(R.drawable.icon_unselect);
        ivZfb.setImageResource(R.drawable.icon_unselect);
        ivWx.setImageResource(R.drawable.icon_unselect);
    }


    /**
     * 获取听币余额
     */
    private void getBalance() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        OkGo.<TingbiBalanceBean>post(UrlProvider.GET_TINGBI_BALANCE).tag(this).params(map).execute(new SimpleJsonCallback<TingbiBalanceBean>(TingbiBalanceBean.class) {

            @Override
            public void onSuccess(Response<TingbiBalanceBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    TingbiBalanceBean.ResultsBean bean = response.body().getResults();
                    listen_money = bean.getListen_money();
                    float aFloat = Float.parseFloat(listen_money);
                    tvBalance.setText("账户余额:( " + listen_money + " 听币)");

                    if (aFloat < money) {
                        ivTing.setVisibility(View.GONE);
                        tvGoRecharge.setVisibility(View.VISIBLE);
                    } else {
                        isTingReady = true;
                        tvGoRecharge.setVisibility(View.GONE);
                        ivTing.setVisibility(View.VISIBLE);
                    }
                    AppSpUtil.getInstance().saveTingbi(listen_money);//本地保存听币数
                }
            }

            @Override
            public void onError(Response<TingbiBalanceBean> response) {
                super.onError(response);
            }
        });

    }


    /**
     * 去支付
     */
    private void goPay() {

        switch (type) {
            case -1:
                break;
            case 0:
                tingPay();
                break;
            case 1:
                ALiPay();
                break;
            case 2:
                wxPay();
                break;
        }


    }

    /**
     * 听币支付
     */
    private void tingPay() {
        payMoney = String.valueOf(money);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("listen_money", payMoney);
        map.put("act_id", actId);
        map.put("type", 1 + "");//这个type 区分是 听币 还是 其他支付方式
        OkGo.<SimpleMsgBean>post(UrlProvider.SHANG).tag(this).params(map).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                if (response.body().getStatus() == 1) {
                    if (formType == 1) {
                        EventBus.getDefault().post(new ShangSuccessEvent(1));
                    } else if (formType == 2) {
                        EventBus.getDefault().post(new ShangSuccessEvent(2, payMoney));
                    }

                    finish();
                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });


    }


    /**
     * 支付宝支付
     */
    public void ALiPay() {

        payMoney = String.valueOf(money);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("pay_type", "3");//打赏
        map.put("act_id", actId);
        map.put("money", payMoney);
        OkGo.<ZfbPay>post(UrlProvider.ALI_PAY).params(map, true).tag(this).execute(new SimpleJsonCallback<ZfbPay>(ZfbPay.class) {
            @Override
            public void onSuccess(Response<ZfbPay> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    String info = response.body().getResults().getResponse();
                    zfbPay(info);
                }
            }
        });

    }

    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                if (formType == 1) {
                    EventBus.getDefault().post(new ShangSuccessEvent(1));
                } else if (formType == 2) {
                    EventBus.getDefault().post(new ShangSuccessEvent(2, payMoney));
                }

                finish();
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    ToastUtils.showBottomToast("正在确认订单");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    ToastUtils.showBottomToast("打赏失败");
                }

            }

        }
    };

    /**
     * 支付宝支付
     *
     * @param payInfo 服务器返回订单信息
     */
    public void zfbPay(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }


    private void wxPay() {
        payMoney = String.valueOf(money);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("pay_type", "3");//打赏
        map.put("act_id", actId);
        map.put("money", payMoney);
        OkGo.<WXpay>post(UrlProvider.WEIXIN_PAY).params(map, true).tag(this).execute(new SimpleJsonCallback<WXpay>(WXpay.class) {
            @Override
            public void onSuccess(Response<WXpay> response) {

                WXpay wXpay = response.body();
                String appid = wXpay.getResults().getAppid();
                if (appid == null || appid.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "服务器异常，请稍后重试", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                String noncestr = wXpay.getResults().getNonceStr();
                String prepayid = wXpay.getResults().getPrepay_id();
                String partnerid = wXpay.getResults().getPartnerid();
                String timestamp = wXpay.getResults().getTimeStamp();
                String sign = wXpay.getResults().getSign();

                PayReq payReq = new PayReq();
                payReq.appId = appid;
                payReq.extData = "app data";
                payReq.sign = sign;
                payReq.nonceStr = noncestr;
                payReq.partnerId = partnerid;
                payReq.packageValue = "Sign=WXPay";
                payReq.prepayId = prepayid;
                payReq.timeStamp = timestamp;
                api.sendReq(payReq);
            }
        });

    }

    /**
     * 微信支付成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWXPaySuccess(WxPaySuccessEvent event) {
        int pay = event.getType();
        Logger.e("接收到微信支付完成事件");
        if (pay == 0) {
            if (formType == 1) {
                EventBus.getDefault().post(new ShangSuccessEvent(1));

            } else if (formType == 2) {

                EventBus.getDefault().post(new ShangSuccessEvent(2, payMoney));
            }
            finish();
        } else if (pay == -1) {
            ToastUtils.showBottomToast("打赏失败");
        } else if (pay == -2) {
            ToastUtils.showBottomToast("取消打赏");
        } else {
            ToastUtils.showBottomToast("打赏失败");
        }

    }

    /**
     * 微信支付成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRechargeSuccess(RechargeSuccessEvent event) {
        getBalance();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }

}
