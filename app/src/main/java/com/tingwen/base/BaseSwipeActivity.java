package com.tingwen.base;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.tingwen.app.AppManager;
import com.tingwen.widget.swipeback.SwipeBackActivity;
import com.tingwen.widget.swipeback.SwipeBackLayout;

/**
 * 描述: Activity基类
 * 名称: BaseSwipeActivity
 * User: kimiffy
 */
public abstract class BaseSwipeActivity extends SwipeBackActivity {
    protected int layoutResId;
    protected boolean isNeedTranslucent=true;
    public SwipeBackLayout mSwipeBackLayout;
    public BaseSwipeActivity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        AppManager.getAppManager().addActivity(this);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
//        int edge = SpUtil.getInt("SwipeEdge",1);
//        switch (edge){
//            case 1:
//                edge = SwipeBackLayout.EDGE_LEFT;
//                break;
//            case 2:
//                edge = SwipeBackLayout.EDGE_RIGHT;
//                break;
//            case 8:
//                edge = SwipeBackLayout.EDGE_BOTTOM;
//                break;
//            case 11:
//                edge = SwipeBackLayout.EDGE_ALL;
//                break;
//
//        }
        mSwipeBackLayout.setEdgeTrackingEnabled(1);// 右滑结束
        savedInstanceState(savedInstanceState);
        setContentViewId(getLayoutResId());
        setStatusBar(isNeedStatusBarTranslucent());

    }


    protected abstract void savedInstanceState(Bundle savedInstanceState);

    /**
     * 设置状态栏是否透明  默认透明
     * @param b
     */
    protected abstract void setStatusBar(Boolean b);


    protected boolean isNeedStatusBarTranslucent() {
        return isNeedTranslucent;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 设置布局id
     *
     * @param layoutResId
     */
    protected abstract void setContentViewId(int layoutResId);

    /**
     * 获取界面布局id
     *
     * @return
     */
    protected int getLayoutResId() {
        return layoutResId;
    }

    /**
     * 描述：数据初始化
     *
     */
    protected abstract void initData();

    /**
     * 描述：渲染界面
     *
     */
    protected abstract void findViewById();

    /**
     * 描述：界面初始化
     *
     */
    protected abstract void initUI();

    /**
     * 描述：设置监听
     *
     */
    protected abstract void setListener();

    /**
     * 点击事件
     * @param view
     */
    public abstract void onClick(View view);
    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

}
