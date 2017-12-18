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
import com.tingwen.bean.ListenCircleBean;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.LauncherHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class CommunityCommentAdapter extends BaseAdapter {

    private List<ListenCircleBean.ResultsBean.ChildCommentBean> list;
    private Context mContext;

    private int currentPosition;
    private CommentListener mListener;
    private ListenCircleBean.ResultsBean mListenCircle;

    public CommunityCommentAdapter(Context context, List<ListenCircleBean.ResultsBean.ChildCommentBean> list, ListenCircleBean.ResultsBean listenerCircle
    ) {
        this.mContext = context;
        this.list = list;
        this.mListenCircle = listenerCircle;

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
        final ListenCircleBean.ResultsBean.ChildCommentBean map = list.get(position);
        currentPosition = position;
        String replyPerson = null;
        if (null != map.getTo_user()) {
            replyPerson = map.getTo_user().getUser_nicename();
            if (TextUtils.isEmpty(replyPerson)) {
                replyPerson = map.getTo_user().getUser_login();
            }
        }
        ListenCircleBean.ResultsBean.ChildCommentBean.UserBean user = map.getUser();
        String commentPerson;
        if (null != user) {
            if (null == map.getUser().getUser_nicename() || map.getUser().getUser_nicename().equals("")) {
                commentPerson = map.getUser().getUser_login();
            } else {
                commentPerson = map.getUser().getUser_nicename();
            }
        } else {
            commentPerson = "匿名用户";
        }


        String content = EmojiUtil.getDecodeMsg(map.getComment()).toString();
        int comentLast;
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
                if (null != map.getUser()) {
                    toOthers(map.getUser());
                }
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

                if (null != map.getTo_user()) {
                    toOthers2(map.getTo_user());
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#5cb8e6"));
            }
        }, replayIndex, replayLat, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (mListener != null) {
                    mListener.onItemClick(map, mListenCircle, position);
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
        void onItemClick(ListenCircleBean.ResultsBean.ChildCommentBean suggest, ListenCircleBean.ResultsBean listenerCircle, int position);
    }

    private void toOthers(ListenCircleBean.ResultsBean.ChildCommentBean.UserBean user) {

        if (!TextUtils.isEmpty(user.getId())) {
            Bundle bundle = new Bundle();
            bundle.putString("id", user.getId());
            LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);
        }

    }

    private void toOthers2(ListenCircleBean.ResultsBean.ChildCommentBean.ToUserBean to_user) {

        if (!TextUtils.isEmpty(to_user.getId())) {
            Bundle bundle = new Bundle();
            bundle.putString("id", to_user.getId());
            LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);
        }

    }

    class ViewHolder {
        TextView tvComment;

    }
}
