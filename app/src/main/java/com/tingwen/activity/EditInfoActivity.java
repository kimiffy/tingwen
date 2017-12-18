package com.tingwen.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.bean.User;
import com.tingwen.event.ReloadUserInfoEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.PhotoChoosePop;
import com.tingwen.popupwindow.SexSelectPop;
import com.tingwen.utils.CropImageUtils;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 编辑个人信息
 * Created by Administrator on 2017/9/29 0029.
 */
public class EditInfoActivity extends BaseActivity implements SexSelectPop.SexSelectListener, PhotoChoosePop.PhotoChooseListener ,EasyPermissions.PermissionCallbacks{

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    @BindView(R.id.rl_change_password)
    RelativeLayout rlChangePassword;
    @BindView(R.id.et_name)
    EditText etName;
    public static final int STORAGE_CAMERA = 105;
    private String signature;
    private String avatar;
    private String sex;
    private String name;
    private String commitSex;
    private SexSelectPop sexSelectPop;
    private PhotoChoosePop photoChoosePop;
    private File tempFile;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_info;
    }


    @Override
    protected void initData() {
        super.initData();
        User userInfo = AppSpUtil.getInstance().getUserInfo();

        if (userInfo != null) {
            signature = userInfo.getResults().getSignature();
            avatar = userInfo.getResults().getAvatar();
            sex = userInfo.getResults().getSex();
            name = userInfo.getResults().getUser_nicename();
        }


    }


    @Override
    protected void initUI() {
        super.initUI();

        if (!TextUtils.isEmpty(signature)) {
            etDescribe.setHint(signature);
        }
        if (!TextUtils.isEmpty(avatar)) {
            if (!avatar.contains("http")) {
                Glide.with(this).load(UrlProvider.URL_IMAGE_USER + avatar).transform(new GlideCircleTransform(this))
                        .error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang).into(ivHeader);
            }
        }
        if (!TextUtils.isEmpty(sex)) {
            switch (sex) {
                case "0":
                    tvSex.setText("保密");
                    break;
                case "1":
                    tvSex.setText("男");
                    break;
                case "2":
                    tvSex.setText("女");
                    break;
            }

        }
        if (!TextUtils.isEmpty(name)) {
            etName.setHint(name);

        }


    }

    @OnClick({R.id.ivLeft, R.id.tv_right, R.id.rl_head, R.id.rl_sex,R.id.rl_change_password})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                closeKeyboard();
                finish();
                break;
            case R.id.tv_right:
                closeKeyboard();
                commit();
                break;

            case R.id.rl_head:
                closeKeyboard();
                checkPerm();

                break;
            case R.id.rl_sex:
                if (sexSelectPop == null) {
                    sexSelectPop = new SexSelectPop(this);
                    sexSelectPop.setSexSelectListenerListener(this);
                }
                closeKeyboard();
                sexSelectPop.showPopupWindow();
                break;

            case R.id.rl_change_password:
                LauncherHelper.getInstance().launcherActivity(this,ChangePswActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    protected void setListener() {
        super.setListener();

        etDescribe.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;
            private int editStart;
            private int editEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = etDescribe.getSelectionStart();
                editEnd = etDescribe.getSelectionEnd();
                if (temp.length() > 20) {//限制长度
                    ToastUtils.showBottomToast("输入的字数超过了限制!");
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    etDescribe.setText(s);
                    etDescribe.setSelection(tempSelection);
                }
            }
        });
    }

    /**
     * 提交修改信息(昵称 性别 签名)
     */
    private void commit() {
        String commitName = etName.getText().toString();
        if (TextUtils.isEmpty(commitName)) {
            commitName = name;
        }
        String describe = etDescribe.getText().toString();
        if (TextUtils.isEmpty(describe)) {
            describe = signature;
        }

        if(null== commitSex ||"".equals(commitSex)){
            commitSex  = checkSex();
        }

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("nicename", commitName);
        map.put("sex", commitSex);
        map.put("signature", describe);
        OkGo.<SimpleMsgBean>post(UrlProvider.MODIFY_USER_INFO).params(map, true).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {

                int status = response.body().getStatus();
                if (status == 1) {
                    getUserInfo();

                } else {
                    String msg = response.body().getMsg();
                    ToastUtils.showBottomToast(msg);
                }


            }
        });


    }

    /**
     * 获取用户信息并保存
     */
    private void getUserInfo() {
        OkGo.<User>post(UrlProvider.GET_USER_INFO).params("accessToken", LoginUtil.getAccessToken()).tag(this).execute(new SimpleJsonCallback<User>(User.class) {
            @Override
            public void onSuccess(Response<User> response) {
                AppSpUtil.getInstance().saveUserInfo(response.body().toString());
                EventBus.getDefault().post(new ReloadUserInfoEvent());
            }
        });

    }

    /**
     * 性别选择回调
     *
     * @param sex 1 男 2 女 3保密
     */
    @Override
    public void selectSex(String sex) {
        sexSelectPop.dismiss();
        commitSex = sex;
        switch (sex) {
            case "1":
                tvSex.setText("男");
                break;
            case "2":
                tvSex.setText("女");
                break;
            case "0":
                tvSex.setText("保密");
                break;
        }
    }

    /**
     * 匹配性别
     * @return
     */
    public String checkSex(){
        if (!TextUtils.isEmpty(sex)) {
            commitSex = sex;
            return commitSex;
        }
        return "1";
    }

    /**
     * 关闭键盘
     */
    private void closeKeyboard() {
        KeyBoardUtils.closeKeyboard(etDescribe);
        KeyBoardUtils.closeKeyboard(etName);
    }

    /**
     * 相册
     */
    @Override
    public void album() {
        photoChoosePop.dismiss();
        CropImageUtils.getInstance().openAlbum(this);
    }

    /**
     * 拍照
     */
    @Override
    public void camera() {
        photoChoosePop.dismiss();
        CropImageUtils.getInstance().takePhoto(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImageUtils.getInstance().onActivityResult(this, requestCode, resultCode, data, new CropImageUtils.OnResultListener() {
            @Override
            public void takePhotoFinish(String path) {
                Logger.e("照片存放在：" + path);
                //拍照回调，去裁剪
                CropImageUtils.getInstance().cropPicture(EditInfoActivity.this, path);
            }

            @Override
            public void selectPictureFinish(String path) {
                Logger.e("打开图片：" + path);
                //相册回调，去裁剪
                CropImageUtils.getInstance().cropPicture(EditInfoActivity.this, path);
            }

            @Override
            public void cropPictureFinish(String path) {
                Logger.e("裁剪保存在：" + path);
                tempFile = new File(path);
                updateIcon();
                //裁剪回调
                Glide.with(EditInfoActivity.this).load(path).transform(new GlideCircleTransform(EditInfoActivity.this)).into(ivHeader);

            }
        });
    }

    /**
     * 上传图片
     */
    private void updateIcon() {

        if (!tempFile.exists() || !tempFile.isFile()) {
           ToastUtils.showBottomToast("更新头像失败");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(UrlProvider.UPDATE_ICON).tag(this).params("accessToken", LoginUtil.getAccessToken()).params("file",tempFile).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ToastUtils.showBottomToast("更新头像成功!");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        tempFile.delete();
                        ToastUtils.showBottomToast("更新头像失败!");
                    }
                });
            }
        }).start();


    }


    /**
     * 检查权限
     */
    @AfterPermissionGranted(STORAGE_CAMERA)
    private void checkPerm() {
        String[] params = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, params)) {
            if (photoChoosePop == null) {
                photoChoosePop = new PhotoChoosePop(this);
                photoChoosePop.setListener(this);
            }
            photoChoosePop.showPopupWindow();
        } else {
            EasyPermissions.requestPermissions(this, "是否允许文件读写,拍照权限，以方便使用接下来的功能？", STORAGE_CAMERA, params);
        }


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
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
