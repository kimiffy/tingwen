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

/**
 * 描述: 懒加载Fragment基类
 * 名称: BaseLazyFragment
 * User: kimiffy
 */
public class BaseLazyFragment extends BaseSwipeFragment {

    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    private boolean hasCreateView;

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, rootView);
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

    @Override
    protected void initData() {

    }

    @Override
    protected void findViewById(View container) {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }

    public void showLoading() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_LOADING);
    }

    public void showEmpty() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_EMPTY);
    }

    public void showError() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_ERROR);
    }

    public void showContent() {
        if (mStateView != null)
            mStateView.setCurrentState(StateView.STATE_CONTENT);
    }

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
}
