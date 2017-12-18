package com.tingwen.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tingwen.R;
import com.tingwen.activity.CommentActivity;
import com.tingwen.activity.LoginActivity;
import com.tingwen.activity.PayActivity;
import com.tingwen.activity.ProgramDetailActivity;
import com.tingwen.activity.RewardListActivity;
import com.tingwen.activity.WBShareActivity;
import com.tingwen.adapter.NewsDetailCommentAdapter;
import com.tingwen.adapter.NewsRewardAdapter;
import com.tingwen.app.AppSpUtil;
import com.tingwen.app.TwApplication;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.NewsCommentBean;
import com.tingwen.bean.NewsDetailBean;
import com.tingwen.bean.PartBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.CollectEvent;
import com.tingwen.event.CollectSuccessEvent;
import com.tingwen.event.LoadMoreNewsReloadUiEvent;
import com.tingwen.event.NewsPlayerNextEvent;
import com.tingwen.event.NewsPlayerPreviousEvent;
import com.tingwen.event.NewsRefreshEvent;
import com.tingwen.event.ShangSuccessEvent;
import com.tingwen.event.ShareEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.DeleteCommentPop;
import com.tingwen.popupwindow.PaySuccessDialog;
import com.tingwen.popupwindow.ReplyPop;
import com.tingwen.popupwindow.ShareDialog;
import com.tingwen.utils.BitmapUtil;
import com.tingwen.utils.DownLoadUtil;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.TimeUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.widget.CommonHeader;
import com.tingwen.widget.NewsContentTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 新闻详情fragment
 * Created by Administrator on 2017/8/23 0023.
 */
public class NewsDetailCopyFragment extends BaseFragment implements DeleteCommentPop.DeleteListener, ReplyPop.ReplyListener, ShareDialog.ShareListener {


    @BindView(R.id.rlv_news_detail)
    LRecyclerView rlvNewsDetail;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<NewsCommentBean.ResultsBean> commentList;//评论列表
    private NewsDetailCommentAdapter commentAdapter;//评论adapter
    private int page = 1;//评论分页
    private List<NewsBean> newsList;//播放列表
    private RoundImageView bigPhoto;//新闻大图
    private int position;//播放新闻的position
    private TextView tittle;//新闻标题
    private TextView date;//新闻时间
    private NewsContentTextView content;//新闻内容
    private RelativeLayout rlAnchor, rlDownload, rlToubi, rlComment, rlFirst, rlSecond, rlThird;
    private TextView name;//主播名称
    private boolean isFollow = false;//是否是该主播的粉丝
    private ImageView anchorHead;//主播头像
    private TextView follow;//关注按钮
    private TextView goldNum;//金币
    private String gold;//金币数
    private TextView commentNum; //评论数
    private RecyclerView rlvShang;//打赏人列表
    private RelativeLayout llShang;//有打赏的布局
    private TextView tvNoShang;//没有打赏
    private TextView tvShangTip;//等多少人打赏
    private TextView hotComment;//热门评论
    private Button shang, btnReward1, btnReward, goList;//赞赏按钮
    private String id = "";//新闻id
    private String actId;//主播id
    private TextView one, five, ten, fifty, hundred, custom, back, back2;//打赏金额选择
    private LinearLayout lldelete, liAnchor;
    private EditText etShangNumber;//自定义输入的金额
    private float shangNumber = 0;//打赏金额
    private DeleteCommentPop deleteCommentPop;
    private ReplyPop replyPop;
    private NewsCommentBean.ResultsBean comment;//点击的评论
    private NewsBean newsBean;//新闻

