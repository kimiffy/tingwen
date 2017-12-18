package com.tingwen.fragment;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimePickerView;
import com.flyco.roundview.RoundTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.activity.BatchDownloadActivity;
import com.tingwen.activity.LoginActivity;
import com.tingwen.base.BaseFragment;
import com.tingwen.bean.CategoryBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.popupwindow.CategoryDialog;
import com.tingwen.utils.LauncherHelper;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;
import com.tingwen.utils.VipUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 批量下载
 * Created by Administrator on 2017/10/19 0019.
 */
public class BatchDownloadFragment extends BaseFragment {

    @BindView(R.id.et_start_time)
    TextView startTime;
    @BindView(R.id.ll_start_time)
    LinearLayout llStartTime;
    @BindView(R.id.et_end_time)
    TextView endTime;
    @BindView(R.id.ll_end_time)
    LinearLayout llEndTime;
    @BindView(R.id.et_category)
    TextView category;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.tv_commit)
    RoundTextView tvCommit;
    private Boolean isStartTime = false;
    private TimePickerView pvTime;
    private List<CategoryBean.ResultsBean> list;
    private CategoryDialog dialog;
    private FragmentManager fragmentManager;
    private String Id = "";
    private String mCategory = "";
    private String timeStart = "";
    private String timeEnd = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_batch_download;
    }

    @Override
    protected void initData() {
        super.initData();
        initPicker();
        getCategory();
        dialog = CategoryDialog.newInstance();
        fragmentManager = getActivity().getFragmentManager();

    }

    @Override
    protected void setListener() {
        super.setListener();
        dialog.setOnChooseNewsCategoryListener(new CategoryDialog.OnChooseNewsCategoryListener() {
            @Override
            public void onChooseNewsCategoryFinished(String content, String categoryId) {
                Id = categoryId;
                mCategory = content;
                category.setText(content);
            }
        });
    }

    @OnClick({R.id.ll_start_time, R.id.ll_end_time, R.id.ll_category, R.id.tv_commit})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_start_time:
                isStartTime = true;
                pvTime.show();
                break;
            case R.id.ll_end_time:
                isStartTime = false;
                pvTime.show();
                break;
            case R.id.ll_category:
                if (null != list && list.size() != 0) {
                    dialog.setList(list);
                    dialog.show(fragmentManager, "newsCategory");
                }
                break;
            case R.id.tv_commit:
                int vipState = VipUtil.getInstance().getVipState();
                if (vipState == 0) {

                    if(!LoginUtil.isUserLogin()){
                        new MaterialDialog.Builder(getActivity())
                                .title("温馨提示")
                                .content("登录后才可以使用批量下载哦~")
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

                    new MaterialDialog.Builder(getActivity())
                            .title("温馨提示")
                            .content("您还不是会员,无法使用批量下载功能,是否前往开通会员?")
                            .contentColorRes(R.color.text_black)
                            .negativeText("取消")
                            .positiveText("开通")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(getActivity(), LoginActivity.class);
                                }
                            }).build().show();
                    return;
                }
                commit();
                break;
            default:
                break;
        }
    }

    /**
     * 提交数据 跳转批量下载
     */
    private void commit() {
        if (TextUtils.isEmpty(startTime.getText()) || TextUtils.isEmpty(endTime.getText())) {
            ToastUtils.showBottomToast("请选择时间");
            return;
        }

        if (TextUtils.isEmpty(Id)) {
            ToastUtils.showBottomToast("请选择新闻分类");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("startTime", timeStart);
        bundle.putString("endTime", timeEnd);
        bundle.putString("category", mCategory);
        bundle.putString("categoryId", Id);
        LauncherHelper.getInstance().launcherActivity(getActivity(), BatchDownloadActivity.class, bundle);
    }

    /**
     * 获取新闻分类数据
     */
    private void getCategory() {

        OkGo.<CategoryBean>post(UrlProvider.GET_CATEGORY).execute(new SimpleJsonCallback<CategoryBean>(CategoryBean.class) {
            @Override
            public void onSuccess(Response<CategoryBean> response) {
                list = response.body().getResults();
            }
        });

    }


    /**
     * 初始化事件选择器
     */
    private void initPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //设置起始日期 最大日期
        startDate.set(2010, 0, 1);
        endDate.set(2030, 11, 31);
        pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(date);
                if (isStartTime) {
                    timeStart = time;
                    startTime.setText(time);

                } else {
                    timeEnd = time;
                    endTime.setText(time);

                }

            }
        }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(19)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#5cb8e6"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#5cb8e6"))//取消按钮文字颜色
                .setTitleBgColor(Color.parseColor("#EEEEEE"))//标题背景颜色
                .setBgColor(Color.WHITE)//滚轮背景颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

    }


}
