package com.tingwen.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.tingwen.adapter.FeedBackAdapter;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.FeedBackBean;
import com.tingwen.bean.FeedBackMessageBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.HasReadMessageEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.PhotoChoosePop;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.NewMessageUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;
import com.tingwen.widget.CommonHeader;
import com.tingwen.widget.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 意见反馈
 * Created by Administrator on 2017/10/17 0017.
 */
public class FeedbackActivity extends BaseActivity implements FeedBackAdapter.FeedBackListener, PhotoChoosePop.PhotoChooseListener, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rlv_feed_back)
    LRecyclerView rlvFeedBack;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.rl_input)
    RelativeLayout rlInput;


    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<FeedBackBean.ResultsBean> list;
    private FeedBackAdapter adapter;
    private int page = 1;//分页
    private FeedBackBean.ResultsBean currentSelectedFeedback;//当前选中的对象
    private boolean isComment;//是否是发表评论
    private String huifuId = "";
    private String huifuName = "";
    private String comment_id = "";
    private String content;//输入的内容
    private PhotoChoosePop photoChoosePop;
    public static final int STORAGE_CAMERA = 101;
    private final static String FILENAME = "tem.jpg";
    private String TEMP_IMAGE;
    private final int RESULT_PHOTO = 200;
    private final int RESULT_PHOTO_BUCKET = 300;
    private CommonHeader head;//新消息头部
    private List<FeedBackMessageBean.ResultsBean> feedBackList;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        rlvFeedBack.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedBackAdapter(this, list);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rlvFeedBack.setAdapter(lRecyclerViewAdapter);
        rlvFeedBack.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rlvFeedBack.setArrowImageView(R.drawable.arrow);
        //设置头部部加载颜色
        rlvFeedBack.setHeaderViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载颜色
        rlvFeedBack.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvFeedBack.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");

        ZoomMediaLoader.getInstance().init(new ImageLoader());
        EventBus.getDefault().register(this);
    }


    @Override
    protected void initUI() {
        super.initUI();
        mProgressHUD.show();
        addHead();
        loadData(1);
    }

    /**
     * 添加新消息头部
     */
    private void addHead() {
        feedBackList = NewMessageUtil.getInstance(this).getFeedBackList();
        if (null != feedBackList && feedBackList.size() != 0) {
            head = new CommonHeader(this, R.layout.header_new_message);
            lRecyclerViewAdapter.addHeaderView(head);
            head.findViewById(R.id.rl_news).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LauncherHelper.getInstance().launcherActivity(FeedbackActivity.this, FeedBackMessageActivity.class);

                }
            });
        }
    }

    @OnClick({R.id.ivLeft, R.id.send})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.send:
                boolean userLogin = LoginUtil.isUserLogin();
                if (userLogin) {
                    send();
                } else {
                    ToastUtils.showBottomToast("还没有登录哟");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();

        rlvFeedBack.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData(page);
            }
        });

        rlvFeedBack.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPublish(0);
            }
        });

        ivRight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                goPublish(1);
                return true;
            }
        });

        adapter.setListener(this);
        TouchUtil.setTouchDelegate(ivRight, 5);
        TouchUtil.setTouchDelegate(ivLeft, 5);
    }
    /**
     * 已经阅读了新消息,移除头部及数据
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHasReadMessageEvent(HasReadMessageEvent event) {
        if(event.getType()==1){
            lRecyclerViewAdapter.removeHeaderView();
            feedBackList=null;
            head = null;
            NewMessageUtil.getInstance(this).clearFeedBackList();
        }
    }

    /**
     * 发布
     *
     * @param i 0 点击  1 长按
     */
    private void goPublish(int i) {
        if (i == 0) {
            checkPerm();
        } else if (i == 1) {
            PublishCircleActivity.getInstance(this, null, false, 1);
        }
    }

    /**
     * 获取数据
     *
     * @param pageNum
     */
    private void loadData(int pageNum) {
        page = pageNum;
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("page", pageNum + "");
        OkGo.<FeedBackBean>post(UrlProvider.GET_SUGGEST).tag(this).params(map, true).execute(new SimpleJsonCallback<FeedBackBean>(FeedBackBean.class) {
            @Override
            public void onSuccess(Response<FeedBackBean> response) {
                mProgressHUD.dismiss();
                list = response.body().getResults();
                if (page == 1) {
                    adapter.setDataList(list);
                    setHead();
                } else {
                    adapter.addAll(list);
                }
                if(null!=rlvFeedBack){
                    rlvFeedBack.refreshComplete(10);
                }

                lRecyclerViewAdapter.notifyDataSetChanged();
            }
        });


    }

    /**
     * 设置新消息数据
     */
    private void setHead() {
        if (null != head) {
            head.findViewById(R.id.rl_news).setVisibility(View.VISIBLE);
            ImageView ivHead = (ImageView) head.findViewById(R.id.iv_head);
            TextView message = (TextView) head.findViewById(R.id.tv_message);
            FeedBackMessageBean.ResultsBean bean = feedBackList.get(0);
            String avatar = bean.getUser().getAvatar();
            if (avatar != null) {
                if (!avatar.startsWith("http:")) {
                    avatar = UrlProvider.URL_IMAGE_USER + avatar;
                }
            }
            Glide.with(this).load(avatar).transform(new GlideCircleTransform(this))
                    .error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang)
                    .into(ivHead);
            message.setText(feedBackList.size() + "条新消息");
        }
    }

    /**
     * 发送
     */
    private void send() {
        content = et.getText().toString().trim();
        if (content.equals("")) {
            ToastUtils.showBottomToast("说点什么吧...");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        if (isComment) {
            map.put("tuid", currentSelectedFeedback.getUser().getId());
        } else {
            map.put("tuid", huifuId);
            map.put("is_comment", "1");
        }
        map.put("to_comment_id", currentSelectedFeedback.getId());
        map.put("comment", EmojiUtil.codeMsg(et.getText().toString()));
        map.put("images", "");
        hideInput();

        OkGo.<SimpleMsgBean>post(UrlProvider.SEND_SUGGEST).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    addComment();
                    ToastUtils.showBottomToast("发送成功!");

                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                boolean netWorkConnected = NetUtil.isHasNetAvailable(FeedbackActivity.this);
                if (!netWorkConnected) {
                    ToastUtils.showBottomToast("无网络连接");
                    return;
                }
                ToastUtils.showBottomToast("发送失败,请稍后重试");
            }
        });


    }

    /**
     * 添加评论(本地数据改变)
     */
    private void addComment() {
        List<FeedBackBean.ResultsBean.ChildCommentBean> CommentList = currentSelectedFeedback.getChild_comment();
        FeedBackBean.ResultsBean.ChildCommentBean commentBean = new FeedBackBean.ResultsBean.ChildCommentBean();
        commentBean.setComment(content);

        FeedBackBean.ResultsBean.ChildCommentBean.UserBean userBean = new FeedBackBean.ResultsBean.ChildCommentBean.UserBean();
        String nicename = AppSpUtil.getInstance().getUserInfo().getResults().getUser_nicename();
        if (TextUtils.isEmpty(nicename)) {
            nicename = AppSpUtil.getInstance().getUserInfo().getResults().getUser_login();
        }
        userBean.setUser_nicename(nicename);
        commentBean.setUser(userBean);
        if (!isComment) {
            FeedBackBean.ResultsBean.ChildCommentBean.ToUserBean touser = new FeedBackBean.ResultsBean.ChildCommentBean.ToUserBean();
            touser.setUser_nicename(huifuName);
            commentBean.setTo_user(touser);
        }
        if (CommentList == null) {
            CommentList = new ArrayList<>();
        }
        CommentList.add(commentBean);
        currentSelectedFeedback.setChild_comment(CommentList);
        lRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void doHuifu(FeedBackBean.ResultsBean resultsBean, FeedBackBean.ResultsBean.ChildCommentBean suggest, int position) {
        currentSelectedFeedback = resultsBean;
        huifuId = suggest.getUser().getId();
        huifuName = suggest.getUser().getUser_nicename();
        if (TextUtils.isEmpty(huifuName)) {
            huifuName = suggest.getUser().getUser_login();
        }
        isComment = false;
        showInput("@ " + huifuName);
    }

    @Override
    public void doComment(FeedBackBean.ResultsBean resultsBean, int position) {
        currentSelectedFeedback = resultsBean;
        isComment = true;
        showInput("");
    }

    @Override
    public void doZan(FeedBackBean.ResultsBean resultsBean, int position) {
        currentSelectedFeedback = resultsBean;
        int is_zan = resultsBean.getIs_zan();
        if (is_zan == 1) {
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("feedback_id", currentSelectedFeedback.getId());
            OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_ZAN).params(map, true).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                @Override
                public void onSuccess(Response<SimpleMsgBean> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        int zan = Integer.valueOf(currentSelectedFeedback.getZan_num());
                        currentSelectedFeedback.setZan_num(--zan);
                        currentSelectedFeedback.setIs_zan(0);
                        lRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            });
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("feedback_id", currentSelectedFeedback.getId());
            OkGo.<SimpleMsgBean>post(UrlProvider.SEND_ZAN).params(map, true).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                @Override
                public void onSuccess(Response<SimpleMsgBean> response) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        int zan = Integer.valueOf(currentSelectedFeedback.getZan_num());
                        currentSelectedFeedback.setZan_num(++zan);
                        currentSelectedFeedback.setIs_zan(1);
                        lRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            });
        }


    }

    /**
     * 显示输入框和软键盘(默认et获取焦点)
     */
    private void showInput(String hint) {
        rlInput.setVisibility(View.VISIBLE);
        et.setText("");
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(STORAGE_CAMERA)
    private void checkPerm() {
        String[] params = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, params)) {
            photoChoosePop = new PhotoChoosePop(FeedbackActivity.this);
            photoChoosePop.setListener(this);
            photoChoosePop.showPopupWindow();

        } else {
            EasyPermissions.requestPermissions(this, "是否允许文件读写,拍照,录音权限，以方便使用接下来的功能？", STORAGE_CAMERA, params);
        }


    }


    /**
     * 相册选择
     */
    @Override
    public void album() {
        photoChoosePop.dismiss();
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.putExtra("limit", 9);
        startActivityForResult(intent, RESULT_PHOTO_BUCKET);

    }

    /**
     * 拍照
     */
    @Override
    public void camera() {
        photoChoosePop.dismiss();
        File dir = new File(AppConfig.EXTRASTROGEDOWNLOADPATH + System.currentTimeMillis() + FILENAME);
        if (!dir.exists()) {
            dir.mkdir();
        }
        TEMP_IMAGE = AppConfig.EXTRASTROGEDOWNLOADPATH + System.currentTimeMillis() + "temp.jpg";
        File file = new File(TEMP_IMAGE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri uri;
        if (android.os.Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            //适配7.0
            uri = FileProvider.getUriForFile(this, "com.tingwen.provider", file);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, RESULT_PHOTO);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case RESULT_PHOTO:
                    PublishCircleActivity.getInstance(this, new String[]{TEMP_IMAGE}, false, 1);
                    break;
                case RESULT_PHOTO_BUCKET:
                    if (data != null) {
                        String[] images = data.getStringArrayExtra("images");
                        PublishCircleActivity.getInstance(this, images, false, 1);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }
}
