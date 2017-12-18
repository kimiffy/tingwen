package com.tingwen.utils;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tingwen.app.GlobalContext;


/**
 * 描述: 应用信息
 * 名称: AppInfoUtils
 */
public class AppInfoUtils {
    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName() {
        try {
            PackageManager packageManager = GlobalContext.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(GlobalContext.getInstance().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            PackageManager packageManager = GlobalContext.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(GlobalContext.getInstance().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getPackageName() {
        try {
            PackageManager packageManager = GlobalContext.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(GlobalContext.getInstance().getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取应用名
     *
     * @return
     */
    public static String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = GlobalContext.getInstance().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    GlobalContext.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 获取Manifest文件中的meta-data值
     *
     * @param metaName
     * @return
     */
    public static String getMetaData(String metaName) {
        PackageManager packageManager = null;
        ApplicationInfo appInfo = null;
        try {
            packageManager = GlobalContext.getInstance().getPackageManager();
            appInfo = packageManager.getApplicationInfo(
                    GlobalContext.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo.metaData.getString(metaName);
    }
}
