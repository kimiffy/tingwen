package com.tingwen.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.RechargeBean;
import com.tingwen.bean.WXpay;
import com.tingwen.bean.ZfbPay;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.RechargeSuccessEvent;
import com.tingwen.event.WxPaySuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.PayPop;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.PayResult;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.UnScrollGridView;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 听币充值
 * Created by Administrator on 2017/11/7 0007.
 */
public class RechargeActivity extends BaseActivity implements PayPop.PayChooseListener {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_recharge_money)
    TextView tvRechargeMoney;
    @BindView(R.id.gv_money)
    UnScrollGridView gvMoney;
    @BindView(R.id.btn_recharge)
    Button btnRecharge;
    RechargeAdapter adapter;
    private PopupWindow popupWindow;
    private int select = 1;//选中的item
    private boolean isBenefit = false;//是否有优惠；
    private List<RechargeBean.ResultsBean> list;
    private float money = 0;//充值金额
    private PayPop payPop;
    private IWXAPI api;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recharge;
    }


    @Override
    protected void initData() {
        super.initData();

        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, true);
        api.registerApp(AppConfig.WX_APP_ID);
        list = new ArrayList<>();
        adapter = new RechargeAdapter();
        gvMoney.setAdapter(adapter);
        getData();
        EventBus.getDefault().register(this);

    }

    /**
     * 获取列表
     */
    private void getData() {

        OkGo.<RechargeBean>post(UrlProvider.RECHARGE_LIST).execute(new SimpleJsonCallback<RechargeBean>(RechargeBean.class) {
            @Override
            public void onSuccess(Response<RechargeBean> response) {
                if (response.body().getStatus() == 1) {
                    list = response.body().getResults();
                    adapter.notifyDataSetChanged();
                } else {
                    setList();
                }
            }

            @Override
            public void onError(Response<RechargeBean> response) {
                super.onError(response);
                setList();
            }
        });

    }


    @Override
    protected void initUI() {
        super.initUI();
        String tingbi = AppSpUtil.getInstance().getTingbi();
        if (!TextUtils.isEmpty(tingbi)) {
            tvMoney.setText(tingbi);
        } else {
            tvMoney.setText("0.00");
        }


    }

    @Override
    protected void setListener() {
        super.setListener();

        gvMoney.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                select = position;
                adapter.notifyDataSetChanged();
                isBenefit = "0".equals(list.get(position).getZs_money()) ? false : true;
                if (isBenefit) {
                    showPop("加送" + list.get(position).getZs_money() + "听币", view.findViewById(R.id.rl));
                }
            }
        });


        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = Float.parseFloat(list.get(select).getMoney());
                if (money > 0.0f) {
                    if (null == payPop) {
                        payPop = new PayPop(RechargeActivity.this);
                        payPop.setListener(RechargeActivity.this);
                    }
                    payPop.showPopupWindow();
                }
            }
        });

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LauncherHelper.getInstance().launcherActivity(RechargeActivity.this,TransactionRecordActivity.class);
            }
        });

    }


    private void showPop(String text, View rlView) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_tingbi_recharge, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(false);
        TextView tv = (TextView) view.findViewById(R.id.tv_tip);
        tv.setText(text);

        int[] location = new int[2];
        rlView.getLocationOnScreen(location);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = view.getMeasuredHeight();
        popupWindow.showAtLocation(rlView, Gravity.NO_GRAVITY, location[0], location[1] - height);
    }

    /**
     * 请求数据如果失败设置默认数据
     */
    private void setList() {
        RechargeBean.ResultsBean bean = new RechargeBean.ResultsBean();
        bean.setMoney("5");
        bean.setZs_money("");
        list.add(bean);

        RechargeBean.ResultsBean bean1 = new RechargeBean.ResultsBean();
        bean.setMoney("10");
        bean.setZs_money("");
        list.add(bean1);
        RechargeBean.ResultsBean bean2 = new RechargeBean.ResultsBean();
        bean.setMoney("20");
        bean.setZs_money("");
        list.add(bean2);

        RechargeBean.ResultsBean bean3 = new RechargeBean.ResultsBean();
        bean.setMoney("50");
        bean.setZs_money("");
        list.add(bean3);

        RechargeBean.ResultsBean bean4 = new RechargeBean.ResultsBean();
        bean.setMoney("100");
        bean.setZs_money("");
        list.add(bean4);

        RechargeBean.ResultsBean bean5 = new RechargeBean.ResultsBean();
        bean.setMoney("500");
        bean.setZs_money("");
        list.add(bean5);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void WxPay() {
        payPop.dismiss();
        String payMoney = String.valueOf(money);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("pay_type", "4");//充值
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

    @Override
    public void ALiPay() {
        payPop.dismiss();
        String payMoney = String.valueOf(money);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("pay_type", "4");//充值
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
        Logger.e("接收到微信支付完成事件");
        if (pay == 0) {
            EventBus.getDefault().post(new RechargeSuccessEvent());
            FollowUtil.getUserInfo();//更新用户信息
            ToastUtils.showBottomToast("充值成功!");

            finish();
        } else if (pay == -1) {
            ToastUtils.showBottomToast("充值失败");
        } else if (pay == -2) {
            ToastUtils.showBottomToast("取消充值");
        } else {
            ToastUtils.showBottomToast("充值失败");
        }

    }

    /**
     * 支付宝支付结果处理
     */
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                EventBus.getDefault().post(new RechargeSuccessEvent());
                FollowUtil.getUserInfo();
                finish();
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    ToastUtils.showBottomToast("正在确认订单");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    ToastUtils.showBottomToast("充值失败");
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
                PayTask alipay = new PayTask(RechargeActivity.this);
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
    private class RechargeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_gv_tingbi_recharge, null);
                viewHolder.tvMoney1 = (TextView) convertView.findViewById(R.id.tv_money1);
                viewHolder.tvMoney2 = (TextView) convertView.findViewById(R.id.tv_money2);
                viewHolder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvMoney1.setText(list.get(position).getMoney() + "听币");
            viewHolder.tvMoney2.setText("¥" + list.get(position).getMoney());

            if (position == select) {
                viewHolder.rl.setSelected(true);
            } else {
                viewHolder.rl.setSelected(false);
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView tvMoney1;
            private TextView tvMoney2;
            private RelativeLayout rl;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }
}
