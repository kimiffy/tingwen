package com.tingwen.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.activity.AuditionDetailActivity;
import com.tingwen.activity.MoreClassActivity;
import com.tingwen.activity.MoreProgramActivity;
import com.tingwen.activity.ProgramDetailActivity;
import com.tingwen.bean.DiscoveryBean;
import com.tingwen.bean.PartBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SizeUtil;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.raphets.roundimageview.RoundImageView;

import java.util.List;

/**
 * 发现Adapter
 * Created by Administrator on 2017/7/13 0013.
 */
public class DiscoveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DiscoveryBean.ResultsBean> dataList;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private static final int ITEM_TYPE_CLASS = 0;
    private static final int ITEM_TYPE_OTHER = 1;


    public DiscoveryAdapter(Context context, List<DiscoveryBean.ResultsBean> list) {
        mContext = context;
        dataList = list;
        mLayoutInflater = LayoutInflater.from(context);

    }

    public void setDataList(List<DiscoveryBean.ResultsBean> List) {
        dataList = List;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CLASS) {
            return new ClassViewHolder(mLayoutInflater.inflate(R.layout.item_discover_class, parent, false));

        } else {
            return new OtherViewHolder(mLayoutInflater.inflate(R.layout.item_discover_other, parent, false));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_CLASS;
        }
        return ITEM_TYPE_OTHER;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClassViewHolder) {
            final DiscoveryBean.ResultsBean bean = dataList.get(position);
            ((ClassViewHolder) holder).tvTypeClass.setText(bean.getType());
            ((ClassViewHolder) holder).moreclass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LauncherHelper.getInstance().launcherActivity(mContext, MoreClassActivity.class);
                }
            });

            ((ClassViewHolder) holder).lvClass.setAdapter(new CommonAdapter<DiscoveryBean.ResultsBean.DataBean>(mContext, R.layout.item_class, bean.getData()) {
                @Override
                protected void convert(ViewHolder viewHolder, DiscoveryBean.ResultsBean.DataBean item, int position) {


                    RoundImageView imageView = viewHolder.getView(R.id.iv_image);
                    TextView name = viewHolder.getView(R.id.tv_className);
                    TextView indroduce = viewHolder.getView(R.id.tv_introduce);
                    TextView money = viewHolder.getView(R.id.tv_money);

                    String imageUrl = String.valueOf(item.getImages());

                    if (imageUrl != null && !imageUrl.contains("ttp:")) {
                        if (imageUrl.contains("/data/upload/")) {
                            imageUrl = UrlProvider.URL_IMAGE + imageUrl;
                        } else {
                            imageUrl = UrlProvider.URL_IMAGE2 + imageUrl;
                        }
                    }

                    String price = subZeroAndDot(item.getPrice());
                    money.setText("¥ " + price);
                    name.setText(item.getName());
                    indroduce.setText(item.getDescription());
                    Glide.with(mContext).load(imageUrl).dontAnimate().into(imageView);

                    ((ClassViewHolder) holder).lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DiscoveryBean.ResultsBean.DataBean dataBean = bean.getData().get(position);
                            Bundle bundle = new Bundle();
                            bundle.putString("act_id",dataBean.getId()+"");
                            if(LoginUtil.isUserLogin()){
                                if(dataBean.getIs_free().equals("1")){
                                    toAnchor(dataBean,true);
                                }else if(dataBean.getIs_free().equals("0")){
                                    LauncherHelper.getInstance().launcherActivity(mContext, AuditionDetailActivity.class,bundle);
                                }
                            }else{
                                LauncherHelper.getInstance().launcherActivity(mContext, AuditionDetailActivity.class,bundle);
                            }
                        }
                    });

                }
            });



        } else if (holder instanceof OtherViewHolder) {
            final DiscoveryBean.ResultsBean bean = dataList.get(position);
            ((OtherViewHolder) holder).tvTypeOther.setText(dataList.get(position).getType());
            ((OtherViewHolder) holder).othermore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",dataList.get(position).getId()+"");
                    LauncherHelper.getInstance().launcherActivity(mContext, MoreProgramActivity.class,bundle);
                }
            });
            int screenWidth = SizeUtil.getScreenWidth();
            int itemWidth = screenWidth / bean.getData().size();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.leftMargin = SizeUtil.dip2px(mContext, 20);
            params.rightMargin = SizeUtil.dip2px(mContext, 20);
            ((OtherViewHolder) holder).gvProgram.setLayoutParams(params); // 设置GirdView布局参数,横向布局
            ((OtherViewHolder) holder).gvProgram.setColumnWidth(itemWidth); // 设置列表项宽
            ((OtherViewHolder) holder).gvProgram.setGravity(Gravity.CENTER_VERTICAL);
            ((OtherViewHolder) holder).gvProgram.setHorizontalSpacing(SizeUtil.dip2px(mContext, 15));
            ((OtherViewHolder) holder).gvProgram.setNumColumns(bean.getData().size()); // 设置列数量=列表集合数
            ((OtherViewHolder) holder).gvProgram.setOverScrollMode(View.OVER_SCROLL_NEVER);
            ((OtherViewHolder) holder).gvProgram.setAdapter(new CommonAdapter<DiscoveryBean.ResultsBean.DataBean>(mContext, R.layout.item_gv_other, bean.getData()) {
                @Override
                protected void convert(ViewHolder viewHolder, DiscoveryBean.ResultsBean.DataBean item, int position) {

                    RoundImageView imageView = viewHolder.getView(R.id.iv_photo);
                    TextView name = viewHolder.getView(R.id.tv_name);
                    String imageUrl = String.valueOf(item.getImages());

                    if (imageUrl != null && !imageUrl.contains("ttp:")) {
                        if (imageUrl.contains("/data/upload/")) {
                            imageUrl = UrlProvider.URL_IMAGE + imageUrl;
                        } else {
                            imageUrl = UrlProvider.URL_IMAGE2 + imageUrl;
                        }
                    }
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) name.getLayoutParams();
                    if (bean.getData().size() == 4) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SizeUtil.dip2px(mContext, 70),
                                SizeUtil.dip2px(mContext, 70));
                        layoutParams.width = SizeUtil.dip2px(mContext, 70);
                        imageView.setLayoutParams(lp);

                    }else{
                        layoutParams.width = SizeUtil.dip2px(mContext, 95);
                    }

                    name.setLayoutParams(layoutParams);
                    Glide.with(mContext).load(imageUrl).dontAnimate().into(imageView);//不执行动画防止拉伸
                    name.setText(item.getName());

                    ((OtherViewHolder) holder).gvProgram.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DiscoveryBean.ResultsBean.DataBean dataBean = bean.getData().get(position);
                            toAnchor(dataBean,false);


                        }
                    });

                }
            });


        }

    }

    private String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


    class ClassViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTypeClass;
        private ListView lvClass;
        private final RelativeLayout moreclass;


        public ClassViewHolder(View itemView) {
            super(itemView);

            tvTypeClass = (TextView) itemView.findViewById(R.id.tvType_class);
            lvClass = (ListView) itemView.findViewById(R.id.lv_class);
            moreclass = (RelativeLayout) itemView.findViewById(R.id.rl_discovery_class_more);


        }
    }


    class OtherViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTypeOther;
        private GridView gvProgram;
        private final RelativeLayout othermore;

        public OtherViewHolder(View itemView) {
            super(itemView);
            tvTypeOther = (TextView) itemView.findViewById(R.id.tvType_other);
            gvProgram = (GridView) itemView.findViewById(R.id.gv_program);
            othermore = (RelativeLayout) itemView.findViewById(R.id.rl_discovery_other_more);

        }


    }

    private void toAnchor(DiscoveryBean.ResultsBean.DataBean dataBean,Boolean isClass) {
        PartBean partBean = new PartBean();
        partBean.setId(dataBean.getId());
        partBean.setName(dataBean.getName());
        partBean.setDescription(dataBean.getDescription());
        partBean.setFan_num(dataBean.getFan_num()+"");
        partBean.setMessage_num(dataBean.getMessage_num());
        partBean.setImages(dataBean.getImages());
        ProgramDetailActivity.actionStart(mContext,partBean,isClass,isClass);
    }

}
