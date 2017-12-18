package com.tingwen.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tingwen.R;
import com.tingwen.base.BaseFragment;



/**
 * 描述: Launcher工具类
 * 名称: LauncherHelper
 */
public class LauncherHelper {
    public static LauncherHelper instance;

    /**
     * 单例
     *
     * @return
     */
    public static synchronized LauncherHelper getInstance() {
        if (instance == null) {
            instance = new LauncherHelper();
        }
        return instance;
    }

    /**
     * 新建Fragment
     *
     * @param fragment
     * @param container
     * @param fragmentManager
     */
    public void addFragment(BaseFragment fragment, int container, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().add(container, fragment).addToBackStack(null).commit();
    }

    /**
     * 显示已创建的Fragment
     *
     * @param fragment
     * @param fragmentManager
     */
    public void showFragment(BaseFragment fragment, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().show(fragment).addToBackStack(null).commit();
    }

    /**
     * 替换Fragment
     *
     * @param fragment
     * @param container
     * @param fragmentManager
     */
    public void replaceFragment(BaseFragment fragment, int container, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().replace(container, fragment).addToBackStack(null).commit();
    }

    /**
     * Fragment切换
     *
     * @param fragmentFrome
     * @param fragmentTo
     * @param container
     * @param fragmentManager
     */
    public void switchFragment(BaseFragment fragmentFrome, BaseFragment fragmentTo, int container, FragmentManager fragmentManager) {
        if (fragmentFrome != null && fragmentTo != null && fragmentFrome != fragmentTo) {
            if (fragmentTo.isAdded()) {
                fragmentManager.beginTransaction().hide(fragmentFrome).show(fragmentTo).addToBackStack(null).commit();
            } else {
                fragmentManager.beginTransaction().hide(fragmentFrome).add(container, fragmentTo).addToBackStack(null).commit();
            }
        }
    }

    /**
     * 打开指定Activity
     *
     * @param context
     * @param class1
     */
    public void launcherActivity(Context context, Class<?> class1) {
        Intent intent = new Intent(context, class1);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(
                R.anim.translate_horizontal_start_in,
                R.anim.translate_horizontal_start_out);
    }

    /**
     * 打开指定Activity并传递数据
     *
     * @param context
     * @param class1
     * @param bundle
     */
    public void launcherActivity(Context context, Class<?> class1, Bundle bundle) {
        Intent intent = new Intent(context, class1);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(
                R.anim.translate_horizontal_start_in,
                R.anim.translate_horizontal_start_out);
    }

    /**
     * 打开指定Activity并要求获取返回结果
     *
     * @param context
     * @param class1
     * @param bundle
     * @param requestCode
     */
    public void launcherActivity(Context context, Class<?> class1, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, class1);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity)context).overridePendingTransition(
                R.anim.translate_horizontal_start_in,
                R.anim.translate_horizontal_start_out);
    }
    /**
     * 打开指定Activity并要求获取返回结果
     *
     * @param context
     * @param class1
     * @param bundle
     * @param requestCode
     * @param forResult 是否需要返回值
     */
    public void launcherActivity(Context context, Class<?> class1, Bundle bundle, int requestCode,boolean forResult) {
        Intent intent = new Intent(context, class1);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(forResult)
            ((Activity) context).startActivityForResult(intent, requestCode);
        else
            context.startActivity(intent);
        ((Activity)context).overridePendingTransition(
                R.anim.translate_horizontal_start_in,
                R.anim.translate_horizontal_start_out);
    }
}
