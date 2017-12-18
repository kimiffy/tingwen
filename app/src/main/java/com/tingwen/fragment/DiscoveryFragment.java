package com.tingwen.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.activity.SearchActivity;
import com.tingwen.adapter.DiscoveryAdapter;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.DiscoveryBean;
import com.tingwen.event.LoginSuccessEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.ToastUtils;

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
 * 发现fragment
 * Created by Administrator on 2017/7/13 0013.
 */
public class DiscoveryFragment extends BaseFragment {


    @BindView(R.id.tv_search)
    TextView tvSearch;
    private List<DiscoveryBean.ResultsBean> list;
    private DiscoveryAdapter discoveryAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void findViewById(View container) {
        super.findViewById(container);

        discoveryAdapter = new DiscoveryAdapter(getActivity(),list );
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity() );

        if (rlvState != null) {
            rlvState.setLayoutManager(layoutManager);
            rlvState.setAdapter(discoveryAdapter);
        }

    }

    @Override
    protected void initUI() {
        super.initUI();
        loadData();
    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.iv_network_error, R.id.tv_search})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_network_error:
                ToastUtils.showBottomToast("重新加载!");
                loadData();
                break;
            case R.id.tv_search:
                LauncherHelper.getInstance().launcherActivity(getActivity(), SearchActivity.class);
                break;

            default:
                break;
        }
    }

    /**
     * 获取数据
     */
    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        OkGo.<DiscoveryBean>post(UrlProvider.SEARCH_TYPE).params(map, true).execute(new SimpleJsonCallback<DiscoveryBean>(DiscoveryBean.class) {

            @Override
            public void onSuccess(Response<DiscoveryBean> response) {
                showContent();
                List<DiscoveryBean.ResultsBean> results = response.body().getResults();
                discoveryAdapter.setDataList(results);
                AppSpUtil.getInstance().saveDiscoveryResult(response.body());

            }

            @Override
            public void onError(Response<DiscoveryBean> response) {
                super.onError(response);
                if(!NetUtil.isHasNetAvailable(getActivity())){
                    ToastUtils.showBottomToast("无网络连接!");
                }else{
                    ToastUtils.showBottomToast("获取数据异常");
                }
                setData();

            }
        });

    }

    /**
     * 设置缓存数据
     */
    private void setData() {
        List<DiscoveryBean.ResultsBean> discoveryData = AppSpUtil.getInstance().getDiscoveryData();
        if(discoveryData!=null){
            showContent();
            discoveryAdapter.setDataList(discoveryData);
        }else{
            showError();
        }
    }

    /**
     * 登录成功事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
