package com.tingwen.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.FollowBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.GlideCircleTransform;

import java.util.List;

/**
 * 关注列表
 * Created by Administrator on 2017/7/12 0012.
 */
public class FollowAdapter extends ListBaseAdapter<FollowBean.ResultsBean> {

    public FollowAdapter(Activity context, List<FollowBean.ResultsBean> list) {
        super(context);
        mDataList=list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_follow;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
       final FollowBean.ResultsBean followBean = mDataList.get(position);

        ImageView ivhead = holder.getView(R.id.iv_head_img);
        TextView tvname = holder.getView(R.id.tv_name);
        TextView tvfans = holder.getView(R.id.tv_fans);
        TextView tvgold = holder.getView(R.id.tv_gold);
        TextView tvfollow = holder.getView(R.id.add_follow);
        TextView tvdescribe = holder.getView(R.id.tv_describe);

        String url = followBean.getImages();
        if (url != null && !url.contains("ttp:")) {
            if (!url.contains("data")) {
                url = UrlProvider.URL_IMAGE + "/data/upload/" + url;
            } else {
                url = UrlProvider.URL_IMAGE + url;
            }
        }

        String name = followBean.getName();
        String description = followBean.getDescription();
        int fan_num = followBean.getFan_num();
        int gold = followBean.getGold();

        Glide.with(mContext).load(url).transform(new GlideCircleTransform(mContext)).into(ivhead);
        tvname.setText(name);
        tvdescribe.setText(description);
        tvfans.setText("粉丝：" + fan_num);
        tvgold.setText("金币：" + gold);

        if(followBean.getIsFollowed()==1){
            tvfollow .setText("关注");
        }else{
            tvfollow .setText("已关注");
        }


        tvfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(followBean.getIsFollowed()==1){
                    followListener.follow(position);
                }else{
                    followListener.delete(position);
                }


            }
        });

    }



    public void setListener(FollowListener deleteListener) {
        this.followListener = deleteListener;
    }

    private FollowListener followListener;

    public interface FollowListener {
        void delete(int position);
        void follow(int position);

    }






}
