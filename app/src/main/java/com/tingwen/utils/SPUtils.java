package com.tingwen.utils;/**
 * Created by PCPC on 2016/5/9.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.tingwen.app.GlobalContext;


/**
 * 描述: SharedPreferences工具类
 * 名称: SPUtils
 * User: kimiffy
 * Date: 05-09
 */
public class SPUtils {
    public static final String STATISTICAL_NAME = "tingwen_statistical";

    /**
     * 保存String ，并情况之前的保存数据
     * @param key 键
     * @param value 值
     */
    public static void setString(String key,String value){
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * append String
     * @param key 键
     * @param value 值
     */
    public static void appendString(String key,String value){
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME, Context.MODE_PRIVATE);
        StringBuilder append = new StringBuilder(sp.getString(key,""));
        SharedPreferences.Editor editor= sp.edit();
        editor.putString(key,append.append(";"+value).toString());
        editor.commit();
    }

    /**
     * 保存字符串，如果未保存过，则新建一条，如果有的话则在原来的数据后面append
     * @param key 键
     * @param value 值
     */
    public static void saveString(String key,String value){
        if(contains(key))
            appendString(key,value);
        else
            setString(key,value);
    }

    /**
     * 获取保存的字符串
     * @param key 键
     * @return  value 值
     */
    public static String getString(String key){
        SharedPreferences sharedPreferences = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    /**
     * 保存长整型
     * @param key 键
     * @param value 值
     */
    public static void setLong(String key,long value){
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.putLong(key,value);
        editor.commit();
    }

    /**
     * 获取长整型数据
     * @param key 键
     * @return  value 值
     */
    public static long getLong(String key){
        SharedPreferences sharedPreferences = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,0);
    }

    /**
     * 时间间隔叠加
     * @param key 键
     * @param value 值
     */
    public static void appendLong(String key,long value){
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME, Context.MODE_PRIVATE);
        long temp = sp.getLong(key,0);
        SharedPreferences.Editor editor= sp.edit();
        editor.putLong(key,temp+value);
        editor.commit();
    }
    /**
     * 移除某个key值已经对应的值
     * @param key 键
     */
    public static void remove(String key) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     * @param key 键
     * @return   值
     */
    public static boolean contains(String key) {
        SharedPreferences sp = GlobalContext.getInstance().getSharedPreferences(STATISTICAL_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
