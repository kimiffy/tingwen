package com.tingwen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.bean.SubscriptionBean;
import com.tingwen.utils.DownLoadUtil;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.SQLHelper;
import com.tingwen.utils.TimeUtil;
import com.tingwen.utils.TouchUtil;

import java.util.List;

/**
 * 订阅
 * Created by Administrator on 2017/7/12 0012.
 */
public class SubscriptionAdapter extends ListBaseAdapter<SubscriptionBean.ResultsBean>{



    private String id;//正在播放的新闻
    public SubscriptionAdapter(Context context, List<SubscriptionBean.ResultsBean> list) {
        super(context);
        mDataList=list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        SubscriptionBean.ResultsBean resultsBean = mDataList.get(position);
        String post_title = resultsBean.getPost_title();
        String post_time = resultsBean.getPost_time();
        String post_size = resultsBean.getPost_size();
        String post_date = resultsBean.getPost_date();
        String smeta = resultsBean.getSmeta();
        String url = smeta.replace("{\"thumb\":\"", "").replace("\\","").replace("\"}", "");

        ImageView image = holder.getView(R.id.iv_news_image);
        TextView title = holder.getView(R.id.tv_news_title);
        TextView time = holder.getView(R.id.tv_news_time);
        TextView data = holder.getView(R.id.tv_news_date);
        ImageView download = holder.getView(R.id.iv_download);

        TouchUtil.setTouchDelegate(download,50);
        Glide.with(mContext).load(url).into(image);

        title.setText(post_title);
        title.setTextColor(Color.parseColor("#2e3133"));//默认颜色

        if(SQLHelper.getInstance().isListenedNews(resultsBean.getId())){
            title.setTextColor(Color.parseColor("#B3B3B3"));//已经收听过的新闻颜色
        }

        if(!TextUtils.isEmpty(id)&&id.equals(resultsBean.getId())){
            title.setTextColor(Color.parseColor("#55B9DD"));//正在听新闻颜色
        }

        if(null!=post_time&&!post_time.isEmpty()){
            time.setText(FileSizeUtil.getFileSize(post_size));
        }
        if (null!=post_date &&!post_date.isEmpty()) {
            long T;
            try {
                T = Integer.valueOf(post_date);
            } catch (Exception e) {
                T = 0;
            }
            if (T > 0) {
                data.setText(TimeUtil.getShortTime(T));
            } else {
                data.setText(TimeUtil.getShortTime(post_date));
            }

        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubscriptionBean.ResultsBean downLoadBean = mDataList.get(position);
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

    /**
     * 设置正在播放的新闻id
     * @param id
     */
    public void setListeningId(String id){
        this.id=id;
        notifyDataSetChanged();
    }
}
