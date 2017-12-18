package com.tingwen.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.app.AppSpUtil;
import com.tingwen.bean.AuditionBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ChangeListenNewsColorEvent;
import com.tingwen.event.ClassListenEvent;
import com.tingwen.event.LoadMoreNewsEvent;
import com.tingwen.event.NewsPlayerNextEvent;
import com.tingwen.event.NewsPlayerPlayStateEvent;
import com.tingwen.event.NewsPlayerPreviousEvent;
import com.tingwen.event.PlayLimitEvent;
import com.tingwen.event.PlayerBarEvent;
import com.tingwen.greendao.HistoryNews;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.FileUtils;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SQLHelper;
import com.tingwen.utils.VipUtil;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放服务
 * Created by Administrator on 2017/8/23 0023.
 */
public class TwNewsService extends TwBaseService {

    private static final int STATE_PLAY = 1;//正在播放
    private static final int STATE_PAUSE = 2;//停止播放
    private NewsMediaPlayBinder mPlayBinder;
    private MediaPlayer mNewsMediaPlayer;
    private String musicUrl = "";//音频url
    private NewsBean currentNews;//正在播放的新闻
    private String currentMp3 = "";//正在播放的试听
    private Boolean isMp3 = false;//是否是试听
    private Boolean isError;//是否发生错误
    private Boolean isPlaying = false;//是否正在播放音频
    private Boolean isPause = false; //是否正在暂停
    private Boolean isTrack = false;//是否在拖动进度条
    private Boolean isPlayLastClass = false;//是否是播放上一次记录
    private String historyPisition = "";
    private Boolean CurrentNewsIsPause = false; //当前正在播放的新闻是否正在暂停(用于区分点击playbar进入后区分状态)
    private String channel = "";//播放的频道
    private String lastChannel="";//上一次播放的频道
    private String classActid="";//课堂节目id
    private String partId="";//节目id
    private String pauseID="";//暂停的id
    private Timer mTimer;
    private Boolean isPlayDownload = false;//是否正在播放下载的音频
    private Boolean isUsedUpTimes = false;//是否使用完每日收听次数

    TelephonyManager tManager;
    private TWPhoneStateListener mTWPhoneStateListener;
    //    private Boolean isListenPhone = false;//是否正在通话
    private HeadsetReceiver headsetReceiver;//耳机监听
    private Boolean isUseHeadset = false;//是否正在使用耳机
    private RemoteViews mRemoteViews;
    protected Notification mNotification;
    private int historyPercent = 0;
    private SQLHelper sqlHelper;//数据库


    @Override
    public void onCreate() {
        super.onCreate();
        mPlayBinder = new NewsMediaPlayBinder();
        iniMediaPlayer();
        initPhoneState();
        initHeadset();
        sqlHelper = SQLHelper.getInstance();
    }

    private void initHeadset() {
        if (headsetReceiver == null) {
            headsetReceiver = new HeadsetReceiver();
        }
        registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

    private void initPhoneState() {
        //获取系统的TelephonyManager对象
        tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //为tManager添加监听器
        if (null != tManager) {
            mTWPhoneStateListener = new TWPhoneStateListener();
            tManager.listen(mTWPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

    }


    private class TWPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://来电
                    Logger.e("来电");
                    mPlayBinder.pause();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接听来电 拨打电话
                    Logger.e("接听 or 拨打");
                    if (mNewsMediaPlayer.isPlaying()) {
                        mPlayBinder.pause();
                    }
                case TelephonyManager.CALL_STATE_IDLE://无任何状态
                    Logger.e("空闲");
                    break;

                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }


    public class HeadsetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        isUseHeadset = false;
                        mPlayBinder.pause();
                    } else if (intent.getIntExtra("state", 0) == 1) {
                        isUseHeadset = true;
                    }
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNewsMediaPlayer != null && mNewsMediaPlayer.isPlaying()) {
            mNewsMediaPlayer.stop();
            mNewsMediaPlayer.release();
            mNewsMediaPlayer = null;
        }
        if (null != headsetReceiver) {
            unregisterReceiver(headsetReceiver);
        }

