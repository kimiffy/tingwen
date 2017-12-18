package com.tingwen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.FansBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.GlideCircleTransform;

import java.util.List;

/**
 * 粉丝列表
 * Created by Administrator on 2017/9/26 0026.
 */
public class FansAdapter extends ListBaseAdapter<FansBean.ResultsBean> {
    private Context context;
    public FansAdapter(Context context, List<FansBean.ResultsBean> list) {
        super(context);
        mDataList=list;
        this.context=context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_fans;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        FansBean.ResultsBean bean = mDataList.get(position);
        ImageView head =  holder.getView(R.id.iv_head);
        TextView name =  holder.getView(R.id.tv_name);
        TextView describe =  holder.getView(R.id.tv_describe);
        TextView add =  holder.getView(R.id.add_follow);

        String avatar = bean.getAvatar();
        String user_nicename = bean.getUser_nicename();
        String user_login = bean.getUser_login();
        String signature = bean.getSignature();
        String uid = bean.getUid();
        if(!TextUtils.isEmpty(avatar)){
            if(!avatar.contains("http")){
                avatar= UrlProvider.URL_IMAGE_USER+ avatar;
            }
        }
        Glide.with(context).load(avatar).transform(new GlideCircleTransform(context))
                .error(R.drawable.img_touxiang).placeholder(R.drawable.img_touxiang).into(head);

        if(!TextUtils.isEmpty(user_nicename)){
            name.setText(user_nicename);
        }else{
            name.setText(user_login);
        }

        if(!TextUtils.isEmpty(signature)){
             describe.setText(signature);
        }else{
             describe.setText("暂无简介");
        }
        // TODO: 2017/9/26 0026 关注  取消关注  相互关注
        if(FollowUtil.isAttentioned(uid)){
            add.setText("互相关注");
        }else{
            add.setText("关注");
        }


    }
}
