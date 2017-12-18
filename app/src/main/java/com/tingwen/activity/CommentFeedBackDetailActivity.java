package com.tingwen.activity;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.adapter.FeedbackDetailCommentAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.CommentDetailBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.TimesUtil;
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
 * 意见反馈消息详情
 * Created by Administrator on 2017/11/13 0013.
 */
public class CommentFeedBackDetailActivity extends BaseActivity implements FeedbackDetailCommentAdapter.CommentListener {

    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView tvtime;
    @BindView(R.id.content)
    LapTextView content;
    @BindView(R.id.tv_lap)
    TextView tvLap;
    @BindView(R.id.ll)
    LinearLayout ll;
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
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    private String id;
    private CommentDetailBean.ResultsBean bean;
    private boolean isComment;//是否是发表评论
    private String huifuid;
    private String huifuName;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitivity_comment_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        id = getIntent().getStringExtra("id");
        getDetail();
    }


    @Override
    protected void setListener() {
        super.setListener();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 获取评论详情
     */
    private void getDetail() {

        Map<String, String> map = new HashMap<>();

        map.put("accessToken", LoginUtil.getAccessToken());

        map.put("feedback_id", id);

        OkGo.<CommentDetailBean>post(UrlProvider.COMMENT_DETAIL).params(map).tag(this).execute(new SimpleJsonCallback<CommentDetailBean>(CommentDetailBean.class) {
            @Override
            public void onSuccess(Response<CommentDetailBean> response) {
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
        llTop.setVisibility(View.VISIBLE);
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
        long time = 0;
        if (bean.getCreate_time() != null && !bean.getCreate_time().equals("")) {
            time = Long.parseLong(bean.getCreate_time());
        }
        tvtime.setText(TimesUtil.setDataFormat(time));


        //图片处理
        if (bean.getImages() != null) {
            String[] images = bean.getImages().split(",");
            if (images.length == 1 && !images[0].equals("")) {
                ivSingle.setVisibility(View.VISIBLE);
                ngl.setVisibility(View.GONE);
                final String imageUrl = ImageUtil.changeSuggestImageAddress(images[0]);
                Glide.with(this).load(imageUrl).into(ivSingle);


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


        SpannableString spannableString = new SpannableString(EmojiUtil.getDecodeMsg(bean.getComment()));
        content.setText(spannableString);
        if (TextUtils.isEmpty(bean.getComment())) {
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
        }


        //处理点赞和评论
        if ("0".equals(bean.getZan_num()) && bean.getChild_comment() == null) {
            rlZanComment.setVisibility(View.GONE);
        } else {
            rlZanComment.setVisibility(View.VISIBLE);
            //处理评论
            List<CommentDetailBean.ResultsBean.ChildCommentBean> commentList = new ArrayList<>();

            if (bean.getChild_comment() != null) {
                lvComment.setVisibility(View.VISIBLE);
                commentList = bean.getChild_comment();

                FeedbackDetailCommentAdapter commentAdapter = new FeedbackDetailCommentAdapter(CommentFeedBackDetailActivity.this, commentList, bean);
                commentAdapter.setListener(this);
                lvComment.setAdapter(commentAdapter);

            } else {

                lvComment.setVisibility(View.GONE);
            }

            //处理点赞
            int zan = Integer.parseInt(bean.getZan_num());
            if (zan != 0) {
                tvZanNumber.setVisibility(View.VISIBLE);
                tvZanNumber.setText(zan + "人点赞");
            } else {
                tvZanNumber.setVisibility(View.GONE);
            }
            final List<CommentDetailBean.ResultsBean.ChildCommentBean> finalCommentList = commentList;

            lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CommentDetailBean.ResultsBean.ChildCommentBean suggest = finalCommentList.get(position);
                    String id1 = suggest.getUser().getId();
//                    String commentID = suggest.getId();
                    if (!id1.equals("") && LoginUtil.getUserId().equals(id1)) {
//                        mFeedBackListener.deleteMyComment(bean, position, commentID);
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
    public void onItemClick(CommentDetailBean.ResultsBean.ChildCommentBean suggest, CommentDetailBean.ResultsBean feedback, int position) {
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
        if (trim.equals("")) {
            ToastUtils.showBottomToast("说点什么吧...");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        if (isComment) {
            map.put("tuid", bean.getUser().getId());
        } else {
            map.put("tuid", huifuid);
            map.put("is_comment", "1");
        }
        map.put("to_comment_id", bean.getId());
        map.put("comment", EmojiUtil.codeMsg(et.getText().toString()));
        map.put("images", "");
        hideInput();

        OkGo.<SimpleMsgBean>post(UrlProvider.SEND_SUGGEST).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
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
                boolean netWorkConnected = NetUtil.isHasNetAvailable(CommentFeedBackDetailActivity.this);
                if (!netWorkConnected) {
                    ToastUtils.showBottomToast("无网络连接");
                    return;
                }
                ToastUtils.showBottomToast("发送失败,请稍后重试");
            }
        });


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


}
