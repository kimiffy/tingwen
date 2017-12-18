package com.tingwen.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.FansChartAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.FansChartBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.widget.CommonHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 赏谢榜
 * Created by Administrator on 2017/10/16 0016.
 */
public class RewardListActivity extends BaseActivity {
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_reward)
    LRecyclerView rlvReward;

    private String act_id;//主播的ID
    private String post_id;//文章的ID

    private List<FansChartBean.ResultsBean.ListBean> list;
    private FansChartAdapter fansChartAdapter;

    private LinearLayoutManager linearLayoutManager;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private TextView tvMyRank;
    private RoundTextView goPay;
    private RoundTextView goTo;
    private RoundTextView goBoard;
    private int rank;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reward_list;
    }

    @Override
    protected void initData() {
        super.initData();
        act_id = getIntent().getStringExtra("act_id");
        post_id = getIntent().getStringExtra("post_id");
        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        rlvReward.setLayoutManager(linearLayoutManager);
        fansChartAdapter = new FansChartAdapter(this, list);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(fansChartAdapter);
        rlvReward.setAdapter(lRecyclerViewAdapter);
        rlvReward.setPullRefreshEnabled(false);
        rlvReward.setLoadMoreEnabled(false);
    }

    @Override
    protected void initUI() {
        super.initUI();
        addHead();
        loadData();
    }

    /**
     * 添加头部
     */
    private void addHead() {
        CommonHeader header = new CommonHeader(this, R.layout.fragment_fans_chart_head);
        lRecyclerViewAdapter.addHeaderView(header);
        tvMyRank = (TextView) header.findViewById(R.id.tv_my_ranking);
//        goPay = (RoundTextView) header.findViewById(R.id.rtv_go_pay);
        goTo = (RoundTextView) header.findViewById(R.id.rtv_go_to);
//        goBoard = (RoundTextView) header.findViewById(R.id.rtv_go_board);

    }
    @Override
    protected void setListener() {
        super.setListener();
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        goPay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.showBottomToast("赞赏");
//            }
//        });

        goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 MoveToPosition(linearLayoutManager,rlvReward,rank+1);

            }
        });



        fansChartAdapter.setListener(new FansChartAdapter.FansChartListener() {
            @Override
            public void HeadClick(int position) {
                FansChartBean.ResultsBean.ListBean listBean = list.get(position);
                String id = listBean.getUser_id();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                LauncherHelper.getInstance().launcherActivity(RewardListActivity.this, PersonalHomePageActivity.class,bundle);

            }
        });

    }

    /**
     * 获取数据
     */
    private void loadData() {
        mProgressHUD.show();
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id",act_id);
        map.put("post_id",post_id);
        OkGo.<FansChartBean>post(UrlProvider.GET_THANKS_LIST).params(map,true).tag(this).execute(new SimpleJsonCallback<FansChartBean>(FansChartBean.class) {


            @Override
            public void onSuccess(Response<FansChartBean> response) {
                mProgressHUD.dismiss();
                list = response.body().getResults().getList();
                if(list!=null){
                    fansChartAdapter.setDataList(list);
                    rank = response.body().getResults().getRank();
                    if(rank==0){
//                        goBoard.setVisibility(View.VISIBLE);
                        tvMyRank.setText("我的排名：暂无");
                    }else{
//                        goBoard.setVisibility(View.GONE);
//                        goPay.setVisibility(View.VISIBLE);
                        goTo.setVisibility(View.VISIBLE);
                        tvMyRank.setText("我的排名："+rank);
//                        goTo.setText("立刻前往");
                    }
                }else {
//                    goBoard.setVisibility(View.VISIBLE);
                    tvMyRank.setText("我的排名：暂无");
                }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
