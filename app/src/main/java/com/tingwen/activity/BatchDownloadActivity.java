package com.tingwen.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.BatchDownloadAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.BatchDownloadBean;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.DownLoadUtil;
import com.tingwen.utils.JsonUtil;
import com.tingwen.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 根据搜索批量下载
 * Created by Administrator on 2017/10/25 0025.
 */
public class BatchDownloadActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rlv_batch)
    RecyclerView rlvBatch;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    private String Id = "";
    private String category;
    private String startTime = "";
    private String endTime = "";
    private BatchDownloadAdapter adapter;
    private List<NewsBean> list;//转化成统一的类型 新闻列表
    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;
    private int state=0;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitivity_batch_download;
    }

    @Override
    protected void initData() {
        super.initData();

        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        category = getIntent().getStringExtra("category");
        Id = getIntent().getStringExtra("categoryId");

        list = new ArrayList<>();
        adapter = new BatchDownloadAdapter(this);
        adapter.setDataList(list);

        rlvBatch.setLayoutManager(new LinearLayoutManager(this));
        rlvBatch.setItemAnimator(new DefaultItemAnimator());
        rlvBatch.setAdapter(adapter);

        getData();

    }

    /**
     * 根据条件查询新闻
     */
    private void getData() {
        mProgressHUD.show();
        Map<String, String> map = new HashMap<>();
        String newStartTime = startTime + " 00:00:01";//必须有空格
        String newEndTime = endTime + " 23:59:59";

        map.put("start_time", newStartTime);
        map.put("end_time", newEndTime);
        map.put("term_id", Id);
        OkGo.<BatchDownloadBean>post(UrlProvider.DOWNLOAD_NEWS).params(map, true).tag(this).execute(new SimpleJsonCallback<BatchDownloadBean>(BatchDownloadBean.class) {
            @Override
            public void onSuccess(Response<BatchDownloadBean> response) {
                mProgressHUD.dismiss();
                List<BatchDownloadBean.ResultsBean> resultList = response.body().getResults();
                if(resultList.size()!=0){
                    Gson gson = new Gson();
                    String json = gson.toJson(resultList);
                    list = JsonUtil.toObjectList(json, NewsBean.class);
                    adapter.setDataList(list);
                }
                tvContent.setText((category + " ( " + startTime + " 至 " + endTime + "  共" + list.size() + "条 )"));
            }

            @Override
            public void onError(Response<BatchDownloadBean> response) {
                super.onError(response);
                mProgressHUD.dismiss();
                ToastUtils.showBottomToast("获取数据失败!");
            }
        });


    }


    @OnClick({R.id.ivLeft,R.id.tv_edit,R.id.tv_all,R.id.tv_download})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tv_edit:

                if(state==MODE_NORMAL){
                    if(list.size()!=0){
                        adapter.setEditMode(MODE_EDIT);
                        adapter.notifyDataSetChanged();
                        state=MODE_EDIT;
                        tvEdit.setText("取消");
                        rlBottom.setVisibility(View.VISIBLE);

                    }
                }else if(state==MODE_EDIT){
                    adapter.setEditMode(MODE_NORMAL);
                    adapter.notifyDataSetChanged();
                    state=MODE_NORMAL;
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
        adapter.setOnItemClickListener(new BatchDownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TwApplication.getNewsPlayer().setNewsList(list);
                NewsDetailActivity.actionStart(BatchDownloadActivity.this,position, AppConfig.CHANNEL_TYPE_BATCH_DOWNLOAD);


            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
       OkGo.getInstance().cancelTag(this);
    }


}
