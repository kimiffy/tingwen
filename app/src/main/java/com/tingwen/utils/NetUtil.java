package com.tingwen.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.tingwen.app.GlobalContext;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 判断网络连接是否可用
 */
public class NetUtil {

    /**
     * 判断是否有可用网络 无线和3G
     *
     * @param context
     * @return
     */
    public static boolean isHasNetAvailable(Context context) {
        if (isWifiConnected(context)) {
            return true;
        } else if (isNetWorkConnected(context)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 判断当前网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (null!=netinfo && netinfo.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    /**
     * 判断当前的网络连接方式是否为WIFI
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {

        Context applicationContext = context.getApplicationContext();

        ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(null==connectivityManager){
            Log.i("NetUtil","无法获取ConnectivityManager");
        }else{
            NetworkInfo wifiNetworkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (wifiNetworkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return 描述：获取手机IP地址
     *
     */
    public static String getIP() {
        WifiManager wifiManager = (WifiManager) GlobalContext.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return int2ip(ipAddress);
        } else {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface
                        .getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {

                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException e) {
            }
        }

        return null;

    }

    /**
     * @param ipInt
     * @return
     * @描述：ip格式化工具
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
}
