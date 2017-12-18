package com.tingwen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.activity.CollectionActivity;
import com.tingwen.activity.DownLoadActivity;
import com.tingwen.activity.ListenCircleActivity;
import com.tingwen.activity.LoginActivity;
import com.tingwen.activity.MyClassActivity;
import com.tingwen.activity.PersonalHomePageActivity;
import com.tingwen.activity.SettingActivity;
import com.tingwen.activity.VipActivity;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.User;
import com.tingwen.event.LoginSuccessEvent;
import com.tingwen.event.NewFeedBackMessageEvent;
import com.tingwen.event.NewListenCircleMessage;
import com.tingwen.event.NewListenPublishEvent;
import com.tingwen.event.ReloadUserInfoEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NewMessageUtil;
import com.tingwen.utils.SizeUtil;
import com.tingwen.widget.pulltozoomview.PullToZoomScrollViewEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.raphets.roundimageview.RoundImageView;

import butterknife.BindView;

/**
 * 我
 * Created by Administrator on 2017/7/20 0020.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.sv_pull_scroll_view)
    PullToZoomScrollViewEx scrollView;
    private LinearLayout tingyou, vip, download, collection, tingwenclass, setting;
    private User user;
    private RoundImageView headImg;
    private TextView nickname;
    private TextView describe;
    private TextView level;
    private RelativeLayout icons;
    private ImageView vipicon;
    private TextView tvSetDot,tvListenDot;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {
        super.initData();
        user = AppSpUtil.getInstance().getUserInfo();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void findViewById(View container) {
        super.findViewById(container);
        loadViewForPullToZoomScrollView(scrollView);
        int screenWidth = SizeUtil.getScreenWidth();
        int screenHeight = SizeUtil.getScreenHeight();
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(screenWidth, (int) (3.0F * (screenHeight / 7.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        tingyou = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.llyt_me_tingyou);
        vip = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.llyt_me_vip);
        download = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.llyt_me_download);
        collection = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.llyt_me_collection);
        tingwenclass = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.llyt_me_class);
        setting = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.llyt_me_setting);
        headImg = (RoundImageView) scrollView.getPullRootView().findViewById(R.id.img_head);
        nickname = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_nick_name);
        describe = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_user_describe);
        level = ((TextView) scrollView.getPullRootView().findViewById(R.id.tv_ranking));
        icons = ((RelativeLayout) scrollView.getPullRootView().findViewById(R.id.ll_user_icons));
        vipicon =  (ImageView) scrollView.getPullRootView().findViewById(R.id.iv_vip);
    }

    @Override
    protected void initUI() {
        super.initUI();
        if (user != null) {
            //头像
            if (!user.getResults().getAvatar().equals("") && !user.getResults().getAvatar().contains("http")) {
                Glide.with(getActivity()).load(UrlProvider.URL_IMAGE_USER + user.getResults().getAvatar()).placeholder(R.drawable.img_touxiang)
                        .error(R.drawable.img_touxiang).into(headImg);
            }else{
                String avatar = user.getResults().getAvatar();
                String replace = avatar.replace("http://admin.tingwen.me/data/upload/avatar/", "");//???
                Glide.with(getActivity()).load(replace).placeholder(R.drawable.img_touxiang)
                        .error(R.drawable.img_touxiang).into(headImg);
            }

            icons.setVisibility(View.VISIBLE);
            nickname.setText(user.getResults().getUser_nicename());//昵称
            String signature = user.getResults().getSignature();//简介
            if(null==signature){
                describe.setText("暂无简介");
            }else{
                if("null".equals(signature)||"".equals(signature)){
                    describe.setText("暂无简介");
                }else{
                    describe.setText(user.getResults().getSignature());
                }
            }

            level.setText(user.getResults().getLevel());//等级
            int memberType = user.getResults().getMember_type();

            if(memberType==0){
                vipicon.setVisibility(View.INVISIBLE);
            }else if(memberType==1){
                vipicon.setVisibility(View.VISIBLE);
                vipicon.setImageResource(R.drawable.icon_vip);
            }else if(memberType==2){
                vipicon.setVisibility(View.VISIBLE);
                vipicon.setImageResource(R.drawable.icon_svip);
            }

        }else{
            Glide.with(getActivity()).load(R.drawable.img_touxiang).into(headImg);
        }
        //未登录
        if (!LoginUtil.isUserLogin()) {
            nickname.setText("点击登录");
            icons.setVisibility(View.GONE);
            describe.setText("");
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        tingyou.setOnClickListener(this);
        vip.setOnClickListener(this);
        download.setOnClickListener(this);
        collection.setOnClickListener(this);
        tingwenclass.setOnClickListener(this);
        setting.setOnClickListener(this);
        headImg.setOnClickListener(this);
        nickname.setOnClickListener(this);

    }

    /**
     * 通过代码设置布局
     *
     * @param scrollView
     */
    private void loadViewForPullToZoomScrollView(PullToZoomScrollViewEx scrollView) {
        //头部
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_view, null);
        //背景图片
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.head_zoom_view, null);
        //底部内容
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.content_view, null);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        tvSetDot = (TextView) contentView.findViewById(R.id.iv_set_new_news);
        tvListenDot = (TextView) contentView.findViewById(R.id.iv_listen_new_news);

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.img_head:
                if(LoginUtil.isUserLogin()){
                    String userId = LoginUtil.getUserId();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",userId);
                    LauncherHelper.getInstance().launcherActivity(getActivity(), PersonalHomePageActivity.class,bundle);
                }else{
                    goLogin();
                }
                break;
            case R.id.tv_nick_name:
                goLogin();
                break;
            case R.id.llyt_me_tingyou:
                if(LoginUtil.isUserLogin()) {
                    LauncherHelper.getInstance().launcherActivity(getActivity(), ListenCircleActivity.class);
                }else{
                    showMessage();
                }
                break;
            case R.id.llyt_me_vip:
                if(LoginUtil.isUserLogin()) {
                    LauncherHelper.getInstance().launcherActivity(getActivity(), VipActivity.class);
                }else{
                    showMessage();
                }
                break;
            case R.id.llyt_me_download:
                LauncherHelper.getInstance().launcherActivity(getActivity(), DownLoadActivity.class);
                break;
            case R.id.llyt_me_collection:
                if(LoginUtil.isUserLogin()) {
                    LauncherHelper.getInstance().launcherActivity(getActivity(), CollectionActivity.class);
                }else{
                    showMessage();
                }
                break;
            case R.id.llyt_me_class:
                if(LoginUtil.isUserLogin()){

                    LauncherHelper.getInstance().launcherActivity(getActivity(), MyClassActivity.class);
                }else{
                    showMessage();
                }
                break;
            case R.id.llyt_me_setting:
                LauncherHelper.getInstance().launcherActivity(getActivity(), SettingActivity.class);
                break;
            default:
                break;

        }
    }


    /**
     * 提示需要登录
     */
    public void showMessage(){
        new MaterialDialog.Builder(getActivity())
                .title("温馨提示")
                .content("您还没有登录哦~")
                .contentColorRes(R.color.text_black)
                .negativeText("取消")
                .positiveText("登录")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);
                    }
                }).build().show();
    }


    /**
     * 登录
     */
    private void goLogin() {

        if(!LoginUtil.isUserLogin()){
           LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);

        }
    }

    /**
     * 登录成功后事件处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        user = AppSpUtil.getInstance().getUserInfo();
        initUI();
    }

    /**
     * 刷新用户数据更新UI
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReloadUserInfoEvent(ReloadUserInfoEvent event) {
        user = AppSpUtil.getInstance().getUserInfo();
        initUI();
    }

    /**
     * 有新的意见反馈消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewFeedBackMessageEvent(NewFeedBackMessageEvent event) {
        if(tvSetDot!=null){
            tvSetDot.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 有新的听友圈评论
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewListenCircleMessage(NewListenCircleMessage event) {
        if(tvListenDot!=null){
            tvListenDot.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 有新的听友圈发表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewListenPublishEvent(NewListenPublishEvent event) {
        if(tvListenDot!=null){
            tvListenDot.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        String listenCircleResult = NewMessageUtil.getInstance(getActivity()).getNewListenCircleResult();//
//        if(!TextUtils.isEmpty(listenCircleResult)){
//            tvListenDot.setVisibility(View.VISIBLE);
//        }else{
//            tvListenDot.setVisibility(View.GONE);
//        }

        int circleMessageSize = NewMessageUtil.getInstance(getActivity()).getListenCircleMessageSize();
        if(circleMessageSize!=0){
            tvListenDot.setVisibility(View.VISIBLE);
        }else{
            tvListenDot.setVisibility(View.GONE);
        }

        int feedBackSize = NewMessageUtil.getInstance(getActivity()).getFeedBackSize();
        if(feedBackSize!=0){
            tvSetDot.setVisibility(View.VISIBLE);
        }else{
            tvSetDot.setVisibility(View.GONE);
        }






    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
