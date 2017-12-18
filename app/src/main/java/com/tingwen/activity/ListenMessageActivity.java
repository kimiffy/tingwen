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
import com.tingwen.bean.ListenCircleMessageBean;
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
 * 听友圈关于自己的评论列表
 * Created by Administrator on 2017/11/10 0010.
 */
public class ListenMessageActivity extends BaseActivity {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_message)
    RecyclerView rlvMessage;
    private List<ListenCircleMessageBean.ResultsBean> listenCircleMessageList;
    private CommonAdapter<ListenCircleMessageBean.ResultsBean> adapter;
    @Override
    protected int getLayoutResId() {
        return R.layout.acitivity_listen_message;
    }

    @Override
    protected void initData() {
        super.initData();
        setSwipeBackEnable(false);//禁止滑动返回
        TouchUtil.setTouchDelegate(ivLeft,50);
        listenCircleMessageList = NewMessageUtil.getInstance(this).getListenCircleMessageList();
        rlvMessage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<ListenCircleMessageBean.ResultsBean>(ListenMessageActivity.this, R.layout.item_feedback_message, listenCircleMessageList) {

            @Override
            protected void convert(ViewHolder holder, ListenCircleMessageBean.ResultsBean bean, int position) {

                ImageView head = holder.getView(R.id.iv_head);
                TextView name = holder.getView(R.id.tv_name);
                TextView content = holder.getView(R.id.tv_comment);
                ListenCircleMessageBean.ResultsBean.UserBean user = bean.getUser();
                String avatar = user.getAvatar();
                if (avatar != null) {
                    if (!avatar.startsWith("http:")) {
                        avatar = UrlProvider.URL_IMAGE_USER + avatar;
                    }
                }
                Glide.with(ListenMessageActivity.this).load(avatar).transform(new GlideCircleTransform(ListenMessageActivity.this)).
                        error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang).into(head);

                if (!TextUtils.isEmpty(user.getUser_nicename())) {
                    name.setText(user.getUser_nicename());
                } else {
                    name.setText(user.getUser_login());
                }

                String s = EmojiUtil.getDecodeMsg(String.valueOf(bean.getContent())).toString();

                SpannableString spannableString = EmojiUtil.getExpressionString(ListenMessageActivity.this, s);
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

                ListenCircleMessageBean.ResultsBean bean = listenCircleMessageList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("comment", bean);
                LauncherHelper.getInstance().launcherActivity(ListenMessageActivity.this,CommentListenDetailActivity.class,bundle);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new HasReadMessageEvent(0));
                finish();
            }
        });

    }


}
