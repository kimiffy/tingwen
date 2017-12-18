package com.tingwen.service;

import android.app.IntentService;
import android.content.Intent;

import com.tingwen.utils.NewMessageUtil;

/**
 * 新消息服务
 * Created by Administrator on 2017/11/9 0009.
 */
public class NewMessageService extends IntentService {

    public NewMessageService() {
        super("NewMessageService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        NewMessageUtil.getInstance(this).getListenCircle();
        NewMessageUtil.getInstance(this).getNewListenCirCleMessage();
        NewMessageUtil.getInstance(this).getNewComment();
//        NewMessageUtil.getInstance(this).getNewZan();

    }


}
