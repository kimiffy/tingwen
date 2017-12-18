package com.tingwen.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tingwen.app.AppSpUtil;
import com.tingwen.bean.FeedBackMessageBean;
import com.tingwen.bean.ListenCircleMessageBean;
import com.tingwen.event.NewFeedBackMessageEvent;
import com.tingwen.event.NewListenCircleMessage;
import com.tingwen.event.NewListenPublishEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新消息
 * Created by Administrator on 2017/11/8 0008.
 */
public class NewMessageUtil {

    private Context context;
    private volatile static NewMessageUtil instance;
    private List<Map<String, Object>> newZans = new ArrayList<>();//关于自己的点赞
    private List<FeedBackMessageBean.ResultsBean> feedbackList = new ArrayList<>();//意见反馈关于自己的评论
    private  Map<String,String> listenCircleNew = new HashMap<>();//听友圈的新消息
    private List<ListenCircleMessageBean.ResultsBean>  listenMessage = new ArrayList<>();//听友圈关于自己的评论
    private String listenCircleNewResult;

    private NewMessageUtil(Context context) {
        this.context = context;
    }

    public static NewMessageUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (NewMessageUtil.class) {
                if (instance == null) {
                    instance = new NewMessageUtil(context);
                }
            }
        }
        return instance;
    }


    /**
     * 获取新的点赞信息
     */
    public void getNewZan() {

        if (LoginUtil.isUserLogin()) {

            String zanTime = AppSpUtil.getInstance().getZanTime();

            Map<String, String> map = new HashMap<>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("page", "1");
            if (TextUtils.isEmpty(zanTime)) {
                map.put("date", (System.currentTimeMillis() / 1000) + "");
                AppSpUtil.getInstance().saveZanTime(System.currentTimeMillis() / 1000 + "");
            } else {
                map.put("date", zanTime);
            }
            map.put("limit", "99");
            OkGo.<String>post(UrlProvider.ZAN_INFO).params(map).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getInt("status") == 1) {
                            String results = jsonObject.getString("results");
                            if (!TextUtils.isEmpty(results)) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Map<String, Object>>>() {
                                }.getType();
                                newZans = gson.fromJson(results, type);
                                // TODO: 2017/11/8 0008  有新的点赞信息
                                Logger.e("完成获取点赞");
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

    }


    /**
     * 获取意见反馈中有关自己的评论
     */
    public void getNewComment() {

        if (LoginUtil.isUserLogin()) {
            final String oldFeedBack = AppSpUtil.getInstance().getFeedbackMessage();
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("page", "1");
            map.put("limit", "100");
            OkGo.<FeedBackMessageBean>post(UrlProvider.COMMENT_LIST).params(map).execute(new SimpleJsonCallback<FeedBackMessageBean>(FeedBackMessageBean.class) {
                @Override
                public void onSuccess(Response<FeedBackMessageBean> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        List<FeedBackMessageBean.ResultsBean> newFeedList = response.body().getResults();

                        if (TextUtils.isEmpty(oldFeedBack)) {//没有旧的数据
                            String json = new Gson().toJson(response.body());
                            AppSpUtil.getInstance().saveFeedbackMessage(json);
                            feedbackList = newFeedList;
                            EventBus.getDefault().post(new NewFeedBackMessageEvent(feedbackList.size()));

                        } else {//有旧的数据

                            FeedBackMessageBean feedBackMessageBean = new Gson().fromJson(oldFeedBack, FeedBackMessageBean.class);
                            List<FeedBackMessageBean.ResultsBean> oldFeedBackList = feedBackMessageBean.getResults();
                            if(null!=newFeedList&&newFeedList.size()!=0){
                                String json = new Gson().toJson(response.body());// TODO: 2017/11/10 0010 暂时把每次请求到的数据保存起来,后期需要后台给一个清除已读的接口
                                AppSpUtil.getInstance().saveFeedbackMessage(json);
                            }

                            ArrayList<FeedBackMessageBean.ResultsBean> list = new ArrayList<>();

                            for (int i = 0; i < newFeedList.size(); i++) {//比较新旧数据
                                FeedBackMessageBean.ResultsBean bean = newFeedList.get(i);
                                for (FeedBackMessageBean.ResultsBean resultsBean : oldFeedBackList) {
                                    if (bean.getId().equals(resultsBean.getId())) {
                                        list.add(bean);
                                    }
                                }
                            }
                            newFeedList.removeAll(list);//删除相同的数据,不同的则为最新的消息
                            feedbackList = newFeedList;//赋值新消息列表
                            EventBus.getDefault().post(new NewFeedBackMessageEvent(feedbackList.size()));



                        }
                    }

                }
            });

        }


    }


    /**
     * 对外提供意见反馈新消息数
     *
     * @return
     */
    public int getFeedBackSize() {
        if (null != feedbackList) {
            return feedbackList.size();
        }
        return 0;
    }

    /**
     * 对外提供意见反馈列表
     * @return
     */
    public List<FeedBackMessageBean.ResultsBean> getFeedBackList() {
        if (null != feedbackList) {
            return feedbackList;
        }
        return null;
    }

    /**
     * 清空意见反馈新消息
     */
    public void clearFeedBackList() {
        if (null != feedbackList) {
            feedbackList.clear();
        }
    }


    /**
     * 是否有新的听友圈发表
     */
    public void getListenCircle(){

        if(LoginUtil.isUserLogin()){

            String time = AppSpUtil.getInstance().getListenCircleTime();
            Map<String, String> map = new HashMap<String, String>();
            map.put("accessToken", LoginUtil.getAccessToken());

            if(TextUtils.isEmpty(time)){
                map.put("date",(System.currentTimeMillis() / 1000) + "");
                AppSpUtil.getInstance().saveListenCircleTime(System.currentTimeMillis() / 1000 + "");
            }else{
                map.put("date",time);
            }

            OkGo.<String>post(UrlProvider.LISTEN_CIRCLE_NEW).params(map).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        //TODO: 2017/11/9 0009 需要换成实体类
                        listenCircleNewResult = jsonObject.getString("results");
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String,String>>(){}.getType();
                        if(!TextUtils.isEmpty(listenCircleNewResult)) {
                            listenCircleNew = gson.fromJson(listenCircleNewResult, type);
                            EventBus.getDefault().post(new NewListenPublishEvent());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

    }


    /**
     * 获取是否有新的听友圈发表的result
     * @return
     */
    public String  getNewListenCircleResult(){
        return listenCircleNewResult;
    }


    /**
     * 获取听友圈关于自己的评论
     */
    public void getNewListenCirCleMessage(){

        if(LoginUtil.isUserLogin()){

            final String oldListenMessage = AppSpUtil.getInstance().getListenCircleMessage();//旧数据

            Map<String, String> map = new HashMap<>();
            map.put("accessToken",LoginUtil.getAccessToken());
            map.put("page", "1");
            map.put("limit", "50");
            OkGo.<ListenCircleMessageBean>post(UrlProvider.LISTEN_CIRCLE_LIST).params(map).execute(new SimpleJsonCallback<ListenCircleMessageBean>(ListenCircleMessageBean.class) {
                @Override
                public void onSuccess(Response<ListenCircleMessageBean> response) {
                    int status = response.body().getStatus();
                    if(status==1){
                        List<ListenCircleMessageBean.ResultsBean> newListenMessageList = response.body().getResults();

                        if(TextUtils.isEmpty(oldListenMessage)){
                            String json = new Gson().toJson(response.body());
                            AppSpUtil.getInstance().saveListenCircleMessage(json);//保存关于自己的评论
                            listenMessage=newListenMessageList;
                            EventBus.getDefault().post(new NewListenCircleMessage(listenMessage.size()));
                        }else{
                            ListenCircleMessageBean bean1 = new Gson().fromJson(oldListenMessage, ListenCircleMessageBean.class);
                            List<ListenCircleMessageBean.ResultsBean> oldListenMessageList = bean1.getResults();

                            if(null!=newListenMessageList&&newListenMessageList.size()!=0){
                            String json =  new Gson().toJson(response.body());// TODO: 2017/11/10 0010 暂时把每次请求到的数据保存起来,后期需要后台给一个清除已读的接口

                            AppSpUtil.getInstance().saveListenCircleMessage(json);
                        }


                        ArrayList<ListenCircleMessageBean.ResultsBean> list = new ArrayList<>();

                        for (int i = 0; i < newListenMessageList.size(); i++) {//比较新旧数据
                            ListenCircleMessageBean.ResultsBean bean = newListenMessageList.get(i);
                            for (ListenCircleMessageBean.ResultsBean resultsBean : oldListenMessageList) {
                                if (bean.getId().equals(resultsBean.getId())) {
                                    list.add(bean);
                                }
                            }
                        }

                        newListenMessageList.removeAll(list);//删除相同的数据,不同的则为最新的消息
                        listenMessage = newListenMessageList;//赋值新消息列表
                        Logger.e("新消息列表: "+listenMessage.size());
                        EventBus.getDefault().post(new NewListenCircleMessage(listenMessage.size()));

                        }
                    }


                }
            });

        }

    }



    /**
     * 对外听友圈新的评论数
     *
     * @return
     */
    public int getListenCircleMessageSize() {
        if (null != listenMessage) {
            return listenMessage.size();
        }
        return 0;
    }

    /**
     * 对外提供听友圈新消息列表
     * @return
     */
    public List<ListenCircleMessageBean.ResultsBean> getListenCircleMessageList() {
        if (null != listenMessage) {
            return listenMessage;
        }
        return null;
    }

    /**
     * 清空听友圈新消息
     */
    public void clearListenCircleList() {
        if (null != listenMessage) {
            listenMessage.clear();
        }
    }

}
