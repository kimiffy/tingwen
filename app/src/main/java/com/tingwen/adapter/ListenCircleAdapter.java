package com.tingwen.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.activity.PersonalHomePageActivity;
import com.tingwen.app.AppSpUtil;
import com.tingwen.app.TwApplication;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.ImageBean;
import com.tingwen.bean.ListenCircleBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.User;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.MediaManager;
import com.tingwen.utils.NoDoubleClickIn1S;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.TimesUtil;
import com.tingwen.widget.LapTextView;
import com.tingwen.widget.NineGridLayout;
import com.tingwen.widget.UnScrollListView;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 听友圈adapter
 * Created by Administrator on 2017/8/31 0031.
 */
public class ListenCircleAdapter extends ListBaseAdapter<ListenCircleBean.ResultsBean> implements CommunityCommentAdapter.CommentListener {
    private static final int CIRCLE_TYPE = 0;
    private static final int STATE_TYPE = 1;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mMaxItemWith, mMinItemWith;
    /*文字超长的集合*/
    private List<ListenCircleBean.ResultsBean> listLongUser = new ArrayList<>();
    private String lastMp3Url = "";//上一次播放的语音
    private Boolean isListenCircle = true;


    public ListenCircleAdapter(Context context, List<ListenCircleBean.ResultsBean> list) {
        super(context);
        mDataList = list;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.30f);
    }


    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == CIRCLE_TYPE) {
            View itemView = mInflater.inflate(R.layout.item_listen_friend_adapter, parent, false);
            return new SuperViewHolder(itemView);
        } else {
            View itemView = mInflater.inflate(R.layout.item_listen_state_adapter, parent, false);
            return new SuperViewHolder(itemView);
        }

    }


    @Override
    public int getLayoutId() {
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        ListenCircleBean.ResultsBean resultsBean = mDataList.get(position);
        ListenCircleBean.ResultsBean.PostBean post = resultsBean.getPost();
        if (TextUtils.isEmpty(post.getId())) {

            return CIRCLE_TYPE;
        } else {
            return STATE_TYPE;
        }
    }


    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        final ListenCircleBean.ResultsBean listenerCircle = mDataList.get(position);


        if (TextUtils.isEmpty(listenerCircle.getPost().getId())) {
            ImageView ivHeader = holder.getView(R.id.header);
            TextView tvName = holder.getView(R.id.name);
            final LapTextView ltvContent = holder.getView(R.id.content);
            final TextView tvLap = holder.getView(R.id.tv_lap);
            LinearLayout llRecord = holder.getView(R.id.ll_record);
            RelativeLayout flRecordLength = holder.getView(R.id.recorder_length);
            TextView tvRecordTime = holder.getView(R.id.recorder_time);
            final View aminView = holder.getView(R.id.id_recorder_anim);
            final NineGridLayout ngl = holder.getView(R.id.ngl);
            final ImageView ivSingle = holder.getView(R.id.iv_single);

            TextView tvCreateTime = holder.getView(R.id.time);
            LinearLayout llComment = holder.getView(R.id.ll_comment);
            LinearLayout llLike = holder.getView(R.id.ll_like);
            ImageView ivZan = holder.getView(R.id.iv_zan);
            RelativeLayout rlZanComment = holder.getView(R.id.rl_zan_comment);
            TextView tvZanNumber = holder.getView(R.id.tvZanNumber);
            UnScrollListView mlvComment = holder.getView(R.id.lv_comment);
            TextView tvDelete = holder.getView(R.id.tv_delete);


            String avatar = listenerCircle.getUser().getAvatar();

            if (avatar != null && !avatar.isEmpty()) {
                if (!avatar.contains("http")) {
                    avatar = UrlProvider.URL_IMAGE_USER + avatar;

                }
                Glide.with(mContext).load(avatar).transform(new GlideCircleTransform(mContext)).error(R.drawable.img_touxiang).into(ivHeader);
            }


            String name = listenerCircle.getUser().getUser_nicename();
            if (TextUtils.isEmpty(name)) {
                name = listenerCircle.getUser().getUser_login();
            }
            tvName.setText(name);
            tvCreateTime.setText(TimesUtil.getTimesMessageByTime(listenerCircle.getCreatetime()));


            List<ListenCircleBean.ResultsBean.ChildCommentBean> commentList = new ArrayList<>();


            if ("0".equals(listenerCircle.getPraisenum()) && listenerCircle.getChild_comment() == null) {
                rlZanComment.setVisibility(View.GONE);
            } else {
                rlZanComment.setVisibility(View.VISIBLE);
                //处理点赞
                if (listenerCircle.getPraisenum() != null && !"0".equals(listenerCircle.getPraisenum())) {
                    tvZanNumber.setVisibility(View.VISIBLE);
                    tvZanNumber.setText(listenerCircle.getPraisenum() + "人点赞");
                } else {
                    tvZanNumber.setVisibility(View.GONE);
                }


                //处理评论
                if (listenerCircle.getChild_comment() != null) {
                    mlvComment.setVisibility(View.VISIBLE);
                    commentList = listenerCircle.getChild_comment();
                    CommunityCommentAdapter communityCommentAdapter = new CommunityCommentAdapter(mContext, commentList, listenerCircle);
                    mlvComment.setAdapter(communityCommentAdapter);
                    communityCommentAdapter.setListener(this);
                } else {
                    mlvComment.setVisibility(View.GONE);
                }


            }
            //图片处理
            if (listenerCircle.getTimages() != null) {
                String[] images = listenerCircle.getTimages().split(",");
                if (images.length == 1 && !images[0].equals("")) {
                    ivSingle.setVisibility(View.VISIBLE);
                    ngl.setVisibility(View.GONE);
                    final String imageUrl = ImageUtil.changeSuggestImageAddress(images[0]);
                    Glide.with(mContext).load(imageUrl).into(ivSingle);

                    final ImageBean imageBean = new ImageBean();
                    imageBean.setImagePath(imageUrl);
                    imageBean.setWidth(SizeUtil.getScreenWidth() / 2);
                    imageBean.setHeight(SizeUtil.getScreenWidth() / 2);

                    ivSingle.setOnClickListener(new NoDoubleClickIn1S(){
                        @Override
                        public void onNoDoubleClick(View v) {
                            ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
                            ThumbViewInfo item;
                            Rect bounds = new Rect();
                            ivSingle.getGlobalVisibleRect(bounds);
                            item = new ThumbViewInfo(imageUrl);
                            item.setBounds(bounds);
                            mThumbViewInfoList.add(item);
                            GPreviewBuilder.from((Activity) mContext)
                                    .setData(mThumbViewInfoList)
                                    .setCurrentIndex(0)
                                    .setType(GPreviewBuilder.IndicatorType.Number)
                                    .start();
                            mThumbViewInfoList.clear();
                            mThumbViewInfoList = null;
                        }
                    });

                } else if (images.length > 1) {
                    for (int i = 0; i < images.length; i++) {
                        images[i] = ImageUtil.changeSuggestImageAddress(images[i]);
                    }
                    ivSingle.setVisibility(View.GONE);
                    ngl.setVisibility(View.VISIBLE);
                    ngl.setImages(images);
                } else {
                    ivSingle.setVisibility(View.GONE);
                    ngl.setVisibility(View.GONE);
                }
            } else {
                ivSingle.setVisibility(View.GONE);
                ngl.setVisibility(View.GONE);
            }


            //发表内容
            int count = 0;
            for (int i = 0; i < listLongUser.size(); i++) {
                ListenCircleBean.ResultsBean user = listLongUser.get(i);
                if (user.getId().equals(listenerCircle.getId())) {
                    count++;
                }
            }
            if (count > 0) {
                tvLap.setVisibility(View.VISIBLE);
            } else {
                tvLap.setVisibility(View.GONE);
            }


            SpannableString spannableString = new SpannableString(EmojiUtil.getDecodeMsg(listenerCircle.getComment()));
            ltvContent.setText(spannableString);
            if (TextUtils.isEmpty(listenerCircle.getComment())) {
                ltvContent.setVisibility(View.GONE);
            } else {
                ltvContent.setVisibility(View.VISIBLE);
            }


            //听友圈
            //音频处理
            if (listenerCircle.getMp3_url() != null && !"".equals(listenerCircle.getMp3_url())) {
                llRecord.setVisibility(View.VISIBLE);
                flRecordLength.setVisibility(View.VISIBLE);
                int record_time = 0;
                if (!TextUtils.isEmpty(listenerCircle.getPlay_time().trim())) {
                    record_time = Integer.valueOf(listenerCircle.getPlay_time().trim());
                }
                tvRecordTime.setText(Math.round(record_time) + "\"");
                ViewGroup.LayoutParams lParams = flRecordLength.getLayoutParams();

                int width = (int) (mMinItemWith + mMaxItemWith / 60f * record_time);
                if (width > mMaxItemWith) {
                    lParams.width = mMaxItemWith;
                } else {
                    lParams.width = width;
                }
                flRecordLength.setLayoutParams(lParams);
            } else {
                llRecord.setVisibility(View.GONE);
                flRecordLength.setVisibility(View.INVISIBLE);
            }


            if (null != AppSpUtil.getInstance().getUserInfo()) {
                if (LoginUtil.getUserId().equals(listenerCircle.getUser().getId())) {
                    tvDelete.setVisibility(View.VISIBLE);
                } else {
                    tvDelete.setVisibility(View.GONE);
                }
            }else{
                tvDelete.setVisibility(View.GONE);
            }

            if (listenerCircle.getZan() == 1) {
                ivZan.setSelected(true);
            } else {
                ivZan.setSelected(false);
            }


            ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isListenCircle) {
                        ListenCircleBean.ResultsBean.UserBean user = listenerCircle.getUser();
                        String userId = user.getId();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", userId);
                        LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);
                    }

                }
            });


            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListenFriendListener.doComment(listenerCircle, position);
                }
            });
            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListenFriendListener.doZan(listenerCircle, position);
                }
            });

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListenFriendListener.delete(listenerCircle, position);

                }
            });


            tvLap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < listLongUser.size(); i++) {
                        ListenCircleBean.ResultsBean bean = listLongUser.get(i);
                        if (listenerCircle.getId().equals(bean.getId())) {
                            if (bean.getFlag() == LapTextView.FLAG_CLOSE) {
                                listenerCircle.setFlag(LapTextView.FLAG_SHOW);
                                bean.setFlag(LapTextView.FLAG_SHOW);
                                ltvContent.show();
                                tvLap.setText("收起");
                            } else if (bean.getFlag() == LapTextView.FLAG_SHOW) {
                                listenerCircle.setFlag(LapTextView.FLAG_CLOSE);
                                bean.setFlag(LapTextView.FLAG_CLOSE);
                                ltvContent.close();
                                tvLap.setText("全文");
                            }
                        }
                    }
                }
            });


            flRecordLength.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MediaManager.getInstance().isPlaying()) {
                        if (lastMp3Url.equals(listenerCircle.getMp3_url())) {
                            MediaManager.getInstance().pause();
                            aminView.setBackgroundResource(R.drawable.v_anim4);
                            return;
                        }
                    }

                    aminView.setBackgroundResource(R.drawable.listen_circle_play_sound);
                    AnimationDrawable drawable = (AnimationDrawable) aminView.getBackground();
                    drawable.start();

                    // TODO: 2017/9/6 0006  判断有无新闻正在播放  停止正在播放的新闻

                    MediaManager.getInstance().playSound(listenerCircle.getMp3_url(),
                            new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    aminView.setBackgroundResource(R.drawable.v_anim4);

                                }
                            });
                    lastMp3Url = listenerCircle.getMp3_url();

                }
            });

            ltvContent.setLapListener(new LapTextView.LapListener() {
                @Override
                public void OnClose() {
                    ltvContent.setVisibility(View.VISIBLE);
                    tvLap.setVisibility(View.VISIBLE);
                    listenerCircle.setFlag(LapTextView.FLAG_CLOSE);
                    listLongUser.add(listenerCircle);
                }
            });


            ivHeader.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListenFriendListener.showLongClick(listenerCircle);
                    return true;
                }
            });


            final List<ListenCircleBean.ResultsBean.ChildCommentBean> finalCommentList = commentList;

            mlvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ListenCircleBean.ResultsBean.ChildCommentBean suggest = finalCommentList.get(position);
                    String id1 = suggest.getUser().getId();
                    String commentID = suggest.getId();
                    if (!id1.equals("") && LoginUtil.getUserId().equals(id1)) {
                        mListenFriendListener.deleteMyComment(listenerCircle, position, commentID);
                    } else {
                        mListenFriendListener.doHuifu(listenerCircle, suggest, position);

                    }

                }
            });

            ngl.setOnItemImageViewClickListener(new NineGridLayout.OnItemImageViewClickListener() {

                @Override
                public void onItemImageViewClick(List<String> list, int position) {
                    ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
                    ThumbViewInfo item;

                    for (int i = 0; i < list.size(); i++) {
                        View itemView = ngl.getChildAt(i);
                        Rect bounds = new Rect();
                        if (itemView != null) {
                            ImageView thumbView = (ImageView) itemView;
                            thumbView.getGlobalVisibleRect(bounds);
                        }
                        item = new ThumbViewInfo(list.get(i));
                        item.setBounds(bounds);
                        mThumbViewInfoList.add(item);
                    }
                    GPreviewBuilder.from((Activity) mContext)
                            .setData(mThumbViewInfoList)
                            .setCurrentIndex(position)
                            .setType(GPreviewBuilder.IndicatorType.Number)
                            .start();
                    mThumbViewInfoList.clear();
                    mThumbViewInfoList = null;
                }
            });


        } else if (!TextUtils.isEmpty(listenerCircle.getPost().getId())) {


            ImageView ivHeader = holder.getView(R.id.header);
            TextView tvName = holder.getView(R.id.name);
            final LapTextView ltvContent = holder.getView(R.id.content);
            final TextView tvLap = holder.getView(R.id.tv_lap);

            RelativeLayout rlFriendState = holder.getView(R.id.rl_friend_state);
            RoundImageView ivNewsPic = holder.getView(R.id.iv_news_item);
            TextView tvNewsTitle = holder.getView(R.id.tv_news_title);

            TextView tvCreateTime = holder.getView(R.id.time);
            LinearLayout llComment = holder.getView(R.id.ll_comment);
            LinearLayout llLike = holder.getView(R.id.ll_like);
            ImageView ivZan = holder.getView(R.id.iv_zan);
            RelativeLayout rlZanComment = holder.getView(R.id.rl_zan_comment);
            TextView tvZanNumber = holder.getView(R.id.tvZanNumber);
            UnScrollListView mlvComment = holder.getView(R.id.lv_comment);
            TextView tvDelete = holder.getView(R.id.tv_delete);


            List<ListenCircleBean.ResultsBean.ChildCommentBean> commentList = new ArrayList<>();
            String headUrl = ImageUtil.changeSuggestImageAddress(listenerCircle.getUser().getAvatar());
            Glide.with(mContext).load(headUrl).transform(new GlideCircleTransform(mContext))
                    .error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang).into(ivHeader);
            String name = listenerCircle.getUser().getUser_nicename();
            if (TextUtils.isEmpty(name)) {
                name = listenerCircle.getUser().getUser_login();
            }
            name = name + "  评论了内容";
            int index = name.indexOf("评论了内容");
            SpannableStringBuilder builder = new SpannableStringBuilder(name);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#5cb8e6")), 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#2e3133")), index - 1, name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tvName.setText(builder);
            tvCreateTime.setText(listenerCircle.getCreatetime());

            if ("0".equals(listenerCircle.getPraisenum()) && listenerCircle.getChild_comment() == null) {
                rlZanComment.setVisibility(View.GONE);
            } else {
                rlZanComment.setVisibility(View.VISIBLE);
                //处理点赞
                if (listenerCircle.getPraisenum() != null && !"0".equals(listenerCircle.getPraisenum())) {
                    tvZanNumber.setVisibility(View.VISIBLE);
                    tvZanNumber.setText(listenerCircle.getPraisenum() + "人点赞");
                } else {
                    tvZanNumber.setVisibility(View.GONE);
                }

                //处理评论
                if (listenerCircle.getChild_comment() != null) {
                    mlvComment.setVisibility(View.VISIBLE);
                    commentList = listenerCircle.getChild_comment();
                    CommunityCommentAdapter communityCommentAdapter = new CommunityCommentAdapter(mContext, commentList, listenerCircle);
                    mlvComment.setAdapter(communityCommentAdapter);
                    communityCommentAdapter.setListener(this);
                } else {
                    mlvComment.setVisibility(View.GONE);
                }

            }


            //发表内容
            int count = 0;
            for (int i = 0; i < listLongUser.size(); i++) {
                ListenCircleBean.ResultsBean user = listLongUser.get(i);
                if (user.getId().equals(listenerCircle.getId())) {
                    count++;
                }
            }
            if (count > 0) {
                tvLap.setVisibility(View.VISIBLE);
            } else {
                tvLap.setVisibility(View.GONE);
            }
            SpannableString spannableString = new SpannableString(EmojiUtil.getDecodeMsg(listenerCircle.getComment()));
            ltvContent.setText(spannableString);
            if (TextUtils.isEmpty(listenerCircle.getComment())) {
                ltvContent.setVisibility(View.GONE);
            } else {
                ltvContent.setVisibility(View.VISIBLE);
            }
            tvCreateTime.setText(TimesUtil.getTimesMessageByTime(listenerCircle.getCreatetime()));
            //好友动态
            //新闻部分的处理
            ListenCircleBean.ResultsBean.PostBean post = listenerCircle.getPost();
            String imageUrl2 = ImageUtil.changeNewsImageAddress(post.getSimpleImage());
            Glide.with(mContext).load(imageUrl2).into(ivNewsPic);
            tvNewsTitle.setText(post.getPost_title());

            if (null != AppSpUtil.getInstance().getUserInfo()) {
                if (listenerCircle.getUser().getId().equals(LoginUtil.getUserId())) {
                    tvDelete.setVisibility(View.VISIBLE);
                } else {
                    tvDelete.setVisibility(View.GONE);
                }
            }else{
                tvDelete.setVisibility(View.GONE);
            }


            if (listenerCircle.getZan() == 1) {
                ivZan.setSelected(true);
            } else {
                ivZan.setSelected(false);
            }


            ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isListenCircle) {
                        ListenCircleBean.ResultsBean.UserBean user = listenerCircle.getUser();
                        String userId = user.getId();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", userId);
                        LauncherHelper.getInstance().launcherActivity(mContext, PersonalHomePageActivity.class, bundle);
                    }

                }
            });
            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListenFriendListener.doComment(listenerCircle, position);
                }
            });
            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListenFriendListener.doZan(listenerCircle, position);
                }
            });
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListenFriendListener.delete(listenerCircle, position);
                }
            });

            rlFriendState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ListenCircleBean.ResultsBean.PostBean postBean = listenerCircle.getPost();
                    NewsBean newsJson = getNewsJson(postBean);
                    List<NewsBean> list = new ArrayList<>();
                    list.add(newsJson);
                    TwApplication.getNewsPlayer().setNewsList(list);
                    NewsDetailActivity.actionStart(mContext, 0, AppConfig.CHANNEL_TYPE_SINGLE);

                }
            });

            ltvContent.setLapListener(new LapTextView.LapListener() {
                @Override
                public void OnClose() {
                    ltvContent.setVisibility(View.VISIBLE);
                    tvLap.setVisibility(View.VISIBLE);
                    listenerCircle.setFlag(LapTextView.FLAG_CLOSE);
                    listLongUser.add(listenerCircle);
                }
            });

            ivHeader.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mListenFriendListener.showLongClick(listenerCircle);
                    return true;
                }
            });
            final List<ListenCircleBean.ResultsBean.ChildCommentBean> finalCommentList = commentList;


            mlvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ListenCircleBean.ResultsBean.ChildCommentBean suggest = finalCommentList.get(position);
                    String id1 = suggest.getUser().getId();
                    String commentID = suggest.getId();
                    if (!id1.equals("") && LoginUtil.getUserId().equals(id1)) {
                        mListenFriendListener.deleteMyComment(listenerCircle, position, commentID);
                    } else {
                        mListenFriendListener.doHuifu(listenerCircle, suggest, position);
                    }

                }
            });


        }


    }


    @Override
    public void onItemClick(ListenCircleBean.ResultsBean.ChildCommentBean suggest, ListenCircleBean.ResultsBean listenerCircle, int position) {

        String id1 = suggest.getUser().getId();
        String commentId = suggest.getId();
        User userInfo = AppSpUtil.getInstance().getUserInfo();

        if (!id1.equals("") && null != userInfo && null != userInfo.getResults().getId() && userInfo.getResults().getId().equals(id1)) {
            mListenFriendListener.deleteMyComment(listenerCircle, position, commentId);
        } else {
            mListenFriendListener.doHuifu(listenerCircle, suggest, position);
        }

    }


    private NewsBean getNewsJson(ListenCircleBean.ResultsBean.PostBean post) {
        NewsBean newsJson = new NewsBean();
        newsJson.id = post.getId();
        newsJson.post_news = post.getPost_news();
        newsJson.post_author = post.getPost_author();
        newsJson.post_content = post.getPost_content();
        newsJson.post_content_filtered = post.getPost_content_filtered();
        newsJson.post_date = post.getPost_date();
        newsJson.post_excerpt = post.getPost_excerpt();
        newsJson.post_hits = post.getPost_hits();
        newsJson.post_keywords = post.getPost_keywords();
        newsJson.post_lai = post.getPost_lai();
        newsJson.post_like = post.getPost_like();
        newsJson.post_mime_type = post.getPost_mime_type();
        newsJson.post_mp = post.getPost_mp();
        newsJson.post_parent = post.getPost_parent();
        newsJson.post_time = post.getPost_time();
        newsJson.post_title = post.getPost_title();
        newsJson.post_type = post.getPost_type();
        newsJson.simpleImage = post.getSimpleImage();
        newsJson.smeta = post.getSmeta();
        newsJson.praisenum = post.getPraisenum();
        if (newsJson.term_id == null) {
            newsJson.term_id = "";
        }
        return newsJson;
    }

    private ListenFriendListener mListenFriendListener;

    public interface ListenFriendListener {

        void showLongClick(ListenCircleBean.ResultsBean listenerCircle);

        void doHuifu(ListenCircleBean.ResultsBean listenerCircle, ListenCircleBean.ResultsBean.ChildCommentBean suggest, int position);

        void doComment(ListenCircleBean.ResultsBean listenerCircle, int position);

        void doZan(ListenCircleBean.ResultsBean listenerCircle, int position);

        void deleteMyComment(ListenCircleBean.ResultsBean listenerCircle, int position, String id);

        void delete(ListenCircleBean.ResultsBean listenerCircle, int position);
    }

    public void setListener(ListenFriendListener listener) {
        this.mListenFriendListener = listener;
    }


    /**
     * 设置是否是听友圈(区分个人中心)
     *
     * @param isListenCircle
     */
    public void setIsListenCircle(Boolean isListenCircle) {
        this.isListenCircle = isListenCircle;
    }
}
