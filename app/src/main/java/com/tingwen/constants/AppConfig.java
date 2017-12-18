package com.tingwen.constants;

import android.os.Environment;

/**
 * app常量类
 */
public class AppConfig {

    // WeiXin
    public static final String WX_APP_ID = "wx53ca9b29537db1d2";
    public static final String WX_MCH_ID = "1295242101";
    public static final String WX_APP_SECRET = "9d20a57b114b0cc0d30d47a4a88e079f";

    // QQ
    public static final String APP_ID = "1101441286";
    public static final String SPLASH_ADD_ID = "4030013670561546";
    public static final String NATIVE_ADD_ID = "";
    public static final String APPLICATION_ID = "1060019682984292";

    // WeiBo
    public static final String WB_APP_KEY = "1063583673";
    public static final String WB_REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
    public static final String WB_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";  // 应用申请的高级权限

    // 小米推送
    public static final String MI_APP_ID = "2882303761517164045";
    public static final String MI_APP_KEY = "5921716469045";

    //友盟统计
    public static final String UM_APP_KEY="5a0bfd71a40fa37a5300004c";

    //登录类型
    public static final String LOGIN_TYPE_NO_LOGIN = "0";
    public static final String LOGIN_TYPE_WEIBO = "1";
    public static final String LOGIN_TYPE_QQ = "2";
    public static final String LOGIN_TYPE_TINGWEN = "3";
    public static final String LOGIN_TYPE_WEIXIN = "4";

    // 外部存储地址
    public static final String EXTRASTROGEFILEDIR = "/TWNews/";//播放器缓冲的新闻
    public static final String EXTRASTROGEFILEDIRCOMPRESS = "/TWNews_Compress/";//听友圈发布的录音
    public static final String EXTRASTROGEDOWNLOADPATH = Environment.getExternalStorageDirectory() + EXTRASTROGEFILEDIR;
    public static final String EXTRASTROGEFILEDIRCOMPRESSPATH = Environment.getExternalStorageDirectory() + EXTRASTROGEFILEDIRCOMPRESS;


    //播放频道
    public static final String CHANNEL_TYPE_NEWS = "news";
    public static final String CHANNEL_TYPE_FAST_NEWS = "fast_news";//快讯
    public static final String CHANNEL_TYPE_FAST_AD = "fast_news";//广告
    public static final String CHANNEL_TYPE_COLUMN_NEWS = "column_news";//专栏
    public static final String CHANNEL_TYPE_CLASS = "class_news";//课堂
    public static final String CHANNEL_TYPE_CLASSIFICATION = "classification_news";//分类新闻
    public static final String CHANNEL_TYPE_SUBSCRIBE = "subscription_news";//订阅
    public static final String CHANNEL_TYPE_COLLECTION = "collected_news";//收藏
    public static final String CHANNEL_TYPE_PART = "part_news";//节目
    public static final String CHANNEL_TYPE_SINGLE = "single_news";//单条新闻
    public static final String CHANNEL_TYPE_DOWNLOAD = "download_news";//已经下载的新闻
    public static final String CHANNEL_TYPE_BATCH_DOWNLOAD = "batch_download_news";//批量下载搜索的新闻
    public static final String CHANNEL_TYPE_SEARCH_NEWS = "search_news";//搜索的新闻



    //通知栏控制播放和暂停
    public final static String BROADCAST_REMOTEVIEW_RECEIVER= "com.tingwen.remoteViews";


    //一些默认值
    public static final String DATE_BASE_NAME = "tingwen.db";//数据库名

    public static int PRE_PLAY_TIME_LIMIT_VALUE = 10;//非VIP播放次数值


    //用户信息
    public static final String KEY_USER_INFO = "key_user_info";
    //用户登录状态
    public static final String KEY_USER_LOGIN = "key_user_login";
    //当前版本号
    public static final String KEY_VERSION_CODE = "key_version_code";
    //当前版本名
    public static final String KEY_VERSION_NAME = "key_version_name";
    //快讯广告
    public static final String KEY_FAST_NEWS_AD = "key_fast_news_ad";
    //发现数据
    public static final String KEY_DISCOVERY = "key_discovery";
    //字体大小
    public static final String KEY_FONT = "key_font";
    //听币数量
    public static final String KEY_TING_BI = "key_ting_bi";
    //金币数量
    public static final String KEY_JING_BI = "key_jing_bi";
    //服务器时间
    public static final String KEY_DAILY_DATE = "key_daily_date";
    //获取点赞数据的时间
    public static final String KEY_ZAN_TIME = "key_zan_time";
    //意见反馈中有关自己的评论
    public static final String KEY_FEED_BACK_MESSAGE = "key_feed_back_message";
    //获取点赞数据的时间
    public static final String KEY_LISTEN_CIRCLR_TIME = "key_listen_time";
    //获取听友圈关于自己的评论
    public static final String KEY_LISTEN_CIRCLR_MESSAGE = "key_listen_message";

    //非VIP每天可听次数
    public static final String KEY_PRE_PLAY_TIME_LIMIT = "key_limit_time";
    //非VIP播放次数key
    public static final String KEY_PLAY_TIME = "key_play_times";


}
