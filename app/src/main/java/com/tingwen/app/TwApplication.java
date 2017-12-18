package com.tingwen.app;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okserver.OkDownload;
import com.tingwen.R;
import com.tingwen.activity.MainActivity;
import com.tingwen.constants.AppConfig;
import com.tingwen.gen.DaoMaster;
import com.tingwen.gen.DaoSession;
import com.tingwen.interfaces.MediaPlayerInterface;
import com.tingwen.service.TwNewsService;
import com.tingwen.utils.ACache;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.GreenDaoUpdateHelper;
import com.tingwen.utils.LightTaskManager;
import com.tingwen.utils.LoginUtil;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;


/**
 * 描述: app
 * 名称: TwApplication
 */
public class TwApplication extends GlobalContext {
    public static TwApplication instance;
    //获取到主线程的handler
    private static Handler mMainThreadHandler;
    //获取到主线程的id
    private static int mMainThreadId;
    //缓存工具
    private ACache cache;
    public static MediaPlayerInterface mNewsPlayerInterface;
    private ServiceConnection mediaPlayServiceConnection;
    private DaoSession daoSession;

    private static MiPushHandler sHandler = null;
    public static final String TAG = "com.xiaomi.mipush";
    public static String playid = 0 + "";//正在播放的新闻ID

    /**
     * 获取Application实例
     */
    public static TwApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mMainThreadHandler = new Handler();
        mMainThreadId = android.os.Process.myTid();
        cache = ACache.get(getApplicationContext());

        initApplication();
    }

    /**
     * 初始化应用
     */
    private void initApplication() {

        //判断用户是否已经打开App，详细见下面方法定义
        if (shouldInit()) {
//        initLeakCanary();
        initOkGo();
        initGreenDao();
        initStorage();
        updateUserInfo();
        initMipush();
        initService();
        }

    }

    /**
     * 初始化小米推送
     */
    private void initMipush() {

        if (shouldInit()) {
            //注册推送服务
            //注册成功后会向MiMessageReceiver发送广播
            // 可以从MiMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
            MiPushClient.registerPush(this, AppConfig.MI_APP_ID, AppConfig.MI_APP_KEY);
            //参数说明
            //context：Android平台上app的上下文，建议传入当前app的application context
            //appID：在开发者网站上注册时生成的，MiPush推送服务颁发给app的唯一认证标识
            //appKey:在开发者网站上注册时生成的，与appID相对应，用于验证appID是否合法
        }

        //下面是与测试相关的日志设置
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);

        if (sHandler == null) {
            sHandler = new MiPushHandler(getApplicationContext());
        }


    }

    /**
     * 初始化GreenDao 数据库
     */
    private void initGreenDao() {

//      MigrationHelper.DEBUG = true;//debug是否开启
        GreenDaoUpdateHelper updateHelper = new GreenDaoUpdateHelper(this, AppConfig.DATE_BASE_NAME, null);
        Database db = updateHelper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        DaoMaster daoSession = new DaoMaster(updateHelper.getWritableDatabase());//如果某个表需要升级 在此操作 参见 :https://github.com/yuweiguocn/GreenDaoUpgradeHelper

    }


//    /**
//     * 初始化leakCanary(检测内存泄漏问题)
//     */
//    private void initLeakCanary() {
//         //内存泄漏检测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//
//    }

    /**
     * 初始化OKGO
     */
    private void initOkGo() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(6000L, TimeUnit.MILLISECONDS);//全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(1);                             //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        //      .addCommonHeaders(headers)                      //全局公共头
        //      .addCommonParams(params);                       //全局公共参数

        OkDownload.getInstance().getThreadPool().setCorePoolSize(2);//设置 OkDownload 下载线程数

    }


    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        LightTaskManager lightTaskManager = new LightTaskManager();
        lightTaskManager.post(new Runnable() {
            @Override
            public void run() {
                if (LoginUtil.isUserLogin()) {
                    FollowUtil.getUserInfo();//更新用户信息
                    FollowUtil.getFollowedPart();//获取已经关注过的节目主播id
                    FollowUtil.getFollowedPersons();//获取已经关注过的人id
                    FollowUtil.getFansList();//获取粉丝列表
                }
            }
        });
    }


    /**
     * 初始化服务
     */
    private void initService() {


        mediaPlayServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
//                mNewsPlayerInterface = null;//注释了 待验证
                Log.e("TW", "服务断开绑定!");
//                TwApplication.getInstance().bindMediaPlayService();//// TODO: 2017/11/29 0029 验证
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    mNewsPlayerInterface = (MediaPlayerInterface) service;
                    Log.e("TW", "服务绑定!");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TW", "服务绑定出错!");
                    TwApplication.getInstance().bindMediaPlayService();//// TODO: 2017/11/29 0029 验证

                }
            }
        };

        if (null == TwApplication.mNewsPlayerInterface) {
            TwApplication.getInstance().bindMediaPlayService();
        }

    }

    /**
     * 外部存储
     */
    private void initStorage() {
        File file = new File(AppConfig.EXTRASTROGEDOWNLOADPATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 绑定服务
     */
    public void bindMediaPlayService() {
        try {
            Log.e("TW", "开始服务绑定!");
            bindService(new Intent(this, TwNewsService.class), mediaPlayServiceConnection, BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解除服务绑定
     */
    public void unBindMediaPlayService() {
        try {
            if (mediaPlayServiceConnection != null) {

                unbindService(mediaPlayServiceConnection);
                stopService(new Intent(this, TwNewsService.class));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static MiPushHandler getHandler() {
        return sHandler;
    }


    //处理推送
    public static class MiPushHandler extends Handler {

        private Context context;

        public MiPushHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1://点击了推送通知
                    String id = (String) msg.obj;
                    Intent intent = new Intent(TwApplication.getInstance().getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", id);
                    TwApplication.getInstance().getApplicationContext().startActivity(intent);

                    break;
                case 2://小米推送注册结果
                    String s = (String) msg.obj;
                    if (!TextUtils.isEmpty(s)) {
                        if (s.equals(context.getString(R.string.register_success)) && LoginUtil.isUserLogin()) {
                            MiPushClient.setAlias(TwApplication.getInstance().getApplicationContext(), LoginUtil.getUserId(), null);
                        }
                    }
                    break;
                case 3://透传消息
                    break;
                case 4://上传命令响应结果
                    break;
                default:
                    break;


            }
        }
    }


    //对外暴露一个主线程的handelr
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    //对外暴露一个主线程ID
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    //对外暴露一个播放器接口
    public static MediaPlayerInterface getNewsPlayer() {

        return mNewsPlayerInterface;
    }

    //对外暴露一个数据库操作DaoSession
    public DaoSession getDaoSession() {
        return daoSession;
    }


}
