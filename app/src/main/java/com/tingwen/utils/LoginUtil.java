package com.tingwen.utils;

import android.annotation.SuppressLint;

import com.tingwen.app.AppSpUtil;
import com.tingwen.constants.AppConfig;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 登录相关
 * Created by Administrator on 2017/7/4 0004.
 */
public class LoginUtil {

    public final static String AESCODE = "ZmH9eB4K";
    public static final String IV = "TW!@Geyp";

    @SuppressLint("TrulyRandom")
    public static String encode(String key, byte[] data) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(IV.getBytes());
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey, zeroIv);
        byte[] encryptedData = cipher.doFinal(data);
        String resultString = com.tingwen.utils.Base64.encode(encryptedData);
        return resultString;
    }

    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] byteMi = com.tingwen.utils.Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(IV.getBytes());
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }


    /**
     * 获取accessToken
     * @return
     */
    public static String getAccessToken() {
        String codeString = "";
        if (isUserLogin()) {
                try {
                    codeString = encode(AESCODE,AppSpUtil.getInstance().getUserInfo().getResults().getUser_login().getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return codeString;
    }


    /*----------------------------------------用户登录信息---------------------------------------------*/

    /**
     * @return Boolean
     * 描述：查看用户是否登录
     */
    public static boolean isUserLogin() {
        if (SpUtil.getBoolean(AppConfig.KEY_USER_LOGIN, false) && AppSpUtil.getInstance().getUserInfo() != null)
            return true;
        else {
            AppSpUtil.getInstance().deleteUserInfo();
            return false;
        }
    }

    /**
     * @return String
     * 描述：获取用户id(用户登录则为真是id，否则为"")
     *
     */
    public static  String getUserId() {
        if (isUserLogin()) {
            return AppSpUtil.getInstance().getUserInfo().getResults().getId();
        } else {
            return "";
        }
    }


    public static  int getIsRecord() {
        if (isUserLogin()) {
            return AppSpUtil.getInstance().getUserInfo().getResults().getIs_record();
        } else {
            return 0;//未提交  1已经提交
        }
    }



    /**
     * @return 0未登录，1登录
     * 描述：用户的登录状态
     *
     */
    public static int LoginStatus() {
        if (isUserLogin()) {
            return 1;
        } else {
            return 0;
        }
    }



}
