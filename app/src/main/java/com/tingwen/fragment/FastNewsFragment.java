package com.tingwen.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.tingwen.activity.ClassificationActivity;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.adapter.FastNewsAdapter;
import com.tingwen.app.AppSpUtil;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseLazyFragment;
import com.tingwen.bean.FastNewsADBean;
import com.tingwen.bean.FastNewsBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ChangeListenNewsColorEvent;
import com.tingwen.event.LoadMoreNewsEvent;
import com.tingwen.event.LoadMoreNewsReloadUiEvent;
import com.tingwen.interfaces.MediaPlayerInterface;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.JsonUtil;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SizeUtil;
import com.tingwen.widget.CommonHeader;
import com.tingwen.widget.FastNewsBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * 快讯Fragment
 * Created by Administrator on 2017/8/2 0002.
 */
public class FastNewsFragment extends BaseLazyFragment {
    @BindView(R.id.rlv_fast)
    LRecyclerView rlvFast;
    private RelativeLayout tvCaijing, tvWenti, tvGuoji, tvKeji, tvShizheng;
    private ViewPager mViewPager;
    private List<FastNewsBean.ResultsBean> list;
    private List<NewsBean> newsList; //统一的新闻对象
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private FastNewsAdapter fastNewsAdapter;
    private int page;
    private int refreshTimes =0;//广告请求次数
    private FragmentManager mFragmentManager;
    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;
    private CommonHeader headerAd;
    private List<FastNewsADBean.ResultsBean> adList;
    private boolean isDragging = false;
    private Timer mTimer;//定时器
    private Handler mHandler = new Handler();
    private boolean  isAutoLoadMore =false;//是否是自动加载更多
    private int playPosition;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_fast_news;
    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            if (list.isEmpty()) {
                getADFromLocal();
                loadData(1);
            }
        }


    }

    @Override
    protected void setListener() {
        super.setListener();

        rlvFast.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData(page);

                if(refreshTimes==0){
                    getAdFromNet();
                }
                if(refreshTimes ==2){//减少广告请求次数
                    getAdFromNet();
                    refreshTimes =1;
                }
                refreshTimes += 1;

            }
        });

        rlvFast.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MediaPlayerInterface newsPlayer = TwApplication.getNewsPlayer();
                if(null!=newsPlayer){
                    TwApplication.getNewsPlayer().setNewsList(newsList);
                    NewsDetailActivity.actionStart(getActivity(),position, AppConfig.CHANNEL_TYPE_FAST_NEWS);
                }
            }

        });

    }

    @Override
    protected void initData() {
        super.initData();

        list = new ArrayList<>();
        newsList = new ArrayList<>();
        adList = new ArrayList<>();

        fastNewsAdapter = new FastNewsAdapter(getActivity(), list);
        rlvFast.setLayoutManager(new LinearLayoutManager(getActivity()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(fastNewsAdapter);

        rlvFast.setAdapter(lRecyclerViewAdapter);
        rlvFast.setRefreshProgressStyle(ProgressStyle.BallPulse); //设置下拉刷新Progress的样式
        rlvFast.setArrowImageView(R.drawable.arrow);  //设置下拉刷新箭头
        //设置头部部加载颜色
        rlvFast.setHeaderViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载颜色
        rlvFast.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvFast.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");
        EventBus.getDefault().register(this);

    }


    @Override
    protected void initUI() {
        super.initUI();
        addADHead();
        addButtonHead();
    }

    /**
     * 添加头部按钮
     */
    private void addButtonHead() {
        CommonHeader headerButtons = new CommonHeader(getActivity(), R.layout.header_button_container);
        lRecyclerViewAdapter.addHeaderView(headerButtons);
        tvCaijing = (RelativeLayout) headerButtons.findViewById(R.id.tvCaijing);
        tvWenti = (RelativeLayout) headerButtons.findViewById(R.id.tvWenti);
        tvGuoji = (RelativeLayout) headerButtons.findViewById(R.id.tvGuoji);
        tvKeji = (RelativeLayout) headerButtons.findViewById(R.id.tvKeji);
        tvShizheng = (RelativeLayout) headerButtons.findViewById(R.id.tvShizheng);
        initClick();

    }

    /**
     * 添加广告
     */
    private void addADHead() {
        headerAd = new CommonHeader(getActivity(), R.layout.fragment_fast_news_ad);
        lRecyclerViewAdapter.addHeaderView(headerAd);
        mViewPager = (ViewPager) headerAd.findViewById(R.id.pager);

    }

    /**
     * 初始化header点击事件
     */
    private void initClick() {
        tvCaijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id","6");
                LauncherHelper.getInstance().launcherActivity(getActivity(), ClassificationActivity.class,bundle);
            }
        });
        tvWenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id","4");
                LauncherHelper.getInstance().launcherActivity(getActivity(), ClassificationActivity.class,bundle);

            }
        });
        tvGuoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id","8");
                LauncherHelper.getInstance().launcherActivity(getActivity(), ClassificationActivity.class,bundle);
            }
        });
        tvKeji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id","7");
                LauncherHelper.getInstance().launcherActivity(getActivity(), ClassificationActivity.class,bundle);
            }
        });
        tvShizheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id","14");
                LauncherHelper.getInstance().launcherActivity(getActivity(), ClassificationActivity.class,bundle);
            }
        });


    }


    /**
     * 加载数据
     *
     * @param pageNumber
     */
    private void loadData(int pageNumber) {
        page = pageNumber;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page", String.valueOf(page));
        map.put("limit", "15");

        OkGo.<FastNewsBean>post(UrlProvider.FAST_NEWS).tag(this).params(map, true).execute(new SimpleJsonCallback<FastNewsBean>(FastNewsBean.class) {
            @Override
            public void onSuccess(Response<FastNewsBean> response) {
                list = response.body().getResults();
                String json = new Gson().toJson(list);
                List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型
                if (page == 1) {
                    fastNewsAdapter.setDataList(list);
                    newsList.clear();
                    newsList.addAll(tempList);
                    startTimer();
                } else {
                    fastNewsAdapter.addAll(list);
                    newsList.addAll(tempList);
                }

                if(isAutoLoadMore){//自动加载更多数据后 重新设置播放列表和播放位置;
                    isAutoLoadMore=false;
                    playPosition+=1;
                    TwApplication.getNewsPlayer().setNewsList(newsList);
                    TwApplication.getNewsPlayer().playNews(playPosition);
                    EventBus.getDefault().post(new LoadMoreNewsReloadUiEvent(playPosition,AppConfig.CHANNEL_TYPE_FAST_NEWS));
                }
                if(null!=rlvFast){

                    rlvFast.refreshComplete(15);//每页加载数量
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (page > 1 && list.size() < 15&&null!=rlvFast) {
                    rlvFast.setNoMore(true);
                }

            }

            @Override
            public void onError(Response<FastNewsBean> response) {
                super.onError(response);

                if(page ==1){
                    startTimer();// TODO: 2017/8/4 0004 在获取本地新闻后再开启
                }

                if (page > 1) {
                    if(null!=rlvFast){

                        rlvFast.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
     * 获取广告
     */
    private void getAdFromNet() {
        mFragmentStatePagerAdapter = null;
        mFragmentManager = null;
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        OkGo.<FastNewsADBean>post(UrlProvider.FAST_AD_LIST).tag(this).params(map,true).execute(new SimpleJsonCallback<FastNewsADBean>(FastNewsADBean.class) {
            @Override
            public void onSuccess(Response<FastNewsADBean> response) {

                int status = response.body().getStatus();
                if(status==1){
                    adList = response.body().getResults();
                    if(adList.size()>0){
                        initADView();
                    }else{
                        if (null != mTimer) {
                            mTimer.cancel();
                            mTimer = null;
                        }
                        removeADView();

                    }
                    AppSpUtil.getInstance().saveADResult(response.body());
                }

            }

            @Override
            public void onError(Response<FastNewsADBean> response) {
                super.onError(response);
                if (null != mTimer) {
                    mTimer.cancel();
                    mTimer = null;
                }

            }
        });

    }

    /**
     * 设置广告数据(本地)
     */
    private void getADFromLocal() {
        adList = AppSpUtil.getInstance().getFastNewsAD();
        if(adList!=null){
            mFragmentStatePagerAdapter = null;
            mFragmentManager = null;
            initADView();
        }else{
            getAdFromNet();
        }
    }



    /**
     * 设置显示广告
     */
    private void initADView() {
        if (mFragmentManager == null) {
            mFragmentManager = getChildFragmentManager();
        }

        if (mViewPager == null) {
            mViewPager = (ViewPager) headerAd.findViewById(R.id.pager);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (SizeUtil.getScreenWidth() * 0.545);
        mViewPager.setLayoutParams(layoutParams);


        mFragmentStatePagerAdapter = new FragmentStatePagerAdapter(mFragmentManager) {

            @Override
            public int getCount() {
                if (adList.size() > 1) {
                    return 10000;
                } else {
                    return adList.size();
                }
            }

            @Override
            public Fragment getItem(int arg0) {
                final int position = arg0 % adList.size();
                FastNewsBanner banner = new FastNewsBanner();
                banner.setFastNewsAD(adList.get(position));
                return banner;
            }

            @Override
            public Object instantiateItem(ViewGroup arg0, int arg1) {
                final int position = arg1 % adList.size();
                FastNewsBanner banner = (FastNewsBanner) super.instantiateItem(arg0, arg1);
                banner.setFastNewsAD(adList.get(position));
                return banner;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);

            }
        };
        mViewPager.setAdapter(mFragmentStatePagerAdapter);
        if (adList.size() > 1) {
            mViewPager.setCurrentItem(5000);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

                switch (arg0) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isDragging = true;
                        break;

                    case ViewPager.SCROLL_STATE_IDLE:
                        isDragging = false;
                        break;
                    default:
                        break;
                }

            }
        });




    }

    /**
     * 移除广告
     */
    private void removeADView() {
        if (mViewPager == null) {
            mViewPager = (ViewPager) headerAd.findViewById(R.id.pager);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = 0;
        mViewPager.setLayoutParams(layoutParams);
    }


    /**
     * 广告轮播定时器
     */
    private void initTimer() {
        if (adList!=null&&adList.size() > 1) {
            if (null != mTimer) {
                mTimer.cancel();
                mTimer = null;
            }
            mTimer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                    if (!isDragging) {
                        if (mViewPager == null) {
                            return;
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mViewPager != null) {
                                    int position = mViewPager.getCurrentItem() + 1;
                                    if (position >= 9999) {
                                        position = 5001;
                                        mViewPager.setCurrentItem(position, false);
                                    } else {
                                        mViewPager.setCurrentItem(position);
                                    }
                                }
                            }
                        });
                    }
                }
            };

            mTimer.schedule(timerTask, 0, 10000);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(list==null){
            return;
        }
        if (getUserVisibleHint()&&list.size()>0) {
            startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        shutTimer();

    }

    /**
     * 暂停定时
     */
    private void shutTimer() {
        if(null!=mTimer){
            mTimer.cancel();
        }
    }

    /**
     * 开启定时
     */
    private void startTimer() {
        initTimer();
    }


    /**
     * 刷新正在播放的新闻标题颜色
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeListenNewsColor(ChangeListenNewsColorEvent event) {
        String channel = event.getChannel();
        fastNewsAdapter.setListeningId("");
        if(null!=channel&&channel.equals(AppConfig.CHANNEL_TYPE_FAST_NEWS)){
            String id = event.getId();
            fastNewsAdapter.setListeningId(id);
        }

    }


    /**
     * 自动加载更多数据事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeListenNewsColor(LoadMoreNewsEvent event) {
        String channel = event.getChannel();
        if(channel.equals(AppConfig.CHANNEL_TYPE_FAST_NEWS)){
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


