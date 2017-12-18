package com.tingwen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.ConsumeBean;
import com.tingwen.utils.TimesUtil;

import java.util.List;

/**
 * 消费记录
 * Created by Administrator on 2017/11/7 0007.
 */
public class ConsumeAdapter extends ListBaseAdapter<ConsumeBean.ResultsBean> {


    public ConsumeAdapter(Context context, List<ConsumeBean.ResultsBean> consumelist) {
        super(context);
        mDataList = consumelist;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_transacton;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        ConsumeBean.ResultsBean bean = mDataList.get(position);
        TextView tvContent = holder.getView(R.id.tv_content);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvMoney = holder.getView(R.id.tv_money);
        if("1".equals(bean.getType())){
            tvContent.setText("赞赏主播“"+bean.getName()+"”");
        }else if("2".equals(bean.getType())){
            tvContent.setText("开通听闻会员");
        }else if ("3".equals(bean.getType())){
            tvContent.setText("购买课堂“"+bean.getName()+"”");
        }
        if(!TextUtils.isEmpty(bean.getDate())){
           tvTime.setText(TimesUtil.setDataFormat(Long.parseLong(bean.getDate())));
        }
        tvMoney.setText(bean.getMoney());
    }
}
