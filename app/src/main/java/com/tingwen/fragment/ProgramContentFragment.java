package com.tingwen.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.adapter.ProgramContentAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.ClassLastPlayData;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.ProgramContentBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ChangeListenNewsColorEvent;
import com.tingwen.event.LoadMoreNewsEvent;
import com.tingwen.event.LoadMoreNewsReloadUiEvent;
import com.tingwen.greendao.HistoryNews;
import com.tingwen.greendao.LastPlayClass;
import com.tingwen.interfaces.MediaPlayerInterface;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.JsonUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SQLHelper;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.CommonHeader;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 节目(主播)的  节目列表
 * Created by Administrator on 2017/8/8 0008.
 */
public class ProgramContentFragment extends BaseFragment {


    @BindView(R.id.rlv_program_content)
    LRecyclerView rlvContent;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<ProgramContentBean.ResultsBean> list;
    private ProgramContentAdapter contentAdapter;
    private int page = 1;
    private int clickPosition = 1;
    private String anchorID = "";
    private boolean isClass = false;
    private List<NewsBean> newsList; //统一的新闻对象
    private boolean isAutoLoadMore = false;//是否是自动加载更多
    private int playPosition;
//    private String time = "";

    //    private  Boolean hasHistory=false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_program_content;
    }

    public static ProgramContentFragment newInstance(String anchorId, Boolean isClass) {
        ProgramContentFragment fragment = new ProgramContentFragment();
        Bundle args = new Bundle();
        args.putString("id", anchorId);
        args.putBoolean("isClass", isClass);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    protected void onFragmentVisibleChange(boolean isVisible) {
//        super.onFragmentVisibleChange(isVisible);
//        if (isVisible && list != null && list.size() == 0) {
//            loadData(1);
//        }
//    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            anchorID = getArguments().getString("id");
            Logger.e("传递 id" + anchorID);
            isClass = getArguments().getBoolean("isClass");
        }
        list = new ArrayList<>();
        newsList = new ArrayList<>();
        rlvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentAdapter = new ProgramContentAdapter(getActivity(), list, isClass, anchorID);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(contentAdapter);
        rlvContent.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvContent.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvContent.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvContent.setFooterViewHint("拼命加载中", "---我是有底线的---", "点击重新加载");
        EventBus.getDefault().register(this);

    }

    @Override
    protected void initUI() {
        super.initUI();
        if (isClass) {
            getClassData();
        } else {
            loadData(1);
        }

    }

    @Override
    protected void setListener() {
        super.setListener();
        rlvContent.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickPosition = position;
                MediaPlayerInterface newsPlayer = TwApplication.getNewsPlayer();
                newsPlayer.setNewsList(newsList);
                SQLHelper helper = SQLHelper.getInstance();
                String id = newsList.get(position).getId();
                if (isClass) {
                    HistoryNews news = helper.isHasHistoryNews(id);
                    if (null != news) {//数据库有记录
                        if (null != newsPlayer.getPlayId() && newsPlayer.getPlayId().equals(id)) {
                            newsPlayer.setIsPlayLastClass(false, "");
                        } else {
                            newsPlayer.setIsPlayLastClass(true, news.getTime());
                        }
                        newsPlayer.setClassActID(anchorID);
                        NewsDetailActivity.actionStart(getActivity(), clickPosition, AppConfig.CHANNEL_TYPE_CLASS);

                    } else {
                        newsPlayer.setIsPlayLastClass(false, "");
                        newsPlayer.setClassActID(anchorID);
                        NewsDetailActivity.actionStart(getActivity(), clickPosition, AppConfig.CHANNEL_TYPE_CLASS);
                    }

                } else {
                    newsPlayer.setPartID(anchorID);
                    NewsDetailActivity.actionStart(getActivity(), clickPosition, AppConfig.CHANNEL_TYPE_PART);
                }


            }
        });

    }


    /**
     * 获取新闻数据
     *
     * @param i
     */
    private void loadData(int i) {
        page = i;
        Map<String, String> map = new HashMap<>();
        map.put("ac_id", anchorID);
        map.put("limit", "10");
        map.put("page", String.valueOf(page));

        OkGo.<ProgramContentBean>post(UrlProvider.ACT_NEWS).params(map, true).tag(this).execute(new SimpleJsonCallback<ProgramContentBean>(ProgramContentBean.class) {
            @Override
            public void onSuccess(Response<ProgramContentBean> response) {
                list = response.body().getResults();
                String json = new Gson().toJson(list);
                List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型
                if (page == 1) {
                    contentAdapter.setDataList(list);
                    newsList.clear();
                    newsList.addAll(tempList);
                    MediaPlayerInterface newsPlayer = TwApplication.getNewsPlayer();
                    if (null != newsPlayer) {
                        String partID = newsPlayer.getPartID();
                        if (null != partID && partID.equals(anchorID)) {
                            String playId = newsPlayer.getPlayId();
                            if (!TextUtils.isEmpty(playId)) {
                                contentAdapter.setId(playId);
                            }
                        }
                    }
                    contentAdapter.setNewsList(newsList);
                } else {
                    contentAdapter.addAll(list);
                    newsList.addAll(tempList);
                    MediaPlayerInterface newsPlayer2 = TwApplication.getNewsPlayer();
                    if (null != newsPlayer2) {
                        String partID = newsPlayer2.getPartID();
                        if (null != partID && partID.equals(anchorID)) {
                            String playId = newsPlayer2.getPlayId();
                            if (!TextUtils.isEmpty(playId)) {
                                contentAdapter.setId(playId);
                            }
                        }
                    }
                    contentAdapter.setNewsList(newsList);
                }
                if (isAutoLoadMore) {//自动加载更多数据后 重新设置播放列表和播放位置;
                    isAutoLoadMore = false;
                    playPosition += 1;
                    TwApplication.getNewsPlayer().setNewsList(newsList);
                    TwApplication.getNewsPlayer().playNews(playPosition);
                    if (isClass) {
                        EventBus.getDefault().post(new LoadMoreNewsReloadUiEvent(playPosition, AppConfig.CHANNEL_TYPE_CLASS));
                    } else {
                        EventBus.getDefault().post(new LoadMoreNewsReloadUiEvent(playPosition, AppConfig.CHANNEL_TYPE_PART));
                    }
                }

                rlvContent.refreshComplete(10);//每页加载数量
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (page > 1 && list.size() < 10) {
                    rlvContent.setNoMore(true);

                }
            }

            @Override
            public void onError(Response<ProgramContentBean> response) {
                super.onError(response);
                page -= 1;
                if (page > 1) {
                    //暂不处理
                } else if (page == 1 && list.size() < 10) {
                    rlvContent.setNoMore(true);
                } else if (page == 0) {
                    ToastUtils.showBottomToast("获取数据失败!");
                }

            }
        });
    }


    /**
     * 获取课堂数据
     */
    private void getClassData() {

        LastPlayClass lastPlayClass = SQLHelper.getInstance().getLastPlayClass(anchorID);

        Map<String, String> map = new HashMap<>();
        map.put("act_id", anchorID);
        map.put("user_id", LoginUtil.getUserId());
        if (null != lastPlayClass) {
            map.put("number", lastPlayClass.getPosition());
        }
        OkGo.<ClassLastPlayData>post(UrlProvider.CLASS_LAST_PLAY_AND_DATA).params(map).tag(this).execute(new SimpleJsonCallback<ClassLastPlayData>(ClassLastPlayData.class) {
            @Override
            public void onSuccess(Response<ClassLastPlayData> response) {
                ClassLastPlayData bean = response.body();
                int status = bean.getStatus();
                if (status == 1) {
                    List<ClassLastPlayData.ResultsBean> results = bean.getResults();
                    newsList.clear();
                    Gson gson = new Gson();
                    String json = gson.toJson(results);
                    List<ProgramContentBean.ResultsBean> lastPlayList = gson.fromJson(json, new TypeToken<List<ProgramContentBean.ResultsBean>>() {
                    }.getType());
                    List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型
                    page = bean.getPage();
                    contentAdapter.setDataList(lastPlayList);
                    newsList.addAll(tempList);
                    addHead();
                    MediaPlayerInterface newsPlayer = TwApplication.getNewsPlayer();
                    if (null != newsPlayer) {
                        String classActID = newsPlayer.getClassActID();
                        if (null != classActID && classActID.equals(anchorID)) {
                            String playId = newsPlayer.getPlayId();
                            if (!TextUtils.isEmpty(playId)) {
                                contentAdapter.setId(playId);
                            }
                        }
                    }
                    contentAdapter.setNewsList(newsList);
                }

            }

            @Override
            public void onError(Response<ClassLastPlayData> response) {
                super.onError(response);
                page -= 1;
                if (page > 1) {
                    //暂不处理
                } else if (page == 1 && list.size() < 10) {
                    rlvContent.setNoMore(true);
                } else if (page == 0) {
                    ToastUtils.showBottomToast("获取数据失败!");
                }
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        contentAdapter.notifyDataSetChanged();//todo 待确认
    }

    /**
     * 增加播放历史的头部
     */
    private void addHead() {
        CommonHeader head = new CommonHeader(getActivity(), R.layout.header_class_last_play);
        lRecyclerViewAdapter.addHeaderView(head);
        RelativeLayout headItem = (RelativeLayout) head.findViewById(R.id.rl_head);
        headItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LastPlayClass lastPlayClass = SQLHelper.getInstance().getLastPlayClass(anchorID);
                if (null != lastPlayClass) {
                    TwApplication.getNewsPlayer().setNewsList(newsList);
                    TwApplication.getNewsPlayer().setIsPlayLastClass(true, lastPlayClass.getTime());
                    NewsDetailActivity.actionStart(getActivity(), Integer.valueOf(lastPlayClass.getPosition()), AppConfig.CHANNEL_TYPE_CLASS);
                }else{
                   ToastUtils.showBottomToast("暂无历史播放记录!");
                }
            }
        });

    }


    /**
     * 刷新正在播放的新闻标题颜色
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeListenNewsColor(ChangeListenNewsColorEvent event) {
        String channel = event.getChannel();
        if (channel.equals(AppConfig.CHANNEL_TYPE_PART) || channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {
            String id = event.getId();
            contentAdapter.setListeningId(id);
        }

    }


    /**
     * 自动加载更多数据事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeListenNewsColor(LoadMoreNewsEvent event) {
        String channel = event.getChannel();
        if (channel.equals(AppConfig.CHANNEL_TYPE_PART) || channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {
            playPosition = event.getPosition();
            page += 1;
            isAutoLoadMore = true;
            if (channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {
                isClass = true;
            }
            if (isClass) {
                // TODO: 2017/11/24 0024
                loadData(page);
            } else {
                loadData(page);
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(this);
    }
}