        Logger.e("播放服务销毁");

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (mNewsMediaPlayer != null && mNewsMediaPlayer.isPlaying()) {
            mNewsMediaPlayer.stop();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mPlayBinder;
    }

    /**
     * 初始化媒体播放器
     */
    private void iniMediaPlayer() {
        if (mNewsMediaPlayer == null) {
            mNewsMediaPlayer = new MediaPlayer();
        }
        mNewsMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mNewsMediaPlayer.setOnPreparedListener(onPreparedListener);
        mNewsMediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
        mNewsMediaPlayer.setOnCompletionListener(onCompletionListener);
        mNewsMediaPlayer.setOnErrorListener(errorListener);
        mNewsMediaPlayer.setOnSeekCompleteListener(seekCompleteListener);
        mNewsMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

    }


    /**
     * 媒体播放器准备完成
     */
    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

            isError = false;
            if (!isMp3) {
                lastChannel=channel;
                if (null != channel && !channel.equals(AppConfig.CHANNEL_TYPE_DOWNLOAD) && !channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {//不是播放下载的新闻 也不是播放课堂

                    if (LoginUtil.isUserLogin()) {//已经登录

                        int vipState = VipUtil.getInstance().getVipState();//会员状态

                        switch (vipState) {
                            case 0: //普通
                                if (AppSpUtil.getInstance().getUserInfo().getResults().getIs_stop() == 1) {
                                    isUsedUpTimes = true;
                                    if (!isPlayDownload) {
                                        EventBus.getDefault().post(new PlayLimitEvent());
                                    } else {
                                        beginUpdateNewsSeekBar();
                                        EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                                        EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                                        isPlaying = true;
                                        isPause = false;
                                    }
                                    return;
                                }

                                int playTimes = AppSpUtil.getInstance().getPlayTimes();
                                Logger.e("已登录 已播放次数:" + playTimes);

                                if (playTimes < AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + 1) {
                                    isUsedUpTimes = false;
                                    AppSpUtil.getInstance().playTimesPlus();//已收听次数+1
                                } else {
                                    isUsedUpTimes = true;
                                    if (!isPlayDownload) {
                                        EventBus.getDefault().post(new PlayLimitEvent());
                                        EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                                    } else {
                                        beginUpdateNewsSeekBar();
                                        EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                                        EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                                        isPlaying = true;
                                        isPause = false;
                                    }
                                    runOutLimitTimes();
                                    return;
                                }
                                break;
                            case 1://会员
                                isUsedUpTimes = false;
                                break;
                            case 2://超级会员
                                isUsedUpTimes = false;
                                break;

                        }


                    } else {//未登录
                        int playTimes = AppSpUtil.getInstance().getPlayTimes();
                        if (playTimes < AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + 1) {
                            isUsedUpTimes = false;
                            AppSpUtil.getInstance().playTimesPlus();//已收听次数+1
                        } else {
                            isUsedUpTimes = true;
                            if (!SQLHelper.getInstance().isHasNews(currentNews.getId())) {

                                EventBus.getDefault().post(new PlayLimitEvent());
                                EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                            } else {
                                beginUpdateNewsSeekBar();
                                EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                                EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                                isPlaying = true;
                                isPause = false;
                            }
                            return;
                        }

                    }
                    beginUpdateNewsSeekBar();
                    EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                    EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                    mNewsMediaPlayer.start();
                    isPlaying = true;
                    isPause = false;
                    CurrentNewsIsPause = false;


                } else {//下载 和 课堂

                    if (null != channel && channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {//课堂

                        if (isPlayLastClass) {
                            int duration = mNewsMediaPlayer.getDuration();
                            if (!TextUtils.isEmpty(historyPisition)) {
                                Integer integer = Integer.valueOf(historyPisition);
                                // 创建一个数值格式化对象
                                NumberFormat numberFormat = NumberFormat.getInstance();
                                // 设置精确到小数点后0位
                                numberFormat.setMaximumFractionDigits(0);
                                String percent = numberFormat.format((float) integer / (float) duration * 100);
                                historyPercent = Integer.valueOf(percent);
                                if(historyPercent==0){
                                    historyPercent=1;
                                }
                                if(historyPercent>100||historyPercent==100){
                                    historyPercent=100;
                                    beginUpdateNewsSeekBar();
                                    EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                                    EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                                    mNewsMediaPlayer.start();
                                    isPlaying = true;
                                    isPause = false;
                                    CurrentNewsIsPause = false;
                                }

                            }


                        } else {
                            beginUpdateNewsSeekBar();
                            EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                            EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                            mNewsMediaPlayer.start();
                            isPlaying = true;
                            isPause = false;
                            CurrentNewsIsPause = false;
                        }


                    } else if (null != channel && channel.equals(AppConfig.CHANNEL_TYPE_DOWNLOAD)) {//下载
                        beginUpdateNewsSeekBar();
                        EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                        EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                        mNewsMediaPlayer.start();
                        isPlaying = true;
                        isPause = false;
                        CurrentNewsIsPause = false;
                    }


                }


            } else {

                EventBus.getDefault().post(new ClassListenEvent(STATE_PLAY, currentMp3));
                EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                mNewsMediaPlayer.start();
                isPlaying = true;
                isPause = false;
                CurrentNewsIsPause = false;
            }
            pauseID="";
//            mNewsMediaPlayer.start();
//            isPlaying = true;
//            isPause = false;
//            CurrentNewsIsPause = false;


        }
    };

