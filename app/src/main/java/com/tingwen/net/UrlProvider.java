package com.tingwen.net;

/**
 * Created by Administrator on 2017/7/4 0004.
 * 项目url
 */
public class UrlProvider {
    public final static String URL_ADDRESS = "http://api.tingwen.me/index.php/api/interface";
    //小游的地址
    public final static String URL_ADDRESS_YOU = "http://api.tingwen.me/index.php/api/interfaceYou";
    //测试地址
    public final static String URL_ADDRESS_TEST = "http://api.tingwen.me/index.php/api/interfaceTest";
    //小沈的地址
    public final static String URL_ADDRESS_NEW = "http://api.tingwen.me/index.php/api/interfaceNew";
    // 获取新闻图片地址
    public final static String URL_IMAGE = "http://admin.tingwen.me";
    // 获取新闻图片地址2
    public final static String URL_IMAGE2 = "http://admin.tingwen.me/data/upload/";
    // 获取用户头像地址
    public final static String URL_IMAGE_USER = "http://admin.tingwen.me/data/upload/avatar/";

    // 新闻分享
    public final static String SHARE = "http://tingwen.me/index.php/article/yulan/id/";
    //节目分享
    public final static String SHARE_PROGRAM = "http://tingwen.me/index.php/act/detail/id/";



    //微信支付
    public final static String WEIXIN_PAY="http://api.tingwen.me/index.php/api/Wxpay/wxpay";
    //支付宝支付
    public final static String ALI_PAY=" http://api.tingwen.me/index.php/api/Alipay/alipay";

