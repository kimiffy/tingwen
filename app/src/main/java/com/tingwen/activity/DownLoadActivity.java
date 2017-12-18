package com.tingwen.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.event.DownLoadChangeStateEvent;
import com.tingwen.event.DownloadEditEvent;
import com.tingwen.fragment.BatchDownloadFragment;
import com.tingwen.fragment.DownLoadedFragment;
import com.tingwen.fragment.DownLoadingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 我的下载
 * Created by Administrator on 2017/10/19 0019.
 */
public class DownLoadActivity extends BaseActivity {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.tv_edit)
    TextView tvEdit;

    @BindView(R.id.viewFirst)
    View line1;
    @BindView(R.id.ll_batch)
    LinearLayout llBatch;
    @BindView(R.id.viewSecond)
    View line2;
    @BindView(R.id.ll_downloaded)
    LinearLayout llDownloaded;
    @BindView(R.id.viewThird)
    View line3;
    @BindView(R.id.ll_downloading)
    LinearLayout llDownloading;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.vp_download)
    ViewPager vpDownload;

    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    protected int currentViewPagerIndex = 0;
    private DownLoadedFragment downLoadedFragment;
    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;
    private int state=0;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_download;
    }

    @Override
    protected void initData() {
        super.initData();

        BatchDownloadFragment batchDownloadFragment = new BatchDownloadFragment();
        downLoadedFragment = new DownLoadedFragment();
        DownLoadingFragment downLoadingFragment = new DownLoadingFragment();
        fragmentList.add(batchDownloadFragment);
        fragmentList.add(downLoadedFragment);
        fragmentList.add(downLoadingFragment);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpDownload.setAdapter(adapter);
        vpDownload.setOffscreenPageLimit(3);
        setView(0);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void initUI() {
        super.initUI();
    }


    @OnClick({R.id.ivLeft,R.id.tv_edit, R.id.ll_batch, R.id.ll_downloaded, R.id.ll_downloading})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.ll_batch:
                if (vpDownload != null) {
                    vpDownload.setCurrentItem(0);
                    setView(0);
                }
                break;
            case R.id.ll_downloaded:
                if (vpDownload != null) {
                    vpDownload.setCurrentItem(1);
                    setView(1);
                }
                break;
            case R.id.ll_downloading:
                if (vpDownload != null) {
                    vpDownload.setCurrentItem(2);
                    setView(2);
                }
                break;
            case R.id.tv_edit:
                if(state==MODE_NORMAL){
                    EventBus.getDefault().post(new DownloadEditEvent(1));
                }else if(state==MODE_EDIT){
                    EventBus.getDefault().post(new DownloadEditEvent(0));
                }

                break;
            default:
                break;
        }
    }


    @Override
    protected void setListener() {
        super.setListener();

        vpDownload.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                currentViewPagerIndex = arg0;
                if (currentViewPagerIndex == 1) {
                    tvEdit.setVisibility(View.VISIBLE);
                }
                setView(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {

            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }


    private void setView(int index) {
        if (index == 0) {
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
        } else if (index == 1) {
            line1.setVisibility(View.GONE);
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.GONE);
        } else {
            line1.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.GONE);

        }
    }

    /**
     * 改变删除按钮状态
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownLoadChangeStateEvent(DownLoadChangeStateEvent event) {
        if(event.getState()==MODE_NORMAL){
            state=MODE_EDIT;
            tvEdit.setText("确定");
        }else if(event.getState()==MODE_EDIT){
            state=MODE_NORMAL;
            tvEdit.setText("删除");

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
