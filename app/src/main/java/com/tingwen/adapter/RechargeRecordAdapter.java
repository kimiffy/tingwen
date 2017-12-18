package com.tingwen.adapter;

import android.content.Context;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.RechargeRecordBean;

import java.util.List;

/**
 * 充值记录
 * Created by Administrator on 2017/11/7 0007.
 */
public class RechargeRecordAdapter extends ListBaseAdapter<RechargeRecordBean.ResultsBean> {


    public RechargeRecordAdapter(Context context, List<RechargeRecordBean.ResultsBean> list) {
        super(context);
        mDataList = list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_transacton;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RechargeRecordBean.ResultsBean bean = mDataList.get(position);
        TextView tvContent = holder.getView(R.id.tv_content);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvMoney = holder.getView(R.id.tv_money);

        tvContent.setText(bean.getMethod());
        tvTime.setText(bean.getDate());
        tvMoney.setText(bean.getMoney());

    }
}
