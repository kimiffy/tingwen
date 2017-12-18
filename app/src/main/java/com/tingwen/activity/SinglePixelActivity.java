package com.tingwen.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.tingwen.app.AppManager;
import com.tingwen.utils.ScreenManager;
import com.tingwen.utils.SystemUtils;
import com.tingwen.widget.logger.Logger;

/**
 * 一个像素的activity
 * Created by Administrator on 2017/11/3 0003.
 */
public class SinglePixelActivity extends AppCompatActivity {

    private static final String TAG = "SinglePixelActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        Logger.e("onCreate--->启动1像素保活");
        // 获得activity的Window对象，设置其属性
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        // 绑定SinglePixelActivity到ScreenManager
        ScreenManager.getScreenManagerInstance(this).setSingleActivity(this);
    }


    @Override
    protected void onDestroy() {
        Logger.e("onDestroy--->1像素保活被终止");
//        Log.d(TAG, "onDestroy--->1像素保活被终止");
        if (!SystemUtils.isAPPALive(this, "com.tingwen")) {
//            Intent intentAlive = new Intent(this, MainActivity.class);
//            intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intentAlive);
            Logger.e("SinglePixelActivity---->APP被干掉了，我要重启它");
        }
        super.onDestroy();
    }
}
