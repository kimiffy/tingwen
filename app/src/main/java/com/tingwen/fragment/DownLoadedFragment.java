package com.tingwen.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tingwen.R;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.adapter.DownLoadedAdapter;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.NewsBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.ChangeListenNewsColorEvent;
import com.tingwen.event.DownLoadChangeStateEvent;
import com.tingwen.event.DownLoadFinishEvent;
import com.tingwen.event.DownloadEditEvent;
import com.tingwen.greendao.DownLoadNews;
import com.tingwen.utils.FileUtils;
import com.tingwen.utils.SQLHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * 已下载
 * Created by Administrator on 2017/10/19 0019.
 */
public class DownLoadedFragment extends BaseFragment {


    @BindView(R.id.rlv_downloaded)
    RecyclerView rlvDownloaded;
    private List<DownLoadNews> downLoadedList;//数据库里已经下载的新闻列表
    private List<NewsBean> list;//转化成统一的类型 新闻列表
    private DownLoadedAdapter adapter;
    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_downloaded;
    }

    @Override
    protected void initData() {
        super.initData();
        downLoadedList = SQLHelper.getInstance().getAllDownloadNews();
        //list转换为json
        if (null != downLoadedList && downLoadedList.size() != 0) {
            Gson gson = new Gson();
            String str = gson.toJson(downLoadedList);
            list = gson.fromJson(str, new TypeToken<List<NewsBean>>() {
            }.getType());
        }

        adapter = new DownLoadedAdapter(getActivity());
        adapter.setDataList(list);
        rlvDownloaded.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlvDownloaded.setItemAnimator(new DefaultItemAnimator());
        rlvDownloaded.setAdapter(adapter);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new DownLoadedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TwApplication.getNewsPlayer().setNewsList(list);
                NewsDetailActivity.actionStart(getActivity(), position, AppConfig.CHANNEL_TYPE_DOWNLOAD);
            }
        });
//        adapter.setOnItemLongClickListener(new DownLoadedAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });

    }


    /**
     * 刷新数据
     */
    public void reFresh() {
        downLoadedList = SQLHelper.getInstance().getAllDownloadNews();
        if (null != downLoadedList && downLoadedList.size() != 0) {
            Gson gson = new Gson();
            String str = gson.toJson(downLoadedList);
            list = gson.fromJson(str, new TypeToken<List<NewsBean>>() {
            }.getType());
            adapter.setDataList(list);
        }else if(null != downLoadedList && downLoadedList.size() == 0){
            list.clear();
            adapter.setDataList(list);
        }

    }


    /**
     * 下载完成刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadFinishEvent(DownLoadFinishEvent event) {
        reFresh();
    }

    /**
     * 管理新闻
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadEditEvent(DownloadEditEvent event) {

        if (null != downLoadedList && downLoadedList.size() > 0) {
            if (event.getState() == MODE_EDIT) {
                adapter.setEditMode(MODE_EDIT);
                adapter.notifyDataSetChanged();
                EventBus.getDefault().post(new DownLoadChangeStateEvent(MODE_NORMAL));
            } else if (event.getState() == MODE_NORMAL) {
                List<NewsBean> deletList = adapter.getDeletList();
                if (null != deletList && deletList.size() != 0) {

                    for (int i = 0; i < deletList.size(); i++) {
                        String id = deletList.get(i).getId();
                        SQLHelper.getInstance().deleteDownloadNews(id);
                        FileUtils.deleteFile(AppConfig.EXTRASTROGEDOWNLOADPATH + "tingwen." + id + ".mp3");
                    }
                    reFresh();
                }
                adapter.setEditMode(MODE_NORMAL);
                adapter.notifyDataSetChanged();
                EventBus.getDefault().post(new DownLoadChangeStateEvent(MODE_EDIT));

            }


        }
    }

    /**
     * 刷新正在播放的新闻标题颜色
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeListenNewsColor(ChangeListenNewsColorEvent event) {
        String channel = event.getChannel();
        adapter.setListeningId("");
        if(channel.equals(AppConfig.CHANNEL_TYPE_DOWNLOAD)){
            String id = event.getId();
            adapter.setListeningId(id);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
