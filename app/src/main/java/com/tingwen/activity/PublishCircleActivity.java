package com.tingwen.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.bean.UpImageBean;
import com.tingwen.bean.VoiceBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.PhotoChoosePop;
import com.tingwen.utils.BitmapUtil;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.MediaManager;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.AdapterView;
import com.tingwen.widget.AudioRecordButton;

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
 * 发表朋友圈
 * Created by Administrator on 2017/9/8 0008.
 */
public class PublishCircleActivity extends BaseActivity implements PhotoChoosePop.PhotoChooseListener, EasyPermissions.PermissionCallbacks {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.id_recorder_anim)
    View recorderAnim;
    @BindView(R.id.recorder_length)
    RelativeLayout recorder;
    @BindView(R.id.recorder_time)
    TextView recorderTime;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.av)
    AdapterView av;
    @BindView(R.id.recordButton)
    AudioRecordButton recordButton;
    private boolean isNeedRecord;//是否有语音
    private int mMinItemWith;//对话框的最小宽度
    private int mMaxItemWith;//对话框的最小宽度
    private ImageView moreImageView;//添加更多图片
    private String[] imagePaths;
    private List<String> imagePathList = new ArrayList<>();
    public static final int STORAGE_CAMERA = 102;
    private PhotoChoosePop photoChoosePop;
    private String TEMP_IMAGE;
    private final int RESULT_IMAGE = 100;
    private final int RESULT_PHOTO = 200;
    private final int RESULT_PHOTO_BUCKET = 300;

    private String recordFile;//本地录音地址
    private String recordLength;//语音时长
    private File[] files;
    private String filepath = "";//上传音频后返回的地址
    private String imagepath="";//上传图片后返回的地址
    private boolean hasUpImage = false;//图片是否已经上传
    private boolean hasUpVoice = false;//音频是否已经上传
    private int fromWhere;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_publish_circle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imagePaths = savedInstanceState.getStringArray("imagePaths");
        } else {
            imagePaths = getIntent().getStringArrayExtra("imagePaths");

        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray("imagePaths", imagePaths);
        super.onSaveInstanceState(outState);
    }

    /**
     *
     * @param context
     * @param imagePaths
     * @param isNeedRecord
     * @param from 0: 听友圈  1:意见反馈
     */
    public static void getInstance(Context context, String[] imagePaths, boolean isNeedRecord,int from) {

        Intent intent = new Intent(context, PublishCircleActivity.class);
        if (imagePaths != null) {
            intent.putExtra("imagePaths", imagePaths);
        }
        intent.putExtra("need_record", isNeedRecord);
        intent.putExtra("from_where", from);
        context.startActivity(intent);

    }

    @Override
    protected void initData() {
        super.initData();
        isNeedRecord = getIntent().getBooleanExtra("need_record", false);
        fromWhere = getIntent().getIntExtra("from_where", -1);
        int screenWidth = SizeUtil.getScreenWidth();
        mMaxItemWith = (int) (screenWidth * 0.7f);
        mMinItemWith = (int) (screenWidth * 0.30f);

        if (isNeedRecord) {
            moreImageView = getMoreImageView();
            av.addView(moreImageView);
        }

        if (imagePaths != null) {
            for (int i = 0; i <imagePaths.length ; i++) {
                String imagePath = imagePaths[i];
                if (imagePath != null && !imagePath.isEmpty()) {
                    adapterViewAddChildView(getImageView(imagePath));
                }
            }

        }

    }


    @Override
    protected void initUI() {
        super.initUI();
        if (!isNeedRecord) {
            llRecord.setVisibility(View.GONE);
            recordButton.setVisibility(View.GONE);
        } else {
            tvDelete.setVisibility(View.GONE);
        }


    }


    @Override
    protected void setListener() {
        super.setListener();

        if (moreImageView != null) {
            moreImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPerm();
                }
            });
        }

        recordButton.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                recorder.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.VISIBLE);
                recorderTime.setText(Math.round(seconds) + "\"");
                ViewGroup.LayoutParams lParams = recorder.getLayoutParams();
                int width = (int) (mMinItemWith + mMaxItemWith / 60f * seconds);
                if (width > mMaxItemWith) {
                    lParams.width = mMaxItemWith;
                } else {
                    lParams.width = width;
                }
                recorder.setLayoutParams(lParams);
                recordFile = filePath;
                recordLength = seconds + "";
                llRecord.setVisibility(View.VISIBLE);
            }
        });

    }

    @OnClick({R.id.ivLeft, R.id.tv_sure, R.id.recorder_length, R.id.tv_delete})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.recorder_length:

                recorderAnim.setBackgroundResource(R.drawable.listen_circle_play_sound);
                AnimationDrawable drawable = (AnimationDrawable) recorderAnim.getBackground();
                drawable.start();
                // 播放音频
                MediaManager.getInstance().getInstance().playSound(recordFile,new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                recorderAnim.setBackgroundResource(R.drawable.v_anim4);
                            }
                        });
                break;
            case R.id.tv_delete:
                recordFile = "";
                llRecord.setVisibility(View.GONE);
                break;

            case R.id.tv_sure:
                String content = et.getText().toString().trim();

                if (isNeedRecord) {
                    //需要录音
                    if (content.isEmpty()) {
                        ToastUtils.showBottomToast("说点什么吧...");
                        return;
                    } else {
                        //有文字
                        if (!TextUtils.isEmpty(recordFile)) {
                            //有录音
                            if (imagePathList.size() == 0) {
                                //没有图片
                                uploadVoice(0);//上传 文字和语音
                            } else {

                                uploadVoice(1); //上传 文字图片语音
                            }

                        } else {
                            //没有录音
                            if (imagePathList.size() == 0) {
                                uploadContent();//上传文字

                            } else {
                                uploadImage();//上传文字,图片

                            }


                        }

                    }


                } else {
                    //不需要录音
                    if (content.isEmpty()) {
                        ToastUtils.showBottomToast("说点什么吧...");
                        return;
                    } else {
                        if (imagePathList.size() == 0) {//没有图片
                            if(fromWhere==0){
                                uploadContent();//上传听友圈文字
                            }else if(fromWhere==1){
                                sendSuggest();//上传意见反馈
                            }

                        } else {
                            uploadImage();//上传文字 图片

                        }
                    }
                }


                break;

            default:
                break;
        }
    }



    /**
     * 上传图片
     */
    private void uploadImage() {

        PostRequest<String> post = OkGo.<String>post(UrlProvider.UPLOADPHOTO).tag(this).params("accessToken", LoginUtil.getAccessToken());

        for (int i = 0; i < imagePathList.size(); i++) {
            File file = null;
            String path = AppConfig.EXTRASTROGEFILEDIRCOMPRESSPATH + System.currentTimeMillis() + i + "compress.jpg";
            file = BitmapUtil.compressBitmap(file, imagePathList.get(i), path);
            String s =  "images" + i;
            if (file.exists()) {
                post.params(s, file);

            }
        }

        post.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                UpImageBean imageBean = new Gson().fromJson(body, UpImageBean.class);
                int status = imageBean.getStatus();
                if(status==1){
                    imagepath = imageBean.getResults();
                    hasUpImage=true;
                    if(fromWhere==0){
                        uploadContent();
                    }else if(fromWhere==1){
                        sendSuggest();
                    }
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showBottomToast("发送失败!");
            }
        });


    }

    /**
     * 上传语音
     *
     * @param i
     */
    private void uploadVoice(final int i) {

        File file = new File(recordFile);
        OkGo.<String>post(UrlProvider.UPLOADVOICE).tag(this).params("accessToken", LoginUtil.getAccessToken()).params("playtime", recordLength).params("mp3", file).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String s = response.body();
                VoiceBean voiceBean = new Gson().fromJson(s, VoiceBean.class);
                int status = voiceBean.getStatus();
                if (status == 1) {
                    filepath = voiceBean.getResults().getFilepath();
                    hasUpVoice = true;
                    if (i == 1) {
                        uploadImage();
                    } else {
                        uploadContent();
                    }
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                hasUpVoice = false;

            }
        });


    }

    /**
     *上传听友圈
     */
    private void uploadContent() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("content", EmojiUtil.codeMsg(et.getText().toString()));
        if(hasUpVoice){
            map.put("mp3_url",filepath);
            map.put("play_time", recordLength);
        }
        if(hasUpImage){
            map.put("timages",imagepath );
        }
        OkGo.<SimpleMsgBean>post(UrlProvider.SEND_LISTEN).params(map).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {

                int status = response.body().getStatus();
                if(status==1){
                    ToastUtils.showBottomToast("发送成功!");
                }else{
                    ToastUtils.showBottomToast("发送失败!");
                }
                finish();
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("发送失败!");
            }
        });

    }

    /**
     * 发送意见反馈
     */
    private void sendSuggest() {
        Map<String,String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("tuid","0");
        map.put("to_comment_id","0");
        map.put("comment", EmojiUtil.codeMsg(et.getText().toString()));
        if(hasUpImage){
            map.put("images", imagepath);
        }
        OkGo.<SimpleMsgBean>post(UrlProvider.SEND_SUGGEST).params(map,true).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    ToastUtils.showBottomToast("发送成功!");
                    finish();
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("发送失败!");
            }
        });


    }



    private ImageView getMoreImageView() {
        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(SizeUtil.dip2px(this, 70), SizeUtil.dip2px(this, 70));
        layoutParams.rightMargin = SizeUtil.dip2px(this, 10);
        layoutParams.topMargin = SizeUtil.dip2px(this, 10);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.icon_more_pic);
        return imageView;
    }

    private void adapterViewAddChildView(View childView) {
        if (childView != null) {
            av.addView(childView, av.getChildCount() - 1);
        }
    }


    private ImageView getImageView(String imagePath) {

        final ImageView imageView = new ImageView(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(SizeUtil.dip2px(this, 70), SizeUtil.dip2px(this, 70));
        layoutParams.rightMargin = SizeUtil.dip2px(this, 10);
        layoutParams.topMargin = SizeUtil.dip2px(this, 10);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(this).load(imagePath).into(imageView);
        imagePathList.add(imagePath);
        imageView.setTag(imagePath);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(PublishCircleActivity.this);
                alertDialog = builder.setMessage("是否删除此图片？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                av.removeView(imageView);
                                String delPath = (String) imageView.getTag();
                                imagePathList.remove(delPath);

                            }
                        }).setNegativeButton("取消", null).create();
                alertDialog.show();
            }
        });
        return imageView;
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(STORAGE_CAMERA)
    private void checkPerm() {
        String[] params = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, params)) {
            photoChoosePop = new PhotoChoosePop(PublishCircleActivity.this);
            photoChoosePop.setListener(this);
            photoChoosePop.showPopupWindow();
        } else {
            EasyPermissions.requestPermissions(this, "是否允许文件读写,拍照权限，以方便使用接下来的功能？", STORAGE_CAMERA, params);
        }


    }


    @Override
    public void album() {
        photoChoosePop.dismiss();
        if (imagePathList.size() >= 9) {
            ToastUtils.showBottomToast("图片不能超过9张");
            return;
        }
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.putExtra("limit", 9 - imagePathList.size());
        startActivityForResult(intent, RESULT_PHOTO_BUCKET);
    }

    @Override
    public void camera() {
        photoChoosePop.dismiss();
        if (imagePathList.size() >= 9) {
            ToastUtils.showBottomToast("图片不能超过9张");
            return;
        }
        takePhoto();
    }


    private void takePhoto() {
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
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE) {
                if (data != null) {
                    final String imagePath;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        imagePath = ImageUtil.getPath(this, data.getData());

                    } else {
                        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                        imagePath = cursor.getString(cursor.getColumnIndex("_data"));

                        cursor.close();
                    }
                    if (imagePath != null && !imagePath.isEmpty()) {
                        adapterViewAddChildView(getImageView(imagePath));

                    }

                }
            } else if (requestCode == RESULT_PHOTO) {

                adapterViewAddChildView(getImageView(TEMP_IMAGE));


            } else if (requestCode == RESULT_PHOTO_BUCKET) {
                if (data != null) {
                    String[] images = data.getStringArrayExtra("images");
                    for (final String image : images) {
                        adapterViewAddChildView(getImageView(image));

                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        String path = AppConfig.EXTRASTROGEFILEDIRCOMPRESSPATH;
        File file = new File(path);
        FileSizeUtil.deleteDir(file);
        if(file != null){
            file =null;
        }
        MediaManager.getInstance().release();
        if(recordButton!=null){
            recordButton.release();
        }
    }
}
