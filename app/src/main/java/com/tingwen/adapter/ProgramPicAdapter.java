package com.tingwen.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.PictureBean;

import java.util.ArrayList;



/**
 * Created by Administrator on 2017/8/14 0014.
 */
public class ProgramPicAdapter extends ListBaseAdapter<PictureBean> {

    private Context mContext;

    public ProgramPicAdapter(Context context, ArrayList<PictureBean> imageUrls) {
        super(context);
        mDataList = imageUrls;
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_program_pic;
    }


    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        PictureBean bean = mDataList.get(position);
        ImageView pic = holder.getView(R.id.tv_pic);
        if (bean != null && !bean.getImg().isEmpty()) {
            Glide.with(mContext).load(bean.getImg()).into(pic);

        }

    }

    public void setList(ArrayList<PictureBean> list) {
        mDataList = list;
        notifyDataSetChanged();
    }
}
