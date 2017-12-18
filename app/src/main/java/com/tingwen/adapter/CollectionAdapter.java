package com.tingwen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.bean.CollectionBean;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.utils.DownLoadUtil;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.SQLHelper;
import com.tingwen.utils.TimeUtil;

import java.util.List;

/**
 * 我的收藏
 * Created by Administrator on 2017/7/31 0031.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {


    private Context mContext;
    private List<CollectionBean.ResultsBean> dataList;

    private String id;//正在播放的新闻


    public CollectionAdapter(Context context, List<CollectionBean.ResultsBean> list) {
        this.mContext=context;
        this.dataList=list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_collection_list, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CollectionBean.ResultsBean bean = dataList.get(position);

        holder.title.setText( bean.getPost_title());

        holder.title.setTextColor(Color.parseColor("#2e3133"));//默认颜色

        if(SQLHelper.getInstance().isListenedNews(bean.getId())){
            holder.title.setTextColor(Color.parseColor("#B3B3B3"));//已经收听过的新闻颜色
        }

        if(!TextUtils.isEmpty(id)&&id.equals(bean.getId())){
            holder.title.setTextColor(Color.parseColor("#55B9DD"));//正在听新闻颜色
        }



        String post_time = bean.getPost_time();
        String post_date = bean.getPost_date();
        String post_size = bean.getPost_size();
        String url =bean.getSmeta().replace("{\"thumb\":\"", "").replace("\\","").replace("\"}", "");
        Glide.with(mContext).load(url).into(holder.image);

        if(null!=post_time&&!post_time.isEmpty()){
            holder. time.setText(FileSizeUtil.getFileSize(post_size));
        }
        if (null!=post_date &&!post_date.isEmpty()) {
            long T;
            try {
                T = Integer.valueOf(post_date);
            } catch (Exception e) {
                T = 0;
            }
            if (T > 0) {
                holder. data.setText(TimeUtil.getShortTime(T));
            } else {
                holder.data.setText(TimeUtil.getShortTime(post_date));
            }

        }

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionBean.ResultsBean downLoadBean = dataList.get(position);
                DownLoadBean news = new DownLoadBean();
                news.setId(downLoadBean.getId());
                news.setPost_mp(downLoadBean.getPost_mp());
                news.setPost_title( downLoadBean.getPost_title());
                news.setSmeta(downLoadBean.getSmeta());
                news.setPost_date(downLoadBean.getPost_date());
                news.setPost_excerpt(downLoadBean.getPost_excerpt());
                news.setPost_size(downLoadBean.getPost_size());
                news.setPost_time(downLoadBean.getPost_time());
                news.setPost_lai(downLoadBean.getPost_lai());

                DownLoadUtil.getInstance().downLoadNews(mContext,news);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView data;
        private TextView time;
        private ImageView download;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_news_image);
            title= ((TextView) itemView.findViewById(R.id.tv_news_title));
            data=((TextView) itemView.findViewById(R.id.tv_news_date));
            time=((TextView) itemView.findViewById(R.id.tv_news_time));
            download = (ImageView) itemView.findViewById(R.id.iv_download);
        }


    }

    public void setDataList(List<CollectionBean.ResultsBean> List) {
        dataList = List;
        notifyDataSetChanged();
    }

    /**
     * 设置正在播放的新闻id
     * @param id
     */
    public void setListeningId(String id){
        this.id=id;
        notifyDataSetChanged();
    }


}
