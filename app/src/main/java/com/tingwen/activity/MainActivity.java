package com.tingwen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.app.AppManager;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.PushNewsBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.LoginSuccessEvent;
import com.tingwen.event.NewFeedBackMessageEvent;
import com.tingwen.event.NewListenCircleMessage;
import com.tingwen.event.NewListenPublishEvent;
import com.tingwen.event.PlayerBarEvent;
import com.tingwen.fragment.DiscoveryFragment;
import com.tingwen.fragment.MainFragment;
import com.tingwen.fragment.MineFragment;
import com.tingwen.fragment.SubscriptionFragment;
import com.tingwen.interfaces.MediaPlayerInterface;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.service.NewMessageService;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.NewMessageUtil;
import com.tingwen.utils.ScreenManager;
import com.tingwen.utils.ScreenReceiverUtil;
import com.tingwen.utils.UpdateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主界面
 * Created by Administrator on 2017/6/28 0028.
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.btn_main_menu_tab1)
    Button btnMainMenuTab1;
    @BindView(R.id.btn_main_menu_tab2)
    Button btnMainMenuTab2;
    @BindView(R.id.btn_main_menu_tab4)
    Button btnMainMenuTab4;
    @BindView(R.id.btn_main_menu_tab5)
    Button btnMainMenuTab5;
    @BindView(R.id.iv_new_news)
    TextView ivNewNews;//新消息小红点
    @BindView(R.id.ivPlay)
    ImageView ivPlay;//播放按钮

    // 菜单按钮数组
    private Button[] mTabs;
    // 当前页面
    private int current_index = 0;
    // 当前选择的界面index
    private int index;
    // Fragment碎片管理
    private FragmentManager fragmentManager;
    //首页听闻
    private MainFragment mainFragment;

    int bundleIndex = 0;
    private SubscriptionFragment subscriptionFragment;
    private DiscoveryFragment discoveryFragment;
    private MineFragment mineFragment;

    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;

    protected RemoteViewReceiver mRemoteViewReceiver;

    @Override
    protected int getLayoutResId() {
        return R.layout.avitivity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        setSwipeBackEnable(false);//禁止滑动返回
        fragmentManager = getSupportFragmentManager();
        EventBus.getDefault().register(this);
    }

    //这个界面不需要状态栏透明
    @Override
    protected boolean isNeedStatusBarTranslucent() {
        return false;
    }

    @Override
    protected void findViewById() {
        super.findViewById();
        mTabs = new Button[4];
        mTabs[0] = btnMainMenuTab1;
        mTabs[1] = btnMainMenuTab2;
        mTabs[2] = btnMainMenuTab4;
        mTabs[3] = btnMainMenuTab5;

    }

    @Override
    protected void initUI() {
        super.initUI();
        selectTab(bundleIndex);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 音频按键获取焦点
        UpdateUtils.getInstance().UpdateVersion(this,false);//检测更新版本

        // 1. 注册锁屏广播监听器  用于锁屏下保活
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);

        initReceiver();

        String id = getIntent().getStringExtra("id");
        getPushMessage(id);//获取推送消息

        getNewMessage();//获取新消息

    }




    /**
     * 状态切换
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_menu_tab1:
                index = 0;
                break;
            case R.id.btn_main_menu_tab2:
                index = 1;
                break;
            case R.id.btn_main_menu_tab4:
                index = 2;
                break;
            case R.id.btn_main_menu_tab5:
                index = 3;
                break;

        }
        selectTab(index);
    }

    /**
     * 获取当前显示的界面index
     */
    public int getIndex() {
        return current_index;
    }

    /**
     * 界面切换
     * <p/>
     * index 位置
     */
    public void selectTab(int index) {
        mTabs[current_index].setSelected(false);
        current_index = index;
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        // 开启事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏当前显示的Fragment
        hideFragment(transaction);

        // Fragment切换
        switch (index) {
            case 0:// 听闻
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.container, mainFragment);
                } else {
                    transaction.show(mainFragment);
                }
                break;
            case 1:// 订阅
                if (subscriptionFragment == null) {
                    subscriptionFragment = new SubscriptionFragment();
                    transaction.add(R.id.container, subscriptionFragment);
                } else {
                    transaction.show(subscriptionFragment);
                }
                break;
            case 2:// 发现
                if (discoveryFragment == null) {
                    discoveryFragment = new DiscoveryFragment();
                    transaction.add(R.id.container, discoveryFragment);
                } else {
                    transaction.show(discoveryFragment);
                }

                break;
            case 3:// 我
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.container, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }



                break;
        }
        transaction.addToBackStack(null);
        // 提交事物
        transaction.commitAllowingStateLoss();
    }


    /**
     * 隐藏已经显示的Fragment,提高应用的运行速度
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }

        if (subscriptionFragment != null) {
            transaction.hide(subscriptionFragment);
        }

        if (discoveryFragment != null) {
            transaction.hide(discoveryFragment);
        }

        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }


    }

    @OnClick({R.id.ivPlay})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivPlay:

                if(null==TwApplication.getNewsPlayer()){
                    return;
                }
                if (TwApplication.getNewsPlayer().isMp3()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("act_id", TwApplication.getNewsPlayer().getActId());
                    LauncherHelper.getInstance().launcherActivity(this, AuditionDetailActivity.class, bundle);
                } else {
                    int playPosition = TwApplication.getNewsPlayer().getPlayPosition();
                    String channel = TwApplication.getNewsPlayer().getChannel();

                    if (playPosition == -1) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("state", -1);
                        LauncherHelper.getInstance().launcherActivity(this, NewsDetailActivity.class, bundle);
                    } else {
                        if (TextUtils.isEmpty(channel)) {
                            NewsDetailActivity.actionStart(this, playPosition, AppConfig.CHANNEL_TYPE_NEWS);
                        } else {
                            NewsDetailActivity.actionStart(this, playPosition, channel);
                        }


                    }


                }
                break;

        }
    }

    /**
     * 按键点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 返回键
            if (current_index == 0) {
                exitDialog();
            } else {
                selectTab(0);
            }
        }
        return true;
    }

    /**
     * 获取新消息
     */
    private void getNewMessage() {
        startService(new Intent(MainActivity.this, NewMessageService.class));
    }

    /**
     * 退出应用的Dialog
     */
    private void exitDialog() {
        new MaterialDialog.Builder(this)
                .title("确定退出听闻？")
                .negativeText("取消")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MediaPlayerInterface newsPlayer = TwApplication.getNewsPlayer();
                        if(null!=newsPlayer){
                            newsPlayer.upLoadClassHistory();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TwApplication.getInstance().unBindMediaPlayService();
                                finish();
                                AppManager.getAppManager().AppExit(MainActivity.this);
                                AppManager.getAppManager().count = 0;
                            }
                        },800);


                    }
                }).build().show();
    }


    /**
     * 控制播放按钮是否旋转事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerBarEvent(PlayerBarEvent event) {
        int state = event.getState();
        switch (state) {
            case 1:
                startRotationAni();
                break;
            case 2:
                stopRotationAni();
                break;
        }


    }

    /**
     * 获取意见反馈消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewFeedBackMessageEvent(NewFeedBackMessageEvent event) {
        int num = event.getNum();
        if(num!=0){
            ivNewNews.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 有新的听友圈评论
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewListenCircleMessage(NewListenCircleMessage event) {
        int num = event.getNum();
        if(num!=0){
            ivNewNews.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 有新的听友圈发表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewListenPublishEvent(NewListenPublishEvent event) {
        ivNewNews.setVisibility(View.VISIBLE);

    }




    /**
     * 登录成功
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        getNewMessage();
    }


    private RotateAnimation mrRotateAnimation;

    /**
     * 播放按键开始旋转动画
     */
    private void startRotationAni() {
        if (mrRotateAnimation == null) {
            mrRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.playbar_play_rotation);
        }
        ivPlay.startAnimation(mrRotateAnimation);
    }

    /**
     * 停止播放按键旋转动画
     */
    private void stopRotationAni() {
        if (mrRotateAnimation != null) {
            ivPlay.clearAnimation();
        }
    }

    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 移除一像素保活
            mScreenManager.finishActivity();
        }


        @Override
        public void onSreenOff() {
            //开启一像素保活
            mScreenManager.startActivity();
        }


        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };

    private void initReceiver() {
        if (null == mRemoteViewReceiver) {
            mRemoteViewReceiver = new RemoteViewReceiver();
            registerReceiver(mRemoteViewReceiver, new IntentFilter(AppConfig.BROADCAST_REMOTEVIEW_RECEIVER));
        }


    }

    /**
     * 接收由Notification中的RemoteView发来的广播,用来控制暂停，继续和下一首
     *
     * @author Administrator
     */
    private class RemoteViewReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int what = intent.getIntExtra("what", -1);
            switch (what) {
                case 0:
                    if(TwApplication.getNewsPlayer().isPlaying()){
                        TwApplication.getNewsPlayer().pause();

                    }else{
                        TwApplication.getNewsPlayer().continuePlay();
                    }
                    break;
                case 1:
                    TwApplication.getNewsPlayer().next();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String id = intent.getStringExtra("id");
        getPushMessage(id);
    }

    /**
     * 获取推送新闻
     */
    private void getPushMessage(String id) {

        if(!TextUtils.isEmpty(id)){
            Map<String, String> map = new HashMap<>();
            map.put("post_id", id);
            OkGo.<PushNewsBean>post(UrlProvider.NEWS_DETIAL).params(map).execute(new SimpleJsonCallback<PushNewsBean>(PushNewsBean.class) {
                @Override
                public void onSuccess(Response<PushNewsBean> response) {
                    if(response.body().getStatus()==1){
                        PushNewsBean.ResultsBean bean = response.body().getResults();
                        NewsBean newsJson = new NewsBean();
                        newsJson.post_title = bean.getPost_title();
                        newsJson.post_mp = bean.getPost_mp();
                        newsJson.id = bean.getId();
                        newsJson.post_excerpt = bean.getPost_excerpt();
                        newsJson.post_date = bean.getPost_modified();
                        newsJson.post_lai = bean.getPost_lai();
                        newsJson.post_time = bean.getPost_time();
                        newsJson.smeta = bean.getSmeta();
                        List<NewsBean> newsList = new ArrayList<>();
                        newsList.add(newsJson);
                        TwApplication.getNewsPlayer().setNewsList(newsList);
                        NewsDetailActivity.actionStart(MainActivity.this,0, AppConfig.CHANNEL_TYPE_SINGLE);

                    }
                }
            });


        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        int circleMessageSize = NewMessageUtil.getInstance(this).getListenCircleMessageSize();
        int feedBackSize = NewMessageUtil.getInstance(this).getFeedBackSize();
        if(circleMessageSize==0&&feedBackSize==0){
            ivNewNews.setVisibility(View.GONE);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(null!=mRemoteViewReceiver){
            unregisterReceiver(mRemoteViewReceiver);
        }
        if(null!=mScreenListener){
            mScreenListener.stopScreenReceiverListener();
        }
    }

}
