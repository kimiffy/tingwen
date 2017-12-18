package com.tingwen.fragment;

import android.os.Bundle;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.User;
import com.tingwen.event.RegisterNextEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NetUtil;
import com.tingwen.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 注册 填写密码 昵称
 * Created by Administrator on 2017/7/26 0026.
 */
public class RegisterPswFragment extends BaseFragment {

    @BindView(R.id.et_psw_1)
    EditText etPsw1;
    @BindView(R.id.et_psw_2)
    EditText etPsw2;
    @BindView(R.id.et_nickName)
    EditText etNickName;
    private String phone;
    private String code;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_psw;
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        phone = (String) bundle.get("phone");
        code = (String) bundle.get("code");

    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterNextEvent(RegisterNextEvent event) {
        int message = event.getMessage();
        //验证码
        if (message == 2) {
            if (etPsw1.getText().toString().trim().isEmpty()) {
                ToastUtils.showBottomToast("请输入密码");
                return;
            }
            if (etPsw2.getText().toString().trim().isEmpty()) {
                ToastUtils.showBottomToast("请确认密码");
                return;
            }

            if (etNickName.getText().toString().trim().isEmpty()) {
                ToastUtils.showBottomToast("请输入昵称");
                return;
            }

            if (!etPsw1.getText().toString().trim().equals(etPsw2.getText().toString().trim())) {
                ToastUtils.showBottomToast("密码不一致,请重新输入!");
                return;
            }

            register();

        }

    }

    /**
     * 注册
     */
    private void register() {

        String number = "";
        String pass = "";
        try {
            number = LoginUtil.encode(LoginUtil.AESCODE, phone.getBytes());
            pass = LoginUtil.encode(LoginUtil.AESCODE, etPsw2.getText().toString().trim().getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", number);
        map.put("vcode", code);
        map.put("password", pass);
        map.put("user_nicename", etNickName.getText().toString().trim());
        OkGo.<User>post(UrlProvider.REGISTER).params(map, true).execute(new SimpleJsonCallback<User>(User.class) {
                    @Override
                    public void onSuccess(Response<User> response) {
                        int status = response.body().getStatus();
                        if (status == 1) {
                            ToastUtils.showBottomToast("注册成功!");
                            getActivity().finish();
                        } else {
                            ToastUtils.showBottomToast(response.body().getMsg());
                        }
                    }
                    @Override
                    public void onError(Response<User> response) {
                        super.onError(response);
                        boolean hasNetAvailable = NetUtil.isHasNetAvailable(TwApplication.getInstance().getApplicationContext());
                        if(!hasNetAvailable){
                            ToastUtils.showBottomToast("网络异常");
                            return;
                        }
                        ToastUtils.showBottomToast("注册失败!");
                        getActivity().finish();
                    }
                });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
