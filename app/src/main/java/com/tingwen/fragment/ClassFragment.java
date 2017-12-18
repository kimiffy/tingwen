package com.tingwen.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.tingwen.R;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.activity.AuditionDetailActivity;
import com.tingwen.activity.ProgramDetailActivity;
import com.tingwen.adapter.ClassAdapter;
import com.tingwen.base.BaseLazyFragment;
import com.tingwen.bean.ClassBean;
import com.tingwen.bean.PartBean;
import com.tingwen.event.ClassReLoadEvent;
import com.tingwen.event.LoginSuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LauncherHelper;
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
 * 课堂
 * Created by Administrator on 2017/7/7 0007.
 */
public class ClassFragment extends BaseLazyFragment {

    @BindView(R.id.rlv_class)
    LRecyclerView rlvClass;
    private int page = 1;
    private ClassAdapter classAdapter;
    private List<ClassBean.ResultsBean> list;
    private List<ClassBean.ResultsBean> allList;
    private LRecyclerViewAdapter lRecyclerViewAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fargment_class;
    }


    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        allList = new ArrayList<>();
        classAdapter = new ClassAdapter(getActivity(), list);
        rlvClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(classAdapter);
        rlvClass.setAdapter(lRecyclerViewAdapter);
        rlvClass.setRefreshProgressStyle(ProgressStyle.BallPulse); //设置下拉刷新Progress的样式
        rlvClass.setArrowImageView(R.drawable.arrow);  //设置下拉刷新箭头
        //设置头部部加载颜色
        rlvClass.setHeaderViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载颜色
        rlvClass.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvClass.setFooterViewHint("拼命加载中", "---我是有底线的---", "点击重新加载");
        EventBus.getDefault().register(this);

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
    protected void setListener() {
        super.setListener();


        rlvClass.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                allList.clear();
                page = 1;
                loadData(page);

            }
        });

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
                if(null==allList||allList.size()==0){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("act_id", allList.get(position).getId()+"");

                if(LoginUtil.isUserLogin()){
                    if(allList.get(position).getIs_free().equals("1")){
                        toAnchor(allList.get(position),true);
                    }else if(allList.get(position).getIs_free().equals("0")){
                        ClassBean.ResultsBean bean = allList.get(position);
                        PartBean part = new PartBean();
                        part.setId(bean.getId());
                        part.setName(bean.getName());
                        part.setDescription(bean.getDescription());
                        part.setImages(bean.getImages());
                        part.setFan_num(bean.getFan_num()+"");
                        part.setMessage_num(bean.getMessage_num());
                        bundle.putSerializable("part",part);


                        LauncherHelper.getInstance().launcherActivity(getActivity(), AuditionDetailActivity.class,bundle);
                    }
                }else{
                    ClassBean.ResultsBean bean = allList.get(position);
                    PartBean part = new PartBean();
                    part.setId(bean.getId());
                    part.setName(bean.getName());
                    part.setDescription(bean.getDescription());
                    part.setImages(bean.getImages());
                    part.setFan_num(bean.getFan_num()+"");
                    part.setMessage_num(bean.getMessage_num());
                    bundle.putSerializable("part",part);
                    LauncherHelper.getInstance().launcherActivity(getActivity(), AuditionDetailActivity.class,bundle);
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
                list = response.body().getResults();
                allList.addAll(list);
                if(page==1){
                    classAdapter.setDataList(list);
                }else{
                    classAdapter.addAll(list);
                }
                if(null!=rlvClass){
                    rlvClass.refreshComplete(15);//每页加载数量
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (page > 1 && list.size() < 15) {
                    rlvClass.setNoMore(true);
                }
            }

            @Override
            public void onError(Response<ClassBean> response) {
                super.onError(response);
                if(page>1){
                    rlvClass.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            loadData(page);
                        }
                    });
                }

            }
        });


    }

    /**
     * 前往节目(主播)详情
     * @param dataBean
     * @param isClass
     */
    private void toAnchor(ClassBean.ResultsBean dataBean, Boolean isClass) {
        PartBean partBean = new PartBean();
        partBean.setId(dataBean.getId());
        partBean.setName(dataBean.getName());
        partBean.setDescription(dataBean.getDescription());
        partBean.setFan_num(dataBean.getFan_num()+"");
        partBean.setMessage_num(dataBean.getMessage_num());
        partBean.setImages(dataBean.getImages());
        ProgramDetailActivity.actionStart(getActivity(),partBean,isClass,true);
    }

    /**
     * 登录成功事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        allList.clear();
        loadData(1);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
