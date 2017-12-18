package com.tingwen.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.CollectionAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.CollectionBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ChangeListenNewsColorEvent;
import com.tingwen.event.LoadMoreNewsEvent;
import com.tingwen.event.LoadMoreNewsReloadUiEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.JsonUtil;
import com.tingwen.utils.LoadMoreView;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的收藏
 * Created by Administrator on 2017/7/31 0031.
 */
public class CollectionActivity extends BaseActivity {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_collection)
    SwipeMenuRecyclerView rlvCollection;
    private List<CollectionBean.ResultsBean> list;
    private List<CollectionBean.ResultsBean> AllList;
    private List<NewsBean> newsList; //统一的新闻对象
    private CollectionAdapter adapter;
    private  int page=1;
    private boolean  isAutoLoadMore =false;//是否是自动加载更多
    private int playPosition;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        AllList = new ArrayList<>();
        newsList = new ArrayList<>();

        mProgressHUD.show();
        rlvCollection.setLayoutManager(new LinearLayoutManager(this));

        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(CollectionActivity.this);
                deleteItem.setText("     删除     ")
                        .setBackground(R.color.red)
                        .setTextColor(getResources().getColor(R.color.white))
                        .setHeight(height);

                rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。
            }
        };
        rlvCollection.setSwipeMenuCreator(mSwipeMenuCreator);//设置监听器
        adapter = new CollectionAdapter(this, list);
        LoadMoreView loadMoreView = new LoadMoreView(this);
        rlvCollection.addFooterView(loadMoreView);
        rlvCollection.setLoadMoreView(loadMoreView);
        rlvCollection.setAdapter(adapter);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void setListener() {
        super.setListener();

        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();

                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int position = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。

                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                    deleteCollection(position);
                    AllList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        rlvCollection.setSwipeMenuItemClickListener(mMenuItemClickListener);//设置菜单点击监听

        rlvCollection.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                TwApplication.getNewsPlayer().setNewsList(newsList);
                NewsDetailActivity.actionStart(CollectionActivity.this,position, AppConfig.CHANNEL_TYPE_COLLECTION);
            }
        });

        SwipeMenuRecyclerView.LoadMoreListener loadMoreListener =  new SwipeMenuRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                page+=1;
                loadData(page);

            }
        };
        rlvCollection.setLoadMoreListener(loadMoreListener); // 加载更多的监听。

    }

    @Override
    protected void initUI() {
        super.initUI();
        loadData(1);
        TouchUtil.setTouchDelegate(ivLeft,50);
    }

    @OnClick({R.id.ivLeft,})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ivLeft:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据
     */
    private void loadData(final int page) {
        if(!LoginUtil.isUserLogin()){
            ToastUtils.showBottomToast("您还未登录!");
            mProgressHUD.dismiss();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page",page+"");
        map.put("limit", "10");
        OkGo.<CollectionBean>post(UrlProvider.COLLECTION).tag(this).params(map, true).execute(new SimpleJsonCallback<CollectionBean>(CollectionBean.class) {

            @Override
            public void onSuccess(Response<CollectionBean> response) {
                mProgressHUD.dismiss();
                int status = response.body().getStatus();
                if (status == 1) {
                    list = response.body().getResults();
                    AllList.addAll(list);
                    String json = new Gson().toJson(list);
                    List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型

                    adapter.setDataList(AllList);
                    newsList.addAll(tempList);
                    rlvCollection.loadMoreFinish(false, true);
                    // TODO: 2017/12/1 0001 验证 收藏自动加载更多!
                    if(isAutoLoadMore){//自动加载更多数据后 重新设置播放列表和播放位置;
                        isAutoLoadMore=false;
                        playPosition+=1;
                        TwApplication.getNewsPlayer().setNewsList(newsList);
                        TwApplication.getNewsPlayer().playNews(playPosition);
                        EventBus.getDefault().post(new LoadMoreNewsReloadUiEvent(playPosition,AppConfig.CHANNEL_TYPE_COLLECTION));
                    }


                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<CollectionBean> response) {
                super.onError(response);
                mProgressHUD.dismiss();
                boolean netWorkConnected = NetUtil.isHasNetAvailable(CollectionActivity.this);
                if (!netWorkConnected) {
                    ToastUtils.showBottomToast("无网络连接");
                    return;
                }
                ToastUtils.showBottomToast("暂无数据");
            }
        });

    }

    /**
     * 删除收藏
     *
     * @param position
     */
    private void deleteCollection(int position) {
        String id = AllList.get(position).getPost_id();
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("post_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_COLLECTION).tag(this).params(map,true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){

                }else{
                    ToastUtils.showBottomToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                boolean netWorkConnected = NetUtil.isHasNetAvailable(CollectionActivity.this);
                if (!netWorkConnected) {
                    ToastUtils.showBottomToast("无网络连接");
                    return;
                }
                ToastUtils.showBottomToast("删除失败,请稍后重试");
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
        if(channel.equals(AppConfig.CHANNEL_TYPE_COLLECTION)){
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
        if(channel.equals(AppConfig.CHANNEL_TYPE_COLLECTION)){
            playPosition = event.getPosition();
            page += 1;
            isAutoLoadMore=true;
            loadData(page);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }
}
