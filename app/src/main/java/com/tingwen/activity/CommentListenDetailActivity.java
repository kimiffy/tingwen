package com.tingwen.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.CommentListenMessageAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.ImageBean;
import com.tingwen.bean.ListenCircleMessageBean;
import com.tingwen.bean.ListenMessageDetailBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.MediaManager;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.LapTextView;
import com.tingwen.widget.NineGridLayout;
import com.tingwen.widget.UnScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 听友圈关于自己的评论详情
 * Created by Administrator on 2017/11/13 0013.
 */
public class CommentListenDetailActivity extends BaseActivity implements CommentListenMessageAdapter.CommentListener {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView tvtime;
    @BindView(R.id.content)
    LapTextView ltvContent;
    @BindView(R.id.tv_lap)
    TextView tvLap;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.id_recorder_anim)
    View idRecorderAnim;
    @BindView(R.id.recorder_length)
    RelativeLayout recorderLength;
    @BindView(R.id.recorder_time)
    TextView recorderTime;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.ngl)
    NineGridLayout ngl;
    @BindView(R.id.iv_single)
    ImageView ivSingle;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.ll_listen_friend)
    LinearLayout llListenFriend;


    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.tvZanNumber)
    TextView tvZanNumber;
    @BindView(R.id.lv_comment)
    UnScrollListView lvComment;
    @BindView(R.id.rl_zan_comment)
    RelativeLayout rlZanComment;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.rl_input)
    RelativeLayout rlInput;

    @BindView(R.id.iv_news_item)
    ImageView ivNewsItem;
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;
    @BindView(R.id.rl_friend_state)
    RelativeLayout rlFriendState;
    @BindView(R.id.sl_content)
    ScrollView slContent;

    private ListenCircleMessageBean.ResultsBean comment;
    private ListenMessageDetailBean.ResultsBean bean;
    private int mMaxItemWith, mMinItemWith;
    private List<ListenMessageDetailBean.ResultsBean.ChildCommentBean> commentList;
    private boolean isComment;//是否是发表评论
    private String huifuid;
    private String huifuName;
    private String comment_id;
    private View aminView;//动画
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_comment_listen_detail;
    }


    @Override
    protected void initData() {
        super.initData();
        comment = (ListenCircleMessageBean.ResultsBean) getIntent().getSerializableExtra("comment");
        WindowManager wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.30f);
        getData();

    }

    @Override
    protected void setListener() {
        super.setListener();
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        aminView = findViewById(R.id.id_recorder_anim);

        recorderLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aminView.setBackgroundResource(R.drawable.listen_circle_play_sound);
                AnimationDrawable drawable = (AnimationDrawable) aminView
                        .getBackground();
                drawable.start();

                // TODO: 2017/11/15 0015 判断有无新闻正在播放 有的话需要停止播放
                MediaManager.getInstance().playSound(bean.getMp3_url(),
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                aminView.setBackgroundResource(R.drawable.v_anim4);

                            }
                        });
            }
        });

    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        String id = String.valueOf(comment.getParentid());
        if (TextUtils.isEmpty(id)) {
            id = String.valueOf(comment.getPost_id());
            if (TextUtils.isEmpty(id)) {
                id = String.valueOf(comment.getId());
            }
        }
        map.put("parentid", id);
        map.put("path", String.valueOf(comment.getPath()));

        OkGo.<ListenMessageDetailBean>post(UrlProvider.LISTEN_CIRCILE_DETAIL).params(map).execute(new SimpleJsonCallback<ListenMessageDetailBean>(ListenMessageDetailBean.class) {
            @Override
            public void onSuccess(Response<ListenMessageDetailBean> response) {

                int status = response.body().getStatus();
                if (status == 1) {
                    bean = response.body().getResults();
                    setData();
                }

            }
        });

    }


    /**
     * 设置数据
     */
    private void setData() {
        slContent.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(bean.getPost().getId())) {//听友圈

            rlFriendState.setVisibility(View.GONE);
            llListenFriend.setVisibility(View.VISIBLE);
            String avatar = bean.getUser().getAvatar();

            if (avatar != null && !avatar.isEmpty()) {
                if (!avatar.contains("http")) {
                    avatar = UrlProvider.URL_IMAGE_USER + avatar;

                }
                Glide.with(this).load(avatar).transform(new GlideCircleTransform(this)).error(R.drawable.img_touxiang).into(header);
            }


            String username = bean.getUser().getUser_nicename();
            if (TextUtils.isEmpty(username)) {
                username = bean.getUser().getUser_login();
            }
            name.setText(username);

            if (bean.getCreatetime() != null && !bean.getCreatetime().equals("")) {
                tvtime.setText(bean.getCreatetime());
            }

            //图片处理
            if (bean.getTimages() != null) {
                String[] images = bean.getTimages().split(",");
                if (images.length == 1 && !images[0].equals("")) {
                    ivSingle.setVisibility(View.VISIBLE);
                    ngl.setVisibility(View.GONE);
                    final String imageUrl = ImageUtil.changeSuggestImageAddress(images[0]);
                    Glide.with(this).load(imageUrl).into(ivSingle);

                    final ImageBean imageBean = new ImageBean();
                    imageBean.setImagePath(imageUrl);
                    imageBean.setWidth(SizeUtil.getScreenWidth() / 2);
                    imageBean.setHeight(SizeUtil.getScreenWidth() / 2);

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

            if (null != bean.getComment()) {
                SpannableString spannableString = new SpannableString(EmojiUtil.getDecodeMsg(bean.getComment()));
                ltvContent.setText(spannableString);
            }
            if (TextUtils.isEmpty(bean.getComment())) {
                ltvContent.setVisibility(View.GONE);
            } else {
                ltvContent.setVisibility(View.VISIBLE);
            }


            //听友圈
            //音频处理
            if (bean.getMp3_url() != null && !"".equals(bean.getMp3_url())) {
                llRecord.setVisibility(View.VISIBLE);
                recorderLength.setVisibility(View.VISIBLE);
                int record_time = 0;
                if (!TextUtils.isEmpty(bean.getPlay_time().trim())) {
                    record_time = Integer.valueOf(bean.getPlay_time().trim());
                }
                recorderTime.setText(Math.round(record_time) + "\"");
                ViewGroup.LayoutParams lParams = recorderLength.getLayoutParams();

                int width = (int) (mMinItemWith + mMaxItemWith / 60f * record_time);
                if (width > mMaxItemWith) {
                    lParams.width = mMaxItemWith;
                } else {
                    lParams.width = width;
                }
                recorderLength.setLayoutParams(lParams);
            } else {
                llRecord.setVisibility(View.GONE);
                recorderLength.setVisibility(View.INVISIBLE);
            }

            if ("0".equals(bean.getPraisenum()) && bean.getChild_comment() == null) {
                rlZanComment.setVisibility(View.GONE);
            } else {
                rlZanComment.setVisibility(View.VISIBLE);
                //处理点赞
                if (bean.getPraisenum() != null && !"0".equals(bean.getPraisenum())) {
                    tvZanNumber.setVisibility(View.VISIBLE);
                    tvZanNumber.setText(bean.getPraisenum() + "人点赞");
                } else {
                    tvZanNumber.setVisibility(View.GONE);
                }


                //处理评论
                if (bean.getChild_comment() != null) {
                    lvComment.setVisibility(View.VISIBLE);
                    commentList = bean.getChild_comment();
                    CommentListenMessageAdapter adapter = new CommentListenMessageAdapter(this, commentList, bean);
                    lvComment.setAdapter(adapter);
                    adapter.setListener(this);
                } else {
                    lvComment.setVisibility(View.GONE);
                }


                lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final List<ListenMessageDetailBean.ResultsBean.ChildCommentBean> finalCommentList = commentList;


                        ListenMessageDetailBean.ResultsBean.ChildCommentBean suggest = finalCommentList.get(position);
                        String id1 = suggest.getUser().getId();

                        if (!id1.equals("") && LoginUtil.getUserId().equals(id1)) {

                        } else {
                            huifuid = suggest.getUser().getId();
                            huifuName = suggest.getUser().getUser_nicename();
                            if (TextUtils.isEmpty(huifuName)) {
                                huifuName = suggest.getUser().getUser_login();
                            }
                            isComment = false;
                            showInput("@ " + huifuName);
                        }

                    }
                });
            }


        } else {//新闻动态
            llListenFriend.setVisibility(View.GONE);
            rlFriendState.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, rlFriendState.getId());
            llLike.setLayoutParams(layoutParams);


            List<ListenMessageDetailBean.ResultsBean.ChildCommentBean> commentList = new ArrayList<>();
            String headUrl = ImageUtil.changeSuggestImageAddress(bean.getUser().getAvatar());
            Glide.with(this).load(headUrl).transform(new GlideCircleTransform(this))
                    .error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang).into(header);
            String titile = bean.getUser().getUser_nicename();
            if (TextUtils.isEmpty(titile)) {
                titile = bean.getUser().getUser_login();
            }
            titile = titile + "  评论了内容";
            int index = titile.indexOf("评论了内容");
            SpannableStringBuilder builder = new SpannableStringBuilder(titile);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#5cb8e6")), 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#2e3133")), index - 1, titile.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            name.setText(builder);
            tvtime.setText(bean.getCreatetime());

            if ("0".equals(bean.getPraisenum()) && bean.getChild_comment() == null) {
                rlZanComment.setVisibility(View.GONE);
            } else {
                rlZanComment.setVisibility(View.VISIBLE);
                //处理点赞
                if (bean.getPraisenum() != null && !"0".equals(bean.getPraisenum())) {
                    tvZanNumber.setVisibility(View.VISIBLE);
                    tvZanNumber.setText(bean.getPraisenum() + "人点赞");
                } else {
                    tvZanNumber.setVisibility(View.GONE);
                }

                //处理评论
                if (bean.getChild_comment() != null) {
                    lvComment.setVisibility(View.VISIBLE);
                    commentList = bean.getChild_comment();
                    CommentListenMessageAdapter adapter = new CommentListenMessageAdapter(this, commentList, bean);
                    lvComment.setAdapter(adapter);
                    adapter.setListener(this);
                } else {
                    lvComment.setVisibility(View.GONE);
                }

            }


            SpannableString spannableString = new SpannableString(EmojiUtil.getDecodeMsg(bean.getComment()));
            ltvContent.setText(spannableString);
            if (TextUtils.isEmpty(bean.getComment())) {
                ltvContent.setVisibility(View.GONE);
            } else {
                ltvContent.setVisibility(View.VISIBLE);
            }

            //好友动态
            //新闻部分的处理


            ListenMessageDetailBean.ResultsBean.PostBean post = bean.getPost();
            String imageUrl2 = ImageUtil.changeNewsImageAddress(post.getSimpleImage());
            Glide.with(this).load(imageUrl2).into(ivNewsItem);
            tvNewsTitle.setText(post.getPost_title());

            final List<ListenMessageDetailBean.ResultsBean.ChildCommentBean> finalCommentList = commentList;


            lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ListenMessageDetailBean.ResultsBean.ChildCommentBean suggest = finalCommentList.get(position);
                    String id1 = suggest.getUser().getId();

                    if (!id1.equals("") && LoginUtil.getUserId().equals(id1)) {

                    } else {
                        huifuid = suggest.getUser().getId();
                        huifuName = suggest.getUser().getUser_nicename();
                        if (TextUtils.isEmpty(huifuName)) {
                            huifuName = suggest.getUser().getUser_login();
                        }
                        isComment = false;
                        showInput("@ " + huifuName);
                    }

                }
            });

        }

    }


    @Override
    public void onItemClick(ListenMessageDetailBean.ResultsBean.ChildCommentBean suggest, ListenMessageDetailBean.ResultsBean listenerCircle, int position) {
        String id = suggest.getId();
        if (!id.equals("") && LoginUtil.getUserId().equals(id)) {

        } else {
            huifuid = suggest.getUser().getId();
            huifuName = suggest.getUser().getUser_nicename();
            if (TextUtils.isEmpty(huifuName)) {
                huifuName = suggest.getUser().getUser_login();
            }
            isComment = false;
            showInput("@ " + huifuName);
        }
    }


    /**
     * 发送
     */
    private void send() {
        String trim = et.getText().toString().trim();
        ListenMessageDetailBean.ResultsBean.PostBean post = bean.getPost();

        if (!TextUtils.isEmpty(post.getId())) {//好友动态
            if (isComment) {
                comment_id = bean.getId();
                huifuid = bean.getUser().getId();
            }
            Map<String, String> map = new HashMap<>();
            if (trim.equals("")) {
                ToastUtils.showBottomToast("说点什么吧...");
                return;
            }
            hideInput();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("post_id", post.getId());
            map.put("post_table", "posts");
            String contents = EmojiUtil.codeMsg(trim);
            map.put("content", contents);
            if (!TextUtils.isEmpty(comment_id)) {
                map.put("comment_id", comment_id);
            }
            if (!TextUtils.isEmpty(huifuid)) {
                map.put("to_uid", huifuid);
            }
            OkGo.<SimpleMsgBean>post(UrlProvider.AddComments).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                @Override
                public void onSuccess(Response<SimpleMsgBean> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        ToastUtils.showBottomToast("发送成功!");
                    }

                }

                @Override
                public void onError(Response<SimpleMsgBean> response) {
                    super.onError(response);
                    boolean netWorkConnected = NetUtil.isHasNetAvailable(CommentListenDetailActivity.this);
                    if (!netWorkConnected) {
                        ToastUtils.showBottomToast("无网络连接");
                        return;
                    }
                    ToastUtils.showBottomToast("发送失败,请稍后重试");
                }
            });


        }

    }


    /**
     * 显示输入框和软键盘(默认et获取焦点)
     */
    private void showInput(String hint) {
        rlInput.setVisibility(View.VISIBLE);
        et.setHint(hint);
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        KeyBoardUtils.openKeyboard(et);
    }

    /**
     * 隐藏输入框和软键盘
     */
    private void hideInput() {
        rlInput.setVisibility(View.GONE);
        KeyBoardUtils.closeKeyboard(et);
    }

    //通过判断事件分发处理 点击软键盘以外的区域 关闭软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (hideInputMethod(this, v)) {
                    rlInput.setVisibility(View.GONE);
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight();
            if (event.getX() > left && event.getY() > top && event.getY() < bottom) {
                // 保留点击输入框(et和 send)的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.getInstance().release();
    }
}
