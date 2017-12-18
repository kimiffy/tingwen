package com.tingwen.base;


import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 描述: Fragment基类
 * 名称: BaseFragment
 * User: kimiffy
 */
public abstract class BaseSwipeFragment extends Fragment {
    private int layoutResId;
    /**
     * 获取界面布局id
     * @return
     */
    protected int getLayoutResId(){
        return layoutResId;
    }
    /**
     * 描述：数据初始化
     */
    protected abstract void initData();

    /**
     * 描述：渲染界面
     */
    protected abstract void findViewById(View container);

    /**
     * 描述：界面初始化
     */
    protected abstract void initUI();

    /**
     * 描述：设置监听
     */
    protected abstract void setListener();

    /**
     * 点击事件
     * @param view
     */
    public abstract void onClick(View view);
}
