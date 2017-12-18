package com.tingwen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.tingwen.bean.NewsDetailBean;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;

import java.util.List;

/**
 * 新闻打赏
 * Created by Administrator on 2017/8/26 0026.
 */
public class NewsRewardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NewsDetailBean.ResultsBean.RewardBean> list;
    private Context mcontext;
    private final LayoutInflater mLayoutInflater;

    public NewsRewardAdapter(Context context, List<NewsDetailBean.ResultsBean.RewardBean> reward) {
        list = reward;
        mcontext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_news_detail_shang, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsDetailBean.ResultsBean.RewardBean bean = list.get(position);
        if (holder instanceof ViewHolder) {
            String url = ImageUtil.changeSuggestImageAddress(bean.getAvatar());
            Glide.with(mcontext).load(url).transform(new GlideCircleTransform(mcontext)).error(R.drawable.img_touxiang).into(((ViewHolder) holder).head);

            ((ViewHolder) holder).money.setText("¥" + bean.getMoney());


        }


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView head;
        private TextView money;

        public ViewHolder(View itemView) {
            super(itemView);

            head = (ImageView) itemView.findViewById(R.id.iv_shang_head);
            money = (TextView) itemView.findViewById(R.id.tv_shang_money);


        }
    }
}
