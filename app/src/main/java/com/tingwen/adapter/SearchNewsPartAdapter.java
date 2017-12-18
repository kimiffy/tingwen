package com.tingwen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.bean.SearchPartBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.GlideCircleTransform;

import java.util.List;

/**
 * 搜索新闻的节目(主播)
 * Created by Administrator on 2017/10/26 0026.
 */
public class SearchNewsPartAdapter extends BaseAdapter {

    private List<SearchPartBean.ResultsBean.ActBean> list;
    private Context context;


    public SearchNewsPartAdapter(Context context, List<SearchPartBean.ResultsBean.ActBean> actBeanList) {
        this.list = actBeanList;
        this.context = context;

    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public SearchPartBean.ResultsBean.ActBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_part, parent, false);
            viewHolder.head = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.tv_describe);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SearchPartBean.ResultsBean.ActBean bean = list.get(position);

        viewHolder.name.setText(bean.getName());
        viewHolder.describe.setText(bean.getDescription());

        String imageUrl = bean.getImages();
        if (imageUrl != null && !imageUrl.contains("ttp:")) {
            if (imageUrl.contains("/data/upload/")) {
                imageUrl = UrlProvider.URL_IMAGE + imageUrl;
            } else {
                imageUrl = UrlProvider.URL_IMAGE2 + imageUrl;
            }
        }
        Glide.with(context).load(imageUrl).transform(new GlideCircleTransform(context)).into(viewHolder.head);


        return convertView;

    }

    private class ViewHolder {

        ImageView head;
        TextView name;
        TextView describe;
    }

    public void setDataList(List<SearchPartBean.ResultsBean.ActBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
