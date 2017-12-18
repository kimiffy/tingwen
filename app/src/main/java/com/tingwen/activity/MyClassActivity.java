package com.tingwen.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.MyClassAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.MyClassBean;
import com.tingwen.bean.PartBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的课堂
 * Created by Administrator on 2017/8/1 0001.
 */
public class MyClassActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_class)
    LRecyclerView rlvClass;

    private List<MyClassBean.ResultsBean> list;
    private List<MyClassBean.ResultsBean> allList;
    private MyClassAdapter classAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private int page = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_class;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        allList = new ArrayList<>();
        classAdapter = new MyClassAdapter(this, list);
    }

    @Override
    protected void initUI() {
        super.initUI();
        rlvClass.setLayoutManager(new LinearLayoutManager(this));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(classAdapter);
        rlvClass.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvClass.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvClass.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvClass.setFooterViewHint("拼命加载中", "---我是有底线的---", "点击重新加载");
        TouchUtil.setTouchDelegate(ivLeft, 50);
        mProgressHUD.show();
        loadData(1);
    }


    @OnClick({R.id.ivLeft})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
        }

    }

    @Override
    protected void setListener() {
        super.setListener();
        rlvClass.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyClassBean.ResultsBean bean = allList.get(position);
                toAnchor(bean, true);
            }
        });

    }

    /**
     * 加载数据
     *
     * @param pageNumber
     */
    private void loadData(int pageNumber) {

        if (!LoginUtil.isUserLogin()) {
            ToastUtils.showBottomToast("您还未登录!");
            return;
        }

        page = pageNumber;
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page", String.valueOf(page));
        map.put("limit", "10");
        OkGo.<MyClassBean>post(UrlProvider.BUY_CLASS).tag(this).params(map, true).execute(new SimpleJsonCallback<MyClassBean>(MyClassBean.class) {
            @Override
            public void onSuccess(Response<MyClassBean> response) {
                try {
                    mProgressHUD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                list = response.body().getResults();
                allList.addAll(list);
                if (page == 1) {
                    classAdapter.setDataList(list);
                } else {
                    classAdapter.addAll(list);
                }
                if (null != rlvClass) {
                    rlvClass.refreshComplete(10);//每页加载数量
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (page > 1 && list.size() < 10) {
                    if (null != rlvClass) {
                        rlvClass.setNoMore(true);
                    }
                }

            }

            @Override
            public void onError(Response<MyClassBean> response) {
                super.onError(response);
                try {
                    mProgressHUD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (page > 1) {
                    if (null != rlvClass) {
                        rlvClass.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                loadData(page);
                            }
                        });
                    }
                } else {
                    ToastUtils.showBottomToast("暂无数据");
                }

            }
        });

    }


    private void toAnchor(MyClassBean.ResultsBean dataBean, Boolean isClass) {
        PartBean partBean = new PartBean();
        partBean.setId(dataBean.getId());
        partBean.setName(dataBean.getName());
        partBean.setDescription(dataBean.getDescription());
        partBean.setFan_num(dataBean.getFan_num() + "");
        partBean.setMessage_num(dataBean.getMessage_num());
        partBean.setImages(dataBean.getImages());
        ProgramDetailActivity.actionStart(this, partBean, isClass, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
