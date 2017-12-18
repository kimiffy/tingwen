package com.tingwen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundTextView;
import com.tingwen.bean.ProgramBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.GlideCircleTransform;

import java.util.List;

/**
 * 主播
 * Created by Administrator on 2017/7/18 0018.
 */
public class ProgramAdapter1 extends RecyclerView.Adapter<ProgramAdapter1.programViewHolder>implements View.OnClickListener {

    private Boolean isSort;
    private Context mContext;
    private List<ProgramBean.ResultsBean> dataList;

    public ProgramAdapter1(Context context, List<ProgramBean.ResultsBean> list) {
        this.mContext = context;
        this.dataList = list;
    }

    public void setDataList(List<ProgramBean.ResultsBean> List) {
        dataList = List;
        notifyDataSetChanged();
    }

    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public programViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_program_list, parent, false);
        programViewHolder holder = new programViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(programViewHolder holder, int position) {
        ProgramBean.ResultsBean bean = dataList.get(position);
        String imageUrl = bean.getImages();
        final String id = bean.getId();
        if (imageUrl != null && !imageUrl.contains("ttp:")) {
            if (!imageUrl.contains("data")) {
                imageUrl = UrlProvider.URL_IMAGE + "/data/upload/" + imageUrl;
            } else {
                imageUrl = UrlProvider.URL_IMAGE + imageUrl;
            }
        }
        Glide.with(mContext).load(imageUrl).transform(new GlideCircleTransform(mContext)).
                error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang).into(holder.head);
        if(isSort){
            holder.llrank.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    holder.rank.setBackgroundResource(R.drawable.program_fist);
                    holder.rank.setText("");
                    break;
                case 1:
                    holder.rank.setBackgroundResource(R.drawable.program_second);
                    holder.rank.setText("");
                    break;
                case 2:
                    holder.rank.setBackgroundResource(R.drawable.program_third);
                    holder.rank.setText("");
                    break;
                default:
                    holder.rank.setBackgroundResource(R.drawable.program_tranprarent);
                    holder.rank.setText((position + 1) + "");
            }}
        holder.name.setText(bean.getName());
        holder.fans.setText("粉丝：" + bean.getFan_num());
        holder.gold.setText("金币: "+bean.getGold());
        if (FollowUtil.isFollowed(id)) {
            holder.follow.setText("已关注");
        } else {
            holder.follow.setText("关注");
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FollowUtil.isFollowed(id)) {
                    followListener.delete(id);
                } else {
                    followListener.follow(id);
                }
            }
        });
        String description = bean.getDescription();
        if(description!=null&&!description.isEmpty()){
            holder.describe.setText(description);
        }else{
            holder.describe.setText("暂无简介");
        }

        holder.itemView.setTag(position);//设置标记

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setIsSort(boolean b) {
        isSort=b;
    }


    class programViewHolder extends RecyclerView.ViewHolder {

        private TextView rank;
        private ImageView head;
        private TextView name;
        private TextView fans;
        private TextView gold;
        private RoundTextView follow;
        private TextView describe;
        private  LinearLayout llrank;

        public programViewHolder(View itemView) {
            super(itemView);
            llrank = (LinearLayout) itemView.findViewById(R.id.ll_ranking);
            rank = (TextView) itemView.findViewById(R.id.tv_ranking);
            head = ( ImageView)itemView.findViewById(R.id.iv_head_img);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            fans = (TextView) itemView.findViewById(R.id.tv_fans);
            gold = (TextView) itemView.findViewById(R.id.tv_gold);
            follow=(RoundTextView)itemView.findViewById(R.id.add_follow);
            describe = (TextView) itemView.findViewById(R.id.tv_describe);
        }
    }

    public void setListener(FollowListener deleteListener) {
        this.followListener = deleteListener;
    }

    private FollowListener followListener;

    public interface FollowListener {
        void delete(String id);

        void follow(String id);

    }

}
