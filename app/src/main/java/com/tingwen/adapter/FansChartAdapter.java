package com.tingwen.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.FansChartBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.TimeUtil;

import java.util.List;

/**
 * 粉丝榜
 * Created by Administrator on 2017/8/8 0008.
 */
public class FansChartAdapter extends ListBaseAdapter<FansChartBean.ResultsBean.ListBean> {

    private Context mContext;
    public FansChartAdapter(Context context, List<FansChartBean.ResultsBean.ListBean> list) {
        super(context);
        mDataList=list;
        mContext=context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_fans_chart;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        FansChartBean.ResultsBean.ListBean bean = mDataList.get(position);

        RelativeLayout item=holder.getView(R.id.rl_all);
        TextView tvRank=holder.getView(R.id.tv_ranking);
        ImageView ivRank=holder.getView(R.id.iv_ranking);
        ImageView ivHead=holder.getView(R.id.iv_head);
        ImageView ivHeadup=holder.getView(R.id.iv_head_up);
        TextView tvName=holder.getView(R.id.tv_name);
        TextView tvTime=holder.getView(R.id.tv_time);
        TextView tvGold=holder.getView(R.id.tv_gold);
        TextView tvTingbi=holder.getView(R.id.tv_listen_money);
        TextView tvMessage=holder.getView(R.id.tv_message);

        if(position<3){
            LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams)item.getLayoutParams();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)ivHead.getLayoutParams();
            params.width = SizeUtil.dip2px(mContext, 65);
            params.height = SizeUtil.dip2px(mContext, 65);
            rlParams.height =SizeUtil.dip2px(mContext, 100);
            item.setLayoutParams(rlParams);
            ivHead.setLayoutParams(params);
            ivRank.setVisibility(View.VISIBLE);
            tvRank.setVisibility(View.INVISIBLE);
            ivHeadup.setVisibility(View.VISIBLE);

            switch (position) {
                case 0:
                    ivRank.setImageResource(R.drawable.ic_fans_first);
                    ivHeadup.setImageResource(R.drawable.ic_fans_head_gold);
                    break;
                case 1:

                    ivRank.setImageResource(R.drawable.ic_fans_second);
                    ivHeadup.setImageResource(R.drawable.ic_fans_head_silver);
                    break;
                case 2:

                    ivRank.setImageResource(R.drawable.ic_fans_third);
                    ivHeadup.setImageResource(R.drawable.ic_fans_head_copper);
                    break;
                default:
                    break;
            }
        }

        if (position > 2) {
            LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams)item.getLayoutParams();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)ivHead.getLayoutParams();
            params.width = SizeUtil.dip2px(mContext, 50);
            params.height = SizeUtil.dip2px(mContext, 50);
            rlParams.height = SizeUtil.dip2px(mContext, 65);
            item.setLayoutParams(rlParams);
            ivHead.setLayoutParams(params);
            ivRank.setVisibility(View.GONE);
            tvRank.setVisibility(View.VISIBLE);
            ivHeadup.setVisibility(View.GONE);
            tvRank.setText(position+1+"");
        }



        tvName.setText(bean.getUser_nicename());
        String imageUrl = bean.getUser_avatar();
        if (imageUrl != null && !imageUrl.contains("ttp:")) {
            if(!imageUrl.contains("data")) {
                imageUrl = UrlProvider.URL_IMAGE + "/data/upload/avatar/" + imageUrl;
            }else {
                imageUrl = UrlProvider.URL_IMAGE + "/"+imageUrl;
            }
        }

        Glide.with(mContext).load(imageUrl).centerCrop().transform(new GlideCircleTransform(mContext)).into(ivHead);
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fansChartListener.HeadClick(position);
            }
        });
        if(bean.getMessage()!=null){
           tvMessage.setText(EmojiUtil.getDecodeMsg(bean.getMessage()));
        }else{
           tvMessage.setText("暂无留言");
        }

        if(!TextUtils.isEmpty(bean.getDate())){
            long time = Long.parseLong(bean.getDate()) * 1000;
             tvTime.setText(TimeUtil.getShortTime(time));
        }

        if (bean.getGold().equals("0")) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_tingbi);
            /// 这一步必须要做,否则不会显示.
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            tvGold.setCompoundDrawables(drawable, null, null, null);
            tvGold.setText(bean.getListen_money());
            tvTingbi.setVisibility(View.INVISIBLE);
            tvGold.setTextColor(mContext.getResources().getColor(R.color.btn_gold));
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_jinbi);
            /// 这一步必须要做,否则不会显示.
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            tvGold.setCompoundDrawables(drawable, null, null, null);
            tvGold.setText(bean.getGold());
            tvGold.setTextColor(mContext.getResources().getColor(R.color.tb_gold));
            if (bean.getListen_money().equals("0.00")) {
                tvTingbi.setVisibility(View.INVISIBLE);
            } else {
                tvTingbi.setVisibility(View.VISIBLE);
                tvTingbi.setText(bean.getListen_money());
            }
        }


    }


    private FansChartListener fansChartListener;

    public interface FansChartListener {

        void HeadClick(int position);
    }

    public void setListener(FansChartListener listener) {
        this.fansChartListener = listener;
    }







}
