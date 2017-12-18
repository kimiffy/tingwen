package com.tingwen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.bean.NewsBean;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.TimeUtil;
import com.tingwen.widget.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 批量下载
 * Created by Administrator on 2017/10/25 0025.
 */
public class BatchDownloadAdapter  extends RecyclerView.Adapter<BatchDownloadAdapter.ViewHolder>implements View.OnClickListener{



    private Context context;
    private LayoutInflater inflater;
    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;
    private int mode=0;
    List<NewsBean> dataList;
    List<NewsBean> downloadList=new ArrayList<>();

    public BatchDownloadAdapter(Context context) {
        this.context = context;
        downloadList.clear();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_downloaded, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsBean bean = dataList.get(position);
        holder.setBean(bean);
        holder.bind();
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
       return dataList == null ? 0 : dataList.size();
    }



    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            if(mode==MODE_NORMAL){
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }else if(mode==MODE_EDIT){
                int tag =(int) v.getTag();
                NewsBean deleteBean = dataList.get(tag);
                if(downloadList.contains(deleteBean)){
                    dataList.get(tag).setSelect(false);
                    downloadList.remove(deleteBean);
                    Logger.e("取消选中");
                }else {
                    dataList.get(tag).setSelect(true);
                    downloadList.add(deleteBean);
                    Logger.e("选中");
                }
                notifyDataSetChanged();
            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_news_image)
        ImageView ivNewsImage;
        @BindView(R.id.tv_news_title)
        TextView tvNewsTitle;
        @BindView(R.id.tv_news_date)
        TextView tvNewsDate;
        @BindView(R.id.tv_news_time)
        TextView tvNewsTime;

        @BindView(R.id.iv_state)
        ImageView state;

        NewsBean bean;

        public NewsBean getBean() {
            return bean;
        }

        public void setBean(NewsBean bean) {
            this.bean = bean;
        }

        public void bind() {
            if (bean != null) {
                String url = bean.getSmeta().replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");
                Glide.with(context).load(url).into(ivNewsImage);
                tvNewsTitle.setText(bean.getPost_title());
                tvNewsTime.setText(FileSizeUtil.getFileSize(bean.getPost_size()));
                tvNewsDate.setText(TimeUtil.getShortTime(bean.getPost_date()));
                if(mode== MODE_NORMAL){
                    state.setVisibility(View.GONE);
                }else if(mode==MODE_EDIT){
                    state.setVisibility(View.VISIBLE);
                    if(bean.isSelect){
                        state.setImageResource(R.drawable.icon_select);
                    }else{
                        state.setImageResource(R.drawable.icon_unselect);
                    }
                }



            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


    public void setDataList(List<NewsBean> List) {
        dataList = List;
        downloadList.clear();
        notifyDataSetChanged();
    }


    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 设置模式
     * @param mode
     */
    public void setEditMode(int mode){
        this.mode=mode;
    }

    /**
     * 获取需要下载的新闻列表
     * @return
     */
    public List<NewsBean> getDownLoadList(){
        return this.downloadList;
    }

    /**
     * 全选
     */
    public void allSelect(){
       if(null!=dataList){
           downloadList.clear();
           for (int i = 0; i < dataList.size(); i++) {
               dataList.get(i).setSelect(true);
               downloadList.add(dataList.get(i));
           }
           notifyDataSetChanged();
       }
    }



}
