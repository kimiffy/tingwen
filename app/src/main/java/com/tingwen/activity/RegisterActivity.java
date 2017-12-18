package com.tingwen.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.event.RegisterCodeEvent;
import com.tingwen.event.RegisterNextEvent;
import com.tingwen.fragment.RegisterCodeFragment;
import com.tingwen.fragment.RegisterPswFragment;
import com.tingwen.utils.TouchUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 * Created by Administrator on 2017/7/25 0025.
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.tv_next)
    RoundTextView tvNext;

    private FragmentManager fragmentManager;
    private RegisterCodeFragment registerFragment;
    private int state = 1; //状态  1:验证码  2密码

    private RegisterPswFragment registerPswFragment;
    private String phoneNum;
    private String codeNum;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initData() {
        super.initData();
        fragmentManager = getSupportFragmentManager();
        EventBus.getDefault().register(this);
        initFragments();
    }

    /**
     * 初始化Fragment
     */
    private void initFragments() {
        registerFragment = new RegisterCodeFragment();
    }

    @Override
    protected void initUI() {
        super.initUI();
        FragmentTransaction  transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        TouchUtil.setTouchDelegate(ivLeft,50);
    }


    @OnClick({R.id.ivLeft, R.id.tv_next})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tv_next:
                if (state == 1) {
                    EventBus.getDefault().post(new RegisterNextEvent(1));
                } else {
                    EventBus.getDefault().post(new RegisterNextEvent(2));
                }

                break;
            default:
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isCodeRightEvent(RegisterCodeEvent event) {
        if (event.isVcodeRight()) {
            state = 2;
            phoneNum = event.getPhoneNum();
            codeNum = event.getCodeNum();
            replaceFragment();
            tvNext.setText("注  册");
        }
    }

    /**
     * 显示填写密码的Fragment
     */
    private void replaceFragment() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (registerFragment != null) {
            transaction.hide(registerFragment);
        }

        if (registerPswFragment == null) {
            registerPswFragment = new RegisterPswFragment();
            if(!registerPswFragment.isVisible()){
                Bundle bundle = new Bundle();
                bundle.putString("phone",phoneNum );
                bundle.putString("code",codeNum );
                registerPswFragment.setArguments(bundle);
                transaction.add(R.id.container, registerPswFragment);
                transaction.commit();
            }

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
