package com.tingwen.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.adapter.ColumnAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseLazyFragment;
import com.tingwen.bean.ColumnNewsBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ChangeListenNewsColorEvent;
import com.tingwen.event.LoadMoreNewsEvent;
import com.tingwen.event.LoadMoreNewsReloadUiEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.JsonUtil;
import com.tingwen.utils.LoginUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 专栏
 * Created by Administrator on 2017/7/11 0011.
 */
public class ColumnFragment extends BaseLazyFragment {
    @BindView(R.id.rlv_column)
    LRecyclerView rlvColumn;
    private int page = 1;
    private List<ColumnNewsBean.ResultsBean> list;
    private List<NewsBean> newsList; //统一的新闻对象
    private ColumnAdapter columnAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private int playPosition;
    private boolean  isAutoLoadMore =false;//是否是自动加载更多
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_column;
    }

    @Override
    protected void initUI() {
        super.initUI();

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            if (list.isEmpty()) {
                loadData(1);
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();

        list = new ArrayList<>();
        newsList = new ArrayList<>();
        columnAdapter = new ColumnAdapter(getActivity(), list);
        rlvColumn.setLayoutManager(new LinearLayoutManager(getActivity()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(columnAdapter);
        rlvColumn.setAdapter(lRecyclerViewAdapter);
        rlvColumn.setRefreshProgressStyle(ProgressStyle.BallPulse); //设置下拉刷新Progress的样式
        rlvColumn.setArrowImageView(R.drawable.arrow);  //设置下拉刷新箭头
        //设置头部部加载颜色
        rlvColumn.setHeaderViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载颜色
        rlvColumn.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvColumn.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");
        EventBus.getDefault().register(this);

    }
    @Override
    protected void setListener() {
        super.setListener();


        rlvColumn.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData(page);

            }
        });

        rlvColumn.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TwApplication.getNewsPlayer().setNewsList(newsList);
                NewsDetailActivity.actionStart(getActivity(),position, AppConfig.CHANNEL_TYPE_COLUMN_NEWS);

            }

        });


    }

    private void loadData(int i) {
        page = i;
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page", String.valueOf(page));
        map.put("limit", "15");
        OkGo.<ColumnNewsBean>post(UrlProvider.COLUMN).params(map, true).tag(this).execute(new SimpleJsonCallback<ColumnNewsBean>(ColumnNewsBean.class) {
            @Override
            public void onSuccess(Response<ColumnNewsBean> response) {

                list = response.body().getResults();
                String json = new Gson().toJson(list);
                List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型

                if(page==1){
                    columnAdapter.setDataList(list);
                    newsList.clear();
                    newsList.addAll(tempList);
                }else{
                    columnAdapter.addAll(list);
                    newsList.addAll(tempList);
                }
                if(isAutoLoadMore){//自动加载更多数据后 重新设置播放列表和播放位置;
                    isAutoLoadMore=false;
                    playPosition+=1;
                    TwApplication.getNewsPlayer().setNewsList(newsList);
                    TwApplication.getNewsPlayer().playNews(playPosition);
                    EventBus.getDefault().post(new LoadMoreNewsReloadUiEvent(playPosition,AppConfig.CHANNEL_TYPE_COLUMN_NEWS));
                }
                if(null!=rlvColumn){
                    rlvColumn.refreshComplete(15);//每页加载数量
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (page > 1 && list.size() < 15) {
                    rlvColumn.setNoMore(true);

                }
            }

            @Override
            public void onError(Response<ColumnNewsBean> response) {
                super.onError(response);
                if(page>1){
                    if(null!=rlvColumn){
                        rlvColumn.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
        columnAdapter.setListeningId("");
        if (channel.equals(AppConfig.CHANNEL_TYPE_COLUMN_NEWS)) {
            String id = event.getId();
            columnAdapter.setListeningId(id);
        }

    }

    /**
     * 自动加载更多数据事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeListenNewsColor(LoadMoreNewsEvent event) {
        String channel = event.getChannel();
        if(channel.equals(AppConfig.CHANNEL_TYPE_COLUMN_NEWS)){
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
