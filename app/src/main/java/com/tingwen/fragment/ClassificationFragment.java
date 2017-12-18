package com.tingwen.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.adapter.ClassificationAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.ClassificationBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ChangeListenNewsColorEvent;
import com.tingwen.event.LoadMoreNewsEvent;
import com.tingwen.event.LoadMoreNewsReloadUiEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.JsonUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * 分类新闻Fragment
 * Created by Administrator on 2017/8/4 0004.
 */
public class ClassificationFragment extends BaseFragment {


    private List<ClassificationBean.ResultsBean> list;
    private List<NewsBean> newsList; //统一的新闻对象
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private ClassificationAdapter adapter;
    private int page = 1;
    private String id;
    private boolean  isAutoLoadMore =false;//是否是自动加载更多
    private int playPosition;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_classification;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        newsList = new ArrayList<>();
        adapter = new ClassificationAdapter(getActivity(), list);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
        EventBus.getDefault().register(this);

    }

    @Override
    protected void findViewById(View container) {
        super.findViewById(container);
        if (pullRv != null) {
            pullRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    protected void initUI() {
        super.initUI();
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        if (pullRv != null) {
            pullRv.setAdapter(lRecyclerViewAdapter);
            pullRv.setRefreshProgressStyle(ProgressStyle.BallPulse); //设置下拉刷新Progress的样式
            pullRv.setArrowImageView(R.drawable.arrow);  //设置下拉刷新箭头
            //设置头部部加载颜色
            pullRv.setHeaderViewColor(R.color.blue, R.color.grey, android.R.color.white);
            //设置底部加载颜色
            pullRv.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
            //设置底部加载文字提示
            pullRv.setFooterViewHint("拼命加载中", "---我是有底线的---", "点击重新加载");
        }
        loadData(1);
    }

    @Override
    protected void setListener() {
        super.setListener();
        if (pullRv != null) {
            pullRv.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    page = 1;
                    loadData(page);

                }
            });

            pullRv.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    page += 1;
                    loadData(page);
                }
            });

        }

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TwApplication.getNewsPlayer().setNewsList(newsList);
                NewsDetailActivity.actionStart(getActivity(), position, AppConfig.CHANNEL_TYPE_CLASSIFICATION);

            }

        });

    }

    @OnClick({R.id.iv_network_error})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_network_error:
                loadData(page);
                break;

        }
    }

    /**
     * 加载数据
     *
     * @param pageNumber
     */
    private void loadData(int pageNumber) {

        page = pageNumber;
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page", String.valueOf(page));
        map.put("term_id", id);
        OkGo.<ClassificationBean>post(UrlProvider.NEWS_LIST).tag(this).params(map, true).execute(new SimpleJsonCallback<ClassificationBean>(ClassificationBean.class) {
            @Override
            public void onSuccess(Response<ClassificationBean> response) {
                if (response.body().getStatus() == 1) {
                    list = response.body().getResults();
                    String json = new Gson().toJson(list);
                    List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型

                    if (page == 1) {
                        if (!list.isEmpty()) {
                            showContent();
                            newsList.clear();
                            newsList.addAll(tempList);
                            adapter.setDataList(list);
                        } else {
                            showEmpty();
                        }

                    } else {
                        adapter.addAll(list);
                        newsList.addAll(tempList);
                    }

                    if(isAutoLoadMore){//自动加载更多数据后 重新设置播放列表和播放位置;
                        isAutoLoadMore=false;
                        playPosition+=1;
                        TwApplication.getNewsPlayer().setNewsList(newsList);
                        TwApplication.getNewsPlayer().playNews(playPosition);
                        EventBus.getDefault().post(new LoadMoreNewsReloadUiEvent(playPosition,AppConfig.CHANNEL_TYPE_CLASSIFICATION));
                    }


                    if (null!=pullRv) {
                        pullRv.refreshComplete(10);//每页加载数量
                    }
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    if (page > 1 && list.size() < 10) {
                        pullRv.setNoMore(true);
                    }

                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }

            }

            @Override
            public void onError(Response<ClassificationBean> response) {
                super.onError(response);
                if (page == 1) {
                    ToastUtils.showBottomToast("获取数据异常!");
                    showError();
                } else {
                    if (pullRv != null) {
                        pullRv.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                loadData(page);
                            }
                        });
                    }
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
        adapter.setListeningId("");
        if (channel.equals(AppConfig.CHANNEL_TYPE_CLASSIFICATION)) {
            String id = event.getId();
            adapter.setListeningId(id);
        }

    }

    /**
     * 自动加载更多数据事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeListenNewsColor(LoadMoreNewsEvent event) {
        String channel = event.getChannel();
        if(channel.equals(AppConfig.CHANNEL_TYPE_CLASSIFICATION)){
            playPosition = event.getPosition();
            page += 1;
            isAutoLoadMore=true;
            loadData(page);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }
}
