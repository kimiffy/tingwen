package com.tingwen.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.tingwen.R;
import com.tingwen.receiver.AlarmReceiver;

/**
 * 定时关闭
 * Created by Administrator on 2017/11/6 0006.
 */
public class AlarmService extends Service {

    //设置定时时间
    public static int timelong = 0;
    private AlarmManager manager;
    private PendingIntent broadcast;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification noti = new Notification.Builder(AlarmService.this)
                .setContentText("定时停止播放")
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.tingwen_logo))
                .setContentTitle("听闻")
                .build();
        // 设置为前台服务
        startForeground(1, noti);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time= timelong+ SystemClock.elapsedRealtime();
        Intent i = new Intent(this, AlarmReceiver.class);
        broadcast = PendingIntent.getBroadcast(this, 0, i, 0);
        //设置定时任务
        if (Build.VERSION.SDK_INT>=19){
            //API19以上使用
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,time, broadcast);

        }else {
            manager.set(AlarmManager.RTC_WAKEUP,time,broadcast);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消定时任务
        if(null!=manager){
            manager.cancel(broadcast);
        }
    }
}