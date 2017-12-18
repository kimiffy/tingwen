package com.tingwen.utils;

import android.text.TextUtils;

import com.tingwen.app.TwApplication;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.gen.DownLoadNewsDao;
import com.tingwen.gen.HistoryNewsDao;
import com.tingwen.gen.LastPlayClassDao;
import com.tingwen.gen.ListenedNewsDao;
import com.tingwen.greendao.DownLoadNews;
import com.tingwen.greendao.HistoryNews;
import com.tingwen.greendao.LastPlayClass;
import com.tingwen.greendao.ListenedNews;

import java.util.List;

/**
 * 数据库工具
 * Created by Administrator on 2017/10/24 0024.
 */
public class SQLHelper {

    private volatile static SQLHelper helper;


    private SQLHelper() {

    }

    public static SQLHelper getInstance() {
        if (helper == null) {
            synchronized (SQLHelper.class) {
                if (helper == null) {
                    helper = new SQLHelper();
                }
            }
        }
        return helper;
    }


    /*-----------------------------------------------已下载的新闻-----------------------------------*/
    /**
     * 数据库增加一条 下载的新闻
     */
    public void insertDownloadNews(DownLoadBean bean) {

        DownLoadNewsDao downLoadNewsDao = TwApplication.getInstance().getDaoSession().getDownLoadNewsDao();

        DownLoadNews downLoadNews = new DownLoadNews();
        downLoadNews.setId(bean.getId());
        downLoadNews.setPost_title(bean.getPost_title());
        downLoadNews.setPost_lai(bean.getPost_lai());
        downLoadNews.setPost_time(bean.getPost_time());
        downLoadNews.setPost_size(bean.getPost_size());
        downLoadNews.setPost_date(bean.getPost_date());
        downLoadNews.setPost_excerpt(bean.getPost_excerpt());
        downLoadNews.setPost_mp(bean.getPost_mp());
        downLoadNews.setSmeta(bean.getSmeta());
        downLoadNewsDao.insert(downLoadNews);

    }



    /**
     * 根据新闻ID 数据库删除这条新闻数据
     * @param id
     */
    public void deleteDownloadNews(String id) {
        DownLoadNewsDao downLoadNewsDao = TwApplication.getInstance().getDaoSession().getDownLoadNewsDao();
        downLoadNewsDao.deleteByKey(id);

    }

    /**
     * 数据库删除所有下载的新闻数据
     */
    public void deleteAllDownloadNews() {
        DownLoadNewsDao downLoadNewsDao = TwApplication.getInstance().getDaoSession().getDownLoadNewsDao();
        downLoadNewsDao.deleteAll();
    }



    /**
     * 数据库查询所有下载的新闻
     * @return
     */
    public List<DownLoadNews> getAllDownloadNews() {
        DownLoadNewsDao downLoadNewsDao = TwApplication.getInstance().getDaoSession().getDownLoadNewsDao();
        List<DownLoadNews> list = downLoadNewsDao.loadAll();
        return list;
    }



    /**
     * 根据新闻ID 数据库查询是否有这条数据
     * @param id
     */
    public Boolean isHasNews(String id) {
        DownLoadNewsDao downLoadNewsDao = TwApplication.getInstance().getDaoSession().getDownLoadNewsDao();
        DownLoadNews load = downLoadNewsDao.load(id);
        if(null!=load){
            return true;
        }
        return false;
    }


    /*---------------------------------------已听过的新闻-------------------------------------------*/
    /**
     * 增加一条已经听过的新闻
     * @param id
     */
    public void insertListenedNews(String id){
        if(!isListenedNews(id)){
            ListenedNewsDao listenedNewsDao = TwApplication.getInstance().getDaoSession().getListenedNewsDao();
            ListenedNews listenedNews = new ListenedNews();
            listenedNews.setId(id);
            listenedNewsDao.insert(listenedNews);
        }
    }