    // 开机广告图
    public final static String AD_BOOT = URL_ADDRESS + "/kaiList";
    //用户登录
    public final static String LOGIN = URL_ADDRESS_NEW + "/login";
    // 获取短信地址
    public final static String SMS = URL_ADDRESS + "/sendVcode";
    // 用户注册
    public final static String REGISTER = URL_ADDRESS_NEW + "/register";
    // 第三方登录
    public final static String OTHER_LOGIN = URL_ADDRESS_NEW + "/oauthLogin";
    // 查询用户信息
    public final static String GET_USER_INFO = URL_ADDRESS_NEW + "/userinfo";
    // 上传用户头像
    public final static String UPDATE_ICON = URL_ADDRESS_NEW + "/uploadHeadImg";
    // 修改用户信息
    public final static String MODIFY_USER_INFO = URL_ADDRESS_NEW + "/modifyUserInfo";
    // 更新版本
    public final static String VERSION = URL_ADDRESS + "/version";
    // 修改密码
    public final static String MODIFY_PASSWORD = URL_ADDRESS + "/modifyPwd";
    // 密码找回
    public final static String GET_PASSWORD = URL_ADDRESS + "/forgetPwd";
    //听闻课堂
    public final static String CLASS = URL_ADDRESS_NEW + "/classroom";
    //专栏
    public final static String COLUMN = URL_ADDRESS_NEW + "/columnList";
    //快讯新闻
    public final static String FAST_NEWS = URL_ADDRESS_NEW + "/information";
    // 获取快讯广告
    public final static String FAST_AD_LIST = URL_ADDRESS_NEW + "/ads";
    //订阅
    public final static String SUBCRIPTION = URL_ADDRESS + "/acanNew";
    //关注列表
    public final static String SUBSCRIP_LIST = URL_ADDRESS_NEW + "/subscribe";
    //发现
    public final static String SEARCH_TYPE = URL_ADDRESS_NEW + "/findIndex";
    //更多(主播,节目) 根据id获取列表
    public final static String SEARCH_TYPE_LIST = URL_ADDRESS_NEW + "/findList";
    //更多(主播,节目)根据id获取列表（根据金币排行）
    public final static String SEARCH_TYPE_SORT = URL_ADDRESS_NEW + "/findList_gold";
    //我的会员
    public final static String VIP = URL_ADDRESS_NEW + "/member";
    //我的收藏
    public final static String COLLECTION = URL_ADDRESS_NEW + "/get_collection";
    //取消收藏
    public final static String CANCEL_COLLECTION = URL_ADDRESS_NEW + "/del_collection";
    //我的课堂
    public final static String BUY_CLASS = URL_ADDRESS_NEW + "/listBuy";
    // 获取新闻类别的列表
    public final static String NEWS_LIST = URL_ADDRESS + "/postList";
    // 关注节目(主播)
    public final static String FOLLOW_ACT = URL_ADDRESS + "/addActtention";
    // 取消关注节目
    public final static String CANCEL_FOLLOW_ACT = URL_ADDRESS + "/cancelActtention";
    // 节目新闻
    public final static String ACT_NEWS = URL_ADDRESS + "/Actinfo";
    //赏谢榜列表
    public final static String GET_THANKS_LIST = URL_ADDRESS_NEW + "/zan_board";
    // 节目留言
    public final static String ACT_COMMENTS = URL_ADDRESS + "/actList";
    // 添加节目评论
    public final static String ADD_ACT_COMMENTS = URL_ADDRESS + "/addAct";
    // 节目评论删除
    public final static String DEL_ACT_COMMENTS = URL_ADDRESS + "/delAct";
    // 节目评论点赞
    public final static String ADD_PRAISE_ACT = URL_ADDRESS + "/addActPraise";
    //课堂试听详情
    public final static String AUDITION_DETAIL = URL_ADDRESS_NEW + "/audition";
    //新闻详情
    public final static String NEWS_DETAIL = URL_ADDRESS_NEW + "/postDetail";
    // 获取评论
    public final static String GET_NEWS_COMMMENT = URL_ADDRESS + "/commentList";
    //投金币
    public final static String TOU_TINGBI = URL_ADDRESS_NEW + "/goldUse";
    public final static String GOLD_USE = URL_ADDRESS_NEW + "/goldUseNum";
    //收藏新闻
    public final static String COLLECT_NEWS = URL_ADDRESS_NEW + "/collection";
    //听友圈列表
    public final static String LISTEREN_FRIEND = URL_ADDRESS_YOU + "/friendDynamics";
    //听友圈评论或者回复的接口
    public final static String SEND_COMMENT = URL_ADDRESS_YOU + "/pinglun";
    // 增加评论
    public final static String AddComments = URL_ADDRESS + "/addComment";
    // 删除评论(听友圈自己的动态)
    public final static String DELETE_COMMENT = URL_ADDRESS + "/delComment";
    //听友圈删除自己的评论内容
    public final static String DELETE_SELF_COMMENT =URL_ADDRESS_YOU+"/delSelfComment";
    // 新闻评论(听友圈)点赞
    public final static String COMMENT_ZAN = URL_ADDRESS + "/addAndCancelPraise";
    //听友圈屏蔽接口
    public final static String SEND_SHEILD = URL_ADDRESS_YOU + "/pingbi";
    //听友圈投诉
    public final static String SEND_COMPLAINT = URL_ADDRESS_YOU + "/tousu";
    //是否可以发表朋友圈
    public final static String CHECK_SEND = URL_ADDRESS_YOU + "/check";
    //图片上传
    public final static String UPLOADPHOTO = URL_ADDRESS_YOU + "/uploadImages";
    //听友圈上传音频文件
    public final static String UPLOADVOICE = URL_ADDRESS_YOU + "/uploadMpVoice";
    //听友圈发表
    public final static String SEND_LISTEN = URL_ADDRESS_YOU + "/fabiao";
    //个人主页列表接口
    public final static String MY_DYNAMICS = URL_ADDRESS_YOU + "/myDynamics";
    // 粉丝列表
    public final static String FANS_LIST = URL_ADDRESS + "/fanList";
    // 关注列表
    public final static String ATTENTION_LIST = URL_ADDRESS + "/attentionList";
    // 添加关注
    public final static String ADD_ATTENTION = URL_ADDRESS + "/addAttention";
    // 取消关注
    public final static String CANCEL_ATTENTION = URL_ADDRESS + "/cancelAttention";
    //获取反馈信息
    public final static String GET_SUGGEST = URL_ADDRESS_YOU + "/feedBackList";
    //发送反馈信息
    public final static String SEND_SUGGEST = URL_ADDRESS_YOU + "/addfeedBack";
    //点赞
    public final static String SEND_ZAN = URL_ADDRESS_YOU + "/addFeedbackZan";
    //取消赞
    public final static String CANCEL_ZAN = URL_ADDRESS_YOU + "/delFeedbackZan";
    //获取新闻分类接口
    public final static String GET_CATEGORY = URL_ADDRESS_YOU + "/termList";
    // 按时间、分类下载新闻
    public final static String DOWNLOAD_NEWS = URL_ADDRESS_NEW + "/xiazai";
    //搜索里的 节目(课堂)
    public final static String SEARCH_PART_CLASS = URL_ADDRESS_NEW + "/searchActLesson";
    //获取听币余额
    public final static String GET_TINGBI_BALANCE = URL_ADDRESS_NEW + "/listen_money";
    //听币打赏
    public final static String SHANG = URL_ADDRESS_NEW + "/listenMoneyUse";
    //打赏后留言
    public final static String LEAVE_MESSAGE = URL_ADDRESS_NEW + "/message";
    //听币充值列表
    public final static String RECHARGE_LIST = URL_ADDRESS_NEW + "/zs_lisMoney";
    //消费记录
    public final static String GET_CONSUME = URL_ADDRESS_NEW + "/use_record";
    //充值记录
    public final static String GET_RECHAR = URL_ADDRESS_NEW + "/recharge_record";
    // 获取用户被点赞信息
    public final static String ZAN_INFO = URL_ADDRESS + "/addcriticism";
    //获取关于自己的评论
    public final static String COMMENT_LIST = URL_ADDRESS_YOU + "/feedbackForMe";
    //听友圈有发布新信息提醒
    public final static String LISTEN_CIRCLE_NEW = URL_ADDRESS_NEW +  "/newsAvatar";
    //听友圈评论自己新消息提醒
    public final static String LISTEN_CIRCLE_LIST = URL_ADDRESS_YOU +"/newPromptForMe";
    //获取关于自己的评论的详情
    public final static String COMMENT_DETAIL = URL_ADDRESS_YOU + "/feedbackGetOne";
    //听友圈获取关于自己的评论详情
    public final static String LISTEN_CIRCILE_DETAIL = URL_ADDRESS_YOU + "/newPromptGetOne";
    // 获取新闻的详情
    public final static String NEWS_DETIAL = URL_ADDRESS + "/postinfo";
    //添加学员信息
    public final static String ADD_CLASS_BUYER_INFO=URL_ADDRESS_NEW+"/get_info";
    //获取每日免费收听次数,以及服务器时间;
    public final static String DAILY_INFO=URL_ADDRESS_NEW+"/getDailyInfo";
    //非会员听完限制次数后通知服务器状态改变
    public final static String HAS_USEDUP_TIMES=URL_ADDRESS_NEW+"/quantitys";
    //获取课堂播放记录及数据
    public final static String CLASS_LAST_PLAY_AND_DATA=URL_ADDRESS_NEW+"/getLastPlay";
    //上传课堂播放时间
    public final static String UPLOAD_CLASS_LAST_PLAY=URL_ADDRESS_NEW+"/insertHistory";

}
