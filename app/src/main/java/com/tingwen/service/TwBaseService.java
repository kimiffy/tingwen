package com.tingwen.service;

import android.app.Service;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Message;

import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.bean.NewsBean;
import com.tingwen.interfaces.MediaPlayerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
public abstract class TwBaseService<T> extends Service {

    //播放新闻list
    protected List<T> newsList = new ArrayList<>();
    //试听list
    protected List<T> mp3List = new ArrayList<>();
    //当前播放的position
    protected int playPosition;
    protected String actId = "";//节目的ID
    protected ExecutorService singleExecutorService = Executors.newSingleThreadExecutor();
    protected Runnable prepareRun;
    protected WifiManager.WifiLock mWifilock;//wifi锁



    @Override
    public void onCreate() {
        super.onCreate();
    }




    protected class MediaPlayBinder extends Binder implements MediaPlayerInterface {


        @Override
        public void playNews(int position) {
            playPosition = position;
        }

        @Override
        public void playMp3(int position) {
            playPosition = position;
        }

        @Override
        public void pause() {

        }

        @Override
        public void next() {

        }

        @Override
        public void previous() {

        }

        @Override
        public void continuePlay() {

        }



        @Override
        public void setNewsList(List list) {
            newsList.clear();
            newsList.addAll(list);
        }

        @Override
        public void addNewsList(List list) {
            newsList.addAll(list);
        }



        @Override
        public List getNewsList() {
            return newsList;
        }

        @Override
        public void setMp3List(List list) {
            mp3List.clear();
            mp3List.addAll(list);
        }

        @Override
        public void nextMP3() {

        }

        @Override
        public String getPlayingMp3() {
            return null;
        }

        @Override
        public void pauseMp3() {

        }

        @Override
        public Boolean isMp3() {
            return null;
        }

        @Override
        public void setActId(String id) {
            actId=id;
        }

        @Override
        public String getActId() {
            return actId;
        }

        @Override
        public void setChannel(String channel) {

        }

        @Override
        public String getChannel() {
            return null;
        }

        @Override
        public String getPlayId() {
            return null;
        }

        @Override
        public void StartTracking() {

        }

        @Override
        public void setIsPlayLastClass(Boolean B,String position) {

        }

        @Override
        public Boolean isPlayLastClass() {
            return null;
        }

        @Override
        public void setClassActID(String id) {

        }

        @Override
        public String getClassActID() {
            return null;
        }

        @Override
        public void setPartID(String id) {

        }

        @Override
        public String getPartID() {
            return null;
        }

        @Override
        public void setPauseID(String id) {

        }

        @Override
        public String getPauseID() {
            return null;
        }

        @Override
        public void upLoadClassHistory() {

        }


        @Override
        public void seekTo(int progress) {

        }

        @Override
        public NewsBean getPlayingNews() {
            return null;
        }

        @Override
        public int getPlayPosition() {
            if(newsList.size()==0){
                return -1;
            }
            return playPosition;
        }

        @Override
        public int getMediaPlayerCurrentPosition() {
            return 0;
        }

        @Override
        public Boolean isPlaying() {
            return null;
        }

        @Override
        public Boolean isPause() {
            return null;
        }

        @Override
        public void release() {

        }

    }


    protected void getWifiLock() {
        if (mWifilock == null) {
            WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            mWifilock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "wifiLock");
            mWifilock.acquire();
        }
    }

    protected void releaseWifiLock() {
        if (mWifilock != null) {
            mWifilock.release();
            mWifilock = null;
        }
    }


    /**设置缓冲进度*/
    protected void setSeekBarSecondary(int allTime,int percent) {
            if (NewsDetailActivity.progressHandler != null) {
                Message messageDetail;
                messageDetail = NewsDetailActivity.progressHandler.obtainMessage();
                messageDetail.what = 2;
                messageDetail.arg1 = allTime * percent / 100;
                messageDetail.sendToTarget();
            }
    }


    /**设置seekBar的最大值*/
    protected void setSeekBarMax(int allTime) {
        if (NewsDetailActivity.progressHandler != null) {
            Message messageDetail;
            messageDetail = NewsDetailActivity.progressHandler.obtainMessage();
            messageDetail.what = 0;
            messageDetail.arg1 = allTime;
            messageDetail.sendToTarget();
        }
    }

    /**
     * 更新seekBar播放进度
     * @param seekBarPosition
     */
    protected void setSeekBarProgressPosition(int seekBarPosition) {

            if (NewsDetailActivity.progressHandler != null) {
                Message messageDetail;
                messageDetail = NewsDetailActivity.progressHandler.obtainMessage();
                messageDetail.what = 1;
                messageDetail.arg1 = seekBarPosition;
                messageDetail.sendToTarget();

            }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
