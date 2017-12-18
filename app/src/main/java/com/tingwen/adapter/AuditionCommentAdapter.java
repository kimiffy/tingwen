package com.tingwen.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.activity.PersonalHomePageActivity;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.CommentBean;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.widget.logger.Logger;

import java.util.List;

/**
 * 课堂试听详情评论
 * Created by Administrator on 2017/8/16 0016.
 */
public class AuditionCommentAdapter extends ListBaseAdapter<CommentBean> {

    private Context mContext;

    public AuditionCommentAdapter(Context context, List<CommentBean> commentList) {
        super(context);
        mDataList = commentList;
        mContext = context;

    }



    @Override
    public int getLayoutId() {
        return R.layout.item_audition_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final CommentBean bean = mDataList.get(position);


        ImageView ivHead = holder.getView(R.id.iv_head);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvContent = holder.getView(R.id.tv_comment);
        String imgUrl = ImageUtil.changeSuggestImageAddress(bean.getAvatar());

        Glide.with(mContext).load(imgUrl).transform(new GlideCircleTransform(mContext))
                .placeholder(R.drawable.img_touxiang).error(R.drawable.img_touxiang)
                .into(ivHead);
        tvName.setText(bean.getUser_nicename());


        tvTime.setText(bean.getCreatetime());


        String content =bean.getContent();
        try {
            content = EmojiUtil.getDecodeMsg(content).toString();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (bean.getTo_uid()!=null&&!bean.getTo_uid().equals("0")) {
            if (!bean.getTo_user_nicename().equals("")) {
                content="回复@"+bean.getTo_user_nicename()+":"+content;
                bean.setEndIndex(bean.getTo_user_nicename().length()+3);
            } else {
                content="回复@"+bean.getTo_user_login()+":"+content;
                bean.setEndIndex(bean.getTo_user_login().length()+3);
            }

        }

        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(bean.getUid())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", bean.getUid());
                    LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);
                }
            }
        });



        int endIndex = bean.getEndIndex();

        if (endIndex > 0&&content!=null) {
            SpannableString spannableString = EmojiUtil.getExpressionString(mContext, content);

            spannableString.setSpan(new ClickableSpan() {

                @SuppressWarnings("deprecation")
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(mContext.getResources().getColor(R.color.blue));
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {

                    if (!TextUtils.isEmpty(bean.getTo_uid())) {
                        Logger.e("to_uid:  "+bean.getTo_uid());
                        Bundle bundle = new Bundle();
                        bundle.putString("id", bean.getTo_uid());
                        LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);
                    }

                }

            }, 2, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.setMovementMethod(LinkMovementMethod
                    .getInstance());
            tvContent.setText(spannableString);
            tvContent.setFocusable(false);
        } else {
            if(content!=null){
                SpannableString spannableString = EmojiUtil.getExpressionString(mContext, content);
                tvContent.setText(spannableString);
            }

        }

    }


}
