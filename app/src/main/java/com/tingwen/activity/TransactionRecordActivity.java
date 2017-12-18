package com.tingwen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.fragment.TransactionRecordFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 交易记录
 * Created by Administrator on 2017/11/7 0007.
 */
public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_consume_record)
    TextView tvConsumeRecord;
    @BindView(R.id.tv_recharge_record)
    TextView tvRechargeRecord;
    @BindView(R.id.view_consume_record)
    View viewConsumeRecord;
    @BindView(R.id.view_recharge_record)
    View viewRechargeRecord;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int type = 1;//1代表消费记录 2代表充值记录
    private ViewPagerAdapter viewPagerAdapter;
    protected int currentViewPagerIndex = 0;

    private TransactionRecordFragment transactionRecordFragment, transactionRecordFragment1;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transcation_record;
    }


    @Override
    protected void initData() {
        super.initData();
        setConsumeRecord();
        transactionRecordFragment = new TransactionRecordFragment();
        transactionRecordFragment1 = new TransactionRecordFragment();
        setArgument();
        fragmentList.add(transactionRecordFragment);
        fragmentList.add(transactionRecordFragment1);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }


    @Override
    protected void setListener() {
        super.setListener();
        tvConsumeRecord.setOnClickListener(this);
        tvRechargeRecord.setOnClickListener(this);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setConsumeRecord();
                } else {
                    setRechargeRecord();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_consume_record:
                setConsumeRecord();
                break;
            case R.id.tv_recharge_record:
                setRechargeRecord();
                break;
            default:
                break;
        }
    }

    private void setConsumeRecord() {
        tvConsumeRecord.setSelected(true);
        tvRechargeRecord.setSelected(false);
        viewConsumeRecord.setVisibility(View.VISIBLE);
        viewRechargeRecord.setVisibility(View.INVISIBLE);
        type = 1;
        currentViewPagerIndex = 0;
        viewPager.setCurrentItem(0);
    }


    private void setArgument() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        transactionRecordFragment.setArguments(bundle);
        Bundle bundle1 = new Bundle();
        bundle1.putInt("type", 2);
        transactionRecordFragment1.setArguments(bundle1);
    }


    private void setRechargeRecord() {
        tvConsumeRecord.setSelected(false);
        tvRechargeRecord.setSelected(true);
        viewConsumeRecord.setVisibility(View.INVISIBLE);
        viewRechargeRecord.setVisibility(View.VISIBLE);
        type = 2;
        currentViewPagerIndex = 1;
        viewPager.setCurrentItem(1);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
