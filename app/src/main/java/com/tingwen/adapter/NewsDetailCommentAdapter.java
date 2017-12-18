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
import com.tingwen.bean.NewsCommentBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.TimeUtil;

import java.util.List;

/**
 * 新闻详情评论
 * Created by Administrator on 2017/8/24 0024.
 */
public class NewsDetailCommentAdapter extends ListBaseAdapter<NewsCommentBean.ResultsBean> {

    private Context mContext;

    public NewsDetailCommentAdapter(Context context, List<NewsCommentBean.ResultsBean> commentList) {
        super(context);
        mDataList = commentList;
        mContext = context;

    }

    public void setData(List<NewsCommentBean.ResultsBean> commentList){
        mDataList = commentList;
        notifyDataSetChanged();
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_news_detail_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final NewsCommentBean.ResultsBean bean = mDataList.get(position);
        ImageView head = holder.getView(R.id.iv_news_comments_header);
        TextView name= holder.getView(R.id.tv_news_comments_nickname);
        TextView time= holder.getView(R.id.tv_news_comments_time);
        TextView tvContent= holder.getView(R.id.tv_news_comments_content);
//        final ImageView zan = holder.getView(R.id.iv_zan);
//        final TextView zanNum= holder.getView(R.id.tv_zan);
//        LinearLayout llZan = holder.getView(R.id.ll_zan);

        String  avatar = bean.getAvatar();
        if(avatar!=null&&!avatar.isEmpty()) {
            if(!avatar.contains("http")) {
                avatar = UrlProvider.URL_IMAGE_USER +avatar;
            }
            Glide.with(mContext).load(avatar).transform(new GlideCircleTransform(mContext)).error(R.drawable.img_touxiang).into(head);
        }

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = bean.getUid();
                Bundle bundle = new Bundle();
                bundle.putString("id",uid);
                LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class,bundle);
            }
        });





        if (null!= bean.getUser_nicename() && !bean.getUser_nicename().isEmpty()) {
            name.setText(bean.getUser_nicename());
        } else if (null!=bean.getUser_nicename() && !bean.getUser_login().isEmpty()) {
            name.setText(bean.getUser_login());
        } else {
            name.setText("匿名用户");
        }

        if (null!=bean.getCreatetime()  && !bean.getCreatetime() .isEmpty()) {
            time.setText(TimeUtil.getShortTime(bean.getCreatetime()));
        }



//        final int praiseFlag = bean.getPraiseFlag();
//        final String praiseNum = bean.getPraisenum();
//        zanNum.setText(praiseNum);
//
//        if(praiseFlag==1){
//            zan.setImageResource(R.drawable.icon_zan_no);
//            zanNum.setTextColor(mContext.getResources().getColor(R.color.light_grey));
//
//        }else if(praiseFlag==2){
//            zan.setImageResource(R.drawable.icon_zan_yes);
//            zanNum.setTextColor(mContext.getResources().getColor(R.color.blue));
//        }
//
//         llZan.setOnClickListener(new NoDoubleClickIn1S() {
//             @Override
//             public void onNoDoubleClick(View v) {
//                 boolean login = LoginUtil.isUserLogin();
//                 if (!login) {
//                     ToastUtils.showBottomToast("登录后才可以点赞哦~");
//                     return;
//                 }
//
//                 NewsCommentBean.ResultsBean bean = mDataList.get(position);
//                 int flag = bean.getPraiseFlag();
//                 String beanPraiseNum = bean.getPraisenum();
//                 if (flag == 1) {
//                     int num;
//                     try {
//                         num = Integer.valueOf(beanPraiseNum);
//                     } catch (Exception e) {
//                         num = 0;
//                     }
//
//                     AnimatorSet animatorSet = new AnimatorSet();
//                     ObjectAnimator scaleXAni = ObjectAnimator.ofFloat(zan, "scaleX", 1.5f, 0.5f, 1f);
//                     scaleXAni.setDuration(500);
//                     scaleXAni.setInterpolator(new OvershootInterpolator(4));
//                     ObjectAnimator scaleYAni = ObjectAnimator.ofFloat(zan, "scaleY", 1f, 2f, 1f);
//                     scaleYAni.setDuration(500);
//                     scaleYAni.setInterpolator(new OvershootInterpolator(4));
//                     animatorSet.play(scaleXAni).with(scaleYAni);
//                     animatorSet.start();
//                     animatorSet.addListener(new Animator.AnimatorListener() {
//                         @Override
//                         public void onAnimationStart(Animator animation) {
//
//                         }
//
//                         @Override
//                         public void onAnimationEnd(Animator animation) {
//                             zan.setImageResource(R.drawable.icon_zan_yes);
//                             zanNum.setTextColor(mContext.getResources().getColor(R.color.blue));
//                         }
//
//                         @Override
//                         public void onAnimationCancel(Animator animation) {
//
//                         }
//
//                         @Override
//                         public void onAnimationRepeat(Animator animation) {
//
//                         }
//                     });
//
//                     zanNum.setText(String.valueOf(num + 1));
//                     bean.setPraisenum(String.valueOf(num + 1));
//                     bean.setPraiseFlag(2);
//
//                 } else {
//                     int num;
//                     try {
//                         num = Integer.valueOf(beanPraiseNum);
//                     } catch (Exception e) {
//                         num = 0;
//                     }
//                     zanNum.setText(String.valueOf(num - 1));
//                     zanNum.setTextColor(mContext.getResources().getColor(R.color.light_grey));
//                     zan.setImageResource(R.drawable.icon_zan_no);
//                     bean.setPraisenum(String.valueOf(num - 1));
//                     bean.setPraiseFlag(1);
//
//                 }
//
//
//             }
//         });

        String content =bean.getContent();
        try {
            content = EmojiUtil.getDecodeMsg(content).toString();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (!bean.getTo_uid().equals("0")) {
            if (!bean.getTo_user_nicename().equals("")) {
                content="回复@"+bean.getTo_user_nicename()+":"+content;
                bean.setEndIndex(bean.getTo_user_nicename().length()+3);
            } else {
                content="回复@"+bean.getTo_user_login()+":"+content;
                bean.setEndIndex(bean.getTo_user_login().length()+3);
            }

        }

        int endIndex = bean.getEndIndex();
        if (endIndex > 0) {
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
            SpannableString spannableString = EmojiUtil.getExpressionString(mContext, content);
            tvContent.setText(spannableString);
        }


    }
}
