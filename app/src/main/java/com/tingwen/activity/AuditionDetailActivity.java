package com.tingwen.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tingwen.R;
import com.tingwen.adapter.AuditionCommentAdapter;
import com.tingwen.adapter.AuditionListenAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.AuditionBean;
import com.tingwen.bean.CommentBean;
import com.tingwen.bean.FansMessageBean;
import com.tingwen.bean.PartBean;
import com.tingwen.bean.WXpay;
import com.tingwen.bean.ZfbPay;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ClassListenEvent;
import com.tingwen.event.ClassReLoadEvent;
import com.tingwen.event.WxPaySuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.PayPop;
import com.tingwen.popupwindow.SuperVipPop;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.PayResult;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.SystemBarHelper;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;
import com.tingwen.widget.AutoAdjustHeightImageView;
import com.tingwen.widget.CommonHeader;
import com.tingwen.widget.logger.Logger;

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
 * 课堂详情
 * Created by Administrator on 2017/8/16 0016.
 */
public class AuditionDetailActivity extends BaseActivity implements PayPop.PayChooseListener, SuperVipPop.CommitListener {
    @BindView(R.id.rl_parent)
    RelativeLayout pareent;
    @BindView(R.id.rlv_audition)
    LRecyclerView rlvAudition;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    @BindView(R.id.tv_shiting)
    TextView tvShiting;
    @BindView(R.id.ll_shiting)
    LinearLayout llShiting;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    private String act_id = "";
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private AuditionCommentAdapter commentAdapter;
    private RoundImageView headPhoto;
    private TextView title, titleListen, titleComment;
    private LinearLayout llContent;
    private LinearLayout photos;
    private RecyclerView rlvListen;
    private List<AuditionBean.ResultsBean.ShitingBean> listenList;
    private View line;
    private AuditionListenAdapter auditionListenAdapter;
    private int page = 1;
    private List<CommentBean> commentList;
    private int screenHeight;
    private PayPop payPop;
    private SuperVipPop superVipPop;
    private String price;
    private String title1;

    private Boolean isBuySvip = false;//是否选择购买超级会员

    private Boolean isSvipCall = false;//是否是购买超级会员

