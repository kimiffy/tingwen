package com.tingwen.app;


import android.app.Application;

import com.tingwen.utils.AppInfoUtils;
import com.tingwen.widget.logger.LogLevel;
import com.tingwen.widget.logger.Logger;


/**
 * baseApplication
 * 处理log
 */
public class GlobalContext extends Application {
    private static GlobalContext globalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
        initLog();
    }

    /**
     * Logger初始化，FULL显示LOG，NONE屏蔽LOG
     */
    private void initLog() {
        Logger.init(AppInfoUtils.getApplicationName()) // default PRETTYLOGGER or use just init()
                .setMethodCount(3)            // default 2
                .hideThreadInfo()             // default show
                .setLogLevel(LogLevel.FULL);  // default LogLevel.FULL;
    }

    public static GlobalContext getInstance() {
        return globalContext;
    }
}
