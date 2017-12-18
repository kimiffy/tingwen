package com.tingwen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tingwen.R;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.PartBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.logger.Logger;

/**
 * 微博分享界面
 * Created by Administrator on 2017/11/2 0002.
 */
public class WBShareActivity extends Activity implements  WbShareCallback {


    private WbShareHandler shareHandler;

    private NewsBean news;

    private PartBean part;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        WbSdk.install(this,new AuthInfo(this, AppConfig.WB_APP_KEY, AppConfig.WB_REDIRECT_URL, AppConfig.WB_SCOPE));
        Intent intent = getIntent();
        news = (NewsBean) intent.getSerializableExtra("news");//新闻
        comment = intent.getStringExtra("comment");//2 来自评论新闻

        Logger.e("评论:"+comment);

        part = (PartBean) getIntent().getExtras().getSerializable("part");//节目

        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        sendMultiMessage();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("onResume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage() {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        shareHandler.shareMessage(weiboMessage, false);
    }

    /**
     * 获取分享的文本
     */
    private String getSharedText() {
        String text="";
        if(null!=news){
            String title = news.getPost_title();
            String id = news.getId();
            if(null!=comment){
                text=comment+"   "+title+"收听链接:"+UrlProvider.SHARE + id + ".html";
            }else{
                text= "【" +title+"】"+"收听链接:" + UrlProvider.SHARE + id + ".html";
            }
        }

        if (null!=part){
            String name = part.getName();
            String id = part.getId();
           String url= UrlProvider.SHARE_PROGRAM + id;
            text= "我觉得【" + name +"】的节目不错~" + "快来听听看吧,收听链接:"+url;
        }
        return text;
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        textObject.title = "xxxx";
        textObject.actionUrl = "http://tingwen.me";
        return textObject;
    }




    @Override
    public void onWbShareSuccess() {
        ToastUtils.showBottomToast("分享成功");
        WBShareActivity.this.finish();
    }

    @Override
    public void onWbShareCancel() {
        ToastUtils.showBottomToast("取消分享");
        WBShareActivity.this.finish();
    }

    @Override
    public void onWbShareFail() {
        ToastUtils.showBottomToast("分享失败");
        WBShareActivity.this.finish();
    }
}
