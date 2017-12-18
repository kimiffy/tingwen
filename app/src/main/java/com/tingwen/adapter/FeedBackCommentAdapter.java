package com.tingwen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tingwen.R;
import com.tingwen.activity.PersonalHomePageActivity;
import com.tingwen.bean.FeedBackBean;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.LauncherHelper;

import java.util.List;

/**
 * 意见反馈 评论
 * Created by Administrator on 2017/10/17 0017.
 */
public class FeedBackCommentAdapter extends BaseAdapter {

    private List<FeedBackBean.ResultsBean.ChildCommentBean> list;
    private Context mContext;

    private int currentPosition;
    private CommentListener mListener;
    private FeedBackBean.ResultsBean feedbackBean;

    public FeedBackCommentAdapter(Context context, List<FeedBackBean.ResultsBean.ChildCommentBean> list, FeedBackBean.ResultsBean feedBack
    ) {
        this.mContext = context;
        this.list = list;
        this.feedbackBean = feedBack;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listen_comment_listview, parent, false);
            holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }




        final FeedBackBean.ResultsBean.ChildCommentBean map = list.get(position);
        currentPosition = position;
        String replyPerson = null;
        if (map.getTo_user() != null) {
            replyPerson = map.getTo_user().getUser_nicename();
            if (TextUtils.isEmpty(replyPerson)) {
                replyPerson = map.getTo_user().getUser_login();
            }
        }
        String commentPerson = map.getUser().getUser_nicename();
        if (commentPerson == null || commentPerson.equals("")) {
            commentPerson = map.getUser().getUser_login();
        }
        String content = EmojiUtil.getDecodeMsg(map.getComment()).toString();
        int comentLast = 0;
        int replayIndex = 0;
        int replayLat = 0;
        if (TextUtils.isEmpty(replyPerson)) {
            content = commentPerson + ":" + content;
            comentLast = content.indexOf(":");
        } else {
            content = commentPerson + "回复 " + replyPerson + ":" + content;
            comentLast = content.indexOf("回复 ");
            replayIndex = content.indexOf("回复 ") + 2;
            replayLat = content.indexOf(":");
        }


        SpannableString spannableString = EmojiUtil.getExpressionString(mContext, content);


        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                toOthers(map.getUser());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#5cb8e6"));
            }
        }, 0, comentLast, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                FeedBackBean.ResultsBean.ChildCommentBean.ToUserBean to_user = map.getTo_user();
                toOthers2(to_user);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#5cb8e6"));//"#33475F"
            }
        }, replayIndex, replayLat, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (mListener != null) {
                    mListener.onItemClick(map, feedbackBean, position);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, replayLat, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.tvComment.setText(spannableString);
        holder.tvComment.setMovementMethod(LinkMovementMethod
                .getInstance());
        holder.tvComment.setFocusable(false);

        return convertView;
    }


    public void setListener(CommentListener listener) {
        this.mListener = listener;
    }

    public interface CommentListener {
        void onItemClick(FeedBackBean.ResultsBean.ChildCommentBean suggest, FeedBackBean.ResultsBean feedback, int position);
    }

    private void toOthers(FeedBackBean.ResultsBean.ChildCommentBean.UserBean user) {
        String id = user.getId();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);
    }

    private void toOthers2(FeedBackBean.ResultsBean.ChildCommentBean.ToUserBean to_user) {
        String id = to_user.getId();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);

    }

    class ViewHolder {
        TextView tvComment;

    }
}
