package com.tingwen.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.FeedBackMessageBean;
import com.tingwen.event.HasReadMessageEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.NewMessageUtil;
import com.tingwen.utils.TouchUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * 意见反馈 新消息
 * Created by Administrator on 2017/11/9 0009.
 */
public class FeedBackMessageActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_message)
    RecyclerView rlvMessage;

    private List<FeedBackMessageBean.ResultsBean> feedBackList;
    private CommonAdapter<FeedBackMessageBean.ResultsBean> adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitivity_feedback_message;
    }

    @Override
    protected void initData() {
        super.initData();
        setSwipeBackEnable(false);//禁止滑动返回
        TouchUtil.setTouchDelegate(ivLeft,50);
        feedBackList = NewMessageUtil.getInstance(this).getFeedBackList();
        rlvMessage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<FeedBackMessageBean.ResultsBean>(FeedBackMessageActivity.this, R.layout.item_feedback_message, feedBackList) {

            @Override
            protected void convert(ViewHolder holder, FeedBackMessageBean.ResultsBean bean, int position) {

                ImageView head = holder.getView(R.id.iv_head);
                TextView name = holder.getView(R.id.tv_name);
                TextView content = holder.getView(R.id.tv_comment);
                FeedBackMessageBean.ResultsBean.UserBean user = bean.getUser();
                String avatar = user.getAvatar();
                if (avatar != null) {
                    if (!avatar.startsWith("http:")) {
                        avatar = UrlProvider.URL_IMAGE_USER + avatar;
                    }
                }
                Glide.with(FeedBackMessageActivity.this).load(avatar).transform(new GlideCircleTransform(FeedBackMessageActivity.this)).
                        error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang).into(head);

                if (!TextUtils.isEmpty(user.getUser_nicename())) {
                    name.setText(user.getUser_nicename());
                } else {
                    name.setText(user.getUser_login());
                }

                String s = EmojiUtil.getDecodeMsg(String.valueOf(bean.getComment())).toString();

                SpannableString spannableString = EmojiUtil.getExpressionString(FeedBackMessageActivity.this, s);
                content.setText(spannableString);
            }
        };
        rlvMessage.setAdapter(adapter);


    }


    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                FeedBackMessageBean.ResultsBean bean = feedBackList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", bean.getTo_comment_id());
                LauncherHelper.getInstance().launcherActivity(FeedBackMessageActivity.this,CommentFeedBackDetailActivity.class,bundle);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new HasReadMessageEvent(1));
                finish();
            }
        });
    }


}