    private IWXAPI api;
    private PartBean part;
    private String svipPrice;
    private String photoUrl;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_audition_detail;
    }

    @Override
    protected boolean isNeedStatusBarTranslucent() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        SystemBarHelper.immersiveStatusBar(this);
        act_id = getIntent().getStringExtra("act_id");
        part = (PartBean) getIntent().getSerializableExtra("part");

        commentList = new ArrayList<>();
        commentAdapter = new AuditionCommentAdapter(this, commentList);
        rlvAudition.setLayoutManager(new LinearLayoutManager(this));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(commentAdapter);
        rlvAudition.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvAudition.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvAudition.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvAudition.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");
        EventBus.getDefault().register(this);

        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, true);
        api.registerApp(AppConfig.WX_APP_ID);

    }

    @Override
    protected void initUI() {
        super.initUI();
        int statusHeight = SizeUtil.getStatusHeight(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, statusHeight, 0, 0);
        rlBack.setLayoutParams(layoutParams);
        TouchUtil.setTouchDelegate(ivLeft, 5);
        addHead();
        getData();
    }


    @Override
    protected void setListener() {
        super.setListener();

        rlvAudition.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                getComment(page);

            }
        });
        rlvAudition.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                if (distanceY == 0) {
                    rlBack.setBackgroundColor(Color.argb(0, 255, 255, 255));
                    ivLeft.setImageResource(R.drawable.icon_left_white);
                    SystemBarHelper.tintStatusBar(AuditionDetailActivity.this, Color.parseColor("#00000000"));
                } else if (distanceY > 0 && distanceY < screenHeight / 3) {
                    rlBack.setBackgroundColor(Color.argb(0, 255, 255, 255));
                    ivLeft.setImageResource(R.drawable.icon_left_white);
                    SystemBarHelper.tintStatusBar(AuditionDetailActivity.this, Color.parseColor("#00000000"));

                } else if (distanceY > 0 && distanceY > screenHeight / 3) {
                    rlBack.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    ivLeft.setImageResource(R.drawable.icon_left_back);
                    SystemBarHelper.tintStatusBar(AuditionDetailActivity.this, Color.parseColor("#ffffff"));

                }

            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });

        headPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(photoUrl)) {
                    ArrayList<String> imagesList = new ArrayList<>();
                    imagesList.add(photoUrl);
                    ImagesActivity.getImagesActivityInstance(AuditionDetailActivity.this, imagesList, 0);
                    overridePendingTransition(R.anim.image_in, 0);
                }
            }
        });

    }

    @OnClick({R.id.ivLeft, R.id.iv_play, R.id.ll_pay, R.id.iv_pause})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.iv_play:
                if (null != listenList && listenList.size() != 0) {
                    ToastUtils.showBottomToast("开始试听");
                    TwApplication.getNewsPlayer().setMp3List(listenList);
                    TwApplication.getNewsPlayer().setActId(act_id);
                    TwApplication.getNewsPlayer().playMp3(0);
                }
            case R.id.iv_pause:

                if (null != listenList && listenList.size() != 0 && TwApplication.getNewsPlayer().isPlaying()) {

                    ToastUtils.showBottomToast("停止试听");
                    TwApplication.getNewsPlayer().pauseMp3();

                }

                break;
            case R.id.ll_pay:
                buyClass();
                break;
            default:
                break;
        }
    }


    /**
     * 添加头部
     */
    private void addHead() {
        CommonHeader head = new CommonHeader(this, R.layout.header_audition);
        lRecyclerViewAdapter.addHeaderView(head);
        headPhoto = (RoundImageView) head.findViewById(R.id.iv_photo);
        title = (TextView) head.findViewById(R.id.tv_title);
        llContent = (LinearLayout) head.findViewById(R.id.ll_content);
        photos = (LinearLayout) head.findViewById(R.id.ll_photos);
        rlvListen = (RecyclerView) head.findViewById(R.id.rlv_try_listen);
        line = head.findViewById(R.id.line_title);
        titleListen = (TextView) head.findViewById(R.id.tv_common_title1);
        titleComment = (TextView) head.findViewById(R.id.tv_common_title2);
        int screenWidth = SizeUtil.getScreenWidth();
        screenHeight = SizeUtil.getScreenHeight();
        headPhoto.setMaxHeight(screenHeight / 3);
        headPhoto.setMaxWidth(screenWidth);
        headPhoto.setAdjustViewBounds(true);
        listenList = new ArrayList<>();
        rlvListen.setLayoutManager(new LinearLayoutManager(this));
        auditionListenAdapter = new AuditionListenAdapter(this, listenList);
        auditionListenAdapter.setAct_id(act_id);
        rlvListen.setAdapter(auditionListenAdapter);
    }

    /**
     * 获取详情数据
     */
    private void getData() {
        mProgressHUD.show();
        Map<String, String> map = new HashMap<>();
        map.put("act_id", act_id);
        OkGo.<AuditionBean>post(UrlProvider.AUDITION_DETAIL).tag(this).params(map, true).execute(new SimpleJsonCallback<AuditionBean>(AuditionBean.class) {
            @Override
            public void onSuccess(Response<AuditionBean> response) {
                if(!isFinishing()&&null!=mProgressHUD){
                    mProgressHUD.dismiss();
                }

                int status = response.body().getStatus();
                if (status != 1) {
                    ToastUtils.showBottomToast(response.body().getMsg());
                    return;
                }
                AuditionBean.ResultsBean resultsBean = response.body().getResults();
                String smeta = resultsBean.getSmeta();
                Logger.e("大图:"+smeta);
                photoUrl = ImageUtil.changeComplexAddress(resultsBean.getSmeta());


                Glide.with(AuditionDetailActivity.this).load(photoUrl).priority(Priority.HIGH).error(R.drawable.tingwen_bg_rectangle).into(headPhoto);
                title.setText(resultsBean.getTitle());
                line.setVisibility(View.VISIBLE);
                String content = resultsBean.getExcerpt();
                addContent(content);
                String[] images = resultsBean.getImages().split(",");
                addImages(images);
                setBottom(resultsBean);
                listenList = resultsBean.getShiting();

                String playingMp3 = TwApplication.getNewsPlayer().getPlayingMp3();
                auditionListenAdapter.setPlayMp3(playingMp3);

                auditionListenAdapter.setDataList(listenList);

                titleListen.setVisibility(View.VISIBLE);
                titleComment.setVisibility(View.VISIBLE);

                List<AuditionBean.ResultsBean.CommentsBean> comments = resultsBean.getComments();
                Gson gson = new Gson();
                String s = gson.toJson(comments);
                commentList = gson.fromJson(s, new TypeToken<List<CommentBean>>() {
                }.getType());//转换成统一的评论对象
                commentAdapter.setDataList(commentList);
                if(null!=rlvAudition){
                    rlvAudition.refreshComplete(5);//每页加载数量
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (page == 1 && commentList.size() < 5) {
                    rlvAudition.setNoMore(true);
                }

            }

            @Override
            public void onError(Response<AuditionBean> response) {
                super.onError(response);
                if(!isFinishing()&&null!=mProgressHUD){
                    mProgressHUD.dismiss();
                }
                ToastUtils.showBottomToast("获取数据失败!");
            }
        });
    }


    /**
     * 获取更多评论
     *
     * @param num
     */
    private void getComment(int num) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(num));
        map.put("limit", "5");
        map.put("uid", LoginUtil.getUserId());
        map.put("act_id", act_id);
        OkGo.<FansMessageBean>post(UrlProvider.ACT_COMMENTS).tag(this).params(map, true).execute(new SimpleJsonCallback<FansMessageBean>(FansMessageBean.class) {
            @Override
            public void onSuccess(Response<FansMessageBean> response) {

                List<FansMessageBean.ResultsBean> results = response.body().getResults();
                Gson gson = new Gson();
                String s = gson.toJson(results);
                List<CommentBean> comment = gson.fromJson(s, new TypeToken<List<CommentBean>>() {
                }.getType());//转换成统一的评论对象列表

                commentList.addAll(comment);
                commentAdapter.setDataList(commentList);
                if (page > 1 && comment.size() < 5) {
                    rlvAudition.setNoMore(true);
                }
                if(null!=rlvAudition){
                    rlvAudition.refreshComplete(5);//每页加载数量
                }

                lRecyclerViewAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Response<FansMessageBean> response) {
                super.onError(response);
                if (page > 1) {
                    rlvAudition.setNoMore(true);
                }
            }
        });

    }


    /**
     * 设置底部(价格)
     *
     * @param resultsBean
     */
    private void setBottom(AuditionBean.ResultsBean resultsBean) {
        title1 = resultsBean.getTitle();
        price = subZeroAndDot(resultsBean.getPrice());
        svipPrice = resultsBean.getSvipPrice();
        String s1 = subZeroAndDot(resultsBean.getSprice());
        String money = "¥ " + price + "\n" + "¥" + s1;
        int index = money.indexOf("\n");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(money);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(SizeUtil.dip2px(18)), 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(SizeUtil.dip2px(14)), index + 1, money.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(new StrikethroughSpan(), index + 1, money.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvMoney.setText(spannableStringBuilder);

    }

    /**
     * 添加图片
     *
     * @param images
     */
    private void addImages(final String[] images) {

        photos.removeAllViews();
        for (int i = 0; i < images.length; i++) {
            if (!TextUtils.isEmpty(images[i])) {
                final AutoAdjustHeightImageView imageView = new AutoAdjustHeightImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = SizeUtil.dip2px(this, 10);
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(layoutParams);
                if (isFinishing()) {
                    return;
                }
                try {
                    Glide.with(this).load(images[i]).error(R.drawable.tingwen_bg_rectangle).into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                photos.addView(imageView);


                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> imagesList = new ArrayList<String>();
                        imagesList.add(images[finalI]);
                        ImagesActivity.getImagesActivityInstance(AuditionDetailActivity.this, imagesList, 0);
                        overridePendingTransition(R.anim.image_in, 0);
                    }

                });
            }
        }
    }


    /**
     * 添加介绍内容
     *
     * @param content
     */
    private void addContent(String content) {
        String[] strings = content.split("【");
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].contains("】")) {
                TextView textview = new TextView(this);
                SpannableStringBuilder builder = new SpannableStringBuilder("【" + strings[i]);
                builder.setSpan(new StyleSpan(Typeface.BOLD), 0, strings[i].indexOf("】") + 1,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                textview.setText(builder);
                textview.setTextSize(15);
                llContent.addView(textview);
            }
        }
    }

    /**
     * 刷新试听列表UI事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClassListenEvent(ClassListenEvent event) {
        int state = event.getState();
        String mp3 = event.getMp3();
        if (state == 1) {
            auditionListenAdapter.setPlayMp3(mp3);
            auditionListenAdapter.notifyDataSetChanged();
            ivPlay.setVisibility(View.VISIBLE);
            ivPause.setVisibility(View.GONE);
        } else if (state == 2) {
            auditionListenAdapter.setPlayMp3("");
            auditionListenAdapter.notifyDataSetChanged();
            ivPause.setVisibility(View.GONE);
            ivPlay.setVisibility(View.VISIBLE);
        } else if (state == 3) {
            ivPlay.setVisibility(View.GONE);
            ivPause.setVisibility(View.VISIBLE);
        } else if (state == 4) {
            ivPause.setVisibility(View.GONE);
            ivPlay.setVisibility(View.VISIBLE);
        }


    }


    /**
     * 去掉小数点和0
     *
     * @param s
     * @return
     */
    public String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 购买课堂
     */
    private void buyClass() {
        if (LoginUtil.isUserLogin()) {
            if (!TextUtils.isEmpty(price)) {
                superVipPop = new SuperVipPop(this, price, title1, svipPrice);
                superVipPop.setCommitListener(this);
                superVipPop.showAtLocation(pareent, Gravity.BOTTOM, 0, 0);
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("from","audition");
            LauncherHelper.getInstance().launcherActivity(AuditionDetailActivity.this, LoginActivity.class,bundle);



        }


    }

    @Override
    public void WxPay() {
        payPop.dismiss();

        if (isBuySvip) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("pay_type", "2");
            map.put("mem_type", "2");
            map.put("month", "12");
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

                    isSvipCall = true;

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

        } else {

            Map<String, String> map = new HashMap<>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("pay_type", 1 + "");
            map.put("act_id", act_id);

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
                    isSvipCall = false;
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


    }

    @Override
    public void ALiPay() {
        payPop.dismiss();
        if (isBuySvip) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("pay_type", "2");
            map.put("mem_type", "2");
            map.put("month", "12");
            OkGo.<ZfbPay>post(UrlProvider.ALI_PAY).params(map, true).tag(this).execute(new SimpleJsonCallback<ZfbPay>(ZfbPay.class) {
                @Override
                public void onSuccess(Response<ZfbPay> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        String info = response.body().getResults().getResponse();
                        isSvipCall = true;
                        zfbPay(info);

                    }
                }
            });

        } else {

            Map<String, String> map = new HashMap<String, String>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("pay_type", "1");//1,购买课堂  2购买会员 3打赏 4充值
            map.put("act_id", act_id);
            OkGo.<ZfbPay>post(UrlProvider.ALI_PAY).params(map, true).tag(this).execute(new SimpleJsonCallback<ZfbPay>(ZfbPay.class) {
                @Override
                public void onSuccess(Response<ZfbPay> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        String info = response.body().getResults().getResponse();
                        isSvipCall = false;
                        zfbPay(info);

                    }
                }
            });


        }

    }


    @Override
    public void cancel() {
        payPop.dismiss();
    }

    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();

            if (TextUtils.equals(resultStatus, "9000")) {
                ToastUtils.showBottomToast("支付成功");
                if (isSvipCall) {
                    FollowUtil.getUserInfo();
                }
                EventBus.getDefault().post(new ClassReLoadEvent());
                toAnchor(part);
                finish();
            } else {
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
                PayTask alipay = new PayTask(AuditionDetailActivity.this);
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
            if (isSvipCall) {
                FollowUtil.getUserInfo();
            }
            EventBus.getDefault().post(new ClassReLoadEvent());
            toAnchor(part);
            finish();
        } else if (pay == -1) {
            ToastUtils.showBottomToast("支付失败");
        } else if (pay == -2) {
            ToastUtils.showBottomToast("取消支付");
        } else {
            ToastUtils.showBottomToast("支付失败");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void VipCommit() {
        superVipPop.dismiss();
        isBuySvip = true;
        payPop = new PayPop(this);
        payPop.setListener(this);
        payPop.showPopupWindow();


    }

    @Override
    public void NorCommit() {
        superVipPop.dismiss();
        isBuySvip = false;
        payPop = new PayPop(this);
        payPop.setListener(this);
        payPop.showPopupWindow();
    }


    /**
     * 购买成功后跳转至相应的节目
     *
     * @param part
     */
    private void toAnchor(PartBean part) {
        ProgramDetailActivity.actionStart(this, part, true,true);
    }

}