    private int textSize;
    private int[] titleSize;
    private int[] dateFromSize;
    private int[] contentsSize;
    private String channel;
    private ShareDialog shareDialog;
    private IWXAPI wxapi;
    private Bitmap bitmap;
    private Tencent tencent;
    private NewsDetailBean.ResultsBean.ActBean act;
    private Handler handler;
    private int goldUseNum;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_news_detail;
    }


    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        channel = bundle.getString("channel");

        commentList = new ArrayList<>();
        commentAdapter = new NewsDetailCommentAdapter(getActivity(), commentList);
        rlvNewsDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(commentAdapter);
        rlvNewsDetail.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvNewsDetail.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvNewsDetail.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvNewsDetail.setFooterViewHint("拼命加载中", "我是有底线的>_<", "点击重新加载");
        EventBus.getDefault().register(this);
        titleSize = getActivity().getResources().getIntArray(R.array.news_title);
        dateFromSize = getActivity().getResources().getIntArray(R.array.news_date_from);
        contentsSize = getActivity().getResources().getIntArray(R.array.news_contents);
    }

    @Override
    protected void initUI() {
        super.initUI();
        getNewsList();
        addHead();
        setData();
        playNews();
    }


    @Override
    protected void setListener() {
        super.setListener();
        rlvNewsDetail.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                getComment(id);
            }
        });
        rlvNewsDetail.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                scrollChangeListener.onChange(distanceY);
            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                commentClick(position);
            }
        });


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFollow();
            }
        });

        liAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != channel && channel.equals(AppConfig.CHANNEL_TYPE_CLASS)) {
                    getActivity().finish();
                    return;
                }
                if (null != act) {
                    PartBean partBean = new PartBean();
                    partBean.setId(act.getId());
                    partBean.setName(act.getName());
                    partBean.setDescription(act.getDescription());
                    partBean.setFan_num(act.getFan_num() + "");
                    partBean.setMessage_num(act.getMessage_num());
                    partBean.setImages(act.getImages());
                    ProgramDetailActivity.actionStart(getActivity(), partBean, false, false);//// TODO: 2017/11/6 0006 判断当前是不是课堂
                }

            }
        });


        rlDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != newsBean) {
                    DownLoadBean news = new DownLoadBean();
                    news.setId(newsBean.getId());
                    news.setPost_mp(newsBean.getPost_mp());
                    news.setPost_title(newsBean.getPost_title());
                    news.setSmeta(newsBean.getSmeta());
                    news.setPost_date(newsBean.getPost_date());
                    news.setPost_excerpt(newsBean.getPost_excerpt());
                    news.setPost_size(newsBean.getPost_size());
                    news.setPost_time(newsBean.getPost_time());
                    news.setPost_lai(newsBean.getPost_lai());

                    DownLoadUtil.getInstance().downLoadNews(getActivity(), news);
                }

            }
        });

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
             sendGold();

            }
        };

        rlToubi.setOnClickListener(new View.OnClickListener() {
            private static final int TIME = 1500;
            private long lastTime = 0;
            @Override
            public void onClick(View v) {
                if (null == handler) {
                    handler = new Handler();
                }
                goldUseNum += 1;

                if (NewsDetailCopyFragment.this.gold != null) {
                    int num = Integer.valueOf(NewsDetailCopyFragment.this.gold) + 1;
                    goldNum.setText(num + "");
                    NewsDetailCopyFragment.this.gold = num + "";
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime <TIME){//小于1.5s 取消任务
                    handler.removeCallbacks(runnable);
                }
                lastTime=currentTime;
                handler.postDelayed(runnable, 1500);//1.5秒后上传

            }
        });

        rlComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean login = LoginUtil.isUserLogin();
                if (!login) {
                    new MaterialDialog.Builder(getActivity())
                            .title("温馨提示")
                            .content("登录后才可以评论哦~")
                            .contentColorRes(R.color.text_black)
                            .negativeText("取消")
                            .positiveText("登录")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);
                                }
                            }).build().show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", newsBean);
                bundle.putString("post_id", newsBean.getId());
                LauncherHelper.getInstance().launcherActivity(getActivity(), CommentActivity.class, bundle);
            }
        });


        shang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlFirst.setVisibility(View.GONE);
                rlSecond.setVisibility(View.VISIBLE);
                shangNumber = 1;
                one.setSelected(true);
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shangNumber = 1;
                setAllUnSelect();
                one.setSelected(true);
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shangNumber = 5;
                setAllUnSelect();
                five.setSelected(true);
            }
        });
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shangNumber = 10;
                setAllUnSelect();
                ten.setSelected(true);
            }
        });
        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shangNumber = 15;
                setAllUnSelect();
                fifty.setSelected(true);
            }
        });
        hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shangNumber = 100;
                setAllUnSelect();
                hundred.setSelected(true);
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSecond.setVisibility(View.GONE);
                rlThird.setVisibility(View.VISIBLE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSecond.setVisibility(View.GONE);
                rlFirst.setVisibility(View.VISIBLE);
            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlThird.setVisibility(View.GONE);
                rlFirst.setVisibility(View.VISIBLE);
            }
        });
        lldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etShangNumber.setText("");
            }
        });
        goList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("act_id", actId);
                bundle.putString("post_id", id);
                LauncherHelper.getInstance().launcherActivity(getActivity(), RewardListActivity.class, bundle);

            }
        });


        btnReward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean login = LoginUtil.isUserLogin();
                if (!login) {
                    new MaterialDialog.Builder(getActivity())
                            .title("温馨提示")
                            .content("登录后才可以赞赏哦~")
                            .contentColorRes(R.color.text_black)
                            .negativeText("取消")
                            .positiveText("登录")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);
                                }
                            }).build().show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putFloat("money", shangNumber);
                bundle.putString("act_id", actId);
                bundle.putInt("type", 1);
                LauncherHelper.getInstance().launcherActivity(getActivity(), PayActivity.class, bundle);
            }
        });


        btnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginUtil.isUserLogin()) {
                    new MaterialDialog.Builder(getActivity())
                            .title("温馨提示")
                            .content("登录后才可以赞赏哦~")
                            .contentColorRes(R.color.text_black)
                            .negativeText("取消")
                            .positiveText("登录")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);
                                }
                            }).build().show();
                    return;
                }
                String money = String.valueOf(etShangNumber.getText());
                if (!TextUtils.isEmpty(money)) {
                    shangNumber = Float.parseFloat(money);
                    if (shangNumber != 0) {
                        if (shangNumber < 0.1) {
                            ToastUtils.showBottomToast("亲，打赏至少要一毛钱哦~");
                            etShangNumber.setText("");
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putFloat("money", shangNumber);
                            bundle.putString("act_id", actId);
                            bundle.putInt("type", 1);
                            LauncherHelper.getInstance().launcherActivity(getActivity(), PayActivity.class, bundle);
                        }
                    } else {
                        ToastUtils.showBottomToast("金额不允许是0");
                        etShangNumber.setText("");
                    }
                } else {
                    ToastUtils.showBottomToast("请填写金额~");
                }
            }
        });
    }


    /**
     * 播放新闻
     */
    private void playNews() {

        TwApplication.getNewsPlayer().setChannel(this.channel);
        TwApplication.getNewsPlayer().playNews(position);

    }

    /**
     * 获取播放新闻列表
     */
    private int getNewsList() {

        newsList = TwApplication.getNewsPlayer().getNewsList();
        return newsList.size();

    }

    /**
     * 添加头部
     */
    private void addHead() {
        CommonHeader head = new CommonHeader(getActivity(), R.layout.header_news_detail);
        lRecyclerViewAdapter.addHeaderView(head);
        bigPhoto = (RoundImageView) head.findViewById(R.id.iv_photo);
        final int height = (int) (SizeUtil.getScreenWidth() * 0.60);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        bigPhoto.setLayoutParams(params);

        rlAnchor = (RelativeLayout) head.findViewById(R.id.rl_anchor);
        liAnchor = (LinearLayout) head.findViewById(R.id.li_anchor);

        anchorHead = (ImageView) head.findViewById(R.id.anchor_head);
        name = (TextView) head.findViewById(R.id.anchor_name);
        follow = (TextView) head.findViewById(R.id.tv_follow);
        tittle = (TextView) head.findViewById(R.id.tv_news_detail_title);
        date = (TextView) head.findViewById(R.id.tv_news_detail_date);
        content = (NewsContentTextView) head.findViewById(R.id.tv_detail_content);
        rlDownload = (RelativeLayout) head.findViewById(R.id.rl_download);
        rlToubi = (RelativeLayout) head.findViewById(R.id.rl_toubi);
        goldNum = (TextView) head.findViewById(R.id.tv_jinbi_num);
        rlComment = (RelativeLayout) head.findViewById(R.id.rl_comment);
        commentNum = (TextView) head.findViewById(R.id.tv_comment_number);
        rlvShang = (RecyclerView) head.findViewById(R.id.rlv_shang);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rlvShang.setLayoutManager(linearLayoutManager);
        llShang = (RelativeLayout) head.findViewById(R.id.ll_shang);
        tvNoShang = (TextView) head.findViewById(R.id.tv_no_shang);
        tvShangTip = (TextView) head.findViewById(R.id.tv_shang_tip);
        hotComment = (TextView) head.findViewById(R.id.tv_hotComment);
        rlFirst = (RelativeLayout) head.findViewById(R.id.rl_first);
        rlSecond = (RelativeLayout) head.findViewById(R.id.rl_second);
        rlThird = (RelativeLayout) head.findViewById(R.id.rl_third);
        shang = (Button) head.findViewById(R.id.btn_shang);
        one = (TextView) head.findViewById(R.id.tv_one);
        five = (TextView) head.findViewById(R.id.tv_five);
        ten = (TextView) head.findViewById(R.id.tv_ten);
        fifty = (TextView) head.findViewById(R.id.tv_fifty);
        hundred = (TextView) head.findViewById(R.id.tv_hundred);
        custom = (TextView) head.findViewById(R.id.tv_custom);
        back = (TextView) head.findViewById(R.id.tv_back);
        back2 = (TextView) head.findViewById(R.id.tv_back_2);
        lldelete = (LinearLayout) head.findViewById(R.id.ll_delete);
        etShangNumber = (EditText) head.findViewById(R.id.et_custom_money);
        goList = (Button) head.findViewById(R.id.btn_look_list);
        btnReward1 = (Button) head.findViewById(R.id.btn_shang_1);
        btnReward = (Button) head.findViewById(R.id.btn_reward);

    }

    /**
     * 设置新闻数据
     */
    private void setData() {

        resetState();
        newsBean = newsList.get(position);
        String smeta = newsBean.getSmeta();
        String url = smeta.replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");
//        final int height = (int) (SizeUtil.getScreenWidth() * 0.60);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
//        bigPhoto.setLayoutParams(params);
        Glide.with(getActivity()).load(url).error(R.drawable.tingwen_bg_rectangle).into(bigPhoto);

        rlAnchor.setVisibility(View.GONE);
        hotComment.setVisibility(View.GONE);
        id = newsBean.getId();
        tittle.setText(newsBean.getPost_title());
        String data = newsBean.getPost_date();
        if (data != null && !data.isEmpty()) {
            long time;
            try {
                time = Integer.valueOf(data);
            } catch (Exception e) {
                time = 0;
            }
            if (time > 0) {
                date.setText("#  来自  " + newsBean.getPost_lai() + "    " + TimeUtil.getShortTime(time * 1000));
            } else {
                date.setText("#  来自  " + newsBean.getPost_lai() + "    " + TimeUtil.getTimesMessageByTime(newsBean.getPost_date()));
            }

        }
        content.setText(newsBean.getPost_excerpt());
        content.invalidate();
        getNewsDetail(id);//获取该条新闻详细信息(主播信息,打赏信息),并更新UI

    }

    /**
     * 获取新闻信息(主播,打赏)
     *
     * @param id
     */
    private void getNewsDetail(String id) {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("post_id", id);
        OkGo.<NewsDetailBean>post(UrlProvider.NEWS_DETAIL).params(map, true).tag(this).execute(new SimpleJsonCallback<NewsDetailBean>(NewsDetailBean.class) {
            @Override
            public void onSuccess(Response<NewsDetailBean> response) {

                NewsDetailBean.ResultsBean results = null;
                try {
                    results = response.body().getResults();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(null==results){
                    return;
                }
                act = results.getAct();
                if (act != null) {
                    rlAnchor.setVisibility(View.VISIBLE);
                    actId = act.getId();
                    String url = ImageUtil.changeSuggestImageAddress_(act.getImages());
                    if (getActivity() != null) {
                        Glide.with(getActivity()).load(url).transform(new GlideCircleTransform(getActivity())).error(R.drawable.img_touxiang).into(anchorHead);
                    }
                    name.setText(act.getName());
                    isFollow = !act.getIs_fan().equals("0");
                    if (isFollow && getActivity() != null) {
                        follow.setText("已关注");
                        follow.setBackgroundResource(R.drawable.background_no_circle_blue);
                        follow.setPadding(SizeUtil.dip2px(getActivity(), 5),
                                SizeUtil.dip2px(getActivity(), 3),
                                SizeUtil.dip2px(getActivity(), 5),
                                SizeUtil.dip2px(getActivity(), 3));
                    } else if (!isFollow && getActivity() != null) {
                        follow.setText("关注");
                        follow.setBackgroundResource(R.drawable.me_mypage_friend_ic_follow);
                        follow.setPadding(SizeUtil.dip2px(getActivity(), 18), 0, 0, 0);
                    }

                }
                gold = results.getGold();
                if (null == gold) {
                    goldNum.setText("0");
                } else if (!"".equals(gold)) {
                    goldNum.setText(gold);
                }
                commentNum.setText(results.getComment_count());
                int isCollection = results.getIs_collection();
                if (isCollection == 1) {//已经收藏
                    EventBus.getDefault().post(new CollectSuccessEvent(1));
                } else {
                    EventBus.getDefault().post(new CollectSuccessEvent(0));
                }

                List<NewsDetailBean.ResultsBean.RewardBean> reward = results.getReward();

                if (reward != null && getActivity() != null) {
                    tvNoShang.setVisibility(View.GONE);
                    llShang.setVisibility(View.VISIBLE);
                    NewsRewardAdapter adapter = new NewsRewardAdapter(getActivity(), reward);
                    rlvShang.setAdapter(adapter);
                    int rewardNum = Integer.parseInt(results.getReward_num());
                    if (rewardNum >= 5) {
                        tvShangTip.setText("等" + rewardNum + "人赞过");
                        tvShangTip.setVisibility(View.VISIBLE);
                    }

                } else {
                    tvNoShang.setVisibility(View.VISIBLE);
                    llShang.setVisibility(View.GONE);
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
        getComment(id);


    }

    /**
     * 获取评论列表
     *
     * @param id
     */
    private void getComment(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("post_id", id);
        map.put("page", page + "");
        map.put("uid", LoginUtil.getUserId());
        map.put("accessToken", LoginUtil.getAccessToken());
        OkGo.<NewsCommentBean>post(UrlProvider.GET_NEWS_COMMMENT).params(map, true).tag(this).execute(new SimpleJsonCallback<NewsCommentBean>(NewsCommentBean.class) {
            @Override
            public void onSuccess(Response<NewsCommentBean> response) {

                List<NewsCommentBean.ResultsBean> List = response.body().getResults();
                if (List != null && rlvNewsDetail != null) {
                    hotComment.setVisibility(View.VISIBLE);
                    commentList.addAll(List);
                    commentAdapter.setData(commentList);
                    if(null!=rlvNewsDetail){

                        rlvNewsDetail.refreshComplete(10);//每页加载数量
                    }
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    if (page > 1 && List.size() < 10 && rlvNewsDetail != null) {
                        if(null!=rlvNewsDetail){

                            rlvNewsDetail.setNoMore(true);
                        }
                    }

                    if (page == 1 && List.size() < 10 && rlvNewsDetail != null) {
                        if(null!=rlvNewsDetail){

                            rlvNewsDetail.setNoMore(true);
                        }
                    }
                }


            }

            @Override
            public void onError(Response<NewsCommentBean> response) {
                super.onError(response);

                if (page > 1 && rlvNewsDetail != null) {
                    rlvNewsDetail.setNoMore(true);
                }
            }
        });


    }

    /**
     * 关注(取消关注)
     */
    private void checkFollow() {
        if (FollowUtil.followPartList.contains(actId)) {//已经关注了
            unFollow();
        } else {//未关注
            followAct();
        }
    }

    /**
     * 取消关注
     */
    private void unFollow() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", actId);
        OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_FOLLOW_ACT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    follow.setText("关注");
                    FollowUtil.followPartList.remove(actId);
                    follow.setBackgroundResource(R.drawable.me_mypage_friend_ic_follow);
                    follow.setPadding(SizeUtil.dip2px(getActivity(), 18), 0, 0, 0);
                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });
    }

    /**
     * 关注
     */
    private void followAct() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", actId);
        OkGo.<SimpleMsgBean>post(UrlProvider.FOLLOW_ACT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    follow.setText("已关注");
                    follow.setBackgroundResource(R.drawable.background_no_circle_blue);
                    follow.setPadding(SizeUtil.dip2px(getActivity(), 5),
                            SizeUtil.dip2px(getActivity(), 3),
                            SizeUtil.dip2px(getActivity(), 5),
                            SizeUtil.dip2px(getActivity(), 3));
                    FollowUtil.followPartList.add(actId);
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });

    }


    /**
     * 投金币
     */
    private void sendGold() {
        boolean login = LoginUtil.isUserLogin();
        if (!login) {
            new MaterialDialog.Builder(getActivity())
                    .title("温馨提示")
                    .content("登录后才可以投币哦~")
                    .contentColorRes(R.color.text_black)
                    .negativeText("取消")
                    .positiveText("登录")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);
                        }
                    }).build().show();
            return;
        }


        String s = AppSpUtil.getInstance().getJingbi();
        final String ownGold = subZeroAndDot(s);
        if (!TextUtils.isEmpty(ownGold)) {
            if (Integer.valueOf(ownGold) > goldUseNum ) {
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", LoginUtil.getAccessToken());
                map.put("post_id", id);
                map.put("act_id", actId);
                map.put("gold_num", goldUseNum +"");
                OkGo.<SimpleMsgBean>post(UrlProvider.GOLD_USE).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                    @Override
                    public void onSuccess(Response<SimpleMsgBean> response) {
                        SimpleMsgBean bean = response.body();
                        int status = bean.getStatus();
                        if (status == 1) {
                            Integer integer = Integer.valueOf(ownGold);
                            int i = integer - goldUseNum;
                            AppSpUtil.getInstance().saveJingbi(i + "");
                            goldUseNum =0;

                        } else {
                            ToastUtils.showBottomToast(bean.getMsg());
                            if (NewsDetailCopyFragment.this.gold != null) {
                                int num = Integer.valueOf(NewsDetailCopyFragment.this.gold) -goldUseNum;
                                goldNum.setText(num + "");
                                NewsDetailCopyFragment.this.gold = num + "";
                                goldUseNum =0;
                            }
                        }
                    }

                    @Override
                    public void onError(Response<SimpleMsgBean> response) {
                        super.onError(response);
                        ToastUtils.showBottomToast("投币失败!请稍后重试");
                        if (NewsDetailCopyFragment.this.gold != null) {
                            int num = Integer.valueOf(NewsDetailCopyFragment.this.gold) -goldUseNum;
                            goldNum.setText(num + "");
                            NewsDetailCopyFragment.this.gold = num + "";
                            goldUseNum =0;
                        }
                    }
                });
            } else {
                ToastUtils.showBottomToast("金币不足!");
                if (NewsDetailCopyFragment.this.gold != null) {
                    int num = Integer.valueOf(NewsDetailCopyFragment.this.gold) -goldUseNum;
                    goldNum.setText(num + "");
                    NewsDetailCopyFragment.this.gold = num + "";
                    goldUseNum =0;
                }
            }


        }


    }

    /**
     * 去掉小数点和多余的0
     *
     * @param s
     * @return
     */
    private String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 收藏新闻
     */
    private void CollectNews() {
        boolean login = LoginUtil.isUserLogin();
        if (!login) {
            new MaterialDialog.Builder(getActivity())
                    .title("温馨提示")
                    .content("登录后才可以收藏哦~")
                    .contentColorRes(R.color.text_black)
                    .negativeText("取消")
                    .positiveText("登录")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);
                        }
                    }).build().show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("post_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.COLLECT_NEWS).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                String s = response.body().getMsg();
                if (status == 1) {
                    EventBus.getDefault().post(new CollectSuccessEvent(1));
                    ToastUtils.showBottomToast(s);
                } else {
                    String msg = response.body().getMsg();
                    ToastUtils.showBottomToast(msg);
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("收藏失败,请稍后重试");
            }
        });


    }

    /**
     * 评论列表点击事件
     *
     * @param position
     */
    private void commentClick(int position) {
        if (!LoginUtil.isUserLogin()) {
            ToastUtils.showBottomToast("请登录后操作");
            return;
        }
        comment = commentList.get(position);
        String userId = LoginUtil.getUserId();
        if (comment.getUid().equals(userId)) {
            deleteCommentPop = new DeleteCommentPop(getActivity());
            deleteCommentPop.setListener(this);
            deleteCommentPop.showPopupWindow();

        } else {
            replyPop = new ReplyPop(getActivity());
            replyPop.setListener(this);
            replyPop.showPopupWindow();
        }

    }

    //删除评论
    @Override
    public void delete() {
        deleteCommentPop.dismiss();
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("id", comment.getId());
        OkGo.<SimpleMsgBean>post(UrlProvider.DELETE_COMMENT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    commentList.remove(comment);
                    lRecyclerViewAdapter.notifyDataSetChanged();
                    if (commentList.size() == 0) {
                        hotComment.setVisibility(View.GONE);
                    }
                } else if (status == 0) {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });


    }

    @Override
    public void cancel() {
        deleteCommentPop.dismiss();

    }

    //回复评论
    @Override
    public void replay() {
        replyPop.dismiss();
        Bundle bundle = new Bundle();
        if (newsBean != null) {
            bundle.putString("post_id", id);
            bundle.putSerializable("news", newsBean);
        }
        if (comment != null) {
            bundle.putString("to_uid", comment.getUid());
            if (comment.getId() != null) {
                bundle.putString("comment_id", comment.getId());
            }
        }

        LauncherHelper.getInstance().launcherActivity(getActivity(), CommentActivity.class, bundle);


    }

    @Override
    public void cancel2() {
        replyPop.dismiss();
    }

    /**
     * 重置状态
     */
    private void resetState() {
        id = "";
        page = 1;
        commentList.clear();
        lRecyclerViewAdapter.notifyDataSetChanged();//加上 防止出现bug
    }

    /**
     * 设置听币为未选择
     */
    private void setAllUnSelect() {
        one.setSelected(false);
        five.setSelected(false);
        ten.setSelected(false);
        fifty.setSelected(false);
        hundred.setSelected(false);
    }

    /**
     * 播放下一条
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextEvent(NewsPlayerNextEvent event) {
        position = event.getPosition();
        if (newsList != null && newsList.size() != 0) {
            setData();
        }

    }

    /**
     * 播放上一条
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPrevious(NewsPlayerPreviousEvent event) {
        position = event.getPosition();
        if (newsList != null && newsList.size() != 0) {
            setData();
        }

    }

    /**
     * 刷新新闻评论数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(NewsRefreshEvent event) {
        int type = event.getType();
        if (type == 1) {
            commentList.clear();
            page = 1;
            lRecyclerViewAdapter.notifyDataSetChanged();
            getComment(id);
        }

    }

    /**
     * 收藏
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollect(CollectEvent event) {
        if (newsBean != null) {
            CollectNews();

        }
    }

    /**
     * 支付成功后
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShangSuccessEvent(ShangSuccessEvent event) {
        int type = event.getType();
        if (type == 1) {
            rlFirst.setVisibility(View.VISIBLE);
            rlSecond.setVisibility(View.GONE);
            rlThird.setVisibility(View.GONE);

            PaySuccessDialog paySuccessDialog = new PaySuccessDialog(getActivity(), actId, id, shangNumber + "");
            paySuccessDialog.setListener(new PaySuccessDialog.PaySuccessShareListener() {
                @Override
                public void paySuccessShare() {
                    // TODO: 2017/10/27 0027 跳转分享

                }

                @Override
                public void paySuccessThanks() {
                    Bundle bundle = new Bundle();
                    bundle.putString("act_id", actId);
                    bundle.putString("post_id", id);
                    LauncherHelper.getInstance().launcherActivity(getActivity(), RewardListActivity.class, bundle);
                }

                @Override
                public void paySuccessfinish() {

                }
            });
            paySuccessDialog.show();
        }


    }


    /**
     * 分享
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollect(ShareEvent event) {

        if (null == shareDialog) {
            shareDialog = new ShareDialog();
        }
        shareDialog.setListener(this);
        shareDialog.show(getActivity().getFragmentManager(), "sharedialog");
    }


    /**
     * 自动加载更多后,重新设置页面数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoloadmoreLoadUi(LoadMoreNewsReloadUiEvent event) {
        position = event.getPosition();
        channel = event.getChannel();
        getNewsList();
        setData();
    }


    @Override
    public void onResume() {
        super.onResume();
        textSize = AppSpUtil.getInstance().getFrontSize();
        updateDetialTextSize();
    }

    /**
     * 更新字体大小
     */
    private void updateDetialTextSize() {
        content.setTextSize(contentsSize[textSize]);
        content.setText(content.getText());

        date.setTextSize(dateFromSize[textSize]);
        date.setText(date.getText());

        tittle.setTextSize(titleSize[textSize]);
        tittle.setText(tittle.getText());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }


    private ScrollChangeListener scrollChangeListener;

    public void setScrollChangeListener(ScrollChangeListener scrollChangeListener) {
        this.scrollChangeListener = scrollChangeListener;
    }

    @Override
    public void onDismiss() {
        shareDialog.dismiss();
    }

    @Override
    public void weiboShare() {
        shareDialog.dismiss();
        if (null == newsBean) {
            return;
        }
        Intent i = new Intent(getActivity(), WBShareActivity.class);
        i.putExtra("news", newsBean);
        getActivity().startActivity(i);

    }

    @Override
    public void qqShare() {
        shareDialog.dismiss();
        if (tencent == null) {
            tencent = Tencent.createInstance(AppConfig.APP_ID, getActivity());
        }
        qq(false);
    }

    @Override
    public void weixinShare() {
        shareDialog.dismiss();
        if (null == newsBean) {
            return;
        }
        flag = false;
        String image = newsBean.getSmeta().replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");

        // 处理缩略图片
        if (image != null && !image.equals("")) {
            if (image.contains("file")) {
                bitmap = getBitmapFromLocal(image.substring(7));
            } else {
                new GetBitmapAsy().execute(image);
                return;
            }
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tingwen_logo);
        }
        wxShare();
    }

    @Override
    public void pengyouShare() {
        shareDialog.dismiss();
        if (null == newsBean) {
            return;
        }
        flag = true;
        String image = newsBean.getSmeta().replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");
        // 处理缩略图片
        if (image != null && !image.equals("")) {
            if (image.contains("file")) {
                bitmap = getBitmapFromLocal(image.substring(7));
            } else {
                new GetBitmapAsy().execute(image);
                return;
            }
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tingwen_logo);
        }
        wxShare();

    }

    @Override
    public void qqZoneShare() {
        shareDialog.dismiss();
        if (tencent == null) {
            tencent = Tencent.createInstance(AppConfig.APP_ID, getActivity());
        }
        qq(true);
    }

    @Override
    public void copy() {
        shareDialog.dismiss();
        if (null != newsBean) {
            String url = UrlProvider.SHARE + newsBean.id + ".html";
            copyUrl(url);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }


    public interface ScrollChangeListener {
        void onChange(float distanceY);
    }


    private class GetBitmapAsy extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            String url = (String) params[0];
            bitmap = BitmapUtil.getBitmapFromInternet_Compress(url);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            wxShare();
        }
    }


    private Bitmap getBitmapFromLocal(String url) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, opts);

        opts.inSampleSize = calculateInSampleSize(opts, 100, 100);
        opts.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(url, opts);

        return bm;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private boolean flag = false;// 用来判断是否分享到朋友圈

    /**
     * 微信分享
     */
    private void wxShare() {

        wxapi = WXAPIFactory.createWXAPI(getActivity(), AppConfig.WX_APP_ID, false);//// TODO: 2017/12/14 0014 getActivity 是否能换成Applivation
        wxapi.registerApp(AppConfig.WX_APP_ID);

        String title = newsBean.getPost_title();
        String musicUrl = newsBean.getPost_mp();

        WXMusicObject music = new WXMusicObject();
        music.musicUrl = UrlProvider.SHARE + id + ".html";
//      music.musicUrl="http://music.163.com/#/song?id=5240776&userid=96395677&from=singlemessage&isappinstalled=1";
        music.musicLowBandUrl = UrlProvider.SHARE + id + ".html";
        music.musicDataUrl = musicUrl;
        music.musicLowBandDataUrl = musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = "";
        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        if (flag) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        wxapi.sendReq(req);
    }


    private void qq(boolean flag) {

        if (null == newsBean) {
            return;
        }

        String title = newsBean.getPost_title();
        String musicUrl = newsBean.getPost_mp();
        String content = newsBean.getPost_content();
        String image = newsBean.getSmeta().replace("{\"thumb\":\"", "").replace("\\", "").replace("\"}", "");
        String url = UrlProvider.SHARE + newsBean.id + ".html";


        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, image);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, musicUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "听闻");
        if (flag) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        tencent.shareToQQ(getActivity(), params, new ShareListener());
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareListener myListener = new ShareListener();
        Tencent.onActivityResultData(requestCode, resultCode, data, myListener);
    }

    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            ToastUtils.showBottomToast("取消分享");
        }

        @Override
        public void onComplete(Object arg0) {
            ToastUtils.showBottomToast("分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            ToastUtils.showBottomToast("分享出错");
        }

    }

    private void copyUrl(String url) {
        ClipData clipData = ClipData.newPlainText("copied text", url);
        ClipboardManager mClipBordManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        mClipBordManager.setPrimaryClip(clipData);
        ToastUtils.showBottomToast("复制成功");
    }


}
