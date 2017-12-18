package com.tingwen.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.previewlibrary.ZoomMediaLoader;
import com.tingwen.R;
import com.tingwen.adapter.ListenCircleAdapter;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.ListenCircleBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.bean.User;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.DeleteCommentPop;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.MediaManager;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.ImageLoader;
import com.tingwen.widget.LevelView;
import com.tingwen.widget.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人主页(自己/好友)
 * Created by Administrator on 2017/9/22 0022.
 */
public class PersonalHomePageActivity extends BaseActivity implements ListenCircleAdapter.ListenFriendListener {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.lv)
    LevelView lv;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.ll_fans)
    LinearLayout llFans;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.ll_follow)
    LinearLayout llFollow;
    @BindView(R.id.rlv_news)
    LRecyclerView rlvNews;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.rl_input)
    RelativeLayout rlInput;

    private User userInfo;
    private String id;
    private int page = 1;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<ListenCircleBean.ResultsBean> list;
    private ListenCircleAdapter listenCircleAdapter;
    private ListenCircleBean.ResultsBean currentSelectedListenerCircle;//当前选中的对象
    private DeleteCommentPop deleteCommentPop;
    private String huifuId = "";
    private String huifuName = "";
    private String comment_id = "";
    private String content;//输入的内容
    private boolean isComment;//是否是发表评论
    private String headUrl;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_homepage;
    }


    @Override
    protected void initData() {
        super.initData();
        id = getIntent().getStringExtra("id");
        list = new ArrayList<>();
        rlvNews.setLayoutManager(new LinearLayoutManager(this));
        listenCircleAdapter = new ListenCircleAdapter(this, list);
        listenCircleAdapter.setIsListenCircle(false);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(listenCircleAdapter);
        rlvNews.setAdapter(lRecyclerViewAdapter);
        rlvNews.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rlvNews.setArrowImageView(R.drawable.arrow);
        //设置头部部加载颜色
        rlvNews.setHeaderViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载颜色
        rlvNews.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvNews.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");
        ZoomMediaLoader.getInstance().init(new ImageLoader());

    }

    @Override
    protected void initUI() {
        super.initUI();
        if (!TextUtils.isEmpty(id)) {
            mProgressHUD.show();
            getUserInfo();
        }
    }

    @OnClick({R.id.ivLeft, R.id.send, R.id.ll_fans, R.id.ll_follow, R.id.iv_right, R.id.iv_header})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.iv_right:
                checkInfo();
                break;
            case R.id.iv_header:

                if (null != userInfo) {
                    ArrayList<String> imagesList = new ArrayList<>();
                    imagesList.add(headUrl);
                    ImagesActivity.getImagesActivityInstance(this, imagesList, 0);
                    overridePendingTransition(R.anim.image_in, 0);
                }
                break;

            case R.id.send:
                if (LoginUtil.isUserLogin()) {
                    send();
                } else {

                }
                break;

            case R.id.ll_fans:
                goFans();
                break;
            case R.id.ll_follow:
                goFollows();
                break;
            default:
                break;
        }
    }


    @Override
    protected void setListener() {
        super.setListener();
        rlvNews.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData(page);
            }
        });

        rlvNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        listenCircleAdapter.setListener(this);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        OkGo.<User>post(UrlProvider.GET_USER_INFO).params(map, true).tag(this).execute(new SimpleJsonCallback<User>(User.class) {
            @Override
            public void onSuccess(Response<User> response) {
                userInfo = response.body();
                setUserInfo();
                loadData(1);

            }

            @Override
            public void onError(Response<User> response) {
                super.onError(response);
                mProgressHUD.dismiss();
            }
        });


    }

    /**
     * 设置数据
     */
    private void setUserInfo() {


        String avatar = userInfo.getResults().getAvatar();
        String level = userInfo.getResults().getLevel();
        String signature = userInfo.getResults().getSignature();
        String guan_num = userInfo.getResults().getGuan_num();
        String fan_num = userInfo.getResults().getFan_num();

        id = userInfo.getResults().getId();

        Logger.e("获取的id"+id);

        headUrl = ImageUtil.changeSuggestImageAddress(avatar);
        Glide.with(this).load(headUrl).transform(new GlideCircleTransform(this)).error(R.drawable.img_touxiang).into(ivHeader);
        if (!TextUtils.isEmpty(level)) {
            lv.setLevel(Integer.parseInt(level));
        }

        if (TextUtils.isEmpty(signature)) {
            signature = "暂无简介";
        }
        tvDescription.setText("简介：" + signature);
        tvFollow.setText(guan_num);
        tvFans.setText(fan_num);

        if (id != null && LoginUtil.getUserId().equals(id)) {
            ivRight.setImageResource(R.drawable.ic_edit);
        } else {


            if (id != null && FollowUtil.isAttentioned(id) && FollowUtil.isFans(id)) {
                ivRight.setBackgroundResource(R.drawable.ic_follow_and_fan);
                ivRight.setContentDescription("互相关注");

            } else if (id != null && FollowUtil.isAttentioned(id)&&!FollowUtil.isFans(id)) {
                ivRight.setBackgroundResource(R.drawable.ic_unfollow);
                ivRight.setContentDescription("取消关注");

            }else if (id != null&& !FollowUtil.isAttentioned(id)&&FollowUtil.isFans(id)) {
                ivRight.setBackgroundResource(R.drawable.ic_addattention);
                ivRight.setContentDescription("添加关注");

            }else{
                ivRight.setBackgroundResource(R.drawable.ic_addattention);
                ivRight.setContentDescription("添加关注");

            }

        }


    }

    /**
     * 右侧按钮点击,区分事件
     */
    private void checkInfo() {
        if (id != null && LoginUtil.getUserId().equals(id)) {//是自己跳转资料编辑界面
            LauncherHelper.getInstance().launcherActivity(this, EditInfoActivity.class);
        } else {

            if (id!= null && FollowUtil.isAttentioned(id) && FollowUtil.isFans(id)) {
                ivRight.setBackgroundResource(R.drawable.ic_follow_and_fan);
                ivRight.setContentDescription("互相关注");
                unFollow();
            } else if (id != null && FollowUtil.isAttentioned(id)&&!FollowUtil.isFans(id)) {
                unFollow();

            } else if (id != null&& !FollowUtil.isAttentioned(id)&&FollowUtil.isFans(id)) {
                addFollow();
            }else{
                addFollow();
            }
        }

    }

    /**
     * 取消关注
     */
    private void unFollow() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("uid_2", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_ATTENTION).tag(this).params(map).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    ToastUtils.showBottomToast("取消关注成功");
                    ivRight.setBackgroundResource(R.drawable.ic_addattention);
                    FollowUtil.attentionsList.remove(id);
                }
            }
        });

    }

    /**
     * 添加关注
     */
    private void addFollow() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("uid_2", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.ADD_ATTENTION).tag(this).params(map).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    ToastUtils.showBottomToast("关注成功");
                    ivRight.setBackgroundResource(R.drawable.ic_unfollow);
                    FollowUtil.attentionsList.add(id);
                }
            }
        });


    }

    /**
     * 获取动态数据
     */
    private void loadData(final int page) {
        mProgressHUD.dismiss();
        Map<String, String> map = new HashMap<>();
        String code = null;
        try {
            code = LoginUtil.encode(LoginUtil.AESCODE, userInfo.getResults().getUser_login().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("accessToken", code);
        map.put("page", page + "");
        OkGo.<ListenCircleBean>post(UrlProvider.MY_DYNAMICS).params(map, true).tag(this).execute(new SimpleJsonCallback<ListenCircleBean>(ListenCircleBean.class) {
            @Override
            public void onSuccess(Response<ListenCircleBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    list = response.body().getResults();
                    if (page == 1) {
                        listenCircleAdapter.setDataList(list);
                    } else {
                        listenCircleAdapter.addAll(list);
                    }
                    rlvNews.refreshComplete(10);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    if (page > 1 && list.size() < 10) {
                        rlvNews.setNoMore(true);
                    }

                }


            }

            @Override
            public void onError(Response<ListenCircleBean> response) {
                super.onError(response);
                boolean netWorkConnected = NetUtil.isHasNetAvailable(PersonalHomePageActivity.this);
                if (!netWorkConnected) {
                    ToastUtils.showBottomToast("无网络连接!");
                    return;
                }
                if (page == 1) {
                    ToastUtils.showBottomToast("暂无数据");
                    return;
                }
                if (page > 1) {
                    rlvNews.setNoMore(true);
                }
            }
        });

    }

    /**
     * 发送
     */
    private void send() {
        content = et.getText().toString().trim();
        ListenCircleBean.ResultsBean.PostBean post = currentSelectedListenerCircle.getPost();

        if (!TextUtils.isEmpty(post.getId())) {//好友动态
            if (isComment) {
                comment_id = currentSelectedListenerCircle.getId();
                huifuId = currentSelectedListenerCircle.getUser().getId();
            }
            Map<String, String> map = new HashMap<>();
            if (content.equals("")) {
                ToastUtils.showBottomToast("说点什么吧...");
                return;
            }
            hideInput();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("post_id", post.getId());
            map.put("post_table", "posts");
            String contents = EmojiUtil.codeMsg(content);
            map.put("content", contents);
            if (!TextUtils.isEmpty(comment_id)) {
                map.put("comment_id", comment_id);
            }
            if (!TextUtils.isEmpty(huifuId)) {
                map.put("to_uid", huifuId);
            }
            OkGo.<SimpleMsgBean>post(UrlProvider.AddComments).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                @Override
                public void onSuccess(Response<SimpleMsgBean> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        ToastUtils.showBottomToast("发送成功!");
                        addComment();
                    }

                }

                @Override
                public void onError(Response<SimpleMsgBean> response) {
                    super.onError(response);
                    boolean netWorkConnected = NetUtil.isHasNetAvailable(PersonalHomePageActivity.this);
                    if (!netWorkConnected) {
                        ToastUtils.showBottomToast("无网络连接");
                        return;
                    }
                    ToastUtils.showBottomToast("发送失败,请稍后重试");
                }
            });


        } else {//听友圈动态
            if (content.equals("")) {
                ToastUtils.showBottomToast("说点什么吧...");
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("comment_id", currentSelectedListenerCircle.getId());
            if (isComment) {
                map.put("to_uid", "0");
            } else {
                map.put("to_uid", huifuId);
            }
            map.put("content", EmojiUtil.codeMsg(et.getText().toString()));
            hideInput();
            OkGo.<SimpleMsgBean>post(UrlProvider.SEND_COMMENT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                @Override
                public void onSuccess(Response<SimpleMsgBean> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        ToastUtils.showBottomToast("发送成功!");
                        addComment();
                    }

                }

                @Override
                public void onError(Response<SimpleMsgBean> response) {
                    super.onError(response);
                    boolean netWorkConnected = NetUtil.isHasNetAvailable(PersonalHomePageActivity.this);
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
     * 添加评论(本地数据改变)
     */
    private void addComment() {
        List<ListenCircleBean.ResultsBean.ChildCommentBean> CommentList = currentSelectedListenerCircle.getChild_comment();
        ListenCircleBean.ResultsBean.ChildCommentBean commentBean = new ListenCircleBean.ResultsBean.ChildCommentBean();
        commentBean.setComment(content);

        ListenCircleBean.ResultsBean.ChildCommentBean.UserBean userBean = new ListenCircleBean.ResultsBean.ChildCommentBean.UserBean();
        String nicename = AppSpUtil.getInstance().getUserInfo().getResults().getUser_nicename();
        if (TextUtils.isEmpty(nicename)) {
            nicename = AppSpUtil.getInstance().getUserInfo().getResults().getUser_login();
        }
        userBean.setUser_nicename(nicename);
        commentBean.setUser(userBean);
        if (!isComment) {
            ListenCircleBean.ResultsBean.ChildCommentBean.ToUserBean touser = new ListenCircleBean.ResultsBean.ChildCommentBean.ToUserBean();
            touser.setUser_nicename(huifuName);
            commentBean.setTo_user(touser);
        }
        if (CommentList == null) {
            CommentList = new ArrayList<>();
        }
        CommentList.add(commentBean);
        currentSelectedListenerCircle.setChild_comment(CommentList);
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 前往关注列表
     */
    private void goFollows() {
        if (userInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putString("name", userInfo.getResults().getUser_nicename());
            bundle.putString("user_login", userInfo.getResults().getUser_login());
            LauncherHelper.getInstance().launcherActivity(this, FollowListActivity.class, bundle);
        }
    }

    /**
     * 前往粉丝列表
     */
    private void goFans() {

        if (userInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putString("name", userInfo.getResults().getUser_nicename());
            bundle.putString("user_login", userInfo.getResults().getUser_login());
            LauncherHelper.getInstance().launcherActivity(this, FansListActivity.class, bundle);
        }


    }

    //头像长按
    @Override
    public void showLongClick(ListenCircleBean.ResultsBean listenerCircle) {

    }

    //回复
    @Override
    public void doHuifu(ListenCircleBean.ResultsBean listenerCircle, ListenCircleBean.ResultsBean.ChildCommentBean suggest, int position) {
        currentSelectedListenerCircle = listenerCircle;
        huifuId = suggest.getUser().getId();
        huifuName = suggest.getUser().getUser_nicename();
        if (TextUtils.isEmpty(huifuName)) {
            huifuName = suggest.getUser().getUser_login();
        }
        isComment = false;
        showInput("@ " + huifuName);
    }

    //评论
    @Override
    public void doComment(ListenCircleBean.ResultsBean listenerCircle, int position) {
        currentSelectedListenerCircle = listenerCircle;
        isComment = true;
        showInput("");

    }

    //点赞
    @Override
    public void doZan(ListenCircleBean.ResultsBean listenerCircle, int position) {
        currentSelectedListenerCircle = listenerCircle;
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("comments_id", currentSelectedListenerCircle.getId());
        OkGo.<SimpleMsgBean>post(UrlProvider.COMMENT_ZAN).params(map, true).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    if (!TextUtils.isEmpty(currentSelectedListenerCircle.getPraisenum())) {
                        int zan = Integer.valueOf(currentSelectedListenerCircle.getPraisenum());
                        if (currentSelectedListenerCircle.getZan() == 1) {
                            currentSelectedListenerCircle.setPraisenum(--zan + "");
                            currentSelectedListenerCircle.setZan(0);
                        } else {
                            currentSelectedListenerCircle.setPraisenum(++zan + "");
                            currentSelectedListenerCircle.setZan(1);
                        }
                    }
                    lRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //删除自己的评论
    @Override
    public void deleteMyComment(ListenCircleBean.ResultsBean listenerCircle, final int position, final String id) {
        currentSelectedListenerCircle = listenerCircle;
        deleteCommentPop = new DeleteCommentPop(this);
        deleteCommentPop.setListener(new DeleteCommentPop.DeleteListener() {
            @Override
            public void delete() {
                List<ListenCircleBean.ResultsBean.ChildCommentBean> list = currentSelectedListenerCircle.getChild_comment();
                list.remove(list.get(position));
                lRecyclerViewAdapter.notifyDataSetChanged();
                deleteCommentPop.dismiss();
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", LoginUtil.getAccessToken());
                map.put("id", id);
                OkGo.<SimpleMsgBean>post(UrlProvider.DELETE_SELF_COMMENT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                    @Override
                    public void onSuccess(Response<SimpleMsgBean> response) {
                        int status = response.body().getStatus();
                        if (status == 1) {
                            ToastUtils.showBottomToast("删除成功");
                        }
                    }
                });
            }

            @Override
            public void cancel() {
                deleteCommentPop.dismiss();
            }
        });
        deleteCommentPop.showPopupWindow();
    }


    //删除整个动态
    @Override
    public void delete(ListenCircleBean.ResultsBean listenerCircle, final int position) {
        currentSelectedListenerCircle = listenerCircle;
        deleteCommentPop = new DeleteCommentPop(this);
        deleteCommentPop.setListener(new DeleteCommentPop.DeleteListener() {
            @Override
            public void delete() {
                listenCircleAdapter.getDataList().remove(position);
                lRecyclerViewAdapter.notifyDataSetChanged();
                deleteCommentPop.dismiss();
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", LoginUtil.getAccessToken());
                map.put("id", currentSelectedListenerCircle.getId());
                OkGo.<SimpleMsgBean>post(UrlProvider.DELETE_COMMENT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                    @Override
                    public void onSuccess(Response<SimpleMsgBean> response) {
                        int status = response.body().getStatus();
                        if (status == 1) {
                            ToastUtils.showBottomToast("删除成功");
                        }
                    }
                });
            }

            @Override
            public void cancel() {
                deleteCommentPop.dismiss();
            }
        });
        deleteCommentPop.showPopupWindow();
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
        OkGo.getInstance().cancelTag(this);
        MediaManager.getInstance().release();
    }

}
