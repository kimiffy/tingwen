package com.tingwen.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.tingwen.R;
import com.tingwen.adapter.MainFragmentTabAdapter;
import com.tingwen.base.BaseFragment;
import com.tingwen.utils.SizeUtil;
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

/**
 * 首页Fragment
 * Created by Administrator on 2017/2/27 0027.
 */
public class MainFragment extends BaseFragment {


    @BindView(R.id.page_indicator)
    MagicIndicator mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<String> tabList = new ArrayList<String>() {{
        add("专栏");
        add("快讯");
        add("课堂");
    }};
    //当前显示的第几个页面
    private int currentPosition;
    //当前Fragment
    private Fragment currentFragment;
    private FragmentManager mFragmentManager;
    private MainFragmentTabAdapter fragPagerAdapter;
    //整个fragment的布局
    private View view;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mFragmentManager = getChildFragmentManager();
        initFragments();
        initIndicator();

    }

    /**
     *初始化指示器和viewpager绑定
     */
    private void initIndicator() {
        fragPagerAdapter = new MainFragmentTabAdapter(mFragmentManager, fragmentList, tabList);
        viewPager.setAdapter(fragPagerAdapter);
        final int screenWidth = SizeUtil.getScreenWidth();//获取屏幕宽
        mTabLayout.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabList == null ? 0 : tabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(tabList.get(index));
                simplePagerTitleView.setTextSize(16);//文字大小
                simplePagerTitleView.setWidth(screenWidth/5);//tab 间距屏幕宽度的5分之1
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
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(SizeUtil.dip2px(context, 3));//线高
                indicator.setLineWidth(SizeUtil.dip2px(context, 15));//线宽
                indicator.setRoundRadius(SizeUtil.dip2px(context, 3));//圆角大小
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setYOffset(SizeUtil.dip2px(context, 2));//上下间距
                indicator.setColors(ContextCompat.getColor(mActivity, R.color.indicator));
                return indicator;
            }
        });
        mTabLayout.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mTabLayout, viewPager);
        viewPager.setCurrentItem(1);
    }

    /**
     * 初始化首页Fragment
     */
    private void initFragments() {
        ColumnFragment columnFragment = new ColumnFragment();
        fragmentList.add(columnFragment);
        FastNewsFragment fastNewsFragment = new FastNewsFragment();
        fragmentList.add(fastNewsFragment);
        ClassFragment classFragment2 = new ClassFragment();
        fragmentList.add(classFragment2);

    }


}
