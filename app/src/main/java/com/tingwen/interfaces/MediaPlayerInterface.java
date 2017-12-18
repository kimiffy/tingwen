package com.tingwen.interfaces;

import com.tingwen.bean.NewsBean;

import java.util.List;

/**
 * 媒体播放器接口
 * Created by Administrator on 2017/8/23 0023.
 */
public interface MediaPlayerInterface<T> {


    /**
     * 播放新闻
     */
    void playNews(int position);

    /**
     * 暂停
     */
    void pause();

    /**
     * 下一条
     */
    void next();

    /**
     * 上一条
     */
    void previous();

    /**
     * 继续播放
     */
    void continuePlay();


    /**
     * 设置播放列表
     * @param list
     */
    void setNewsList(List<NewsBean> list);

    /**
     * 添加播放列表
     * @param list
     */
    void addNewsList(List<NewsBean> list);

    /**
     * 获取播放列表
     * @return
     */
    List<NewsBean> getNewsList();

    /**
     * 跳至指定进度
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 获取正在播放的新闻
     * @return
     */
    NewsBean getPlayingNews();


    /**
     * 获取正在播放的position
     * @return
     */
    int getPlayPosition();

    /**
     * 获取媒体播放器当前播放进度位置
     * @return
     */
    int getMediaPlayerCurrentPosition();

    /**
     * 是否正在播放
     * @return
     */
    Boolean isPlaying();

    /**
     * 是否正在暂停
     * @return
     */
    Boolean isPause();

    /**
     * 释放资源
     */
    void release();

    /**
     * 播放Mp3
     */
    void playMp3(int position);
    /**
     * 设置试听mp3列表
     * @param list
     */
    void setMp3List(List<T> list);

    /**
     * 下一条试听
     */
    void nextMP3();

    /**
     * 获取正在播放的mp3
     */
    String getPlayingMp3();

    /**
     * 暂停试听
     */
    void pauseMp3();

    /**
     * 是否是试听
     * @return
     */
    Boolean isMp3();

    /**
     * 设置播放试听的课堂ID
     * @return
     */
    void setActId(String id);
    /**
     * 获取播放试听的课堂ID
     * @return
     */
    String getActId();


    /**
     * 设置播放频道
     * @param channel
     */
    void setChannel(String channel);

    /**
     * 获取当前正在播放的频道
     * @return
     */
    String getChannel();

    /**
     * 获取当前正在播放id
     * @return
     */
    String getPlayId();

    /**
     * 拖动进度条
     */
    void StartTracking();

    /**
     * 设置是否是播放历史课堂纪录及上一次保存的进度
     */
    void setIsPlayLastClass(Boolean B,String position);

    /**
     * 是否是播放课堂历史纪录
     * @return
     */
    Boolean isPlayLastClass();

    /**
     * 设置课堂id
     */
    void setClassActID(String id);

    /**
     * 获取课堂id
     * @return
     */
    String getClassActID();

    /**
     * 设置节目id
     */
    void setPartID(String id);
    /**
     * 获取节目id
     * @return
     */
    String getPartID();

    /**
     * 设置暂停id
     * @param id
     * @return
     */
    void setPauseID(String id);


    String getPauseID();

    void upLoadClassHistory();
}
