package com.tingwen.activity;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.flyco.roundview.RoundTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.CodeBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.TouchUtil;
import com.tingwen.widget.buttons.UIButton;
import com.tingwen.widget.countdowntimer.CountdownTimer;
import com.tingwen.widget.countdowntimer.OnFinishListener;
import com.tingwen.widget.countdowntimer.OnTickListener;
import com.tingwen.widget.countdowntimer.StringUtils;
import com.tingwen.widget.countdowntimer.TimeUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回密码
 * Created by Administrator on 2017/7/28 0028.
 */
public class ForgetPswActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_vcode)
    EditText etVcode;
    @BindView(R.id.btn_get_vcode)
    UIButton btnGetVcode;
    @BindView(R.id.tv_commit)
    RoundTextView tvCommit;

    private CountdownTimer timer;
    private String code;
    private String phoneNum;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget_psw;
    }

    @Override
    protected void initData() {
        super.initData();
        timer = new CountdownTimer();
    }

    @OnClick({R.id.ivLeft, R.id.btn_get_vcode, R.id.tv_commit})
    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {

            case R.id.ivLeft:
                finish();
                break;
            case R.id.btn_get_vcode:
                phoneNum = etPhone.getText().toString().trim();
                if (!phoneNum.isEmpty()) {
                    getCode();
                }
                break;

            case R.id.tv_commit:

                KeyBoardUtils.closeKeyboard(etVcode);
                if (!etVcode.getText().toString().trim().isEmpty() && etVcode.getText().toString().trim().equals(code + "")) {
                    getPsw();
                } else {
                    ToastUtils.showBottomToast("验证码有误!");
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void setListener() {
        super.setListener();

        TouchUtil.setTouchDelegate(ivLeft, 50);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    btnGetVcode.setEnabled(true);
                    btnGetVcode.setUnpressedColor(getResources().getColor(R.color.blue_primary));
                } else {
                    btnGetVcode.setUnpressedColor(getResources().getColor(R.color.color_unable));
                    btnGetVcode.setEnabled(false);
                }
                if (s.length() > 0) {
                    btnGetVcode.setVisibility(View.VISIBLE);
                } else {
                    btnGetVcode.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /**
         * 实时修改计时秒数
         */
        timer.setOnTickListener(new OnTickListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != btnGetVcode) {
                    btnGetVcode.setText(StringUtils.padTimeUnit(TimeUtils.millisecondsToSeconds(millisUntilFinished)) + " s");
                    btnGetVcode.setEnabled(false);
                    btnGetVcode.setUnpressedColor(getResources().getColor(R.color.color_unable));
                }
            }
        });

        /**
         * 倒计时结束
         */
        timer.setOnFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {
                if (null != btnGetVcode) {
                    btnGetVcode.setText("重新获取验证码");
                    btnGetVcode.setEnabled(true);
                    btnGetVcode.setUnpressedColor(getResources().getColor(R.color.blue_primary));
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String number = "";
        try {
            number = LoginUtil.encode(LoginUtil.AESCODE, phoneNum.getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", number);
        map.put("useType", "2");//1 注册  2 找回密码
        OkGo.<CodeBean>post(UrlProvider.SMS).params(map, true).execute(new SimpleJsonCallback<CodeBean>(CodeBean.class) {
            @Override
            public void onSuccess(Response<CodeBean> response) {
                if (response.body().getStatus() == 1) {
                    ToastUtils.showBottomToast(response.body().getMsg());
                    code = response.body().getResults();

                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                    timer.stop();
                }
            }

            @Override
            public void onError(Response<CodeBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("获取失败!请稍后重试");
                timer.stop();
            }
        });

        //倒计时开始
        timer.start(TimeUtils.toMilliseconds(0, 0, 60));

    }


    /**
     * 获取短信密码
     */
    private void getPsw() {
        Map<String, String> map = new HashMap<>();
        try {
            String code = LoginUtil.encode(LoginUtil.AESCODE,
                    phoneNum.getBytes());
            map.put("accessToken", code);
            map.put("vcode", etVcode.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkGo.<SimpleMsgBean>post(UrlProvider.GET_PASSWORD).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                if (response.body().getStatus() == 1) {
                    new MaterialDialog.Builder(ForgetPswActivity.this)
                            .title("温馨提示")
                            .content("密码已发送至您的手机，请注意查看")
                            .contentColorRes(R.color.text_black)
                            .positiveText("确定")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            }).build().show();

                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("获取密码失败,请稍后重试!");

            }
        });

    }


}
