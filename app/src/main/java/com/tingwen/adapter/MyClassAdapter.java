package com.tingwen.adapter;

import android.content.Context;
import android.widget.TextView;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.MyClassBean;
import com.tingwen.net.UrlProvider;

import org.raphets.roundimageview.RoundImageView;

import java.util.List;

/**
 * 我的课堂
 * Created by Administrator on 2017/8/1 0001.
 */
public class MyClassAdapter extends ListBaseAdapter<MyClassBean.ResultsBean> {

    private String imageUrl;

    public MyClassAdapter(Context context, List<MyClassBean.ResultsBean> list) {
        super(context);
        mDataList=list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_class;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MyClassBean.ResultsBean classbean = mDataList.get(position);

        RoundImageView imageView = holder.getView(R.id.iv_image);
        TextView name = holder.getView(R.id.tv_className);
        TextView indroduce = holder.getView(R.id.tv_introduce);

        imageUrl = String.valueOf(classbean.getImages());

        if (imageUrl != null && !imageUrl.contains("ttp:")) {
            if (imageUrl.contains("/data/upload/")) {
                imageUrl = UrlProvider.URL_IMAGE + imageUrl;
            } else {
                imageUrl = UrlProvider.URL_IMAGE2 + imageUrl;
            }
        }
        name.setText(classbean.getName());
        indroduce.setText(classbean.getDescription());
        Glide.with(mContext).load(imageUrl).into(imageView);

    }

}
