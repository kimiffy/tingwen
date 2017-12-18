package com.tingwen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.NewsBean;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.TimeUtil;
import com.tingwen.widget.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 */
public class ProgramDownloadAdapter extends ListBaseAdapter<NewsBean> implements View.OnClickListener {

    private LayoutInflater inflater;
    private Context mContext;
    List<NewsBean> downloadList = new ArrayList<>();
    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;
    private int mode = 0;


    public ProgramDownloadAdapter(Context context, List<NewsBean> list) {
        super(context);
        mDataList = list;
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        NewsBean bean = mDataList.get(position);

        ImageView ivNewsImage = holder.getView(R.id.iv_news_image);

        TextView tvNewsTitle = holder.getView(R.id.tv_news_title);

        TextView tvNewsTime = holder.getView(R.id.tv_news_time);

        TextView tvNewsDate = holder.getView(R.id.tv_news_date);

        ImageView state = holder.getView(R.id.iv_state);

        if (bean != null) {
            String url = bean.getSmeta().replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");
            Glide.with(mContext).load(url).into(ivNewsImage);
            tvNewsTitle.setText(bean.getPost_title());
            tvNewsTime.setText(FileSizeUtil.getFileSize(bean.getPost_size()));
            tvNewsDate.setText(TimeUtil.getShortTime(bean.getPost_date()));
            if (mode == MODE_NORMAL) {
                state.setVisibility(View.GONE);
            } else if (mode == MODE_EDIT) {
                state.setVisibility(View.VISIBLE);
                if (bean.isSelect) {
                    state.setImageResource(R.drawable.icon_select);
                } else {
                    state.setImageResource(R.drawable.icon_unselect);
                }
            }


        }

        holder.itemView.setTag(position);


    }


    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_downloaded, parent, false);
        view.setOnClickListener(this);
        return new SuperViewHolder(view);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            if(mode==MODE_NORMAL){
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }else if(mode==MODE_EDIT){
                int tag =(int) v.getTag();
                NewsBean deleteBean = mDataList.get(tag);
                if(downloadList.contains(deleteBean)){
                    mDataList.get(tag).setSelect(false);
                    downloadList.remove(deleteBean);
                    Logger.e("取消选中");
                }else {
                    mDataList.get(tag).setSelect(true);
                    downloadList.add(deleteBean);
                    Logger.e("选中");
                }
                notifyDataSetChanged();
            }
        }
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
     *
     * @param mode
     */
    public void setEditMode(int mode) {
        this.mode = mode;
    }

    /**
     * 获取需要下载的新闻列表
     *
     * @return
     */
    public List<NewsBean> getDownLoadList() {
        return this.downloadList;
    }

    /**
     * 全选
     */
    public void allSelect() {

        if(null!=mDataList){
            downloadList.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                mDataList.get(i).setSelect(true);
                downloadList.add(mDataList.get(i));
            }
            notifyDataSetChanged();
        }


    }


}
