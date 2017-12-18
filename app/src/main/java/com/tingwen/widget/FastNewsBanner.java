package com.tingwen.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.activity.AuditionDetailActivity;
import com.tingwen.activity.NewsDetailActivity;
import com.tingwen.activity.ProgramDetailActivity;
import com.tingwen.app.TwApplication;
import com.tingwen.bean.FastNewsADBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.PartBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.GlideRoundTransform;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.VipUtil;
import com.tingwen.widget.logger.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * 快讯广告
 * Created by Administrator on 2017/4/11 0011.
 */
public class FastNewsBanner extends Fragment {
    private ImageView iv;
    private TextView tv;
    private View view;
    public static final String NO_BUY = 0 + "";
    public static final String HAS_BUY = 1 + "";
    private FastNewsADBean.ResultsBean fastNewsAD;


    public FastNewsADBean.ResultsBean getFastNewsAD() {
        return fastNewsAD;
    }

    public void setFastNewsAD(FastNewsADBean.ResultsBean fastNewsAD) {
        this.fastNewsAD = fastNewsAD;
        if (view != null) {
            initView();
            initData();
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fast_news_ad_item, null);
        }
        initView();
        initData();
        initClick();
        return view;
    }


    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        super.onDestroyView();
    }

    private void initView() {
        iv = (ImageView) view.findViewById(R.id.iv);
        tv = (TextView) view.findViewById(R.id.tv);
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int width = screenWidth - SizeUtil.dip2px(getActivity(), 70);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.leftMargin = SizeUtil.dip2px(getActivity(), 10);
        tv.setLayoutParams(params);
    }

    private void initData() {
        if (fastNewsAD != null && fastNewsAD.getPost_list() != null) {
            if (!"".equals(fastNewsAD.getPost_list().getPost_title())
                    && fastNewsAD.getPost_list().getPost_title() != null) {
                tv.setText(fastNewsAD.getPost_list().getPost_title());
            } else {
                tv.setText(fastNewsAD.getPost_list().getPost_title());//?????
            }

            if (fastNewsAD.getPicture() != null&& !"".equals(fastNewsAD.getPicture())) {

                String s = fastNewsAD.getPicture().replace("\\", "").replace("{\"thumb\":\"", "").replace("\"}", "");

                fastNewsAD.setPicture(s);
                if (!fastNewsAD.getPicture().contains("http")) {
                    String v = UrlProvider.URL_IMAGE + s;
                    fastNewsAD.setPicture(v);
                }

            }

            Glide.with(getActivity()).load(fastNewsAD.getPicture()).transform(new GlideRoundTransform(getActivity(), 5)).error(R.drawable.tingwen_bg_rectangle).into(iv);
        } else if (fastNewsAD != null) {

            if (!"".equals(fastNewsAD.getDescription())
                    && fastNewsAD.getDescription() != null) {
                tv.setText(fastNewsAD.getDescription());
            }

            if (fastNewsAD.getPicture() != null
                    && !"".equals(fastNewsAD.getPicture())) {

                String s = fastNewsAD.getPicture().replace("\\", "").replace("{\"thumb\":\"", "").replace("\"}", "");

                fastNewsAD.setPicture(s);


                if (!fastNewsAD.getPicture().contains("http")) {
                    String v = UrlProvider.URL_IMAGE + s;
                    fastNewsAD.setPicture(v);
                }

            }


            Glide.with(getActivity()).load(fastNewsAD.getPicture()).transform(new GlideRoundTransform(getActivity(), 5))
                    .error(R.drawable.tingwen_bg_rectangle).into(iv);


        }
    }

    /**
     * 点击事件(根据类型跳转不同界面)
     */
    private void initClick() {
        iv.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {

                                      if (null != fastNewsAD) {

                                          if ("1".equals(fastNewsAD.getCate())) {//新闻

                                              if (fastNewsAD.getPost_list() != null) {
                                                  List<NewsBean> newsList = new ArrayList<>();
                                                  NewsBean bean = getNewsJsonByThemeNewsJson(fastNewsAD);
                                                  newsList.add(bean);
                                                  TwApplication.getNewsPlayer().setNewsList(newsList);
                                                  NewsDetailActivity.actionStart(getActivity(), 0, AppConfig.CHANNEL_TYPE_FAST_AD);


                                              }
                                          } else if ("2".equals(fastNewsAD.getCate())) {//浏览器
                                              Intent intent = new Intent(Intent.ACTION_VIEW);
                                              intent.setData(Uri.parse(fastNewsAD.getUrl()));
                                              startActivity(intent);

                                          } else if ("3".equals(fastNewsAD.getCate())) {//课堂

                                              if (!LoginUtil.isUserLogin()) {
                                                  Intent intent = new Intent(getActivity(), AuditionDetailActivity.class);
                                                  intent.putExtra("act_id", String.valueOf(fastNewsAD.getUrl()));
                                                  getActivity().startActivity(intent);

                                              } else if (LoginUtil.isUserLogin()) {

                                                  if (VipUtil.getInstance().getVipState() == 2) {
                                                      toAnchor(fastNewsAD, true);
                                                  } else {
                                                      if (HAS_BUY.equals(fastNewsAD.getIs_free())) {
                                                          toAnchor(fastNewsAD, true);
                                                      } else if (NO_BUY.equals(fastNewsAD.getIs_free())) {
                                                          Intent intent = new Intent(getActivity(), AuditionDetailActivity.class);
                                                          intent.putExtra("act_id", String.valueOf(fastNewsAD.getUrl()));
                                                          getActivity().startActivity(intent);
                                                      }
                                                  }

                                              }


                                          }


                                      }


                                  }


                              }

        );

    }

    private void toAnchor(FastNewsADBean.ResultsBean dataBean, Boolean isClass) {
        PartBean partBean = new PartBean();
        partBean.setId(dataBean.getUrl());
        partBean.setName(dataBean.getName());
        partBean.setDescription(dataBean.getDes());
        partBean.setImages(dataBean.getImages());
        ProgramDetailActivity.actionStart(getActivity(), partBean, isClass,true);

    }


    private NewsBean getNewsJsonByThemeNewsJson(FastNewsADBean.ResultsBean fastNewsAD) {
        NewsBean newsJson = new NewsBean();
        if (fastNewsAD != null) {
            newsJson.post_author = fastNewsAD.getPost_list().getPost_author();
            newsJson.post_content = fastNewsAD.getPost_list().getPost_content();
            newsJson.post_date = fastNewsAD.getPost_list().getPost_date();
            newsJson.post_excerpt = fastNewsAD.getPost_list().getPost_excerpt();
            newsJson.post_hits = fastNewsAD.getPost_list().getPost_hits();
            newsJson.post_keywords = fastNewsAD.getPost_list().getPost_keywords();
            newsJson.post_lai = fastNewsAD.getPost_list().getPost_lai();
            newsJson.post_like = fastNewsAD.getPost_list().getPost_like();
            newsJson.post_mime_type = fastNewsAD.getPost_list().getPost_mime_type();
            newsJson.post_mp = fastNewsAD.getPost_list().getPost_mp();
            newsJson.post_parent = fastNewsAD.getPost_list().getPost_parent();
            newsJson.post_time = fastNewsAD.getPost_list().getPost_time();
            newsJson.post_title = fastNewsAD.getPost_list().getPost_title();
            newsJson.term_id = fastNewsAD.getPost_list().getTerm_id();
            newsJson.term_name = fastNewsAD.getPost_list().getTerm_name();
            newsJson.id = fastNewsAD.getPost_list().getId();
            newsJson.comment_count = fastNewsAD.getPost_list().getComment_count();
            newsJson.smeta = fastNewsAD.getPost_list().getSmeta();
            newsJson.istop = fastNewsAD.getPost_list().getIstop();
            newsJson.post_size = fastNewsAD.getPost_list().getPost_size();
            newsJson.recommended = fastNewsAD.getPost_list().getRecommended();
            newsJson.toutiao = fastNewsAD.getPost_list().getToutiao();
            newsJson.url = fastNewsAD.getPost_list().getUrl();
            newsJson.simpleImage = fastNewsAD.getPicture();
            newsJson.praisenum = fastNewsAD.getPost_list().getPraisenum();
        }
        return newsJson;
    }
}
