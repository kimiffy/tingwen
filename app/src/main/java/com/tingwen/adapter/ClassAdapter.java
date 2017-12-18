package com.tingwen.adapter;


import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.ClassBean;
import com.tingwen.net.UrlProvider;

import org.raphets.roundimageview.RoundImageView;

import java.util.List;

/**
 * 听闻课堂adapter
 * Created by Administrator on 2017/7/10 0010.
 */
public class ClassAdapter extends ListBaseAdapter<ClassBean.ResultsBean> {

    private String imageUrl;

    public ClassAdapter(Context context, List<ClassBean.ResultsBean> list) {
        super(context);
        mDataList=list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_class;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ClassBean.ResultsBean classbean = mDataList.get(position);

        RoundImageView imageView = holder.getView(R.id.iv_image);
        TextView name = holder.getView(R.id.tv_className);
        TextView indroduce = holder.getView(R.id.tv_introduce);
        TextView money = holder.getView(R.id.tv_money);

        imageUrl = String.valueOf(classbean.getImages());

        if (imageUrl != null && !imageUrl.contains("ttp:")) {
            if (imageUrl.contains("/data/upload/")) {
                imageUrl = UrlProvider.URL_IMAGE + imageUrl;
            } else {
                imageUrl = UrlProvider.URL_IMAGE2 + imageUrl;
            }
        }

        String price = subZeroAndDot(classbean.getPrice());
        money.setText("¥ " + price);
        name.setText(classbean.getName());
        indroduce.setText(classbean.getDescription());
        Glide.with(mContext).load(imageUrl).into(imageView);

    }


    private String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }



}
