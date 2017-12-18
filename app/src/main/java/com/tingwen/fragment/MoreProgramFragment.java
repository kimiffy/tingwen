package com.tingwen.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.tingwen.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.readystatesoftware.chuck.internal.support.DividerItemDecoration;
import com.tingwen.activity.ProgramDetailActivity;
import com.tingwen.adapter.ProgramAdapter1;
import com.tingwen.adapter.ProgramAdapter2;
import com.tingwen.base.BaseLazyFragment;
import com.tingwen.bean.PartBean;
import com.tingwen.bean.ProgramBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.FollowUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * 更多节目 主播
 * Created by Administrator on 2017/7/18 0018.
 */
public class MoreProgramFragment extends BaseLazyFragment implements ProgramAdapter1.FollowListener, ProgramAdapter2.FollowListener {
    //分类的ID
    private String typeId;
    //是否需要排名
    private boolean isSort;
    private String url;
    private List<ProgramBean.ResultsBean> list;
    private ProgramAdapter1 programAdapter1;//主播
    private ProgramAdapter2 programAdapter2;//节目

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_more_program;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        typeId = getArguments().getString("id");
        isSort = getArguments().getBoolean("sort");
        if ("0".equals(typeId)) {
            programAdapter1 = new ProgramAdapter1(getActivity(), list);
            programAdapter1.setListener(this);
            if (isSort) {
                programAdapter1.setIsSort(true);
            } else {
                programAdapter1.setIsSort(false);
            }
            programAdapter1.setOnItemClickListener(new ProgramAdapter1.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    ProgramBean.ResultsBean bean = list.get(position);
                    toAnchor(bean, false);


                }
            });

        } else {
            programAdapter2 = new ProgramAdapter2(getActivity(), list);
            programAdapter2.setListener(this);
            if (isSort) {
                programAdapter2.setIsSort(true);
            } else {
                programAdapter2.setIsSort(false);
            }
            programAdapter2.setOnItemClickListener(new ProgramAdapter2.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ProgramBean.ResultsBean bean = list.get(position);
                    toAnchor(bean, false);
                }
            });
        }


    }


    @Override
    protected void findViewById(View container) {
        super.findViewById(container);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if (rlvState != null) {
            rlvState.setLayoutManager(layoutManager);

            if ("0".equals(typeId)) {
                rlvState.setAdapter(programAdapter1);
            } else {
                rlvState.setAdapter(programAdapter2);
            }
            rlvState.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        }

    }

    @OnClick({R.id.iv_network_error})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_network_error:
                loadData();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            if (list.isEmpty()) {
                loadData();
            }
        }
    }


    private void loadData() {

        Map<String, String> map = new HashMap<>();
        map.put("term_id", typeId);
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("limit", "10000");
        if (isSort) {
            url = UrlProvider.SEARCH_TYPE_SORT;
        } else {
            url = UrlProvider.SEARCH_TYPE_LIST;
        }

        OkGo.<ProgramBean>post(url).params(map, false).execute(new SimpleJsonCallback<ProgramBean>(ProgramBean.class) {
            @Override
            public void onSuccess(Response<ProgramBean> response) {
                list = response.body().getResults();
                if (list.size() > 0) {
                    showContent();
                } else {
                    showEmpty();
                }
                if ("0".equals(typeId)) {
                    programAdapter1.setDataList(list);
                } else {
                    programAdapter2.setDataList(list);
                }

            }

            @Override
            public void onError(Response<ProgramBean> response) {
                super.onError(response);
                showError();
            }
        });
    }


    private void toAnchor(ProgramBean.ResultsBean dataBean, Boolean isClass) {
        PartBean partBean = new PartBean();
        partBean.setId(dataBean.getId());
        partBean.setName(dataBean.getName());
        partBean.setDescription(dataBean.getDescription());
        partBean.setFan_num(dataBean.getFan_num() + "");
        partBean.setMessage_num(dataBean.getMessage_num());
        partBean.setImages(dataBean.getImages());
        ProgramDetailActivity.actionStart(getActivity(), partBean, isClass,false);
    }

    @Override
    public void delete(final String id) {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_FOLLOW_ACT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    FollowUtil.followPartList.remove(id);
                    programAdapter1.setDataList(list);

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

    @Override
    public void follow(final String id) {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id",id );
        OkGo.<SimpleMsgBean>post(UrlProvider.FOLLOW_ACT).tag(this).params(map,true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    FollowUtil.followPartList.add(id);
                    programAdapter1.setDataList(list);
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
    public void delete2(final String id) {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id", id);
        OkGo.<SimpleMsgBean>post(UrlProvider.CANCEL_FOLLOW_ACT).tag(this).params(map, true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if (status == 1) {
                    FollowUtil.followPartList.remove(id);
                    programAdapter2.setDataList(list);

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

    @Override
    public void follow2(final String id) {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("act_id",id );
        OkGo.<SimpleMsgBean>post(UrlProvider.FOLLOW_ACT).tag(this).params(map,true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    FollowUtil.followPartList.add(id);
                    programAdapter2.setDataList(list);
                }
            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("请稍后重试");
            }
        });
    }
}
