package com.tingwen.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.User;
import com.tingwen.bean.VipBean;
import com.tingwen.bean.WXpay;
import com.tingwen.bean.ZfbPay;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ReloadUserInfoEvent;
import com.tingwen.event.WxPaySuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.PayPop;
import com.tingwen.utils.ColorPhrase;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.PayResult;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的会员
 * Created by Administrator on 2017/7/31 0031.
 */
public class VipActivity extends BaseActivity implements PayPop.PayChooseListener {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_head_image)
    RoundImageView ivHeadImage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_describe)
    TextView tvDescribe;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.rlv_vip)
    RecyclerView rlvVip;
    @BindView(R.id.iv_vip_icon)
    ImageView ivVipIcon;
    @BindView(R.id.ll_bottom)
    LinearLayout llcontent;
    private List<VipBean.ResultsBean.MemprcieBean> list;
    private CommonAdapter<VipBean.ResultsBean.MemprcieBean> commonAdapter;
    private String mem_type;//会员类型
    private String month;  //时间
    private PayPop payPop;
    private IWXAPI api;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vip;
    }


    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, true);
        api.registerApp(AppConfig.WX_APP_ID);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void initUI() {
        super.initUI();
        mProgressHUD.show();
        TouchUtil.setTouchDelegate(ivLeft, 50);
        rlvVip.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }


    @OnClick({R.id.ivLeft,})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            default:
                break;
        }
    }


    /***
     * 加载数据
     */
    private void loadData() {
        if (!LoginUtil.isUserLogin()) {
            ToastUtils.showBottomToast("您还未登录!");
            mProgressHUD.dismiss();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        OkGo.<VipBean>post(UrlProvider.VIP).tag(this).params(map, true).execute(new SimpleJsonCallback<VipBean>(VipBean.class) {

            @Override
            public void onSuccess(Response<VipBean> response) {
                mProgressHUD.dismiss();
                VipBean.ResultsBean resultsBean = response.body().getResults();

                int member_type = resultsBean.getMember_type();
                String endDate = resultsBean.getEnd_date();

                VipBean.ResultsBean.UserBean user = resultsBean.getUser();
                String avatar = user.getAvatar();
                if(null==avatar){//避免没有头像的用户,获取数据为空 造成空指针
                    avatar="";
                }
                String id = user.getId();
                String nicename = user.getUser_nicename();
                list = resultsBean.getMemprcie();

                if (!avatar.equals("") && !avatar.contains("http")) {
                    Glide.with(VipActivity.this).load(UrlProvider.URL_IMAGE_USER + avatar).placeholder(R.drawable.img_touxiang)
                            .error(R.drawable.img_touxiang).into(ivHeadImage);
                } else {
                    String replace = avatar.replace("http://admin.tingwen.me/data/upload/avatar/", "");
                    Glide.with(VipActivity.this).load(replace).placeholder(R.drawable.img_touxiang)
                            .error(R.drawable.img_touxiang).into(ivHeadImage);
                }

                tvName.setText(nicename);
                User userInfo = AppSpUtil.getInstance().getUserInfo();
                if (userInfo != null && member_type != 0) {
                    if (member_type == 1) {
                        String s = "您已是会员,有效期至< " + endDate + " >,新购买的会员将在此日期后自动生效";
                        CharSequence format = ColorPhrase.from(s).withSeparator("<>").innerColor(Color.parseColor("#5cb8e6")).format();
                        tvDescribe.setText(format);
                        ivVipIcon.setImageResource(R.drawable.icon_vip);
                    } else if (member_type == 2) {

                        String s = "您已是超级会员,有效期至< " + endDate + " >,新购买的会员将在此日期后自动生效";
                        CharSequence format = ColorPhrase.from(s).withSeparator("<>").innerColor(Color.parseColor("#5cb8e6")).format();
                        tvDescribe.setText(format);
                        ivVipIcon.setImageResource(R.drawable.icon_svip);
                    }
                } else {
                    tvDescribe.setText("您还不是会员,开通会员可收听更多资讯");
                }
                commonAdapter = new CommonAdapter<VipBean.ResultsBean.MemprcieBean>(VipActivity.this, R.layout.item_vip, list) {
                    @Override
                    protected void convert(ViewHolder holder, final VipBean.ResultsBean.MemprcieBean bean, final int position) {

                        RelativeLayout head = holder.getView(R.id.rl_type_head);
                        TextView type = holder.getView(R.id.tv_type);
                        TextView month = holder.getView(R.id.tv_month);
                        TextView price = holder.getView(R.id.tv_month_price);
                        RoundTextView pay = holder.getView(R.id.rtv_vip_pay);


                        if (bean.getType().equals("1") && position == 0) {
                            head.setVisibility(View.VISIBLE);
                            type.setText("普通会员");
                        }

                        if (bean.getType().equals("2")) {
                            head.setVisibility(View.VISIBLE);
                            type.setText("超级会员");
                        }
                        month.setText(bean.getMonthes() + "个月");
                        price.setText("¥ " + bean.getPrice());
                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String beanType = bean.getType();
                                String beanMonthe = bean.getMonthes();
                                buyVIP(beanType, beanMonthe);

                            }
                        });

                    }
                };
                rlvVip.setAdapter(commonAdapter);
                llcontent.setVisibility(View.VISIBLE);

            }


            @Override
            public void onError(Response<VipBean> response) {
                super.onError(response);
                mProgressHUD.dismiss();
                boolean netWorkConnected = NetUtil.isHasNetAvailable(VipActivity.this);
                if (!netWorkConnected) {
                    ToastUtils.showBottomToast("无网络连接!");
                    return;
                }
                ToastUtils.showBottomToast("请求失败,请稍后重试");

            }
        });


    }


    /**
     * 购买会员
     */
    private void buyVIP(String beanType, String beanMonth) {

        if (LoginUtil.isUserLogin()) {
            mem_type = beanType;
            month = beanMonth;
            payPop = new PayPop(this);
            payPop.setListener(this);
            payPop.showPopupWindow();

        }

    }

    /**
     * 获取用户信息并保存
     */
    private void getUserInfo() {
        OkGo.<User>post(UrlProvider.GET_USER_INFO).params("accessToken", LoginUtil.getAccessToken()).tag(this).execute(new SimpleJsonCallback<User>(User.class) {
            @Override
            public void onSuccess(Response<User> response) {
                AppSpUtil.getInstance().saveUserInfo(response.body().toString());
                EventBus.getDefault().post(new ReloadUserInfoEvent());
            }
        });

    }


    /**
     * 微信购买会员
     * pay_type 1:课堂 2: 会员
     * mem_type 1:普通会员 2: 超级会员
     * month 月数
     */
    @Override
    public void WxPay() {
        payPop.dismiss();
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("pay_type", "2");
        map.put("mem_type", mem_type);
        map.put("month", month);
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
     * 支付宝支付
     */
    @Override
    public void ALiPay() {
        payPop.dismiss();
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("pay_type", "2");//1,购买课堂 2购买会员 3打赏 4充值
        map.put("mem_type", mem_type);
        map.put("month", month);

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

    @Override
    public void cancel() {
        payPop.dismiss();
    }


    /**
     * 微信支付成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWXPaySuccess(WxPaySuccessEvent event) {
        int pay = event.getType();
        if (pay == 0) {
            ToastUtils.showBottomToast("支付成功");
            loadData();
            getUserInfo();
        } else if (pay == -1) {
            ToastUtils.showBottomToast("支付失败");
        } else if (pay == -2) {
            ToastUtils.showBottomToast("取消支付");
        }else{
            ToastUtils.showBottomToast("支付失败");
        }

    }


    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                ToastUtils.showBottomToast("支付成功");
                loadData();
                getUserInfo();
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    ToastUtils.showBottomToast("正在确认订单");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    ToastUtils.showBottomToast("支付失败");
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
                PayTask alipay = new PayTask(VipActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }


}
