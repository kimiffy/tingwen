package com.tingwen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.app.TwApplication;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.ProgramContentBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.greendao.HistoryNews;
import com.tingwen.interfaces.MediaPlayerInterface;
import com.tingwen.utils.FileSizeUtil;
import com.tingwen.utils.SQLHelper;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.TimeUtil;
import com.tingwen.widget.logger.Logger;

import java.text.NumberFormat;
import java.util.List;

/**
 * 节目(主播) 的新闻内容adapter
 * Created by Administrator on 2017/8/8 0008.
 */
public class ProgramContentAdapter extends ListBaseAdapter<ProgramContentBean.ResultsBean> {

    private Context mContext;
    private Boolean isClass;
    private String id = 0 + "";//正在播放的新闻ID
    private List<NewsBean> newsList;//新闻
    private String pauseId = "";//正在暂停的id
    private final SQLHelper sqlHelper;
    private String pauseID;
    private String ClassID="";//课程id
    public ProgramContentAdapter(Context context, List<ProgramContentBean.ResultsBean> list, boolean b,String anchorID) {
        super(context);
        mDataList = list;
        this.mContext = context;
        ClassID=anchorID;
        isClass = b;
        sqlHelper = SQLHelper.getInstance();
        pauseID = TwApplication.getNewsPlayer().getPauseID();
        id=TwApplication.getNewsPlayer().getPlayId();
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_program_content;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final ProgramContentBean.ResultsBean bean = mDataList.get(position);

        TextView tvdate = holder.getView(R.id.tv_date);
        TextView title = holder.getView(R.id.tv_title);
        ImageView play = holder.getView(R.id.iv_image);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvHistory = holder.getView(R.id.tv_history);

        title.setText(bean.getPost_title());
        title.setTextColor(Color.parseColor("#2e3133"));//默认颜色

        if (sqlHelper.isListenedNews(bean.getId())) {
            title.setTextColor(Color.parseColor("#B3B3B3"));//已经收听过的新闻颜色
        }

        if (!TextUtils.isEmpty(id) && id.equals(bean.getId())) {
            title.setTextColor(Color.parseColor("#55B9DD"));//正在听新闻颜色
        }


        //课堂
        if (isClass) {
            tvdate.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            tvHistory.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayerInterface newsPlayer = TwApplication.getNewsPlayer();
                    if (newsPlayer.getPlayId().equals(bean.getId())) {
                        if (newsPlayer.isPlaying()) {
                            newsPlayer.pause();
                            pauseId = bean.getId();
                        } else {
                            newsPlayer.continuePlay();
                            pauseID ="";
                            pauseId = "";
                            id=bean.getId();
                        }
                        notifyDataSetChanged();
                    } else {
                        HistoryNews news = sqlHelper.isHasHistoryNews(bean.getId());
                        if(null!=news){
                            newsPlayer.setNewsList(newsList);
                            newsPlayer.setClassActID(ClassID);
                            newsPlayer.setChannel(AppConfig.CHANNEL_TYPE_CLASS);
                            newsPlayer.setIsPlayLastClass(true, news.getTime());
                            newsPlayer.playNews(position);

                        }else{
                            newsPlayer.setNewsList(newsList);
                            newsPlayer.setClassActID(ClassID);
                            newsPlayer.setChannel(AppConfig.CHANNEL_TYPE_CLASS);
                            newsPlayer.setIsPlayLastClass(false, "");
                            newsPlayer.playNews(position);
                        }

                    }


                }
            });

            if (id.equals(bean.getId())) {
                Logger.e("id:" + id);
                play.setImageResource(R.drawable.icon_pause);
                if (pauseId.equals(id)) {
                    play.setImageResource(R.drawable.icon_play);
                }
                if(pauseID.equals(id)){
                    play.setImageResource(R.drawable.icon_play);
                }
            } else {
                play.setImageResource(R.drawable.icon_play);
            }
            tvHistory.setText("");
            HistoryNews hasHistoryNews = sqlHelper.isHasHistoryNews(bean.getId());
            if(null!=hasHistoryNews){
                String time = hasHistoryNews.getTime();
                String totalTime = hasHistoryNews.getTotaltime();
                Integer num1 = Integer.valueOf(time);
                Integer num2 = Integer.valueOf(totalTime);
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                String result = numberFormat.format((float) num1 / (float) num2 * 100);

                tvHistory.setText("已播放:"+result+ "%");
            }



        } else {//节目(主播)
            if (bean.getPost_size() != null) {
                tvTime.setText(FileSizeUtil.getFileSize(bean.getPost_size()));
            }
            String date = "";
            if (bean.getPost_date() != null && !bean.getPost_date().isEmpty()) {

                long time;
                try {
                    time = Integer.valueOf(bean.getPost_date());
                } catch (Exception e) {
                    time = 0;
                }
                if (time > 0) {
                    date = TimeUtil.getTimeMouth_(time * 1000);
                } else {
                    date = TimeUtil.getTimeMouth_(bean.getPost_date());
                }

            }

            SpannableStringBuilder builder = new SpannableStringBuilder(date);
            builder.setSpan(new AbsoluteSizeSpan(SizeUtil.dip2px(mContext, 20)), 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(SizeUtil.dip2px(mContext, 8)), 2, date.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tvdate.setText(builder);
        }


    }


    /**
     * 设置正在播放的新闻id
     *
     * @param id
     */
    public void setListeningId(String id) {
        this.id = id;
        notifyDataSetChanged();
    }

    /**
     * 设置播放id不刷新数据
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public void setNewsList(List<NewsBean> list) {
        this.newsList = list;
        notifyDataSetChanged();
    }
}