    /**
     * 根据新闻ID 数据库查询是否已经听过这条新闻
     * @param id
     */
    public Boolean isListenedNews(String id) {

        ListenedNewsDao listenedNewsDao = TwApplication.getInstance().getDaoSession().getListenedNewsDao();
        ListenedNews load = listenedNewsDao.load(id);

        if(null!=load){
            return true;
        }
        return false;
    }


/*---------------------------------------------课堂播放历史记录--------------------------------------*/
    /**
     * 增加一条历史时长新闻
     * @param id
     */
    public void insertHistorydNews(String id,String time,String totaltime){
        if(!isHistoryNews(id)){//没有就增加一条
            HistoryNewsDao historyNewsDao = TwApplication.getInstance().getDaoSession().getHistoryNewsDao();
            HistoryNews historyNews = new HistoryNews();
            historyNews.setId(id);
            historyNews.setTime(time);
            historyNews.setTotaltime(totaltime);
            historyNewsDao.insert(historyNews);
        }else{
            updateHistoryNews(id,time,totaltime);//有这个ID的新闻 就更新数据
        }
    }

    /**
     * 根据新闻ID 数据库查询是否已经有这条新闻的历史时长记录
     * @param id
     */
    public Boolean isHistoryNews(String id) {
        HistoryNewsDao historyNewsDao = TwApplication.getInstance().getDaoSession().getHistoryNewsDao();
        HistoryNews load = historyNewsDao.load(id);
        if(null!=load){
            return true;
        }
        return false;
    }


    /**
     * 更新历史播放新闻数据
     * @param id
     * @param time
     * @return
     */
    public void updateHistoryNews(String id,String time,String totaltime) {
        HistoryNewsDao historyNewsDao = TwApplication.getInstance().getDaoSession().getHistoryNewsDao();
        HistoryNews historyNews = new HistoryNews();
        historyNews.setId(id);
        historyNews.setTime(time);
        historyNews.setTotaltime(totaltime);
        historyNewsDao.update(historyNews);

    }

    /**
     * 根据新闻ID 数据库查询是否已经有这条新闻的历史时长记录,有就返回该条数据
     * @param id
     */
    public HistoryNews isHasHistoryNews(String id) {
        HistoryNewsDao historyNewsDao = TwApplication.getInstance().getDaoSession().getHistoryNewsDao();
        HistoryNews load = historyNewsDao.load(id);
        if(null!=load){
            return load;
        }
        return null;
    }


/*-------------------------------------课堂最后播放记录----------------------------------------------*/
    /**
     * 根据课堂ID,增加一条最后播放的课堂
     * @param id
     */
    public void insertLastPlayClass(String id,String position,String time){

        if(TextUtils.isEmpty(id)){
            return;
        }
        if(!isLastPLayClass(id)){//没有就增加一条
            LastPlayClassDao lastPlayClassDao = TwApplication.getInstance().getDaoSession().getLastPlayClassDao();
            LastPlayClass lastPlayClass = new LastPlayClass();
            lastPlayClass.setId(id);
            lastPlayClass.setPosition(position);
            lastPlayClass.setTime(time);
            lastPlayClassDao.insert(lastPlayClass);
        }else{
            upLastPlayClass(id,position,time);//有这个ID的课堂 就更新数据
        }
    }

    /**
     * 根据课堂ID 查询这个课堂的最后播放记录
     * @param id
     */
    public LastPlayClass getLastPlayClass(String id){
        LastPlayClassDao lastPlayClassDao = TwApplication.getInstance().getDaoSession().getLastPlayClassDao();
        LastPlayClass load = lastPlayClassDao.load(id);
        if(null!=load){
            return load;
        }
        return null;
    }



    /**
     * 根据课堂ID 数据库查询是否已经有这个课堂的最后播放记录
     * @param id
     */
    public Boolean isLastPLayClass(String id) {
        LastPlayClassDao lastPlayClassDao = TwApplication.getInstance().getDaoSession().getLastPlayClassDao();
        LastPlayClass load = lastPlayClassDao.load(id);
        if(null!=load){
            return true;
        }
        return false;
    }

    /**
     * 更新课堂最后播放记录
     * @param id
     * @param time
     * @return
     */
    public void upLastPlayClass(String id,String position,String time) {
        LastPlayClassDao lastPlayClassDao = TwApplication.getInstance().getDaoSession().getLastPlayClassDao();
        LastPlayClass lastPlayClass = new LastPlayClass();
        lastPlayClass.setId(id);
        lastPlayClass.setTime(time);
        lastPlayClass.setPosition(position);
        lastPlayClassDao.update(lastPlayClass);
    }


}
