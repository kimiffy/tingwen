package com.tingwen.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.fragment.ClassificationFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分类新闻
 * Created by Administrator on 2017/8/4 0004.
 */
public class ClassificationActivity extends BaseActivity {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl)
    FrameLayout fl;

    private FragmentManager fragmentManager;
    private String id;
    private ClassificationFragment classificationFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_classification;
    }


    @Override
    protected void initData() {
        super.initData();

      id=getIntent().getExtras().getString("id");

    }


    @Override
    protected void initUI() {
        super.initUI();
        tvTitle.setText(getTitle(id));
        fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag("classificationFragment") == null) {
            classificationFragment = new ClassificationFragment();
        } else {
            classificationFragment = (ClassificationFragment) fragmentManager.findFragmentByTag("classificationFragment");
        }
        if (id != null && !id.equals("")) {
            if(!classificationFragment.isVisible()){
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                classificationFragment.setArguments(bundle);
                fragmentManager.beginTransaction().add(R.id.fl, classificationFragment, "classificationFragment").commit();
            }

        }


    }

    /**
     * 通过id设置title
     * @param id 分类id
     * @return 标题
     */
    private String getTitle(String id) {
        String title="";
        switch (id){
            case "6":
                title="财经";
                break;
            case "4":
                title="文娱";
                break;
            case "8":
                title="国际";
                break;
            case "7":
                title="科技";
                break;
            case "14":
                title="时政";
                break;
            default:
                break;

        }
        return title;
    }




    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.ivLeft})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            default:
                break;
        }
    }



}
