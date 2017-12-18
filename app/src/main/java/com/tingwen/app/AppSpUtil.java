package com.tingwen.app;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tingwen.bean.DiscoveryBean;
import com.tingwen.bean.FastNewsADBean;
import com.tingwen.bean.User;
import com.tingwen.constants.AppConfig;
import com.tingwen.utils.SpUtil;

import java.util.List;

/**
 * 描述: 存储APP主要信息  Sp工具类
 * 名称: AppSpUtil
 */
public class AppSpUtil {

    static AppSpUtil instance;

    public static AppSpUtil getInstance() {
        if (instance == null) {
            instance = new AppSpUtil();
        }
        return instance;
    }



    /*----------------------------------------用户信息---------------------------------------------*/

    /**
     * param json_user_info
     * 描述：将用户信息作为json保存起来,并将用户是否登录设置为true
     */
    public synchronized void saveUserInfo(String json_user_info) {
        SpUtil.setString(AppConfig.KEY_USER_INFO, json_user_info);
        SpUtil.setBoolean(AppConfig.KEY_USER_LOGIN, true);
    }

    /**
     * @return user
     * 描述：获取用户信息
     */
    public User getUserInfo() {
        String json_user_info = SpUtil.getString(AppConfig.KEY_USER_INFO);
        User user = null;
        if (!TextUtils.isEmpty(json_user_info)) {
            user = new Gson().fromJson(json_user_info, User.class);
        }
        return user;
    }

    /**
     * 描述：删除用户信息,并设置用户登录为false
     */
    public void deleteUserInfo() {
        SpUtil.setString(AppConfig.KEY_USER_INFO, "");
        SpUtil.setBoolean(AppConfig.KEY_USER_LOGIN, false);

    }
/*----------------------------------广告------------------------------------*/

    /**
     * 存储快讯广告数据
     *
     * @param result
     */
    public void saveADResult(FastNewsADBean result) {
        String results = new Gson().toJson(result);
        SpUtil.setString(AppConfig.KEY_FAST_NEWS_AD, results);
    }

    /**
     * 获取快讯广告数据
     *
     * @return
     */
    public List<FastNewsADBean.ResultsBean> getFastNewsAD() {
        FastNewsADBean result = null;
        String json = SpUtil.getString(AppConfig.KEY_FAST_NEWS_AD);
        if (!TextUtils.isEmpty(json)) {
            result = new Gson().fromJson(json, FastNewsADBean.class);
        }
        return result != null ? result.getResults() : null;
    }


/*--------------------------------------------发现--------------------------------------*/

    /**
     * 存储发现数据
     *
     * @param result
     */
    public void saveDiscoveryResult(DiscoveryBean result) {
        String results = new Gson().toJson(result);

        SpUtil.setString(AppConfig.KEY_DISCOVERY, results);


    }


    /**
     * 获取发现数据
     *
     * @return
     */
    public List<DiscoveryBean.ResultsBean> getDiscoveryData() {
        DiscoveryBean result = null;
        String json = SpUtil.getString(
                AppConfig.KEY_DISCOVERY);
        if (!TextUtils.isEmpty(json)) {
            result = new Gson().fromJson(json, DiscoveryBean.class);
        }
        return result != null ? result.getResults() : null;
    }


/*------------------------------------------字体设置-----------------------------------------------*/

    /**
     * 设置字体大小
     *
     * @param i
     */
    public void saveFrontSize(int i) {

        SpUtil.setInt(AppConfig.KEY_FONT, i);

    }

    /**
     * 获取字体大小
     */
    public int getFrontSize() {
        return SpUtil.getInt(AppConfig.KEY_FONT, 4);//默认 4

    }
/*------------------------------------------听币数量-----------------------------------------------*/

    /**
     * 保存用户听币数
     *
     * @param i
     */
    public void saveTingbi(String i) {
        SpUtil.setString(AppConfig.KEY_TING_BI, i);
    }

    /**
     * 获取用户听币
     */
    public String getTingbi() {
        return SpUtil.getString(AppConfig.KEY_TING_BI);
    }

