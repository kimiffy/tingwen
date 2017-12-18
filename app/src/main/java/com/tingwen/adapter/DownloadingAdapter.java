package com.tingwen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.tingwen.R;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.event.DownLoadFinishEvent;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.LogDownloadListener;
import com.tingwen.utils.SQLHelper;
import com.tingwen.widget.NumberProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 正在下载的新闻
 * Created by Administrator on 2017/10/23 0023.
 */
public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;

    private LayoutInflater inflater;
    private Context context;
    private int type;
    private List<DownloadTask> values;

    public DownloadingAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
//        if (type == TYPE_ALL) values = OkDownload.restore(DownloadManager.getInstance().getAll());
//        if (type == TYPE_FINISH) values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        if (type == TYPE_ING) values = OkDownload.restore(DownloadManager.getInstance().getDownloading());

        for (DownloadTask value : values) {
            value.register(null);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_downloading, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadTask task = values.get(position);
        String holderTag = task.progress.tag;
        task.register(new ListDownloadListener(holderTag, holder))//
                .register(new LogDownloadListener());
        holder.setTag(holderTag);
        holder.setTask(task);
        holder.bind();
        holder.refresh(task.progress);
    }

    public void unRegister() {
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister("ListDownloadListener_" + type);
        }
    }


    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.pbProgress)
        NumberProgressBar pbProgress;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tvSize)
        TextView tvSize;

        private DownloadTask task;

        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }


        public void bind() {
            Progress progress = task.progress;
            DownLoadBean bean = (DownLoadBean) progress.extra1;
            if (bean != null) {
                String url = bean.getSmeta().replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");
                Glide.with(context).load(url).into(ivImage);
                title.setText(bean.getPost_title());
                tvSize.setText(FileSizeUtil.getFileSize(bean.getPost_size()));
            }
        }
        public void refresh(Progress progress) {

            switch (progress.status) {
                case Progress.NONE:
                    tvState.setText("下载");
                    break;
                case Progress.WAITING:
                    tvState.setText("等待");
                    tvState.setBackgroundResource(R.drawable.bg_downloading_grey);
                    break;
                case Progress.PAUSE:
                    tvState.setText("继续");
                    break;
                case Progress.ERROR:
                    tvState.setText("出错");
                    tvState.setBackgroundResource(R.drawable.bg_downloading_red);
                    break;
                case Progress.LOADING:
                    tvState.setText("下载中");
                    tvState.setBackgroundResource(R.drawable.bg_downloading_blue);
                    break;
            }

            pbProgress.setMax(10000);
            pbProgress.setProgress((int) (progress.fraction * 10000));
        }

        @OnClick(R.id.tv_state)
        public void start() {
            Progress progress = task.progress;
            switch (progress.status) {
                case Progress.PAUSE:
                    task.start();
                case Progress.NONE:
                    task.start();
                case Progress.ERROR:
                    task.restart();
                    break;
                case Progress.LOADING:
                    task.pause();
                    break;
            }
            refresh(progress);
        }

    }


    private class ListDownloadListener extends DownloadListener {

        private ViewHolder holder;

        ListDownloadListener(Object tag, ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {

        }

        @Override
        public void onProgress(Progress progress) {
            if (this.tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            DownLoadBean bean = (DownLoadBean) progress.extra1;
            Boolean hasNews = SQLHelper.getInstance().isHasNews(bean.getId());
            if(!hasNews){
                SQLHelper.getInstance().insertDownloadNews(bean);
                EventBus.getDefault().post(new DownLoadFinishEvent(bean.getId()));
            }
            updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }
}
