package com.tingwen.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tingwen.R;
import com.tingwen.adapter.DownloadingAdapter;
import com.tingwen.base.BaseFragment;
import com.tingwen.widget.logger.Logger;

import butterknife.BindView;

/**
 * 正在下载
 * Created by Administrator on 2017/10/19 0019.
 */
public class DownLoadingFragment extends BaseFragment {


    @BindView(R.id.rlv_downloading)
    RecyclerView rlvDownloading;

    private DownloadingAdapter adapter;



    @Override
    protected int getLayoutResId() {
        return R.layout.fragmet_downloading;
    }

    @Override
    protected void initData() {
        super.initData();
        adapter = new DownloadingAdapter(getActivity());
//        adapter.updateData(DownloadingAdapter.TYPE_ING);
        rlvDownloading.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlvDownloading.setItemAnimator(new DefaultItemAnimator());
        rlvDownloading.setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.unRegister();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateData(DownloadingAdapter.TYPE_ING);
        adapter.notifyDataSetChanged();
    }




}