    /*------------------------------------------金币数量-----------------------------------------------*/
    /**
     * 获取用户听币
     */
    public String getJingbi() {
        return SpUtil.getString(AppConfig.KEY_JING_BI);
    }
    /**
     * 保存用户金币数
     *
     * @param i
     */
    public void saveJingbi(String i) {
        SpUtil.setString(AppConfig.KEY_JING_BI, i);
    }



/*--------------------------------------------保存应用版本信息--------------------------------------*/


    /**
     * @param version_name 版本名称
     * @param version_code 版本号
     *                     描述：保存版本信息
     */
    public void saveVersion(String version_name, int version_code) {
        SpUtil.setString(AppConfig.KEY_VERSION_NAME, version_name + "");
        SpUtil.setString(AppConfig.KEY_VERSION_CODE, version_code + "");
    }

    /**
     * 描述：获取保存的版本信息
     */
    public String getLocalVersionName() {
        return SpUtil.getString(AppConfig.KEY_VERSION_NAME);
    }



    /*--------------------------------------------保存每天限制信息--------------------------------------*/

    /**
     * 保存服务器时间
     *
     * @param i
     */
    public void saveDate(String i) {
        SpUtil.setDate(AppConfig.KEY_DAILY_DATE, i);
    }


    /**
     * 获取服务器时间
     */
    public String getDate() {
        return SpUtil.getDate(AppConfig.KEY_DAILY_DATE);
    }


    /**
     * 保存每日可收听次数
     *
     * @param i
     */
    public void saveLimit(int i) {
        SpUtil.setInt(AppConfig.KEY_PRE_PLAY_TIME_LIMIT, i);
    }

    /**
     * 获取每日可收听次数
     *
     * @return
     */
    public int getLimit() {
        return SpUtil.getInt(AppConfig.KEY_PRE_PLAY_TIME_LIMIT, 10);//默认值10
    }

    /**
     * 保存每日已经收听次数
     *
     * @param i
     */
    public void savePlayTimes(int i) {
        SpUtil.setInt(AppConfig.KEY_PLAY_TIME, i);
    }

    /**
     * 获取每日已经收听次数
     *
     * @param
     */
    public int getPlayTimes() {
        return SpUtil.getInt(AppConfig.KEY_PLAY_TIME, 0);
    }

    /**
     * 本地播放次数加 1 ,并保存返回保存后的值
     */
    public void playTimesPlus() {
        int playTimes = getPlayTimes();
        playTimes += 1;
        savePlayTimes(playTimes);
    }

    /**
     * 保存获取点赞数据的时间
     *
     * @param time
     */
    public void saveZanTime(String time) {
        SpUtil.setString(AppConfig.KEY_ZAN_TIME, time);
    }

    /**
     * 获取上一次获取点赞的时间
     *
     * @return
     */
    public String getZanTime() {
        return SpUtil.getString(AppConfig.KEY_ZAN_TIME);
    }

    /**
     * 保存 意见反馈中有关自己的评论
     *
     * @param message
     */
    public void saveFeedbackMessage(String message) {
        SpUtil.setString(AppConfig.KEY_FEED_BACK_MESSAGE, message);
    }

    /**
     * 获取保存的意见反馈中有关自己的评论
     *
     * @return
     */
    public String getFeedbackMessage() {
        return SpUtil.getString(AppConfig.KEY_FEED_BACK_MESSAGE);
    }


    /**
     * 保存获取点赞数据的时间
     *
     * @param time
     */
    public void saveListenCircleTime(String time) {
        SpUtil.setString(AppConfig.KEY_LISTEN_CIRCLR_TIME, time);
    }

    /**
     * 获取上一次获取点赞的时间
     *
     * @return
     */
    public String getListenCircleTime() {
        return SpUtil.getString(AppConfig.KEY_LISTEN_CIRCLR_TIME);
    }

    /**
     * 本地保存听友圈关于自己的评论
     *
     * @param
     */
    public void saveListenCircleMessage(String json) {
        SpUtil.setString(AppConfig.KEY_LISTEN_CIRCLR_MESSAGE, json);
    }

    /**
     * 获取本地听友圈关于自己的评论
     * @return
     */
    public String getListenCircleMessage() {
        return SpUtil.getString(AppConfig.KEY_LISTEN_CIRCLR_MESSAGE);
    }



}
