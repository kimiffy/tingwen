package com.tingwen.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.activity.PersonalHomePageActivity;
import com.tingwen.activity.RewardListActivity;
import com.tingwen.adapter.FansChartAdapter;
import com.tingwen.base.BaseLazyFragment;
import com.tingwen.bean.FansChartBean;
import com.tingwen.event.ShangSuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.PaySuccessDialog;
import com.tingwen.popupwindow.ZanShangPopupWindow;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.widget.CommonHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;



/**
 * 粉丝排行榜
 * Created by Administrator on 2017/8/8 0008.
 */
public class FansChartFragment extends BaseLazyFragment{

    @BindView(R.id.rlv_fans_chart)
    LRecyclerView rlvFansChart;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<FansChartBean.ResultsBean.ListBean> list;
    private String anchorID;
    private FansChartAdapter fansChartAdapter;
    private TextView tvMyRank;
    private RoundTextView goPay;
    private RoundTextView goTo;
    private RoundTextView goBoard;
    private LinearLayoutManager linearLayoutManager;
    private int rank;
    private ZanShangPopupWindow popupWindow;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_fans_chart;
    }

    public static FansChartFragment newInstance(String id) {
        FansChartFragment fragment = new FansChartFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible&&list!=null&&list.size()==0) {
            loadData();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            anchorID = getArguments().getString("id");

        }
        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rlvFansChart.setLayoutManager(linearLayoutManager);
        fansChartAdapter = new FansChartAdapter(getActivity(), list);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(fansChartAdapter);
        rlvFansChart.setAdapter(lRecyclerViewAdapter);
        rlvFansChart.setPullRefreshEnabled(false);
        rlvFansChart.setLoadMoreEnabled(false);
        EventBus.getDefault().register(this);

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
        CommonHeader header = new CommonHeader(getActivity(), R.layout.fragment_fans_chart_head);
        lRecyclerViewAdapter.addHeaderView(header);
        tvMyRank = (TextView) header.findViewById(R.id.tv_my_ranking);
        goPay = (RoundTextView) header.findViewById(R.id.rtv_go_pay);
        goTo = (RoundTextView) header.findViewById(R.id.rtv_go_to);
        goBoard = (RoundTextView) header.findViewById(R.id.rtv_go_board);

    }


    @Override
    protected void setListener() {
        super.setListener();
        goPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null==popupWindow){
                    popupWindow = new ZanShangPopupWindow(getActivity(), anchorID);
                }
                popupWindow.setOnListener(new ZanShangPopupWindow.ZanShangListener() {
                    @Override
                    public void paySuccessCallBack(String tingbi, Float shangNumber, String id) {

                    }

                    @Override
                    public void payNoLogin(Float shangNumber, String id) {

                    }
                });
                popupWindow.showPopupWindow(getView());
            }
        });

        goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               MoveToPosition(linearLayoutManager,rlvFansChart,rank+1);


            }
        });

        goBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(null==popupWindow){
                    popupWindow = new ZanShangPopupWindow(getActivity(), anchorID);
                }
                popupWindow.setOnListener(new ZanShangPopupWindow.ZanShangListener() {
                    @Override
                    public void paySuccessCallBack(String tingbi, Float shangNumber, String id) {

                    }

                    @Override
                    public void payNoLogin(Float shangNumber, String id) {

                    }
                });
                popupWindow.showPopupWindow(getView());

            }
        });

        fansChartAdapter.setListener(new FansChartAdapter.FansChartListener() {
            @Override
            public void HeadClick(int position) {
                FansChartBean.ResultsBean.ListBean listBean = list.get(position);
                String id = listBean.getUser_id();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                LauncherHelper.getInstance().launcherActivity(getActivity(), PersonalHomePageActivity.class,bundle);


            }
        });

    }

    /**
     * 移动到指定position
     * @param manager
     * @param mRecyclerView
     * @param n
     */
    private void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }

    /**
     * 获取数据
     */
    private void loadData() {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", anchorID);
        OkGo.<FansChartBean>post(UrlProvider.GET_THANKS_LIST).params(map,true).tag(this).execute(new SimpleJsonCallback<FansChartBean>(FansChartBean.class) {


            @Override
            public void onSuccess(Response<FansChartBean> response) {
                list = response.body().getResults().getList();
                if(list!=null){
                    fansChartAdapter.setDataList(list);
                    rank = response.body().getResults().getRank();
                    if(rank==0){
                        goBoard.setVisibility(View.VISIBLE);
                        tvMyRank.setText("我的排名：暂无");
                    }else{
                        goBoard.setVisibility(View.GONE);
                        goPay.setVisibility(View.VISIBLE);
                        goTo.setVisibility(View.VISIBLE);
                        tvMyRank.setText("我的排名："+rank);
//                        goTo.setText("立刻前往");
                    }
                }else {
                    goBoard.setVisibility(View.VISIBLE);
                    tvMyRank.setText("我的排名：暂无");
                }

            }
        });


    }


    /**
     * 支付成功后
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShangSuccessEvent(ShangSuccessEvent event) {
        int type = event.getType();
        popupWindow.dismiss();
        if(type==2){
            String money = event.getMoney();
            PaySuccessDialog paySuccessDialog = new PaySuccessDialog(getActivity(), anchorID, "",money);
            paySuccessDialog.setListener(new PaySuccessDialog.PaySuccessShareListener() {
                @Override
                public void paySuccessShare() {
                    // TODO: 2017/10/27 0027 跳转分享
                }

                @Override
                public void paySuccessThanks() {
                    Bundle bundle = new Bundle();
                    bundle.putString("act_id", anchorID);
                    bundle.putString("post_id", "");
                    LauncherHelper.getInstance().launcherActivity(getActivity(), RewardListActivity.class, bundle);
                }

                @Override
                public void paySuccessfinish() {

                }
            });
            paySuccessDialog.show();
        }


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }


}
