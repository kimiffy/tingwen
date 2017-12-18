package com.tingwen.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.ProgramDownloadAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.ProgramContentBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.DownLoadUtil;
import com.tingwen.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 下载节目
 * Created by Administrator on 2017/10/27 0027.
 */
public class ProgramDownloadActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.rlv_batch)
    LRecyclerView rlvBatch;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private int page = 1;
    private String anchorID;
    private ProgramDownloadAdapter adapter;
    private List<NewsBean> newsList; //统一的新闻对象
    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;
    private int state = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_program_download;
    }

    @Override
    protected void initData() {
        super.initData();

        anchorID = getIntent().getStringExtra("id");//传递过来的ID
        newsList = new ArrayList<>();
        rlvBatch.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProgramDownloadAdapter(this, newsList);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rlvBatch.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvBatch.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvBatch.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvBatch.setFooterViewHint("拼命加载中", "---我是有底线的---", "点击重新加载");


    }


    @Override
    protected void initUI() {
        super.initUI();
        loadData(1);
        mProgressHUD.show();
    }

    @OnClick({R.id.ivLeft, R.id.tv_all,R.id.tv_edit,R.id.tv_download})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tv_edit:
                if (state == MODE_NORMAL) {
                    if (newsList.size() != 0) {
                        adapter.setEditMode(MODE_EDIT);
                        adapter.notifyDataSetChanged();
                        state = MODE_EDIT;
                        tvEdit.setText("取消");
                        rlBottom.setVisibility(View.VISIBLE);

                    }
                } else if (state == MODE_EDIT) {
                    adapter.setEditMode(MODE_NORMAL);
                    adapter.notifyDataSetChanged();
                    state = MODE_NORMAL;
                    tvEdit.setText("编辑");
                    rlBottom.setVisibility(View.GONE);

                }
                break;
            case R.id.tv_all:
                adapter.allSelect();
                break;
            case R.id.tv_download:
                List<NewsBean> downLoadList = adapter.getDownLoadList();

                for (NewsBean bean : downLoadList) {
                    DownLoadBean news = new DownLoadBean();
                    news.setId(bean.getId());
                    news.setPost_mp(bean.getPost_mp());
                    news.setPost_title(bean.getPost_title());
                    news.setSmeta(bean.getSmeta());
                    news.setPost_date(bean.getPost_date());
                    news.setPost_excerpt(bean.getPost_excerpt());
                    news.setPost_size(bean.getPost_size());
                    news.setPost_time(bean.getPost_time());
                    news.setPost_lai(bean.getPost_lai());
                    DownLoadUtil.getInstance().batchDownLoadNews(this,news);

                }
                adapter.setEditMode(MODE_NORMAL);
                adapter.notifyDataSetChanged();
                state=MODE_NORMAL;
                tvEdit.setText("编辑");
                rlBottom.setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        rlvBatch.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        adapter.setOnItemClickListener(new ProgramDownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TwApplication.getNewsPlayer().setNewsList(newsList);
                NewsDetailActivity.actionStart(ProgramDownloadActivity.this,position, AppConfig.CHANNEL_TYPE_BATCH_DOWNLOAD);

            }
        });
    }

    /**
     * 获取数据
     *
     * @param i
     */
    private void loadData(int i) {
        page = i;
        Map<String, String> map = new HashMap<>();
        map.put("ac_id", anchorID);
        map.put("limit", "15");
        map.put("page", String.valueOf(page));

        OkGo.<ProgramContentBean>post(UrlProvider.ACT_NEWS).tag(this).params(map, true).execute(new SimpleJsonCallback<ProgramContentBean>(ProgramContentBean.class) {
            @Override
            public void onSuccess(Response<ProgramContentBean> response) {
                mProgressHUD.dismiss();
                List<ProgramContentBean.ResultsBean> results = response.body().getResults();
                String json = new Gson().toJson(results);
                List<NewsBean> tempList = JsonUtil.toObjectList(json, NewsBean.class);//转换成统一的新闻类型
                if (page == 1) {
                    adapter.setDataList(tempList);
                    newsList.clear();
                    newsList.addAll(tempList);
                } else {
                    adapter.addAll(tempList);
                    newsList.addAll(tempList);
                }
                rlvBatch.refreshComplete(15);//每页加载数量
                lRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response<ProgramContentBean> response) {
                super.onError(response);
                mProgressHUD.dismiss();
                page -= 1;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
