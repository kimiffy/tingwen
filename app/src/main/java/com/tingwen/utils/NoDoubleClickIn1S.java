package com.tingwen.utils;

import android.view.View;

/**
 * 防止过快点击(1s)
 * Created by Administrator on 2017/5/22 0022.
 */
public abstract class NoDoubleClickIn1S implements View.OnClickListener{
    private static final int TIME = 1000;
    private long lastTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();

        if(currentTime - lastTime > TIME){
            lastTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
