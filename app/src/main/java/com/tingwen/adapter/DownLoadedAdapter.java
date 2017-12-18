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
import com.tingwen.bean.NewsBean;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.SQLHelper;
import com.tingwen.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已经下载的新闻
 * Created by Administrator on 2017/10/24 0024.
 */
public class DownLoadedAdapter extends RecyclerView.Adapter<DownLoadedAdapter.ViewHolder> implements View.OnClickListener{

    List<NewsBean> dataList;
    private LayoutInflater inflater;
    private Context context;
    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;
    private int mode=0;
    List<NewsBean> deletList=new ArrayList<>();

    private String id;//正在播放的新闻

    public DownLoadedAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDataList(List<NewsBean> List) {
        dataList = List;
        deletList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_downloaded, parent, false);
        view.setOnClickListener(this);
//      view.setOnLongClickListener(this);
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
                if(deletList.contains(deleteBean)){
                    dataList.get(tag).setSelect(false);
                    deletList.remove(deleteBean);
                }else {
                    dataList.get(tag).setSelect(true);
                    deletList.add(deleteBean);
                }
                notifyDataSetChanged();
            }
        }

    }

//    @Override
//    public boolean onLongClick(View v) {
//        if (mOnItemLongClickListener != null) {
//            mOnItemLongClickListener.onItemLongClick(v,(int)v.getTag());
//        }
//        return true;
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_news_image)
        ImageView ivNewsImage;
        @BindView(R.id.tv_news_title)
        TextView tvNewsTitle;
        @BindView(R.id.tv_news_date)
        TextView tvNewsDate;
        @BindView(R.id.tv_news_time)
        TextView tvNewsTime;
//        @BindView(R.id.iv_delete)
//        ImageView delete;
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
                tvNewsTitle.setTextColor(Color.parseColor("#2e3133"));//默认颜色

                if(SQLHelper.getInstance().isListenedNews(bean.getId())){
                    tvNewsTitle.setTextColor(Color.parseColor("#B3B3B3"));//已经收听过的新闻颜色
                }

                if(!TextUtils.isEmpty(id)&&id.equals(bean.getId())){
                    tvNewsTitle.setTextColor(Color.parseColor("#55B9DD"));//正在听新闻颜色
                }

                tvNewsTime.setText(FileSizeUtil.getFileSize(bean.getPost_size()));
                tvNewsDate.setText(TimeUtil.getShortTime(bean.getPost_date()));
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        FileUtils.deleteFile(AppConfig.EXTRASTROGEDOWNLOADPATH+"tingwen." + bean.getId() + ".mp3");
//                        SQLHelper.getInstance().deleteDownloadNews(bean.getId());
//                        dataList.remove(bean);
//                        notifyDataSetChanged();
//                    }
//                });
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


    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

//    private OnItemLongClickListener mOnItemLongClickListener = null;
//
//    public interface OnItemLongClickListener {
//        void onItemLongClick(View view , int position);
//    }
//
//    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
//        this.mOnItemLongClickListener = listener;
//    }


    public void setEditMode(int mode){
        this.mode=mode;
    }

    public List<NewsBean> getDeletList(){
       return this.deletList;
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
