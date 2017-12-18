package com.tingwen.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MainFragmentTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabList = new ArrayList<>();


    public MainFragmentTabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabTitles) {
        super(fm);
        this.fragmentList = fragments;
        this.tabList = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment page = null;
        if (fragmentList.size() > position) {
            page = fragmentList.get(position);
            if (page != null) {
                return page;
            }
        }
        return page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return tabList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }
}
