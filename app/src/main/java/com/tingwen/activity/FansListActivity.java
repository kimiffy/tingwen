package com.tingwen.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.FansAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.FansBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 粉丝列表
 * Created by Administrator on 2017/9/26 0026.
 */
public class FansListActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rlv_fans)
    LRecyclerView rlvFans;
    private String name, user_login;
    private int page = 1;
    private List<FansBean.ResultsBean> list;
    private List<FansBean.ResultsBean> allList;
    private FansAdapter fansAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitivity_fans_list;
    }

    @Override
    protected void initData() {
        super.initData();
        name = getIntent().getStringExtra("name");
        user_login = getIntent().getStringExtra("user_login");
        list = new ArrayList<>();
        allList = new ArrayList<>();
        rlvFans.setLayoutManager(new LinearLayoutManager(this));
        fansAdapter = new FansAdapter(this, list);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(fansAdapter);
        rlvFans.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvFans.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvFans.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvFans.setFooterViewHint("拼命加载中", "---我是有底线的---", "点击重新加载");


    }

    @Override
    protected void initUI() {
        super.initUI();
        if (!TextUtils.isEmpty(name)) {
            tvTitle.setText(name + "的粉丝列表");
        }
        mProgressHUD.show();
        loadData(1);
    }

    @Override
    protected void setListener() {
        super.setListener();
        rlvFans.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FansBean.ResultsBean bean = allList.get(position);
                // TODO: 2017/9/26 0026 这里拿的 uid才是好友id  和关注不一致  关注列表拿的 friend id ,后期最好统一
                String id = bean.getUid();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                LauncherHelper.getInstance().launcherActivity(FansListActivity.this, PersonalHomePageActivity.class,bundle);

            }
        });

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData(final int page) {
        Map<String, String> map = new HashMap<>();
        try {
            if (!TextUtils.isEmpty(user_login)) {
                String code = LoginUtil.encode(LoginUtil.AESCODE, user_login.getBytes());
                map.put("accessToken", code);
            }
            map.put("page", String.valueOf(page));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGo.<FansBean>post(UrlProvider.FANS_LIST).tag(this).params(map, true).execute(new SimpleJsonCallback<FansBean>(FansBean.class) {
            @Override
            public void onSuccess(Response<FansBean> response) {
                mProgressHUD.dismiss();
                int status = response.body().getStatus();
                if (status == 1) {
                    list = response.body().getResults();
                    allList.addAll(list);
                    if (page == 1) {
                        fansAdapter.setDataList(list);
                    } else {
                        fansAdapter.addAll(list);
                    }
                    if(null!=rlvFans){
                        rlvFans.refreshComplete(10);//每页加载数量
                    }
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    if (page > 1 && list.size() < 10) {
                        rlvFans.setNoMore(true);
                    }
                }
            }

            @Override
            public void onError(Response<FansBean> response) {
                super.onError(response);
                mProgressHUD.dismiss();

            }
        });

    }



}
