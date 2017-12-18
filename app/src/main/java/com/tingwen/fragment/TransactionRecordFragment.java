package com.tingwen.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.ConsumeAdapter;
import com.tingwen.adapter.RechargeRecordAdapter;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.ConsumeBean;
import com.tingwen.bean.RechargeRecordBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LoginUtil;
import com.tingwen.widget.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 交易记录
 * Created by Administrator on 2017/11/7 0007.
 */
public class TransactionRecordFragment extends BaseFragment {

    @BindView(R.id.rlv_record)
    LRecyclerView rlvRecord;

    private int type = 1;//1代表消费记录 2代表充值记录
    private int page = 1;
    private List<ConsumeBean.ResultsBean> consumelist;
    private List<RechargeRecordBean.ResultsBean> rechargeRecordList;
    private ConsumeAdapter consumeAdapter;
    private RechargeRecordAdapter rechargeRecordAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    @Override
    protected int getLayoutResId() {
        return R.layout.fargment_transaction;
    }

    @Override
    protected void initData() {
        super.initData();
        if(getArguments() != null) {
            type = getArguments().getInt("type");

        }
        if(type == 1){
            consumelist = new ArrayList<>();
            consumeAdapter = new ConsumeAdapter(getActivity(), consumelist);
            lRecyclerViewAdapter = new LRecyclerViewAdapter(consumeAdapter);
            rlvRecord.setAdapter(lRecyclerViewAdapter);

        }else{
            rechargeRecordList = new ArrayList<>();
            rechargeRecordAdapter = new RechargeRecordAdapter(getActivity(), rechargeRecordList);
            lRecyclerViewAdapter = new LRecyclerViewAdapter(rechargeRecordAdapter);
            rlvRecord.setAdapter(lRecyclerViewAdapter);


        }

        rlvRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
        //禁止下拉刷新
        rlvRecord.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvRecord.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvRecord.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");

    }


    @Override
    protected void initUI() {
        super.initUI();
        if(type == 1){
            getConsume();
        }else{
            getChongzhi();
        }

    }

    @Override
    protected void setListener() {
        super.setListener();

        rlvRecord.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                if(type == 1){
                    getConsume();
                }else{
                    getChongzhi();
                }

            }
        });

    }

    /**
     * 获取充值记录
     */
    private void getChongzhi() {
        Map<String,String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page",page+"");
        OkGo.<RechargeRecordBean>post(UrlProvider.GET_RECHAR).params(map).tag(this).execute(new SimpleJsonCallback<RechargeRecordBean>(RechargeRecordBean.class) {
            @Override
            public void onSuccess(Response<RechargeRecordBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    List<RechargeRecordBean.ResultsBean> list = response.body().getResults();
                    Logger.e(list.size()+"");
                    if (page == 1) {
                        rechargeRecordAdapter.setDataList(list);
                    } else {
                        rechargeRecordAdapter.addAll(list);
                    }
                    if(null!=rlvRecord){
                        rlvRecord.refreshComplete(10);//每页加载数量
                    }
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    if (page > 1 && list.size() < 10) {
                        rlvRecord.setNoMore(true);
                    }
                }
            }
        });

    }

    /**
     * 获取消费记录
     */
    private void getConsume() {
        Map<String,String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page",page+"");
        OkGo.<ConsumeBean>post(UrlProvider.GET_CONSUME).params(map).tag(this).execute(new SimpleJsonCallback<ConsumeBean>(ConsumeBean.class) {
            @Override
            public void onSuccess(Response<ConsumeBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    List<ConsumeBean.ResultsBean> list = response.body().getResults();
                    Logger.e(list.size()+"");
                    if (page == 1) {
                        consumeAdapter.setDataList(list);
                    } else {
                        consumeAdapter.addAll(list);
                    }
                    if(null!=rlvRecord){
                        rlvRecord.refreshComplete(10);//每页加载数量
                    }
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    if (page > 1 && list.size() < 10) {
                        rlvRecord.setNoMore(true);
                    }
                }
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
