package com.tingwen.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.progressindicator.AVLoadingIndicatorView;
import com.tingwen.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * 自定义 侧滑删除recyclerview的加载更多view
 * Created by Administrator on 2017/9/22 0022.
 */
public class LoadMoreView extends LinearLayout implements SwipeMenuRecyclerView.LoadMoreView,View.OnClickListener{
    private AVLoadingIndicatorView mLoadingView;
    private TextView mTvMessage;
    private SwipeMenuRecyclerView.LoadMoreListener mLoadMoreListener;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        setGravity(Gravity.CENTER);
        setVisibility(GONE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int minHeight = (int) (displayMetrics.density * 60 + 0.5);
        setMinimumHeight(minHeight);

        inflate(getContext(), R.layout.recycler_swipe_load_more, this);
        mLoadingView = (AVLoadingIndicatorView) findViewById(R.id.loading_view);
        mTvMessage = (TextView) findViewById(R.id.tv_load_more_message);
        setOnClickListener(this);
    }
    /**
     * 马上开始回调加载更多了，这里应该显示进度条。
     */
    @Override
    public void onLoading() {
        // 展示加载更多的动画和提示信息。
        setVisibility(VISIBLE);
        mLoadingView.setVisibility(VISIBLE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText("拼命加载中");

    }

    /**
     * 加载更多完成了。
     *
     * @param dataEmpty 是否请求到空数据。
     * @param hasMore   是否还有更多数据等待请求。
     */
    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        // 根据参数，显示没有数据的提示、没有更多数据的提示。
        // 如果都不存在，则都不用显示。
        if (!hasMore) {
            setVisibility(VISIBLE);

            if (dataEmpty) {
                mLoadingView.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText("暂无数据");
            } else {
                mLoadingView.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText("拼命加载中");
            }
        } else {
            setVisibility(INVISIBLE);
        }
    }

    /**
     * 加载出错啦，下面的错误码和错误信息二选一。
     *
     * @param errorCode    错误码。
     * @param errorMessage 错误信息。
     */
    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(TextUtils.isEmpty(errorMessage) ? "出错啦" : errorMessage);

    }

    /**
     * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，此方法被调用，并传入listener。
     */
    @Override
    public void onWaitToLoadMore(SwipeMenuRecyclerView.LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
        setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText("加载更多");
    }

    /**
     * 非自动加载更多时mLoadMoreListener才不为空。
     */
    @Override
    public void onClick(View v) {
        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
    }
}
