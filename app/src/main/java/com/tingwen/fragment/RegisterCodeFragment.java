package com.tingwen.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.CodeBean;
import com.tingwen.event.RegisterCodeEvent;
import com.tingwen.event.RegisterNextEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.buttons.UIButton;
import com.tingwen.widget.countdowntimer.CountdownTimer;
import com.tingwen.widget.countdowntimer.OnFinishListener;
import com.tingwen.widget.countdowntimer.OnTickListener;
import com.tingwen.widget.countdowntimer.StringUtils;
import com.tingwen.widget.countdowntimer.TimeUtils;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 注册 验证码Fragment
 * Created by Administrator on 2017/7/26 0026.
 */
public class RegisterCodeFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.et_phone)
    EditText etphone;
    @BindView(R.id.et_vcode)
    EditText etVcode;
    @BindView(R.id.btn_get_vcode)
    UIButton btnGetVcode;

    private CountdownTimer timer;
    private String code;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register;
    }


    @Override
    protected void initData() {
        super.initData();
        timer = new CountdownTimer();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.btn_get_vcode:
                if (!etphone.getText().toString().trim().isEmpty()) {
                    getCode();
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void setListener() {
        super.setListener();

        btnGetVcode.setOnClickListener(this);

        etphone.addTextChangedListener(new TextWatcher() {
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
                    btnGetVcode.setUnpressedColor(getActivity().getResources().getColor(R.color.blue_primary));
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterNextEvent(RegisterNextEvent event) {

        int message = event.getMessage();
        //验证码
        if (message == 1) {
            String phone = etphone.getText().toString().trim();
            String code = etVcode.getText().toString().trim();
            if (phone.isEmpty()) {
                ToastUtils.showBottomToast("请输入手机号码");
                return;
            }
            if (code.isEmpty()) {
                ToastUtils.showBottomToast("请输入验证码");
                return;
            }

            if (checkCode()) {
                EventBus.getDefault().post(new RegisterCodeEvent(true, phone, code));
            } else {
                ToastUtils.showBottomToast("验证码错误");
            }

        }

    }


    /**
     * 获取验证码
     */
    private void getCode() {
        String number = "";
        try {
            number = LoginUtil.encode(LoginUtil.AESCODE, etphone.getText().toString().getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", number);
        Logger.e("注册手机号:" + number);
        map.put("useType", "1");
        OkGo.<CodeBean>post(UrlProvider.SMS).params(map, true).execute(new SimpleJsonCallback<CodeBean>(CodeBean.class) {
            @Override
            public void onSuccess(Response<CodeBean> response) {
                if (response.body().getStatus() == 1) {
                    ToastUtils.showBottomToast("发送成功!");
                    code = response.body().getResults();

                } else {

                    timer.stop();

                    new MaterialDialog.Builder(getActivity())
                            .title("温馨提示")
                            .content("该手机号码已注册")
                            .contentColorRes(R.color.text_black)
                            .positiveText("知道了")
                            .build().show();

                    if(null!=btnGetVcode){
                        btnGetVcode.setText("获取验证码");
                        btnGetVcode.setEnabled(true);
                        btnGetVcode.setUnpressedColor(getActivity().getResources().getColor(R.color.blue_primary));
                    }

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
     * 验证验证码是否正确
     */
    private Boolean checkCode() {
        return etVcode.getText().toString().trim().equals(code + "");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
