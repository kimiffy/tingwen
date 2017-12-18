package com.tingwen.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tingwen.R;
import com.tingwen.adapter.MoreProgramTabAdapter;
import com.tingwen.base.BaseActivity;
import com.tingwen.fragment.MoreProgramFragment;
import com.tingwen.utils.SizeUtil;
import com.tingwen.widget.tablayout.ColorTransitionPagerTitleView;
import com.tingwen.widget.tablayout.CommonNavigator;
import com.tingwen.widget.tablayout.CommonNavigatorAdapter;
import com.tingwen.widget.tablayout.IPagerIndicator;
import com.tingwen.widget.tablayout.IPagerTitleView;
import com.tingwen.widget.tablayout.LinePagerIndicator;
import com.tingwen.widget.tablayout.MagicIndicator;
import com.tingwen.widget.tablayout.SimplePagerTitleView;
import com.tingwen.widget.tablayout.ViewPagerHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更多 (节目,主播)
 * Created by Administrator on 2017/7/17 0017.
 */
public class MoreProgramActivity extends BaseActivity {
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.indicator)
    MagicIndicator mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabList = new ArrayList<>();
    //传递过来的id
    private String id = "";
    private android.support.v4.app.FragmentManager fragmentManager;
    private MoreProgramTabAdapter moreProgramTabAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_more_program;
    }

    @Override
    protected void initData() {
        super.initData();
        id = getIntent().getStringExtra("id");
        if("0".equals(id)){
            tabList.clear();
            tabList.add("主播推荐");
            tabList.add("主播排行");
        }else{
            tabList.clear();
            tabList.add("节目推荐");
            tabList.add("节目排行");
        }
        fragmentManager = getSupportFragmentManager();
        initFragments();
        initIndicator();
    }

    /**
     * 初始化Fragment
     */
    private void initFragments() {
        MoreProgramFragment fragment1 = new MoreProgramFragment();
        MoreProgramFragment fragment2 = new MoreProgramFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putBoolean("sort", false);
        fragment1.setArguments(bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putString("id", id);
        bundle2.putBoolean("sort", true);
        fragment2.setArguments(bundle2);

        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
    }

    /**
     *初始化指示器和viewpager绑定
     */
    private void initIndicator() {
        moreProgramTabAdapter = new MoreProgramTabAdapter(fragmentManager, fragmentList, tabList);
        viewPager.setAdapter(moreProgramTabAdapter);
        final int screenWidth = SizeUtil.getScreenWidth();//获取屏幕宽
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabList == null ? 0 : tabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(tabList.get(index));
                simplePagerTitleView.setTextSize(16);//文字大小
                simplePagerTitleView.setWidth(screenWidth/4);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mActivity, R.color.text_gray));//未选中颜色
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mActivity,R.color.text_black));//选中颜色
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setRoundRadius(5);
                indicator.setColors(Color.parseColor("#5cb8e6"));

                return indicator;
            }
        });

        mTabLayout.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mTabLayout, viewPager);
    }



    @OnClick({R.id.ivLeft})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ivLeft:
                finish();
                break;
            default:
                break;
        }
    }
}
