package com.tingwen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.app.TwApplication;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.AuditionBean;
import com.tingwen.event.ClassListenEvent;
import com.tingwen.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 课堂试听
 * Created by Administrator on 2017/8/18 0018.
 */
public class AuditionListenAdapter extends ListBaseAdapter<AuditionBean.ResultsBean.ShitingBean> {

    private String mp3;
    private String act_id;

    public AuditionListenAdapter(Context context,List<AuditionBean.ResultsBean.ShitingBean> shitingList) {
        super(context);
        mDataList=shitingList;

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_audition_listen_list;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final AuditionBean.ResultsBean.ShitingBean bean = mDataList.get(position);

        TextView  tvTitle = holder.getView(R.id.tv_title);
        final ImageView imageView = holder.getView(R.id.iv_image);

        imageView.setImageResource(R.drawable.icon_play);

        tvTitle.setText(bean.getS_title());
        final String s_mpurl = bean.getS_mpurl();


        if(!TextUtils.isEmpty(mp3)&&s_mpurl.equals(mp3)){
            imageView.setImageResource(R.drawable.icon_pause);
            EventBus.getDefault().post(new ClassListenEvent(3,mp3));
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp3="";
                notifyDataSetChanged();
                EventBus.getDefault().post(new ClassListenEvent(4,mp3));
                TwApplication.getNewsPlayer().setMp3List(mDataList);
                String playingMp3 = TwApplication.getNewsPlayer().getPlayingMp3();
                if(s_mpurl.equals(playingMp3)){
                    ToastUtils.showBottomToast("停止试听");
                    TwApplication.getNewsPlayer().pauseMp3();
                }else{
                    ToastUtils.showBottomToast("开始试听");
                    TwApplication.getNewsPlayer().playMp3(position);
                    TwApplication.getNewsPlayer().setActId(act_id);
                }

            }
        });





    }

    /**
     * 设置当前正在播放的mp3地址
     * @param url
     */
    public void setPlayMp3(String url){
        mp3=url;
    }

    /**
     * 设置id
     * @param id
     */
    public void setAct_id(String id){
        act_id=id;
    }

}
