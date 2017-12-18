package com.tingwen.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.tingwen.event.ReloadUserInfoEvent;
import com.tingwen.popupwindow.ExitPop;
import com.tingwen.utils.GlideCatchUtil;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NewMessageUtil;
import com.tingwen.utils.UpdateUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 * Created by Administrator on 2017/10/16 0016.
 */
public class SettingActivity extends BaseActivity implements ExitPop.ExitListener{

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.rl_clear)
    RelativeLayout rlClear;
    @BindView(R.id.rl_font)
    RelativeLayout rlFont;
    @BindView(R.id.tv_font_size)
    TextView tvFont;

    @BindView(R.id.rl_advice)
    RelativeLayout rlAdvice;
    @BindView(R.id.iv_feedback_news)
    TextView ivDot;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.rl_login_out)
    RelativeLayout rlLoginOut;
    private ExitPop exitPop;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }


    @Override
    protected void initUI() {
        super.initUI();
        if(LoginUtil.isUserLogin()){
            rlLoginOut.setVisibility(View.VISIBLE);
        }else {
            rlLoginOut.setVisibility(View.GONE);
        }
        String version = getVersion();
        String cacheSize = GlideCatchUtil.getInstance().getCacheSize();

        tvUpdate.setText(version);
        tvCache.setText(cacheSize);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String fontSize = getFontSize();
        tvFont.setText(fontSize);
        int feedBackSize = NewMessageUtil.getInstance(this).getFeedBackSize();
        if(feedBackSize!=0){
            ivDot.setVisibility(View.VISIBLE);
        }else{
            ivDot.setVisibility(View.GONE);
        }

    }

    @Override
    protected void setListener() {
        super.setListener();


    }

    @OnClick({R.id.ivLeft,R.id.rl_update,R.id.rl_clear,R.id.rl_font,R.id.rl_advice,R.id.rl_about,R.id.rl_login_out})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.rl_update:
                UpdateUtils.getInstance().UpdateVersion(this,true);
                break;
            case R.id.rl_clear:
                if( GlideCatchUtil.getInstance().clearCacheDiskSelf()){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvCache.setText("已清空");
                        }
                    },800);
                }
                break;
            case R.id.rl_font:
                LauncherHelper.getInstance().launcherActivity(this,SetFontActivity.class);
                break;
            case R.id.rl_advice:
                LauncherHelper.getInstance().launcherActivity(this,FeedbackActivity.class);
                break;
            case R.id.rl_about:
                LauncherHelper.getInstance().launcherActivity(this,AboutActivity.class);
                break;
            case R.id.rl_login_out:
                if(null==exitPop){
                    exitPop = new ExitPop(this);
                    exitPop.setListener(this);
                }
                exitPop.showPopupWindow();
                break;
            default:
                break;
        }
    }

    @Override
    public void exit() {
        AppSpUtil.getInstance().deleteUserInfo();
        EventBus.getDefault().post(new ReloadUserInfoEvent());
        if(null!=exitPop){
            exitPop.dismiss();
        }
        finish();
    }

    private String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private String getFontSize() {
        int frontSize = AppSpUtil.getInstance().getFrontSize();
        String front="";
        switch (frontSize){
            case 0:
                front="巨无霸";
            break;
            case 1:
                front= "巨大";
                break;
            case 2:
                front= "超大";
                break;
            case 3:
                front= "大";
                break;
            case 4:
                front= "中";
                break;
            case 5:
                front= "小";
                break;
            default:
                break;
        }
        return front;
    }

    @Override
    public void cancel() {
        if(null!=exitPop){
            exitPop.dismiss();
        }
    }


}
