package com.tingwen.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.tingwen.R;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.adapter.FansMessageAdapter;
import com.tingwen.base.BaseLazyFragment;
import com.tingwen.bean.FansMessageBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.event.DeleteMessageEvent;
import com.tingwen.event.FansMessageEvent;
import com.tingwen.event.FansReplayEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 留言
 * Created by Administrator on 2017/8/10 0010.
 */
public class FansMessageFragment extends BaseLazyFragment {

    @BindView(R.id.rlv_message)
    LRecyclerView rlvMessage;


    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<FansMessageBean.ResultsBean> list;
    private List<FansMessageBean.ResultsBean> allList;
    private String anchorId;
    public int page = 1;
    private FansMessageAdapter fansMessageAdapter;


    public static FansMessageFragment newInstance(String anchorId) {
        FansMessageFragment fragment = new FansMessageFragment();
        Bundle args = new Bundle();
        args.putString("id", anchorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mesage;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible &&list!=null&&list.size() == 0) {
            loadData(1);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            anchorId = getArguments().getString("id");
        }

        list = new ArrayList<>();
        allList= new ArrayList<>();
        rlvMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        fansMessageAdapter = new FansMessageAdapter(getActivity(), list);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(fansMessageAdapter);
        rlvMessage.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新
        rlvMessage.setPullRefreshEnabled(false);
        //设置底部加载颜色
        rlvMessage.setFooterViewColor(R.color.blue, R.color.grey, android.R.color.white);
        //设置底部加载文字提示
        rlvMessage.setFooterViewHint("拼命加载中", "我是有底线的", "点击重新加载");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void setListener() {
        super.setListener();
        rlvMessage.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page += 1;
                loadData(page);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!LoginUtil.isUserLogin()) {
                    return;
                }
                FansMessageBean.ResultsBean bean = allList.get(position);
                String uid = bean.getUid();
                String commentId = bean.getId();
                String to_uid = bean.getTo_uid();
                String userNicename = bean.getUser_nicename();
                String userLogin = bean.getUser_login();
                EventBus.getDefault().post(new FansReplayEvent(uid, commentId, to_uid, userNicename, userLogin));

            }
        });

    }


    /**
     * 获取数据
     *
     * @param i
     */
    private void loadData(int i) {
        page = i;
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("limit", "20");
        if (LoginUtil.isUserLogin()) {
            map.put("uid", LoginUtil.getUserId());
        }
        map.put("act_id", anchorId);

        OkGo.<FansMessageBean>post(UrlProvider.ACT_COMMENTS).tag(this).params(map, true).execute(new SimpleJsonCallback<FansMessageBean>(FansMessageBean.class) {
            @Override
            public void onSuccess(Response<FansMessageBean> response) {
                list = response.body().getResults();
                allList.addAll(list);
                if (page == 1) {
                    fansMessageAdapter.setDataList(list);
                } else {
                    fansMessageAdapter.addAll(list);
                }
                if(null!=rlvMessage){

                    rlvMessage.refreshComplete(20);
                }
                lRecyclerViewAdapter.notifyDataSetChanged();
                if (list.size() < 20) {
                    rlvMessage.setNoMore(true);
                }
            }

            @Override
            public void onError(Response<FansMessageBean> response) {
                super.onError(response);
                if (page > 1) {
                    if(null!=rlvMessage){

                        rlvMessage.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                loadData(page);
                            }
                        });
                    }

                } else if (page == 1 && list.size() < 20) {
                    if(null!=rlvMessage){
                        rlvMessage.setNoMore(true);
                    }
                } else if (page == 0) {
                    ToastUtils.showBottomToast("获取数据失败!");
                }
            }
        });

    }

    /**
     * 留言 /回复
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendMessageEvent(FansMessageEvent event) {
        String message = event.getMessage();
        int type = event.getType();
        String id = event.getId();
        String touid = event.getTouid();
        if (type == 0) {
            sendMessage(message, null, null);
        } else if (type == 1) {
            sendMessage(message, touid, id);
        }


    }


    /**
     * 发送留言/回复
     */
    private void sendMessage(String message, String to_uid, String comment_id) {
        String content = EmojiUtil.codeMsg(message);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("content", content);
        map.put("act_id", anchorId);
        map.put("act_table", "act");
        if (!TextUtils.isEmpty(to_uid)) {
            map.put("to_uid", to_uid);
        }
        if (!TextUtils.isEmpty(comment_id)) {
            map.put("comment_id ", comment_id);
        }

        OkGo.<SimpleMsgBean>post(UrlProvider.ADD_ACT_COMMENTS).params(map, true).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                if (response.body().getStatus() == 1) {
                    loadData(1);
                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }

            }
        });
    }


    /**
     * 删除自己的留言事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteMessageEvent(DeleteMessageEvent event) {
        String id = event.getId();
        deleteMessage(id);
    }

    /**
     * 删除自己的留言
     */
    private void deleteMessage(String id) {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("ac_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.DEL_ACT_COMMENTS).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    loadData(1);
                } else {
                    ToastUtils.showBottomToast(response.body().getMsg());
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
    }
}
