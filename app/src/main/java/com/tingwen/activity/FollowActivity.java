package com.tingwen.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.FollowAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.FollowBean;
import com.tingwen.bean.PartBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的订阅
 * Created by Administrator on 2017/7/12 0012.
 */
public class FollowActivity extends BaseActivity implements FollowAdapter.FollowListener{


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_follow)
    LRecyclerView rlvFollow;
    private List<FollowBean.ResultsBean> list;
    private FollowAdapter followAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_follow;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        mProgressHUD.show();
    }

    @Override
    protected void initUI() {
        super.initUI();
        followAdapter = new FollowAdapter(this, list);
        followAdapter.setListener(this);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(followAdapter);
        rlvFollow.setLayoutManager(new LinearLayoutManager(this));
        //分割线
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.divider_padding_0dp)
                .setColorResource(R.color.divider)
                .build();
        rlvFollow.addItemDecoration(divider);

        rlvFollow.setAdapter(lRecyclerViewAdapter);
        rlvFollow.setPullRefreshEnabled(false);
        rlvFollow.setLoadMoreEnabled(false);
        TouchUtil.setTouchDelegate(ivLeft,50);
        loadData();

    }

    @OnClick(R.id.ivLeft)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void setListener() {
        super.setListener();

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FollowBean.ResultsBean bean = list.get(position);
                toAnchor(bean,false);
            }

        });

    }

    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("limit", "10000");
        OkGo.<FollowBean>post(UrlProvider.SUBSCRIP_LIST).params(map, true).execute(new SimpleJsonCallback<FollowBean>(FollowBean.class) {

            @Override
            public void onSuccess(Response<FollowBean> response) {
                mProgressHUD.dismiss();
                list = response.body().getResults();
                followAdapter.setDataList(list);
                lRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response<FollowBean> response) {
                super.onError(response);
                mProgressHUD.dismiss();
                if(!NetUtil.isHasNetAvailable(FollowActivity.this)){
                    ToastUtils.showBottomToast("无网络连接!");
                }else{
                    ToastUtils.showBottomToast("暂无订阅!");
                }
            }
        });

    }

    private void toAnchor(FollowBean.ResultsBean dataBean, Boolean isClass) {
        PartBean partBean = new PartBean();
        partBean.setId(dataBean.getId());
        partBean.setName(dataBean.getName());
        partBean.setDescription(dataBean.getDescription());
        partBean.setFan_num(dataBean.getFan_num()+"");
        partBean.setMessage_num(dataBean.getNews_num());
        partBean.setImages(dataBean.getImages());
        ProgramDetailActivity.actionStart(FollowActivity.this,partBean,isClass,false);
    }


    /**
     * 取消关注
     * @param position
     */
    @Override
    public void delete(final int position) {

        String id = list.get(position).getId();
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_FOLLOW_ACT).tag(this).params(map,true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    list.get(position).setIsFollowed(1);
                    followAdapter.setDataList(list);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    FollowUtil.followPartList.remove(list.get(position).getId());
                }else{
                    ToastUtils.showBottomToast(response.body().getMsg());
                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });



    }

    /**
     * 关注
     * @param position
     */
    @Override
    public void follow(final int position) {
        String id = list.get(position).getId();
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id",id );
        OkGo.<SimpleMsgBean>post(UrlProvider.FOLLOW_ACT).tag(this).params(map,true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    list.get(position).setIsFollowed(0);
                    followAdapter.setDataList(list);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    FollowUtil.followPartList.add(list.get(position).getId());
                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });
    }
}
