package com.tingwen.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.tingwen.R;
import com.tingwen.activity.LoginActivity;
import com.tingwen.activity.VipActivity;
import com.tingwen.app.AppSpUtil;
import com.tingwen.bean.DownLoadBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.DownLoadFinishEvent;
import com.tingwen.widget.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * 下载工具类
 * Created by Administrator on 2017/10/23 0023.
 */
public class DownLoadUtil {


    private volatile static DownLoadUtil downLoadUtil;

    private DownLoadUtil() {

    }

    public static DownLoadUtil getInstance() {

        if (downLoadUtil == null) {
            synchronized (DownLoadUtil.class) {
                if (downLoadUtil == null) {
                    downLoadUtil = new DownLoadUtil();
                }
            }
        }
        return downLoadUtil;
    }




    /**
     * 单条下载新闻
     */
    public void downLoadNews(final Context context, final DownLoadBean news) {

        String url = news.getPost_mp();
        String id = news.getId();
        if (SQLHelper.getInstance().isHasNews(id)) {
            ToastUtils.showBottomToast("新闻已经下载!");
            return;
        }

        int playTimes;
        if (!LoginUtil.isUserLogin()) {
            playTimes = AppSpUtil.getInstance().getPlayTimes();

            if (playTimes > AppConfig.PRE_PLAY_TIME_LIMIT_VALUE) {
                String message = "你还不是会员,每日可收听(或下载)的 " + AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + " 条已听完,开通会员可收听更多资讯";

                MaterialDialog mDialog = new MaterialDialog.Builder(context)
                        .title("温馨提示")
                        .content(message)
                        .contentColorRes(R.color.text_black)
                        .negativeText("忽略")
                        .positiveText("开通会员")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                LauncherHelper.getInstance().launcherActivity(context, LoginActivity.class);
                                ToastUtils.showBottomToast("登录后才可以开通会员哦~");
                            }
                        }).build();
                    mDialog.show();
                return;

            }
            playTimes += 1;
            AppSpUtil.getInstance().savePlayTimes(playTimes);


        } else {

            if (VipUtil.getInstance().getVipState() == 0) {
                playTimes = AppSpUtil.getInstance().getPlayTimes();

                if (playTimes > AppConfig.PRE_PLAY_TIME_LIMIT_VALUE) {
                    String message = "你还不是会员,每日可收听(或下载)的 " + AppConfig.PRE_PLAY_TIME_LIMIT_VALUE + " 条已听完,开通会员可收听更多资讯";
                    new MaterialDialog.Builder(context)
                            .title("温馨提示")
                            .content(message)
                            .contentColorRes(R.color.text_black)
                            .negativeText("忽略")
                            .positiveText("开通会员")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LauncherHelper.getInstance().launcherActivity(context, VipActivity.class);
                                }
                            }).build().show();
                    return;
                }
                playTimes += 1;
                AppSpUtil.getInstance().savePlayTimes(playTimes);
            }

        }

        GetRequest<File> request = OkGo.get(url);
        DownloadTask task = OkDownload.request(id, request)
                .folder(AppConfig.EXTRASTROGEDOWNLOADPATH)
                .fileName("tingwen." + id + ".mp3")
                .extra1(news)
                .save()
                .register(new LogDownloadListener())
                .register(new DownloadListener(id) {
                    @Override
                    public void onStart(Progress progress) {
                        ToastUtils.showBottomToast("开始下载!");
                    }

                    @Override
                    public void onProgress(Progress progress) {

                    }

                    @Override
                    public void onError(Progress progress) {
                        DownLoadBean bean = (DownLoadBean) progress.extra1;
                        SQLHelper.getInstance().deleteDownloadNews(bean.getId());
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        DownLoadBean bean = (DownLoadBean) progress.extra1;
                        Boolean hasNews = SQLHelper.getInstance().isHasNews(bean.getId());
                        if (!hasNews) {
                            SQLHelper.getInstance().insertDownloadNews(bean);
                            EventBus.getDefault().post(new DownLoadFinishEvent(bean.getId()));
                        }

                    }

                    @Override
                    public void onRemove(Progress progress) {

                    }
                });
        task.start();//开启下载任务
    }


    /**
     * 批量下载新闻
     */
    public void batchDownLoadNews(Context context,final DownLoadBean news) {

        String url = news.getPost_mp();
        String id = news.getId();
        GetRequest<File> request = OkGo.get(url);
        DownloadTask task = OkDownload.request(id, request)
                .folder(AppConfig.EXTRASTROGEDOWNLOADPATH)
                .fileName("tingwen." + id + ".mp3")
                .extra1(news)
                .save()
                .register(new LogDownloadListener())
                .register(new DownloadListener(id) {
                    @Override
                    public void onStart(Progress progress) {

                    }

                    @Override
                    public void onProgress(Progress progress) {

                    }

                    @Override
                    public void onError(Progress progress) {
                        DownLoadBean bean = (DownLoadBean) progress.extra1;
                        Logger.e("onError" + bean.getPost_title());
                        SQLHelper.getInstance().deleteDownloadNews(bean.getId());
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        DownLoadBean bean = (DownLoadBean) progress.extra1;
                        Boolean hasNews = SQLHelper.getInstance().isHasNews(bean.getId());
                        if (!hasNews) {
                            SQLHelper.getInstance().insertDownloadNews(bean);
                            EventBus.getDefault().post(new DownLoadFinishEvent(bean.getId()));
                        }

                    }

                    @Override
                    public void onRemove(Progress progress) {

                    }
                });
        task.start();//开启下载任务
    }


//    public List<String> getList() {
//        return list;
//    }

}
