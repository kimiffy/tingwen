package com.tingwen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwen.R;

import java.util.List;

/**
 * 定时关闭程序的adapter
 * Created by Administrator on 2017/11/6 0006.
 */
public class AlarmAdapter extends BaseAdapter {

    private List<Integer> list;
    private Context context;
    private int mPosition=-1; //判断选中的哪个选项
    public AlarmAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_time,null);
            viewHolder = new ViewHolder();
            viewHolder.tvLeft = (TextView) convertView.findViewById(R.id.tvLeft);
            viewHolder.ivSelect = (ImageView) convertView.findViewById(R.id.ivSelect);
//            viewHolder.tvTimer = (TextView) convertView.findViewById(R.id.tvTimer);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvLeft.setText(list.get(position)+"分钟");
        if(mPosition == position){
            viewHolder.ivSelect.setImageResource(R.drawable.icon_select);
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivSelect.setVisibility(View.GONE);
        }


        return convertView;
    }


    public void setSlectPosition(int position){
        mPosition= position;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvLeft;
        ImageView ivSelect;
//        TextView tvTimer;
    }
}
