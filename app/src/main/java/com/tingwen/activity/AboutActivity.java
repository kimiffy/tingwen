package com.tingwen.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.base.BaseActivity;

import butterknife.BindView;

/**
 * 关于听闻
 * Created by Administrator on 2017/10/16 0016.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.textView2)
    TextView tvVersion;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }


    @Override
    protected void initData() {
        super.initData();
        String version = getVersion();
        tvVersion.setText(version+ " 正式版");

    }

    @Override
    protected void setListener() {
        super.setListener();
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String getVersion() {
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
}
