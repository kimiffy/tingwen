package com.tingwen.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tingwen.R;
import com.tingwen.widget.StateView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述: 扩展的Fragment基类
 * 名称: BaseFragment
 * User: kimiffy
 */
public class BaseFragment extends BaseSwipeFragment {
    public View rootView;
    public BaseSwipeActivity mActivity;
    @Nullable
    @BindView(R.id.rlv_state)
    public RecyclerView rlvState;
    @Nullable
    @BindView(R.id.pull_rv)
    public LRecyclerView pullRv;
    @Nullable
    @BindView(R.id.mStateView)
    public StateView mStateView;
    public KProgressHUD mProgressHUD;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mActivity = (BaseSwipeActivity) getActivity();
        mProgressHUD = KProgressHUD.create(mActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("请稍候...")
                .setCancellable(true);
        initData();
        findViewById(rootView);
        initUI();
        setListener();
        return rootView;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 获取布局
     * @param container
     */
    @Override
    protected void findViewById(View container) {

    }

    /**
     * 初始化ui
     */
    @Override
    protected void initUI() {

    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {

    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {

    }

    /**
     * 显示加载状态
     */
    public void showLoading() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_LOADING);
    }

    /**
     * 显示空白状态
     */
    public void showEmpty() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_EMPTY);
    }

    /**
     * 显示错误状态
     */
    public void showError() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_ERROR);
    }

    /**
     * 显示内容
     */
    public void showContent() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_CONTENT);
    }

    /**
     * 显示内容正在加载
     */
    public void showContentLoading() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_CONTENT_LOADING);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getLocalClassName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
