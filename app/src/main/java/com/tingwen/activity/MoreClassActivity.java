package com.tingwen.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.ClassAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.ClassBean;
import com.tingwen.bean.PartBean;
import com.tingwen.event.ClassReLoadEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;

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
 * 更多课堂
 * Created by Administrator on 2017/7/17 0017.
 */
public class MoreClassActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_class)
    LRecyclerView rlvClass;
    @BindView(R.id.llyt_network_error)
    LinearLayout llytNetworkError;


    private List<ClassBean.ResultsBean> list;
    private List<ClassBean.ResultsBean> allList;
    private ClassAdapter classAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private int page = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_more_class;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        allList=new ArrayList<>();
        classAdapter = new ClassAdapter(this, list);
        EventBus.getDefault().register(this);
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
        loadData(1);
        mProgressHUD.show();
    }


    @OnClick({R.id.llyt_network_error, R.id.ivLeft})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.llyt_network_error:
                ToastUtils.showMidToast("重新加载!");
                loadData(page);
                break;
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
                Bundle bundle = new Bundle();
                bundle.putString("act_id",allList.get(position).getId()+"");
                if(LoginUtil.isUserLogin()){
                    if(allList.get(position).getIs_free().equals("1")){
                        toAnchor(allList.get(position),true);
                    }else if(allList.get(position).getIs_free().equals("0")){
                        LauncherHelper.getInstance().launcherActivity(MoreClassActivity.this, AuditionDetailActivity.class,bundle);
                    }
                }else{
                    LauncherHelper.getInstance().launcherActivity(MoreClassActivity.this, AuditionDetailActivity.class,bundle);
                }



            }
        });

    }


    /**
     * 加载数据
     */
    private void loadData(int pageNumber) {
        page = pageNumber;
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page", String.valueOf(page));
        map.put("limit", "15");
        OkGo.<ClassBean>post(UrlProvider.CLASS).params(map, true).execute(new SimpleJsonCallback<ClassBean>(ClassBean.class) {
            @Override
            public void onSuccess(Response<ClassBean> response) {
                mProgressHUD.dismiss();
                list = response.body().getResults();
                allList.addAll(list);
                if (page == 1) {
                    classAdapter.setDataList(list);
                } else {
                    classAdapter.addAll(list);
                }
                if(null!=rlvClass){

                    rlvClass.refreshComplete(15);//每页加载数量
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (page > 1 && list.size() < 15) {
                    if(null!=rlvClass){

                        rlvClass.setNoMore(true);
                    }
                }
            }

            @Override
            public void onError(Response<ClassBean> response) {
                super.onError(response);
                mProgressHUD.dismiss();


                if (page > 1) {
                    if(null!=rlvClass){
                        rlvClass.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                loadData(page);
                            }
                        });
                    }
                } else {
                    if(null!=rlvClass){
                        rlvClass.setEmptyView(llytNetworkError);
                    }
                }

            }
        });

    }

    /**
     * 重新加载事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReloadEvent(ClassReLoadEvent event) {
        allList.clear();
        loadData(1);
    }

    private void toAnchor(ClassBean.ResultsBean dataBean, boolean isClass) {
        PartBean partBean = new PartBean();
        partBean.setId(dataBean.getId());
        partBean.setName(dataBean.getName());
        partBean.setDescription(dataBean.getDescription());
        partBean.setFan_num(dataBean.getFan_num()+"");
        partBean.setMessage_num(dataBean.getMessage_num());
        partBean.setImages(dataBean.getImages());
        ProgramDetailActivity.actionStart(this,partBean,isClass,true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
