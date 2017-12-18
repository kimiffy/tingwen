package com.tingwen.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.tingwen.R;
import com.tingwen.utils.SystemBarHelper;
import com.tingwen.widget.StateView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述: 经过扩展的BaseActivity
 * 名称: BaseActivity
 * User: kimiffy
 */
public class BaseActivity extends BaseSwipeActivity {

    protected LayoutInflater inflater;
    @Nullable
    @BindView(R.id.mStateView)
    public StateView mStateView;
    public KProgressHUD mProgressHUD;
    public Unbinder unbinder;


    /**
     * @param layoutResId
     */
    @Override
    protected void setContentViewId(int layoutResId) {
        setContentView(layoutResId);
        unbinder = ButterKnife.bind(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (SystemBarHelper.isMIUI6Later() || SystemBarHelper.isFlyme4Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                SystemBarHelper.setStatusBarDarkMode(this);
//              SystemBarHelper.tintStatusBar(this, Color.parseColor("#ffffff"), 0);  //透明状态栏不在基类里处理,如有需要在相应的界面使用即可
//            }
//        }
        inflater = LayoutInflater.from(this);
        setSwipeBackEnable(true);
        mProgressHUD = KProgressHUD.create(mActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("请稍候...")
                .setCancellable(true);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void savedInstanceState(Bundle savedInstanceState) {

    }

    /**
     * 设置状态栏是否需要透明(默认需要)
     * @param isNeed
     */
    @Override
    protected void setStatusBar(Boolean isNeed) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (SystemBarHelper.isMIUI6Later() || SystemBarHelper.isFlyme4Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SystemBarHelper.setStatusBarDarkMode(this);
                if (isNeed) {
//                 SystemBarHelper.tintStatusBar(this, Color.parseColor("#9e9e9e"),1);//放弃这种实现方式
                }
            }
        }


    }


    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 渲染界面
     */
    @Override
    protected void findViewById() {

    }

    /**
     * 初始化UI
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
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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


}
