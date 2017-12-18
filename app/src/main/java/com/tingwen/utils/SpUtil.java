package com.tingwen.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tingwen.app.GlobalContext;

import java.util.Map;

/**
 * 描述：SharedPreferences工具类
 * 名称：SpUtil
 */
public class SpUtil {

    private static final String SP_NAME = "tingwen_pref";

    /**
     * 描述：将布尔值写入SharedPreferences文件中
     *
     * @param key   键
     * @param value 值
     */
    public static void setBoolean(String key, boolean value) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value).commit();
    }

    /**
     * 描述：获取存储的boolean数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 描述：存储String数据到SharedPreferences中
     *
     * @param key
     * @param value
     */
    public static void setString(String key, String value) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value).commit();
    }

    /**
     * 描述：获取存储的String数据
     *
     * @param key 键
     * @return
     */
    public static String getString(String key) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 描述：保存int
     *
     * @param key
     * @param value
     */
    public static void setInt(String key, int value) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value).commit();
    }

    /**
     * 描述：取出int值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 移除某个key值已经对应的值
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     * @return
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }


    /**
     * 描述：存储服务器时间String数据到SharedPreferences中
     *
     * @param key
     * @param value
     */
    public static void setDate(String key, String value) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value).commit();
    }

    /**
     * 描述：获取存储的服务器时间
     *
     * @param key 键
     * @return
     */
    public static String getDate(String key) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "1993-06-01");//给一个默认值
    }



}
