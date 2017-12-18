package com.tingwen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseActivity;
import com.tingwen.event.CollectEvent;
import com.tingwen.event.CollectSuccessEvent;
import com.tingwen.event.NewsPlayerNextEvent;
import com.tingwen.event.NewsPlayerPlayStateEvent;
import com.tingwen.event.NewsPlayerPreviousEvent;
import com.tingwen.event.PlayLimitEvent;
import com.tingwen.event.ShareEvent;
import com.tingwen.fragment.NewsDetailCopyFragment;
import com.tingwen.popupwindow.AlarmPop;
import com.tingwen.popupwindow.LimitDialog;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.SystemBarHelper;
import com.tingwen.utils.TimeUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新闻详情
 * Created by Administrator on 2017/8/23 0023.
 */
public class NewsDetailActivity extends BaseActivity implements NewsDetailCopyFragment.ScrollChangeListener, AlarmPop.AlarmListener {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.ll_player_clock)
    LinearLayout llPlayerClock;
    @BindView(R.id.rl_player_previous)
    RelativeLayout rlPlayerPrevious;
    @BindView(R.id.iv_player_play)
    ImageView ivPlayerPlay;
    @BindView(R.id.iv_player_next)
    RelativeLayout ivPlayerNext;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.ll_player_collect)
    LinearLayout llPlayerCollect;
    @BindView(R.id.mPlaying_seekBar)
    SeekBar mSeekBar;
    @BindView(R.id.tv_player_current_time)
    TextView tvPlayerCurrentTime;
    @BindView(R.id.tv_player_total_time)
    TextView tvPlayerTotalTime;
    private View pop;
    //新闻详情
    private NewsDetailCopyFragment newsDetailFragment;
    public static ProgressHandler progressHandler;
    private float height;//新闻图片高度
    private int state = 0;//-1 播放按钮跳转过来时,新闻列表为空   0 不为空
    private LimitDialog limitDialog;
    private AlarmPop alarmPop;
    private PopupWindow popupWindow;


    @Override
    protected void savedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Logger.e("新闻 savedInstanceState");
            newsDetailFragment = (NewsDetailCopyFragment) getSupportFragmentManager().getFragment(savedInstanceState, "newsDetailFragment");
        }

    }

    @Override
    protected boolean isNeedStatusBarTranslucent() {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (state != -1) {
            Logger.e("新闻onSaveInstanceState");
            getSupportFragmentManager().putFragment(outState, "newsDetailFragment", newsDetailFragment);
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_news_detail;
    }


    public static void actionStart(Context context, int position, String channelName) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("position", position);
        Logger.e("position:" + position);
        intent.putExtra("channelName", channelName);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(
                R.anim.translate_horizontal_start_in,
                R.anim.translate_horizontal_start_out);
    }


    @Override
    protected void initData() {
        super.initData();

        if (newsDetailFragment == null) {
            newsDetailFragment = new NewsDetailCopyFragment();
        }

        SystemBarHelper.immersiveStatusBar(this);
        //从播放按钮跳转过来时,如果是-1 代表播放列表为空
        state = getIntent().getIntExtra("state", 0);
        int position = getIntent().getIntExtra("position", 0);
        String channelName = getIntent().getStringExtra("channelName");
        if (progressHandler == null) {
            progressHandler = new ProgressHandler(this);
        }
        height = (float) (SizeUtil.getScreenWidth() * 0.60);
        EventBus.getDefault().register(this);

        if (state != -1) {
            if (!newsDetailFragment.isVisible()) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("channel", channelName);
                newsDetailFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fl_container, newsDetailFragment).commit();
            }

        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Logger.e("新闻onNewIntent");