    /**
     * 媒体播放器完成播放
     */
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {


            if (isMp3) {
                if (playPosition < mp3List.size() - 1 && !isError) {
                    isPlaying = false;
                    isPause = false;
                    EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                    mPlayBinder.nextMP3();
                    EventBus.getDefault().post(new ClassListenEvent(STATE_PLAY, currentMp3));

                } else if (playPosition == mp3List.size() - 1 && !isError) {
                    EventBus.getDefault().post(new ClassListenEvent(STATE_PAUSE, ""));
                }
            } else {

                if(null!=currentNews&&null != channel && channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {//课堂)
                    sqlHelper.insertHistorydNews(currentNews.getId(),currentNews.getPost_time()+"",currentNews.getPost_time()+"");
                }

                if (playPosition < newsList.size() - 1 && !isError) {
                    isPlaying = false;
                    EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                    mPlayBinder.next();
                } else if (playPosition == newsList.size() - 1 && !isError) {
                    Logger.e("加载更多啦!");
                    EventBus.getDefault().post(new LoadMoreNewsEvent(playPosition, channel));
                }
            }

        }
    };

    /**
     * 媒体播放器缓冲
     */
    MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (null != currentNews && !isMp3) {
                if (null != channel && channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {

                    if(historyPercent==100&&percent>1){
                        historyPercent=1;
                        isPlayLastClass=false;
                    }
                    if (isPlayLastClass && historyPercent != 0 && percent > historyPercent&&!mNewsMediaPlayer.isPlaying()) {

                        mNewsMediaPlayer.seekTo(Integer.valueOf(historyPisition));
                        beginUpdateNewsSeekBar();
                        CurrentNewsIsPause = false;
                        initHistoryClass();
                    }else {
                        setSeekBarSecondary(Integer.valueOf(currentNews.getPost_time()), percent);
                    }

                } else {
                    if (!isUsedUpTimes) {//没用光收听次数 才去刷新缓冲进度
                        setSeekBarSecondary(Integer.valueOf(currentNews.getPost_time()), percent);
                    }
                }
            }
        }
    };


    /**
     * 媒体播放器拉动进度条完成
     */
    MediaPlayer.OnSeekCompleteListener seekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            getWifiLock();
            isTrack = false;
            if (newsList.size() != 0 && null != currentNews && null != mPlayBinder) {

                if (isPlayDownload) {//区分是不是下载
                    EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                    EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                    isPlaying = true;
                    isPause = false;
                    mNewsMediaPlayer.start();
                } else {
                    if (!isUsedUpTimes) {//没用光次数才执行
                        EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                        EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                        mNewsMediaPlayer.start();
                        isPlaying = true;
                        isPause = false;
                    }
                }

            }


        }
    };

    /**
     * 媒体播放器出错
     */
    MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            isError = true;
            if (isMp3) {
                if (mp3List.size() != 0) {
                    mPlayBinder.playMp3(playPosition);
                }
            } else {
                if (newsList.size() != 0) {
                    mPlayBinder.playNews(playPosition);
                }
            }

            return false;
        }
    };


    /**
     * 媒体播放器binder
     */
    public class NewsMediaPlayBinder extends MediaPlayBinder {

        @Override
        public void playNews(int position) {
            super.playNews(position);
            isPlaying = false;
            isMp3 = false;
            getWifiLock();
            NewsBean news = (NewsBean) newsList.get(position);

            setSeekBarMax(getAllTime(news));
            SQLHelper.getInstance().insertListenedNews(news.getId());//已收听数据库增加该条新闻id

            if (null != currentNews && currentNews.getId().equals(news.getId())&&!isPlayLastClass) {

                if (CurrentNewsIsPause) {
                    beginUpdateNewsSeekBar();
                    EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PAUSE));
                    return;
                } else {
                    isPlaying = true;
                    beginUpdateNewsSeekBar();
                    EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                    return;
                }

            }
            setSeekBarProgressPosition(0);
            setSeekBarSecondary(0, 0);
            musicUrl = news.getPost_mp();

            if (SQLHelper.getInstance().isHasNews(news.getId())) {
                musicUrl = AppConfig.EXTRASTROGEDOWNLOADPATH + "tingwen." + news.getId() + ".mp3";//本地文件路径
                if (FileUtils.isFileExists(musicUrl)) {
                    playDownload();//播放下载的新闻
                } else {
                    SQLHelper.getInstance().deleteDownloadNews(news.getId());//文件如果不存在 数据库删除该新闻数据
//                  next();
                }
            } else {
                play();//播放新闻
            }
            currentNews = news;
            currentMp3 = "";
            EventBus.getDefault().post(new ChangeListenNewsColorEvent(channel, currentNews.getId()));//改变正在播放的新闻标题颜色

            String smeta = currentNews.getSmeta();
            String title = currentNews.getPost_title();
            String url = smeta.replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");
            startForceGround(url, title);//通知栏

        }


        @Override
        public void playMp3(int position) {
            super.playMp3(position);
            isPlaying = false;
            isMp3 = true;
            AuditionBean.ResultsBean.ShitingBean mp3Bean = (AuditionBean.ResultsBean.ShitingBean) mp3List.get(position);
            musicUrl = mp3Bean.getS_mpurl();
            currentMp3 = musicUrl;
            play();
        }


        @Override
        public void next() {
            super.next();
            if (null!=newsList&&newsList.size() != 0 && playPosition < newsList.size() - 1) {

                if (null!=channel&&!channel.equals(AppConfig.CHANNEL_TYPE_DOWNLOAD) && !channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {//不是播放下载的新闻 也不是播放课堂

                    if (LoginUtil.isUserLogin()) {//已经登录

                        int vipState = VipUtil.getInstance().getVipState();//会员状态

                        switch (vipState) {
                            case 0: //普通
                                if (AppSpUtil.getInstance().getUserInfo().getResults().getIs_stop() == 1) {
                                    isUsedUpTimes = true;
                                    if (!SQLHelper.getInstance().isHasNews(currentNews.getId())) {
                                        EventBus.getDefault().post(new PlayLimitEvent());
                                    }

                                    return;
                                }
                                int playTimes = AppSpUtil.getInstance().getPlayTimes();

                                if (playTimes < AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + 1) {
                                    isUsedUpTimes = false;
                                } else {
                                    isUsedUpTimes = true;
                                    if (!SQLHelper.getInstance().isHasNews(currentNews.getId())) {
                                        EventBus.getDefault().post(new PlayLimitEvent());
                                        EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                                    }
                                    runOutLimitTimes();
                                    return;
                                }
                                break;
                            case 1://会员
                                break;
                            case 2://超级会员
                                break;

                        }


                    } else {//未登录
                        int playTimes = AppSpUtil.getInstance().getPlayTimes();
                        if (playTimes < AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + 1) {
                            isUsedUpTimes = false;
                        } else {
                            isUsedUpTimes = true;
                            if (!SQLHelper.getInstance().isHasNews(currentNews.getId())) {
                                EventBus.getDefault().post(new PlayLimitEvent());
                            }
                            return;
                        }


                    }


                }

                playPosition += 1;
                if(null != channel && channel.equals(AppConfig.CHANNEL_TYPE_CLASS)){
                    NewsBean news = (NewsBean) newsList.get(playPosition);
                    String id = news.getId();
                    HistoryNews historyNews = sqlHelper.isHasHistoryNews(id);
                    if(null!=historyNews){
                        isPlayLastClass = true;
                        historyPisition = historyNews.getTime();
                    }

                }

                EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                playNews(playPosition);
                EventBus.getDefault().post(new NewsPlayerNextEvent(playPosition));

            }
        }

        @Override
        public void nextMP3() {
            super.nextMP3();
            if (null!=mp3List&&mp3List.size() != 0 && playPosition < mp3List.size() - 1) {
                playPosition += 1;
                playMp3(playPosition);

            }

        }

        @Override
        public void previous() {
            super.previous();
            if (null!=newsList&&newsList.size() != 0 && playPosition > 0) {
                if (null!=channel&&!channel.equals(AppConfig.CHANNEL_TYPE_DOWNLOAD) && !channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {//不是播放下载的新闻 也不是播放课堂

                    if (LoginUtil.isUserLogin()) {//已经登录
                        int vipState = VipUtil.getInstance().getVipState();//会员状态
                        switch (vipState) {
                            case 0: //普通
                                if (AppSpUtil.getInstance().getUserInfo().getResults().getIs_stop() == 1) {
                                    isUsedUpTimes = true;
                                    if (!SQLHelper.getInstance().isHasNews(currentNews.getId())) {
                                        EventBus.getDefault().post(new PlayLimitEvent());
                                    }
                                    return;
                                }

                                int playTimes = AppSpUtil.getInstance().getPlayTimes();

                                if (playTimes < AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + 1) {
                                    isUsedUpTimes = false;
                                } else {
                                    isUsedUpTimes = true;
                                    if (!SQLHelper.getInstance().isHasNews(currentNews.getId())) {
                                        EventBus.getDefault().post(new PlayLimitEvent());
                                        EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                                    }
                                    runOutLimitTimes();
                                    return;
                                }
                                break;
                            case 1://会员
                                break;
                            case 2://超级会员
                                break;

                        }

                    } else {//未登录
                        int playTimes = AppSpUtil.getInstance().getPlayTimes();

                        if (playTimes < AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + 1) {
                            isUsedUpTimes = false;
                        } else {
                            isUsedUpTimes = true;
                            if (!SQLHelper.getInstance().isHasNews(currentNews.getId())) {
                                EventBus.getDefault().post(new PlayLimitEvent());
                            }
                            return;
                        }
                    }

                }

                playPosition -= 1;

                if(null != channel && channel.equals(AppConfig.CHANNEL_TYPE_CLASS)){
                    NewsBean news = (NewsBean) newsList.get(playPosition);
                    String id = news.getId();
                    HistoryNews historyNews = sqlHelper.isHasHistoryNews(id);
                    if(null!=historyNews){
                        isPlayLastClass = true;
                        historyPisition = historyNews.getTime();
                    }

                }

                EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                playNews(playPosition);
                EventBus.getDefault().post(new NewsPlayerPreviousEvent(playPosition));
            }
        }


        @Override
        public void pause() {
            super.pause();
            releaseWifiLock();
            Logger.e("暂停");
            if (mNewsMediaPlayer != null && mNewsMediaPlayer.isPlaying() && newsList != null) {
                isPause = true;
                isPlaying = false;
                mNewsMediaPlayer.pause();
                CurrentNewsIsPause = true;
                pauseID=currentNews.getId();
                EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PAUSE));
                EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                if (null != mRemoteViews && null != mNotification) {
                    mRemoteViews.setImageViewResource(R.id.imageView_main_play_startplay,
                            R.drawable.playbar_play_selector);
                    startForeground(1111, mNotification);
                }

                //课堂暂停时,存贮正在听的课程以及时间
                if (null != channel && channel.equals(AppConfig.CHANNEL_TYPE_CLASS) && mNewsMediaPlayer.getCurrentPosition() != 0 && !isTrack) {
                    int currentPosition = mNewsMediaPlayer.getCurrentPosition();
                    sqlHelper.insertHistorydNews(currentNews.getId(),currentPosition+"",mNewsMediaPlayer.getDuration()+"");
                    sqlHelper.insertLastPlayClass(classActid,playPosition+"",currentPosition+"");
                    Logger.e("暂停: playPosition"+playPosition+"  id:"+classActid);
                }


            }

        }

        @Override
        public void continuePlay() {
            super.continuePlay();
            Logger.e("继续播放");
            getWifiLock();
            if (mNewsMediaPlayer != null && newsList != null && !isPlaying) {

                mNewsMediaPlayer.start();
                isPlaying = true;
                isPause = false;
                CurrentNewsIsPause = false;
                pauseID="";
                EventBus.getDefault().post(new NewsPlayerPlayStateEvent(STATE_PLAY));
                EventBus.getDefault().post(new PlayerBarEvent(STATE_PLAY));
                if (null != mRemoteViews && null != mNotification) {
                    mRemoteViews.setImageViewResource(R.id.imageView_main_play_startplay,
                            R.drawable.playbar_pause_selector);
                    startForeground(1111, mNotification);
                }
            }

        }

        @Override
        public void pauseMp3() {
            super.pauseMp3();
            releaseWifiLock();
            if (mNewsMediaPlayer != null && mNewsMediaPlayer.isPlaying() && mp3List != null) {
                currentMp3 = "";
                mNewsMediaPlayer.pause();
                isPause = true;
                isPlaying = false;
                EventBus.getDefault().post(new ClassListenEvent(STATE_PAUSE, currentMp3));
                EventBus.getDefault().post(new PlayerBarEvent(STATE_PAUSE));
                if (null != mRemoteViews && null != mNotification) {
                    mRemoteViews.setImageViewResource(R.id.imageView_main_play_startplay,
                            R.drawable.playbar_play_selector);
                    startForeground(1111, mNotification);
                }
            }
        }

        @Override
        public Boolean isPlaying() {
            return isPlaying;
        }

        @Override
        public Boolean isPause() {
            return isPause;

        }

        @Override
        public void seekTo(int progress) {
            super.seekTo(progress);
            Logger.e("seekTo");
            if (null!=newsList&&newsList.size() != 0 && currentNews != null && mPlayBinder != null) {
                mNewsMediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void setNewsList(List list) {
            super.setNewsList(list);
        }

        @Override
        public void addNewsList(List list) {
            super.addNewsList(list);
        }

        @Override
        public List getNewsList() {
            return super.getNewsList();
        }

        @Override
        public String getPlayingMp3() {
            return currentMp3;
        }

        @Override
        public NewsBean getPlayingNews() {
            return currentNews;
        }

        @Override
        public void setMp3List(List list) {
            super.setMp3List(list);
        }

        @Override
        public void release() {
            super.release();
            if (mNewsMediaPlayer != null && mNewsMediaPlayer.isPlaying()) {
                mNewsMediaPlayer.release();
                mNewsMediaPlayer = null;
            }
        }


        @Override
        public int getPlayPosition() {
            return super.getPlayPosition();
        }


        @Override
        public Boolean isMp3() {
            return isMp3;
        }


        @Override
        public void setChannel(String s) {

            //切换频道,上一次播放的是课堂 ,保存正在播放课堂的记录
            if(null!=lastChannel&&lastChannel.equals(AppConfig.CHANNEL_TYPE_CLASS)&&null!=classActid&&!TextUtils.isEmpty(classActid)&&mNewsMediaPlayer.isPlaying()){

                int currentPosition = mNewsMediaPlayer.getCurrentPosition();
                lastChannel="";
                sqlHelper.insertHistorydNews(currentNews.getId(),currentPosition+"",mNewsMediaPlayer.getDuration()+"");
                sqlHelper.insertLastPlayClass(classActid,playPosition+"",currentPosition+"");
                Logger.e("上一次播放的是课堂"+"id:  "+classActid+"播放位置:  "+playPosition);
            }
            channel = s;
        }

        @Override
        public String getChannel() {
            return channel;
        }

        @Override
        public int getMediaPlayerCurrentPosition() {
            return mNewsMediaPlayer.getCurrentPosition();
        }

        @Override
        public String getPlayId() {
            if (null != currentNews) {
                return currentNews.getId();
            } else {
                return "";
            }
        }

        @Override
        public void StartTracking() {
            super.StartTracking();
            isTrack = true;
            mPlayBinder.pause();
        }

        @Override
        public void setIsPlayLastClass(Boolean B, String position) {
            super.setIsPlayLastClass(B, position);
            isPlayLastClass = B;
            historyPisition = position;
        }

        @Override
        public Boolean isPlayLastClass() {
            return isPlayLastClass;

        }


        @Override
        public void setClassActID(String id) {

            if(TextUtils.isEmpty(classActid)){
                classActid=id;
            }else{
                 //由一个课堂直接换到另外的课堂 ,上传上一个正在播放课堂的记录
                if(null!=channel&&channel.equals(AppConfig.CHANNEL_TYPE_CLASS)&&!classActid.equals(id)&&isPlaying()){

                    int currentPosition = mNewsMediaPlayer.getCurrentPosition();
                    lastChannel=AppConfig.CHANNEL_TYPE_CLASS;
                    sqlHelper.insertHistorydNews(currentNews.getId(),currentPosition+"",mNewsMediaPlayer.getDuration()+"");
                    sqlHelper.insertLastPlayClass(classActid,playPosition+"",currentPosition+"");
                    Logger.e("设置频道 上传上一次id"+classActid+"  播放位置:"+playPosition);
                }
                classActid=id;
            }

        }

        @Override
        public String getClassActID() {
            return classActid;
        }

        @Override
        public String getPartID() {
            return partId;
        }

        @Override
        public void setPartID(String id) {
            partId=id;
        }

        @Override
        public String getPauseID() {
            return pauseID;
        }

        @Override
        public void setPauseID(String id) {
            pauseID=id;
        }

        @Override
        public void upLoadClassHistory() {
            super.upLoadClassHistory();
            if(null!=channel&&channel.equals(AppConfig.CHANNEL_TYPE_CLASS)&&isPlaying()){
                int currentPosition = mNewsMediaPlayer.getCurrentPosition();
                if(currentPosition>1000){
                    sqlHelper.insertHistorydNews(currentNews.getId(),currentPosition+"",mNewsMediaPlayer.getDuration()+"");
                    sqlHelper.insertLastPlayClass(classActid,playPosition+"",currentPosition+"");
                    Logger.e("停止: playPosition"+playPosition+"  id:"+classActid);
                }

            }
        }
    }


    /**
     * 播放新闻
     */
    private void play() {
        isPlayDownload = false;
        if (prepareRun == null) {
            prepareRun = new Runnable() {
                @Override
                public void run() {
                    if (mNewsMediaPlayer != null) {
                        mNewsMediaPlayer.reset();
                        try {
                            mNewsMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                            if (musicUrl != null) {
                                mNewsMediaPlayer.setDataSource(musicUrl);
                                mNewsMediaPlayer.prepareAsync();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        iniMediaPlayer();
                        mNewsMediaPlayer.reset();
                        try {
                            mNewsMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            if (musicUrl != null) {
                                mNewsMediaPlayer.setDataSource(musicUrl);
                                mNewsMediaPlayer.prepareAsync();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
        if (!singleExecutorService.isShutdown()) {
            singleExecutorService.execute(prepareRun);
        }
    }

    /**
     * 播放下载的文件
     */
    private void playDownload() {
        isPlayDownload = true;
        mNewsMediaPlayer.reset();
        if (null != musicUrl) {

            try {
                mNewsMediaPlayer.setDataSource(musicUrl);
                mNewsMediaPlayer.prepare();
                mNewsMediaPlayer.start();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取新闻的总播放的时间
     *
     * @param news
     * @return
     */
    private int getAllTime(NewsBean news) {
        int allTime = 0;
        if (news != null) {
            try {
                allTime = Integer.valueOf(news.getPost_time());
            } catch (Exception e) {
                allTime = 0;
            }
        }
        return allTime;
    }


    /**
     * 定时器更新播放进度
     */
    private void beginUpdateNewsSeekBar() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mNewsMediaPlayer != null) {
                    if (mNewsMediaPlayer.isPlaying()) {
                        setSeekBarProgressPosition(mNewsMediaPlayer.getCurrentPosition());
                    }
                }
            }
        };
        mTimer.schedule(timerTask, 0, 500);

    }


    /**
     * 用户听完每天限制次数后,通知服务器该用户当日已经听完
     */
    private void runOutLimitTimes() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        OkGo.<SimpleMsgBean>post(UrlProvider.HAS_USEDUP_TIMES).params(map).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                if (VipUtil.getInstance().getVipState() == 0) {
                    FollowUtil.getUserInfo();
                }

            }
        });

    }

    @SuppressWarnings("deprecation")
    private void startForceGround(String imgUrl, final String title) {

        if (mRemoteViews == null) {
            mRemoteViews = new RemoteViews(getPackageName(), R.layout.remoteview);
        }

        Glide.with(this).load(imgUrl).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                PendingIntent mPendingIntent;
                mRemoteViews.setImageViewBitmap(R.id.iv, resource);
                mRemoteViews.setImageViewResource(R.id.imageView_main_play_startplay, R.drawable.playbar_pause_selector);
                mRemoteViews.setTextViewText(R.id.tv_title, title);
                mRemoteViews.setOnClickPendingIntent(R.id.imageView_main_play_startplay,
                        PendingIntent.getBroadcast(TwNewsService.this, 0, new Intent(AppConfig.BROADCAST_REMOTEVIEW_RECEIVER).putExtra("what", 0), 0));
                mRemoteViews.setOnClickPendingIntent(R.id.playing_img_next,
                        PendingIntent.getBroadcast(TwNewsService.this, 1, new Intent(AppConfig.BROADCAST_REMOTEVIEW_RECEIVER).putExtra("what", 1), 0));
                mPendingIntent = PendingIntent.getActivity(TwNewsService.this, 0, new Intent(TwNewsService.this, NewsDetailActivity.class), 0);

                mNotification = new Notification(R.drawable.ic_notification, title, System.currentTimeMillis());


                mNotification.contentView = mRemoteViews;
                mNotification.contentIntent = mPendingIntent;

                startForeground(1111, mNotification);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                PendingIntent mPendingIntent;
                mRemoteViews.setImageViewResource(R.id.iv, R.drawable.tingwen_logo);
                mRemoteViews.setTextViewText(R.id.tv_title, title);
                mRemoteViews.setOnClickPendingIntent(R.id.imageView_main_play_startplay,
                        PendingIntent.getBroadcast(TwNewsService.this, 0, new Intent(AppConfig.BROADCAST_REMOTEVIEW_RECEIVER).putExtra("what", 0), 0));
                mRemoteViews.setOnClickPendingIntent(R.id.playing_img_next,
                        PendingIntent.getBroadcast(TwNewsService.this, 1, new Intent(AppConfig.BROADCAST_REMOTEVIEW_RECEIVER).putExtra("what", 1), 0));

                mPendingIntent = PendingIntent.getActivity(TwNewsService.this, 0, new Intent(TwNewsService.this, NewsDetailActivity.class), 0);
                mNotification = new Notification(R.drawable.ic_notification, title, System.currentTimeMillis());
                mNotification.contentView = mRemoteViews;
                mNotification.contentIntent = mPendingIntent;
                startForeground(1111, mNotification);
            }


        });


    }

    private void initHistoryClass(){
        isPlayLastClass=false;
        historyPercent=0;
        historyPisition="";
    }

}
