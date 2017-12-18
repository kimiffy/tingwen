package com.tingwen.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.ListBaseAdapter;
import com.tingwen.base.SuperViewHolder;
import com.tingwen.bean.FeedBackBean;
import com.tingwen.bean.ImageBean;
import com.tingwen.bean.ListenCircleBean;
import com.tingwen.bean.User;
import com.tingwen.net.UrlProvider;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.GlideCircleTransform;
import com.tingwen.utils.ImageUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.NoDoubleClickIn1S;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.TimesUtil;
import com.tingwen.widget.LapTextView;
import com.tingwen.widget.NineGridLayout;
import com.tingwen.widget.UnScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈
 * Created by Administrator on 2017/10/17 0017.
 */
public class FeedBackAdapter extends ListBaseAdapter<FeedBackBean.ResultsBean> implements FeedBackCommentAdapter.CommentListener {
    private static final int CIRCLE_TYPE = 0;
    /*文字超长的集合*/
    private List<ListenCircleBean.ResultsBean> listLongUser = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    public FeedBackAdapter(Context context, List<FeedBackBean.ResultsBean> list) {
        super(context);
        mDataList = list;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = mInflater.inflate(R.layout.item_feed_back, parent, false);
        return new SuperViewHolder(itemView);

    }


    @Override
    public int getLayoutId() {
        return 0;
    }


    @Override
    public int getItemViewType(int position) {

        return CIRCLE_TYPE;

    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        final FeedBackBean.ResultsBean bean = mDataList.get(position);

        ImageView ivHeader = holder.getView(R.id.header);
        TextView tvName = holder.getView(R.id.name);
        LapTextView ltvContent = holder.getView(R.id.content);
        TextView tvLap = holder.getView(R.id.tv_lap);
        final NineGridLayout ngl = holder.getView(R.id.ngl);
        final ImageView ivSingle = holder.getView(R.id.iv_single);
        TextView tvCreateTime = holder.getView(R.id.time);
        LinearLayout llComment = holder.getView(R.id.ll_comment);
        LinearLayout llLike = holder.getView(R.id.ll_like);
        ImageView ivZan = holder.getView(R.id.iv_zan);
        RelativeLayout rlZanComment = holder.getView(R.id.rl_zan_comment);
        TextView tvZanNumber = holder.getView(R.id.tvZanNumber);
        UnScrollListView mlvComment = holder.getView(R.id.lv_comment);

        String avatar = bean.getUser().getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            if (!avatar.contains("http")) {
                avatar = UrlProvider.URL_IMAGE_USER + avatar;

            }
            Glide.with(mContext).load(avatar).transform(new GlideCircleTransform(mContext)).error(R.drawable.img_touxiang).into(ivHeader);
        }


        String name = bean.getUser().getUser_nicename();
        if (TextUtils.isEmpty(name)) {
            name = bean.getUser().getUser_login();
        }
        tvName.setText(name);
        long time = 0;
        if (bean.getCreate_time() != null && !bean.getCreate_time().equals("")) {
            time = Long.parseLong(bean.getCreate_time());
        }
        tvCreateTime.setText(TimesUtil.setDataFormat(time));


