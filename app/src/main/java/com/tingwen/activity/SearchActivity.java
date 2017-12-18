package com.tingwen.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.SearchNewsAdapter;
import com.tingwen.adapter.SearchNewsClassAdapter;
import com.tingwen.adapter.SearchNewsPartAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.PartBean;
import com.tingwen.bean.SearchNewsBean;
import com.tingwen.bean.SearchPartBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.JsonUtil;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.widget.CommonHeader;
import com.tingwen.widget.UnScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索新闻
 * Created by Administrator on 2017/10/26 0026.
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.et_search_input_keyword)
    EditText etSearch;

    @BindView(R.id.rlv_search_news)
    LRecyclerView rlvSearchNews;
    @BindView(R.id.iv_search)
    ImageView ivSearch;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<SearchNewsBean.ResultsBean> list;
    private SearchNewsAdapter adapter;
    private int page = 1;//分页
    private String keyword = "";//搜索的关键词
    private List<SearchPartBean.ResultsBean.ActBean> actBeanList;
    private List<SearchPartBean.ResultsBean.LessonBean> classBeanList;
    private SearchNewsPartAdapter partAdapter;
    private SearchNewsClassAdapter classAdapter;
    private UnScrollListView lvPart;
    private UnScrollListView lvClass;
    private List<NewsBean> newsList; //统一的新闻对象
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        actBeanList = new ArrayList<>();
        classBeanList = new ArrayList<>();
        newsList = new ArrayList<>();
        adapter = new SearchNewsAdapter(this, list);
        rlvSearchNews.setLayoutManager(new LinearLayoutManager(this));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rlvSearchNews.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvSearchNews.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvSearchNews.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvSearchNews.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");


    }


    @OnClick({R.id.ivLeft, R.id.et_search_input_keyword, R.id.iv_search})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.iv_search:
                keyword = etSearch.getText().toString();
                if (TextUtils.isEmpty(keyword)) {
                    return;
                }
                page = 1;
                getParts();
                getNews();
                KeyBoardUtils.closeKeyboard(etSearch);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initUI() {
        super.initUI();
        addHead();
    }


    /**
     * 添加头部
     */
    private void addHead() {
        CommonHeader head = new CommonHeader(this, R.layout.header_search_news);
        lRecyclerViewAdapter.addHeaderView(head);
        lvPart = (UnScrollListView) head.findViewById(R.id.lv_part);
        lvClass = (UnScrollListView) head.findViewById(R.id.lv_listen);

        //节目
        partAdapter = new SearchNewsPartAdapter(this, actBeanList);
        lvPart.setAdapter(partAdapter);
        //课堂
        classAdapter = new SearchNewsClassAdapter(this, classBeanList);
        lvClass.setAdapter(classAdapter);


    }

    @Override
    protected void setListener() {
        super.setListener();

        rlvSearchNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                getNews();
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                TwApplication.getNewsPlayer().setNewsList(newsList);

                NewsDetailActivity.actionStart(SearchActivity.this,position, AppConfig.CHANNEL_TYPE_SEARCH_NEWS);
            }

        });


        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = etSearch.getText().toString();
                    if (!TextUtils.isEmpty(keyword)) {
                        page = 1;
                        getParts();
                        getNews();
                        KeyBoardUtils.closeKeyboard(etSearch);
                    }
                }
                return false;
            }
        });

        lvPart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchPartBean.ResultsBean.ActBean bean = actBeanList.get(position);

                PartBean partBean = new PartBean();
                partBean.setId(bean.getId());
                partBean.setName(bean.getName());
                partBean.setDescription(bean.getDescription());
                partBean.setFan_num(bean.getFan_num()+"");
                partBean.setMessage_num(bean.getMessage_num());
                partBean.setImages(bean.getImages());
                ProgramDetailActivity.actionStart(SearchActivity.this,partBean,false,false);
            }
        });

        lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchPartBean.ResultsBean.LessonBean bean = classBeanList.get(position);


                Bundle bundle = new Bundle();
                bundle.putString("act_id", bean.getId()+"");
                if(LoginUtil.isUserLogin()){

                    if(bean.getIs_free().equals("1")){
                        PartBean partBean = new PartBean();
                        partBean.setId(bean.getId());
                        partBean.setName(bean.getName());
                        partBean.setDescription(bean.getDescription());
                        partBean.setFan_num(bean.getFan_num()+"");
                        partBean.setMessage_num(bean.getMessage_num());
                        partBean.setImages(bean.getImages());
                        ProgramDetailActivity.actionStart(SearchActivity.this,partBean,true,true);

                    }else if(bean.getIs_free().equals("0")){
                        LauncherHelper.getInstance().launcherActivity(SearchActivity.this, AuditionDetailActivity.class,bundle);
                    }
                }else{
                    LauncherHelper.getInstance().launcherActivity(SearchActivity.this, AuditionDetailActivity.class,bundle);
                }

            }
        });



    }


    /**
     * 搜索节目
     */
    private void getParts() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("keyword", keyword);
        map.put("limit", "1000000");
        OkGo.<SearchPartBean>post(UrlProvider.SEARCH_PART_CLASS).tag(this).params(map).execute(new SimpleJsonCallback<SearchPartBean>(SearchPartBean.class) {
            @Override
            public void onSuccess(Response<SearchPartBean> response) {
                if (response.body().getStatus() == 1) {
                    actBeanList = response.body().getResults().getAct();
                    classBeanList = response.body().getResults().getLesson();
                    partAdapter.setDataList(actBeanList);
                    classAdapter.setDataList(classBeanList);

                }

            }

            @Override
            public void onError(Response<SearchPartBean> response) {
                super.onError(response);

            }
        });

    }


    /**
     * 获取新闻数据
     */
    private void getNews() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("keyword", keyword);
        map.put("page", String.valueOf(page));

        OkGo.<SearchNewsBean>post(UrlProvider.NEWS_LIST).params(map).tag(this).execute(new SimpleJsonCallback<SearchNewsBean>(SearchNewsBean.class) {
            @Override
            public void onSuccess(Response<SearchNewsBean> response) {
                List<SearchNewsBean.ResultsBean> results = response.body().getResults();
                String json = new Gson().toJson(results);
                List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型

                if (page == 1) {
                    adapter.setDataList(results);
                    newsList.clear();
                    newsList.addAll(tempList);

                } else {
                    adapter.addAll(results);
                    newsList.addAll(tempList);
                }
                if(null!=rlvSearchNews){
                    rlvSearchNews.refreshComplete(10);//每页加载数量
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<SearchNewsBean> response) {
                super.onError(response);
            }
        });


    }



    //通过判断事件分发处理 点击软键盘以外的区域 关闭软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight();
            if (event.getX() > left && event.getY() > top && event.getY() < bottom) {
                // 保留点击输入框(et和 send)的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

}
