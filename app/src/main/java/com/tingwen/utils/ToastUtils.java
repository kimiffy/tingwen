package com.tingwen.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.tingwen.app.GlobalContext;


/**
 * 描述: Toast工具类
 * 名称: ToastUtils
 */
public class ToastUtils {

    /**
     * @param msg
     * 描述：显示位于顶部的Toast
     */
    public static void showTopToast(String msg) {
        Toast toast = Toast.makeText(GlobalContext.getInstance(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * @param msgId
     * 描述：显示位于顶部的Toast
     */
    public static void showTopToast(int msgId) {
        Toast toast = Toast.makeText(GlobalContext.getInstance(), msgId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * @param msg
     * 描述：显示位于中部的Toast
     */
    public static void showMidToast(String msg) {
        Toast toast = Toast.makeText(GlobalContext.getInstance(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void showMidToast(Context context,String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * @param msgId
     * 显示位于中部的Toast
     */
    public static void showMidToast(int msgId) {
        Toast toast = Toast.makeText(GlobalContext.getInstance(), msgId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * @param msg
     * 显示位于底部的Toast
     */
    public static void showBottomToast(String msg) {
        Toast.makeText(GlobalContext.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msgId
     * 描述：显示位于底部的Toast
     */
    public static void showBottomToast(int msgId) {
        Toast.makeText(GlobalContext.getInstance(), msgId, Toast.LENGTH_SHORT).show();
    }
}
