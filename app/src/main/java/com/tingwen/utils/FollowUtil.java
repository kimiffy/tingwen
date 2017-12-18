package com.tingwen.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.app.AppSpUtil;
import com.tingwen.bean.FansBean;
import com.tingwen.bean.FollowBean;
import com.tingwen.bean.FollowListBean;
import com.tingwen.bean.User;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.widget.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 粉丝,关注相关的工具类
 * Created by Administrator on 2017/9/19 0019.
 */
public class FollowUtil {

    public static ArrayList<String> followPartList = new ArrayList<>();//关注的节目
    public static ArrayList<String> friendList = new ArrayList<>();
    public static ArrayList<String> attentionsList = new ArrayList<>();//关注的人
    public static ArrayList<String> fansList = new ArrayList<>();//粉丝



    /**
     * 获取个人信息并保存
     */
    public static void getUserInfo() {

        OkGo.<User>post(UrlProvider.GET_USER_INFO).params("accessToken", LoginUtil.getAccessToken()).execute(new SimpleJsonCallback<User>(User.class) {
            @Override
            public void onSuccess(Response<User> response) {
                AppSpUtil.getInstance().saveUserInfo(response.body().toString());
                String listenMoney = response.body().getResults().getListen_money();
                AppSpUtil.getInstance().saveTingbi(listenMoney);
                AppSpUtil.getInstance().saveJingbi(response.body().getResults().getGold());
                Logger.e("更新用户信息成功");
            }
        });


    }

    /**
     * 获取关注过的节目(主播)ID
     */
    public static void getFollowedPart() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("limit", "10000");
        OkGo.<FollowBean>post(UrlProvider.SUBSCRIP_LIST).params(map, true).execute(new SimpleJsonCallback<FollowBean>(FollowBean.class) {

            @Override
            public void onSuccess(Response<FollowBean> response) {
                List<FollowBean.ResultsBean> results = null;
                try {
                    results = response.body().getResults();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < results.size(); i++) {
                    String id = results.get(i).getId();
                    followPartList.add(id);
                }
            }

            @Override
            public void onError(Response<FollowBean> response) {
                super.onError(response);
            }
        });

    }

    /**
     * 是否关注(节目,主播)
     *
     * @param id
     * @return
     */
    public static Boolean isFollowed(String id) {
        return !(followPartList == null || followPartList.size() == 0) && followPartList.contains(id);
    }


    /**
     * 获取关注过的人
     */
    public static void getFollowedPersons() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("limit", "10000");
        OkGo.<FollowListBean>post(UrlProvider.ATTENTION_LIST).params(map, true).execute(new SimpleJsonCallback<FollowListBean>(FollowListBean.class) {
            @Override
            public void onSuccess(Response<FollowListBean> response) {

                int status = 0;
                try {
                    status = response.body().getStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (status == 1) {
                    List<FollowListBean.ResultsBean> results = response.body().getResults();
                    for (int i = 0; i < results.size(); i++) {
                        String id = results.get(i).getFriend_id();
                        attentionsList.add(id);
                        Logger.e("添加关注: id"+id);
                    }
                }

            }

            @Override
            public void onError(Response<FollowListBean> response) {
                super.onError(response);

            }
        });

    }

    /**
     * 是否关注过
     * @param id
     * @return
     */
    public static Boolean isAttentioned(String id) {
        return null != attentionsList && attentionsList.contains(id);
    }






    /**
     * 获取粉丝列表
     */
    public static void getFansList() {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("limit","100000");

        OkGo.<FansBean>post(UrlProvider.FANS_LIST).params(map, true).execute(new SimpleJsonCallback<FansBean>(FansBean.class) {
            @Override
            public void onSuccess(Response<FansBean> response) {
                int status = 0;
                try {
                    status = response.body().getStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (status == 1) {
                    List<FansBean.ResultsBean> results = response.body().getResults();
                    for (int i = 0; i < results.size(); i++) {
                        String id = results.get(i).getUid();
                        fansList.add(id);
                        Logger.e("添加粉丝: id"+id);
                    }
                }
            }

            @Override
            public void onError(Response<FansBean> response) {
                super.onError(response);


            }
        });


    }

    /**
     * 是否是粉丝
     * @param id
     * @return
     */
    public static Boolean isFans(String id) {
        return null != fansList && fansList.contains(id);
    }




}
