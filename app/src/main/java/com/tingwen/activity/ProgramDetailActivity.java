package com.tingwen.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.PartBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.DeleteMessageEvent;
import com.tingwen.event.FansMessageEvent;
import com.tingwen.event.FansReplayEvent;
import com.tingwen.fragment.FansChartFragment;
import com.tingwen.fragment.FansMessageFragment;
import com.tingwen.fragment.ProgramContentFragment;
import com.tingwen.fragment.ProgramPictureFragment;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.ClassBuyerInfoPop;
import com.tingwen.popupwindow.DeleteCommentPop;
import com.tingwen.popupwindow.ReplyPop;
import com.tingwen.popupwindow.ShareDialog;
import com.tingwen.utils.BitmapUtil;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.VipUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 节目(主播)详情
 * Created by Administrator on 2017/8/7 0007.
 */
public class ProgramDetailActivity extends BaseActivity implements ReplyPop.ReplyListener, DeleteCommentPop.DeleteListener, ShareDialog.ShareListener {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.ll_fan_message_container)
    LinearLayout llFanMessageContainer;
    @BindView(R.id.iv_anchor_content)
    ImageView ivAnchorContent;
    @BindView(R.id.tv_anchor_content)
    TextView tvAnchorContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.iv_anchor_fans)
    ImageView ivAnchorFans;
    @BindView(R.id.tv_anchor_fans)
    TextView tvAnchorFans;
    @BindView(R.id.ll_fans)
    LinearLayout llFans;
    @BindView(R.id.iv_anchor_messages)
    ImageView ivAnchorMessages;
    @BindView(R.id.tv_anchor_messages)
    TextView tvAnchorMessages;
    @BindView(R.id.ll_messages)
    LinearLayout llMessages;
    @BindView(R.id.iv_anchor_pic)
    ImageView ivAnchorPic;
    @BindView(R.id.tv_anchor_pic)
    TextView tvAnchorPic;
    @BindView(R.id.ll_pic)
    LinearLayout llPic;
    @BindView(R.id.iv_anchor_download)
    ImageView ivAnchorDownload;
    @BindView(R.id.tv_anchor_download)
    TextView tvAnchorDownload;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.rl_input)
    RelativeLayout rlInput;

    private PartBean part;
    private Boolean isClass;//是否是课堂
    private Boolean isNeedShowPop = false;//是否需要填写资料
    private String id="";
    private List<Fragment> fragmetList = new ArrayList<>();
    private VpAdapter mVpAdapter;
    private String images;

    //留言传递过来的信息
    private String commentId;
    private String to_uid;
    private String userNicename;
    private String userLogin;
    private String uid;
    private ReplyPop replyPop;
    private DeleteCommentPop deleteCommentPop;
    private Boolean isReply = false;

    private ShareDialog shareDialog;
    private IWXAPI wxapi;
    private Bitmap bitmap;
    private Tencent tencent;

    private ClassBuyerInfoPop buyerInfoPop;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_program_detail;
    }


    //这个界面不需要状态栏透明,设置透明以后滑动至顶部,显示效果不好
    @Override
    protected boolean isNeedStatusBarTranslucent() {
        return false;
    }


    /**
     * 跳转至该activity并传递数据
     *
     * @param context
     * @param part
     * @param isFromClass 是否是课堂
     */
    public static void actionStart(Context context, PartBean part, Boolean isFromClass, Boolean isNeedshowpop) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("part", part);
        bundle.putSerializable("isClass", isFromClass);
        bundle.putSerializable("isNeedShowPop", isNeedshowpop);
        LauncherHelper.getInstance().launcherActivity(context, ProgramDetailActivity.class, bundle);

    }


    @Override
    protected void initData() {
        super.initData();

        part = (PartBean) getIntent().getExtras().getSerializable("part");
        isClass = (Boolean) getIntent().getExtras().getSerializable("isClass");
        isNeedShowPop = (Boolean) getIntent().getExtras().getSerializable("isNeedShowPop");


        if (null!=part) {
            id = part.getId();
            tvName.setText(part.getName());
            tvDescription.setText(part.getDescription());
            tvFans.setText(part.getFan_num() + "");
            tvMessage.setText(part.getMessage_num());
            String imageUrl = part.getImages();
            if (imageUrl != null && !imageUrl.contains("ttp:")) {
                if (!imageUrl.contains("data")) {
                    imageUrl = UrlProvider.URL_IMAGE + "/data/upload/" + imageUrl;
                } else {
                    imageUrl = UrlProvider.URL_IMAGE + imageUrl;
                }
            }
            Glide.with(this).load(imageUrl).transform(new GlideCircleTransform(this)).error(R.drawable.img_touxiang).into(ivHeader);

            if (FollowUtil.isFollowed(id)) {
                tvFollow.setText("取消");
            } else {
                tvFollow.setText("关注");
            }

            // TODO: 2017/8/15 0015 这个图片之前是拿到 Post_content()的值去处理,很多没有数据直接换成头像的图片了
//            images = part.getPost_content();
//            if(images == null || images.equals("") || images.equals("null")){
            images = imageUrl;
//            }


            initFragments();
        }


        EventBus.getDefault().register(this);


    }

    @Override
    protected void initUI() {
        super.initUI();

        if (isClass != null && isClass) {
            tvFollow.setVisibility(View.GONE);
            llFanMessageContainer.setVisibility(View.GONE);
        }
        viewPager.setOffscreenPageLimit(4);
        mVpAdapter = new VpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mVpAdapter);
        initState(0);

    }

    @OnClick({R.id.ivLeft, R.id.iv_share, R.id.tv_follow, R.id.ll_content, R.id.ll_fans, R.id.ll_messages, R.id.ll_pic, R.id.ll_download, R.id.send})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.iv_share:
                if (null == shareDialog) {
                    shareDialog = new ShareDialog();
                }
                shareDialog.setListener(this);
                shareDialog.show(getFragmentManager(), "sharedialog");
                break;
            case R.id.tv_follow:
                if (!LoginUtil.isUserLogin()) {
                    new MaterialDialog.Builder(this)
                            .title("温馨提示")
                            .content("登录后才可以关注哦~")
                            .contentColorRes(R.color.text_black)
                            .negativeText("取消")
                            .positiveText("前往登录")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(ProgramDetailActivity.this, LoginActivity.class);
                                }
                            }).build().show();
                    return;
                }
                if (FollowUtil.isFollowed(id)) {
                    unFollow();
                } else {
                    follow();
                }
                break;
            case R.id.ll_content:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_fans:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_messages:
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_pic:
                viewPager.setCurrentItem(3);
                break;
            case R.id.ll_download:

                if (!LoginUtil.isUserLogin()) {
                    new MaterialDialog.Builder(this)
                            .title("温馨提示")
                            .content("您还未登录哦~")
                            .contentColorRes(R.color.text_black)
                            .negativeText("取消")
                            .positiveText("前往登录")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(ProgramDetailActivity.this, LoginActivity.class);
                                }
                            }).build().show();
                    return;
                }

                if (VipUtil.getInstance().getVipState() == 0) {
                    new MaterialDialog.Builder(this)
                            .title("温馨提示")
                            .content("您还不是会员,无法使用批量下载功能,是否前往开通会员?")
                            .contentColorRes(R.color.text_black)
                            .negativeText("取消")
                            .positiveText("开通")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(ProgramDetailActivity.this, VipActivity.class);
                                }
                            }).build().show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                LauncherHelper.getInstance().launcherActivity(this, ProgramDownloadActivity.class, bundle);
                break;
            case R.id.send:
                sendComment();

                break;

        }

    }

    /**
     * 点击留言后的处理(回复/删除)
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReplayEvent(FansReplayEvent event) {
        uid = event.getUid();
        commentId = event.getId();

        userNicename = event.getUserNicename();
        userLogin = event.getUserLogin();
        if (!LoginUtil.getUserId().equals(uid)) {//评论其他人
            replyPop = new ReplyPop(this);
            replyPop.setListener(this);
            replyPop.showPopupWindow();
        } else {//删除自己
            deleteCommentPop = new DeleteCommentPop(this);
            deleteCommentPop.setListener(this);
            deleteCommentPop.showPopupWindow();
        }


    }

    /**
     * 删除自己的留言
     */
    @Override
    public void delete() {
        deleteCommentPop.dismiss();
        if (commentId != null) {
            EventBus.getDefault().post(new DeleteMessageEvent(commentId));
        }
    }

    @Override
    public void cancel() {
        deleteCommentPop.dismiss();
        resetReply();

    }

    /**
     * 回复其他人的留言
     */
    @Override
    public void replay() {
        replyPop.dismiss();
        et.setHint("@" + userNicename);
        et.requestFocus();
        KeyBoardUtils.openKeyboard(et);
        isReply = true;

    }

    @Override
    public void cancel2() {
        replyPop.dismiss();
        resetReply();
    }

    /**
     * 留言
     */
    private void sendComment() {
        if (!LoginUtil.isUserLogin()) {
            ToastUtils.showBottomToast("登录后才可以评论哦");
            return;
        }

        String content = et.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {

            if (isReply) {
                EventBus.getDefault().post(new FansMessageEvent(1, content, commentId, uid));
                KeyBoardUtils.closeKeyboard(et);
                et.setText("");
                return;
            }

            EventBus.getDefault().post(new FansMessageEvent(0, content, commentId, uid));
            KeyBoardUtils.closeKeyboard(et);
            et.setText("");


        } else {
            ToastUtils.showBottomToast("说点什么吧");
        }
    }


    /**
     * 取消关注
     */
    private void unFollow() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_FOLLOW_ACT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    tvFollow.setText("关注");
                    FollowUtil.followPartList.remove(id);
                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });


    }

    /**
     * 关注
     */
    private void follow() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.FOLLOW_ACT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    tvFollow.setText("取消");
                    FollowUtil.followPartList.add(id);
                    ToastUtils.showBottomToast("关注成功");
                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });


    }


    @Override
    protected void setListener() {
        super.setListener();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        initState(0);
                        break;
                    case 1:
                        initState(1);
                        break;
                    case 2:
                        initState(2);
                        break;
                    case 3:
                        initState(3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 初始化Fragments
     */
    private void initFragments() {

        ProgramContentFragment contentFragment = ProgramContentFragment.newInstance(id, isClass);
        fragmetList.add(contentFragment);
        FansChartFragment fansChartFragment = FansChartFragment.newInstance(id);
        fragmetList.add(fansChartFragment);
        FansMessageFragment fansMessageFragment = FansMessageFragment.newInstance(id);
        fragmetList.add(fansMessageFragment);
        ProgramPictureFragment pictureFragment = ProgramPictureFragment.newInstance(images);
        fragmetList.add(pictureFragment);

        if (isNeedShowPop && LoginUtil.isUserLogin() && LoginUtil.getIsRecord() == 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    buyerInfoPop = new ClassBuyerInfoPop(ProgramDetailActivity.this);
                    buyerInfoPop.setOnListener(new ClassBuyerInfoPop.BuyerInfoListener() {
                        @Override
                        public void cancelCommit() {
                            buyerInfoPop.dismiss();
                        }

                        @Override
                        public void commit(String name, String phone, String weixin, String job, String city) {

                            Map<String, String> map = new HashMap<>();
                            map.put("accessToken", LoginUtil.getAccessToken());
                            map.put("name", name);
                            map.put("phone", phone);
                            map.put("wx_num", weixin);
                            map.put("city", city);
                            map.put("job", job);

                            OkGo.<SimpleMsgBean>post(UrlProvider.ADD_CLASS_BUYER_INFO).params(map).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                                @Override
                                public void onSuccess(Response<SimpleMsgBean> response) {
                                    int status = response.body().getStatus();
                                    if (status == 1) {
                                        buyerInfoPop.dismiss();
                                        ToastUtils.showBottomToast("提交成功");
                                        FollowUtil.getUserInfo();
                                    }else{
                                        String msg = response.body().getMsg();
                                        ToastUtils.showBottomToast( msg);
                                    }
                                }

                                @Override
                                public void onError(Response<SimpleMsgBean> response) {
                                    super.onError(response);
                                    ToastUtils.showBottomToast("提交失败,请稍后重试");
                                }
                            });


                        }
                    });
                    RelativeLayout rlParent = (RelativeLayout)findViewById(R.id.rl_parent);
                    buyerInfoPop.showPopupWindow(rlParent);

                }


            }, 1000);
        }


    }

    /**
     * 根据viewPager的position 设置显示状态
     *
     * @param position
     */
    private void initState(int position) {
        ivAnchorContent.setSelected(false);
        tvAnchorContent.setSelected(false);
        ivAnchorFans.setSelected(false);
        tvAnchorFans.setSelected(false);
        ivAnchorMessages.setSelected(false);
        tvAnchorMessages.setSelected(false);
        ivAnchorPic.setSelected(false);
        tvAnchorPic.setSelected(false);
        ivAnchorDownload.setSelected(false);
        tvAnchorDownload.setSelected(false);
        switch (position) {
            case 0:
                ivAnchorContent.setSelected(true);
                tvAnchorContent.setSelected(true);
                rlInput.setVisibility(View.GONE);

                break;
            case 1:
                ivAnchorFans.setSelected(true);
                tvAnchorFans.setSelected(true);
                rlInput.setVisibility(View.GONE);

                break;
            case 2:
                ivAnchorMessages.setSelected(true);
                tvAnchorMessages.setSelected(true);
                rlInput.setVisibility(View.VISIBLE);
                resetReply();
                break;
            case 3:
                ivAnchorPic.setSelected(true);
                tvAnchorPic.setSelected(true);
                rlInput.setVisibility(View.GONE);

                break;
            case 4:
                ivAnchorDownload.setSelected(true);
                tvAnchorDownload.setSelected(true);
                rlInput.setVisibility(View.GONE);

                break;

        }
    }

    /**
     * 重置留言
     */
    private void resetReply() {
        et.setHint("快留言与主播互动吧~");
        commentId = null;
        to_uid = null;
        uid = null;
        userNicename = null;
        userLogin = null;
        isReply = false;
    }

    @Override
    public void onDismiss() {
        shareDialog.dismiss();
    }

    @Override
    public void weiboShare() {
        shareDialog.dismiss();
        if (null == part) {
            return;
        }
        Intent i = new Intent(this, WBShareActivity.class);
        i.putExtra("part", part);
        startActivity(i);

    }

    @Override
    public void qqShare() {
        shareDialog.dismiss();
        if (tencent == null) {
            tencent = Tencent.createInstance(AppConfig.APP_ID, this);
        }
        qq(false);
    }

    @Override
    public void weixinShare() {
        shareDialog.dismiss();
        if (null == part) {
            return;
        }
        flag = false;
        String image = part.getImages();
        if (image != null && !image.contains("ttp:")) {
            if (!image.contains("data")) {
                image = UrlProvider.URL_IMAGE + "/data/upload/" + image;
            } else {
                image = UrlProvider.URL_IMAGE + image;
            }
        }

        // 处理缩略图片
        if (image != null && !image.equals("")) {
            if (image.contains("file")) {
                bitmap = getBitmapFromLocal(image.substring(7));
            } else {
                new GetBitmapAsy().execute(image);
                return;
            }
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tingwen_logo);
        }
        wxShare();
    }

    @Override
    public void pengyouShare() {
        shareDialog.dismiss();
        if (null == part) {
            return;
        }
        flag = true;
        String image = part.getImages();
        if (image != null && !image.contains("ttp:")) {
            if (!image.contains("data")) {
                image = UrlProvider.URL_IMAGE + "/data/upload/" + image;
            } else {
                image = UrlProvider.URL_IMAGE + image;
            }
        }

        // 处理缩略图片
        if (image != null && !image.equals("")) {
            if (image.contains("file")) {
                bitmap = getBitmapFromLocal(image.substring(7));
            } else {
                new GetBitmapAsy().execute(image);
                return;
            }
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tingwen_logo);
        }
        wxShare();

    }

    @Override
    public void qqZoneShare() {
        shareDialog.dismiss();
        if (tencent == null) {
            tencent = Tencent.createInstance(AppConfig.APP_ID, this);
        }
        qq(true);
    }

    @Override
    public void copy() {
        shareDialog.dismiss();
        if (null != part) {
            String url = UrlProvider.SHARE + part.getId() + ".html";
            copyUrl(url);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }




    private class GetBitmapAsy extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            String url = (String) params[0];
            bitmap = BitmapUtil.getBitmapFromInternet_Compress(url);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            wxShare();
        }
    }


    private Bitmap getBitmapFromLocal(String url) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, opts);

        opts.inSampleSize = calculateInSampleSize(opts, 100, 100);
        opts.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(url, opts);

        return bm;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private boolean flag = false;// 用来判断是否分享到朋友圈

    /**
     * 微信分享
     */
    private void wxShare() {

        wxapi = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, false);
        wxapi.registerApp(AppConfig.WX_APP_ID);

        String title = part.getName();
        String content = part.getDescription();

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.tingwen_logo);
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = UrlProvider.SHARE_PROGRAM + id;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = flag ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        wxapi.sendReq(req);
    }


    private void qq(boolean flag) {
        if (tencent.isSessionValid() && tencent.getOpenId() == null) {
            ToastUtils.showBottomToast("您还未安装QQ");
        }
        if (null == part) {
            return;
        }

        String title = part.getName();
        String content = part.getDescription();
        String url = UrlProvider.SHARE_PROGRAM + part.getId();
        String image = part.getImages();
        if (image != null && !image.contains("ttp:")) {
            if (!image.contains("data")) {
                image = UrlProvider.URL_IMAGE + "/data/upload/" + image;
            } else {
                image = UrlProvider.URL_IMAGE + image;
            }
        }

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, image);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "听闻");
        if (flag) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        tencent.shareToQQ(this, params, new ShareListener());
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareListener myListener = new ShareListener();
        Tencent.onActivityResultData(requestCode, resultCode, data, myListener);
    }

    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            ToastUtils.showBottomToast("取消分享");
        }

        @Override
        public void onComplete(Object arg0) {
            ToastUtils.showBottomToast("分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            ToastUtils.showBottomToast("分享出错");
        }

    }

    private void copyUrl(String url) {
        ClipData clipData = ClipData.newPlainText("copied text", url);
        ClipboardManager mClipBordManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mClipBordManager.setPrimaryClip(clipData);
        ToastUtils.showBottomToast("复制成功");
    }


    private class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragmetList.get(position);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);

            return object;
        }

        @Override
        public int getCount() {
            return fragmetList.size();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    //通过判断事件分发处理 点击软键盘以外的区域 关闭软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (hideInputMethod(this, v)) {
                    resetReply();
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
