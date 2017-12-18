package com.tingwen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tingwen.app.TwApplication;
import com.tingwen.service.AlarmService;

/**
 * 接收到定时关闭广播后退出APP
 * Created by Administrator on 2017/11/6 0006.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TwApplication.getNewsPlayer().pause();
        Intent stop = new Intent(context, AlarmService.class);
        context.stopService(stop);

    }
}
