package com.tingwen.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.flyco.roundview.RoundTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 * Created by Administrator on 2017/10/10 0010.
 */
public class ChangePswActivity extends BaseActivity {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.et_psw_old)
    EditText etPswOld;
    @BindView(R.id.et_psw_new)
    EditText etPswNew;
    @BindView(R.id.et_psw_new_2)
    EditText etPswNew2;
    @BindView(R.id.tv_commit)
    RoundTextView tvCommit;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_change_psw;
    }

    @OnClick({R.id.ivLeft,R.id.tv_commit})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                closeKeyboard();
                finish();
                break;
            case R.id.tv_commit:
                closeKeyboard();
                commit();
                break;
            default:
                break;
        }
    }

    /**
     * 提交修改
     */
    private void commit() {
        String oldPsw = etPswOld.getText().toString();
        String newPsw = etPswNew.getText().toString();
        String newPsw2 = etPswNew2.getText().toString();
        if(TextUtils.isEmpty(oldPsw)){
            ToastUtils.showBottomToast("请输入密码");
            return;
        }
        if(TextUtils.isEmpty(newPsw)){
            ToastUtils.showBottomToast("请输入新密码");
            return;
        }

        if(TextUtils.isEmpty(newPsw2)){
            ToastUtils.showBottomToast("请再次输入新密码");
            return;
        }
        if(!newPsw.equals(newPsw2)){
            ToastUtils.showBottomToast("新密码不一致,请重新输入");
            return;
        }

        Map<String, String> map = new HashMap<>();
        try {
            String oldCode = LoginUtil.encode(LoginUtil.AESCODE,
                    oldPsw.getBytes());
            String newCode = LoginUtil.encode(LoginUtil.AESCODE,
                    newPsw2.getBytes());
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("opassword",oldCode);
            map.put("password", newCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        OkGo.<SimpleMsgBean>post(UrlProvider.MODIFY_PASSWORD).tag(this).params(map,true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    ToastUtils.showBottomToast("修改成功!");
                    finish();
                }else{
                    String msg = response.body().getMsg();
                    ToastUtils.showBottomToast(msg);
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("修改密码失败，请稍后重试");
            }
        });
    }

    /**
     * 关闭键盘
     */
    private void closeKeyboard() {
        KeyBoardUtils.closeKeyboard(etPswOld);
        KeyBoardUtils.closeKeyboard(etPswNew);
        KeyBoardUtils.closeKeyboard(etPswNew2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