//        //从播放按钮跳转过来时,如果是-1 代表播放列表为空
//        state = getIntent().getIntExtra("state", 0);
//        int position = getIntent().getIntExtra("position", 0);
//        String channelName = getIntent().getStringExtra("channelName");
//        if (progressHandler == null) {
//            progressHandler = new ProgressHandler(this);
//        }
//        height = (float) (SizeUtil.getScreenWidth() * 0.60);
//        EventBus.getDefault().register(this);
//        if(state !=-1){
//            if (!newsDetailFragment.isVisible()) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                bundle.putString("channel", channelName);
//                newsDetailFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().add(R.id.fl_container, newsDetailFragment).commit();
//            }
//        }
    }

    @Override
    protected void initUI() {
        super.initUI();
        int statusHeight = SizeUtil.getStatusHeight(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, statusHeight, 0, 0);
        rlTop.setLayoutParams(layoutParams);
        TouchUtil.setTouchDelegate(ivLeft, 5);
        TouchUtil.setTouchDelegate(ivShare, 5);

        pop = LayoutInflater.from(this).inflate(R.layout.pop_seek_bar, null);
        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        ImageView ivFastBack = (ImageView) pop.findViewById(R.id.iv_fast_back);
        ImageView ivFastGo = (ImageView) pop.findViewById(R.id.iv_fast_go);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ivFastBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = TwApplication.getNewsPlayer().getMediaPlayerCurrentPosition();
                TwApplication.getNewsPlayer().seekTo(progress - 15 * 1000);
            }
        });

        ivFastGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = TwApplication.getNewsPlayer().getMediaPlayerCurrentPosition();
                TwApplication.getNewsPlayer().seekTo(progress + 15 * 1000);
            }
        });
    }


    @OnClick({R.id.ivLeft, R.id.iv_share, R.id.ll_player_clock, R.id.rl_player_previous, R.id.iv_player_next, R.id.ll_player_collect, R.id.iv_player_play})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (state == -1) {
            return;
        }
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.iv_share:
                EventBus.getDefault().post(new ShareEvent());
                break;
            case R.id.ll_player_clock:
                if (null == alarmPop) {
                    alarmPop = new AlarmPop(this);
                    alarmPop.setListener(this);
                }
                alarmPop.showPopupWindow();

                break;
            case R.id.ll_player_collect:
                EventBus.getDefault().post(new CollectEvent());
                break;
            case R.id.rl_player_previous:
                if (null != TwApplication.getNewsPlayer()) {
                    TwApplication.getNewsPlayer().previous();
                }
                break;
            case R.id.iv_player_next:
                if (null != TwApplication.getNewsPlayer()) {
                    TwApplication.getNewsPlayer().next();
                }
                break;
            case R.id.iv_player_play:
                if (null == TwApplication.getNewsPlayer()) {
                    return;
                }
                Boolean playing = TwApplication.getNewsPlayer().isPlaying();
                if (playing) {
                    TwApplication.getNewsPlayer().pause();
                } else {
                    Boolean pause = TwApplication.getNewsPlayer().isPause();
                    if (pause) {
                        TwApplication.getNewsPlayer().continuePlay();
                    }
                }

                break;
            default:
                break;
        }
    }


    @Override
    protected void setListener() {
        super.setListener();
        newsDetailFragment.setScrollChangeListener(this);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (state == -1) {
            return;
        }


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int currentProcess;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentProcess = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                showFastWindow();
                TwApplication.getNewsPlayer().StartTracking();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                TwApplication.getNewsPlayer().seekTo(currentProcess);
            }
        });


    }

    private void showFastWindow() {

        int[] location = new int[2];
        mSeekBar.getLocationOnScreen(location);
        pop.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = pop.getMeasuredHeight();
        popupWindow.showAtLocation(mSeekBar, Gravity.NO_GRAVITY, location[0], location[1] - height);
    }


    /**
     * 播放还是暂停状态切换事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayEvent(NewsPlayerPlayStateEvent event) {
        int state = event.getState();
        if (state == 1) {
            ivPlayerPlay.setImageResource(R.drawable.home_news_ic_pause);
        } else if (state == 2) {
            ivPlayerPlay.setImageResource(R.drawable.home_news_ic_play);
        }
    }


    /**
     * 播放下一条事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextEvent(NewsPlayerNextEvent event) {
        ivPlayerPlay.setImageResource(R.drawable.home_news_ic_play);
        ivCollection.setImageResource(R.drawable.icon_player_collection);
    }

    /**
     * 播放上一条事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPrevious(NewsPlayerPreviousEvent event) {
        ivPlayerPlay.setImageResource(R.drawable.home_news_ic_play);
        ivCollection.setImageResource(R.drawable.icon_player_collection);
    }

    /**
     * 收藏成功事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectSuccess(CollectSuccessEvent event) {
        int type = event.getType();
        if (type == 1) {
            ivCollection.setImageResource(R.drawable.icon_player_collectioned);
        } else {
            ivCollection.setImageResource(R.drawable.icon_player_collection);
        }


    }

    /**
     * 每日可收听次数使用完毕,弹窗提示
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollect(PlayLimitEvent event) {

        if (null == limitDialog) {
            limitDialog = new LimitDialog(this);
        }

        limitDialog.setLimitListener(new LimitDialog.LimitListener() {
            @Override
            public void dismiss() {
                limitDialog.dismiss();
            }

            @Override
            public void buyVip() {
                if (LoginUtil.isUserLogin()) {
                    LauncherHelper.getInstance().launcherActivity(NewsDetailActivity.this, VipActivity.class);
                    limitDialog.dismiss();
                } else {
                    LauncherHelper.getInstance().launcherActivity(NewsDetailActivity.this, LoginActivity.class);
                    ToastUtils.showBottomToast("登录后才可以开通会员哦~");
                }
            }
        });
        limitDialog.show();
    }


    @Override
    public void alarmCancel() {

    }


    /**
     * 用于更新进度条的handler
     */
    public static class ProgressHandler extends Handler {

        //弱引用避免handler的内存泄漏
        private WeakReference<NewsDetailActivity> mWeakReference;

        public ProgressHandler(NewsDetailActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewsDetailActivity activity = mWeakReference.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    if (null != activity.mSeekBar) {
                        activity.mSeekBar.setMax(msg.arg1);//播放最大进度
                    }
                    if (null != activity.tvPlayerTotalTime) {
                        activity.tvPlayerTotalTime.setText(TimeUtil.setTimeFormat(msg.arg1));//播放总时长

                    }
                    break;
                case 1:

                    if (null != activity.mSeekBar) {
                        activity.mSeekBar.setProgress(msg.arg1);//播放进度
                    }
                    if (null != activity.tvPlayerCurrentTime) {
                        activity.tvPlayerCurrentTime.setText(TimeUtil.setTimeFormat(msg.arg1));//当前播放时间

                    }
                    break;
                case 2:
                    if (null != activity.mSeekBar) {
                        activity.mSeekBar.setSecondaryProgress(msg.arg1);//加载进度
                    }
                    break;
            }
        }
    }

    /**
     * 滑动回调 处理状态栏 和返回的显示
     *
     * @param distanceY
     */
    @Override
    public void onChange(float distanceY) {

        if (distanceY == 0) {
            rlTop.setBackgroundColor(Color.argb(0, 255, 255, 255));
            ivLeft.setImageResource(R.drawable.icon_left_white);
            ivShare.setImageResource(R.drawable.icon_news_share_white);
            SystemBarHelper.tintStatusBar(this, Color.parseColor("#00000000"));
        } else if (distanceY > 0 && distanceY < height) {
            rlTop.setBackgroundColor(Color.argb(0, 255, 255, 255));
            ivLeft.setImageResource(R.drawable.icon_left_white);
            ivShare.setImageResource(R.drawable.icon_news_share_white);
            SystemBarHelper.tintStatusBar(this, Color.parseColor("#00000000"));

        } else if (distanceY > 0 && distanceY > height) {
            rlTop.setBackgroundColor(Color.argb(255, 255, 255, 255));
            ivLeft.setImageResource(R.drawable.icon_left_back);
            ivShare.setImageResource(R.drawable.icon_news_share);
            SystemBarHelper.tintStatusBar(this, Color.parseColor("#ffffff"));

        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {//提前释放资源,若在onDestroy中处理可能会出现快速重复启动这个activity,导致刚创建,调用了上一个activity的销毁方法,handler被销毁无法接受消息
            if (progressHandler != null) {
                progressHandler.removeCallbacksAndMessages(null);
                progressHandler = null;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
