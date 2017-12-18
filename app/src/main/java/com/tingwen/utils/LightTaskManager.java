package com.tingwen.utils;

import android.os.Handler;
import android.os.HandlerThread;


/**
 *  Created by Administrator on 2015/12/24 0024.
 */
public class LightTaskManager {
    private Handler mHandler;
    public LightTaskManager() {
        HandlerThread mHandlerThread = new HandlerThread("TingWenLightTaskManager");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void post(Runnable run) {
        mHandler.post(run);
    }

    public void postAtFrontOfQueue(Runnable runnable) {
        mHandler.postAtFrontOfQueue(runnable);
    }

    public void postDelay(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    public void postAtTime(Runnable runnable, long time) {
        mHandler.postAtTime(runnable,time);
    }
}