        //图片处理
        if (bean.getImages() != null) {
            String[] images = bean.getImages().split(",");
            if (images.length == 1 && !images[0].equals("")) {
                ivSingle.setVisibility(View.VISIBLE);
                ngl.setVisibility(View.GONE);
                final String imageUrl = ImageUtil.changeSuggestImageAddress(images[0]);
                Glide.with(mContext).load(imageUrl).into(ivSingle);

                final ImageBean imageBean = new ImageBean();
                imageBean.setImagePath(imageUrl);
                imageBean.setWidth(SizeUtil.getScreenWidth() / 2);
                imageBean.setHeight(SizeUtil.getScreenWidth() / 2);

                ivSingle.setOnClickListener(new NoDoubleClickIn1S() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
                        ThumbViewInfo item;
                        Rect bounds = new Rect();
                        ivSingle.getGlobalVisibleRect(bounds);
                        item = new ThumbViewInfo(imageUrl);
                        item.setBounds(bounds);
                        mThumbViewInfoList.add(item);
                        GPreviewBuilder.from((Activity) mContext)
                                .setData(mThumbViewInfoList)
                                .setCurrentIndex(0)
                                .setType(GPreviewBuilder.IndicatorType.Number)
                                .start();
                        mThumbViewInfoList.clear();
                        mThumbViewInfoList = null;
                    }
                });

            } else if (images.length > 1) {
                for (int i = 0; i < images.length; i++) {
                    images[i] = ImageUtil.changeSuggestImageAddress(images[i]);
                }
                ivSingle.setVisibility(View.GONE);
                ngl.setVisibility(View.VISIBLE);
                ngl.setImages(images);
            } else {
                ivSingle.setVisibility(View.GONE);
                ngl.setVisibility(View.GONE);
            }
        } else {
            ivSingle.setVisibility(View.GONE);
            ngl.setVisibility(View.GONE);
        }


        //发表内容
        int count = 0;
        for (int i = 0; i < listLongUser.size(); i++) {
            ListenCircleBean.ResultsBean user = listLongUser.get(i);
            if (user.getId().equals(bean.getId())) {
                count++;
            }
        }
        if (count > 0) {
            tvLap.setVisibility(View.VISIBLE);
        } else {
            tvLap.setVisibility(View.GONE);
        }

        SpannableString spannableString = new SpannableString(EmojiUtil.getDecodeMsg(bean.getComment()));
        ltvContent.setText(spannableString);
        if (TextUtils.isEmpty(bean.getComment())) {
            ltvContent.setVisibility(View.GONE);
        } else {
            ltvContent.setVisibility(View.VISIBLE);
        }

        if (bean.getIs_zan() == 1) {
            ivZan.setSelected(true);
        } else {
            ivZan.setSelected(false);
        }
        List<FeedBackBean.ResultsBean.ChildCommentBean> commentList = new ArrayList<>();
        //处理点赞和评论
        if (0 == bean.getZan_num() && bean.getChild_comment() == null) {
            rlZanComment.setVisibility(View.GONE);
        } else {
            rlZanComment.setVisibility(View.VISIBLE);
            //处理评论

            if (bean.getChild_comment() != null) {
                mlvComment.setVisibility(View.VISIBLE);
                commentList = bean.getChild_comment();

                FeedBackCommentAdapter commentAdapter = new FeedBackCommentAdapter(mContext, commentList, bean);
                commentAdapter.setListener(this);
                mlvComment.setAdapter(commentAdapter);

            } else {

                mlvComment.setVisibility(View.GONE);
            }

            //处理点赞
            int zan = bean.getZan_num();
            if (zan != 0) {
                tvZanNumber.setVisibility(View.VISIBLE);
                tvZanNumber.setText(zan + "人点赞");
            } else {
                tvZanNumber.setVisibility(View.GONE);
            }

        }


        final List<FeedBackBean.ResultsBean.ChildCommentBean> finalCommentList = commentList;
        mlvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FeedBackBean.ResultsBean.ChildCommentBean suggest = finalCommentList.get(position);
                String id1 = suggest.getUser().getId();
//                    String commentID = suggest.getId();
                if (!id1.equals("") && LoginUtil.getUserId().equals(id1)) {
//                        mFeedBackListener.deleteMyComment(bean, position, commentID);
                } else {
                    mFeedBackListener.doHuifu(bean, suggest, position);
                }

            }
        });

        ngl.setOnItemImageViewClickListener(new NineGridLayout.OnItemImageViewClickListener() {

            @Override
            public void onItemImageViewClick(List<String> list, int position) {
                ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
                ThumbViewInfo item;

                for (int i = 0; i < list.size(); i++) {
                    View itemView = ngl.getChildAt(i);
                    Rect bounds = new Rect();
                    if (itemView != null) {
                        ImageView thumbView = (ImageView) itemView;
                        thumbView.getGlobalVisibleRect(bounds);
                    }
                    item = new ThumbViewInfo(list.get(i));
                    item.setBounds(bounds);
                    mThumbViewInfoList.add(item);
                }


                GPreviewBuilder.from((Activity) mContext)
                        .setData(mThumbViewInfoList)
                        .setCurrentIndex(position)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        .start();
                mThumbViewInfoList.clear();
                mThumbViewInfoList = null;
            }
        });


        llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFeedBackListener.doComment(bean, position);
            }
        });


        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFeedBackListener.doZan(bean, position);
            }
        });


    }


    @Override
    public void onItemClick(FeedBackBean.ResultsBean.ChildCommentBean suggest, FeedBackBean.ResultsBean feedback, int position) {

        String id1 = suggest.getUser().getId();
        User userInfo = AppSpUtil.getInstance().getUserInfo();

        if (!id1.equals("") && null != userInfo && null != userInfo.getResults().getId() && userInfo.getResults().getId().equals(id1)) {

        } else {
            mFeedBackListener.doHuifu(feedback, suggest, position);
        }


    }

    private FeedBackListener mFeedBackListener;

    public interface FeedBackListener {

        void doHuifu(FeedBackBean.ResultsBean resultsBean, FeedBackBean.ResultsBean.ChildCommentBean suggest, int position);

        void doComment(FeedBackBean.ResultsBean resultsBean, int position);

        void doZan(FeedBackBean.ResultsBean resultsBean, int position);

//        void deleteMyComment(FeedBackBean.ResultsBean resultsBean, int position, String id);

//        void delete(FeedBackBean.ResultsBean resultsBean, int position);
    }

    public void setListener(FeedBackListener listener) {
        this.mFeedBackListener = listener;
    }


}
