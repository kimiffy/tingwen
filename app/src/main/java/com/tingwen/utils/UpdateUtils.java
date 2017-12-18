package com.tingwen.utils;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.bean.VersionBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.widget.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 版本更新工具
 * Created by Administrator on 2017/10/17 0017.
 */
public class UpdateUtils {

    private Context context;
    private int version;//版本号
    private String versionName;//版本名
    private String apkUrl;//下载地址
    private String apkSize;//文件大小
    private String versionInfo;//更新信息
    private Notification mNotify;
    private NotificationManager mNotifyMgr;
    private boolean isShowMessage = false;//检测到时最新版本时,是否提示


    private volatile static UpdateUtils instance;


    private UpdateUtils() {

    }

    public static UpdateUtils getInstance() {
        if (instance == null) {
            synchronized (UpdateUtils.class) {
                if (instance == null) {
                    instance = new UpdateUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 检测版本,并下载更新
     */
    public void UpdateVersion(Context context,Boolean isNeedShowMsg) {
        this.context = context;
        isShowMessage = isNeedShowMsg;
        OkGo.<String>post(UrlProvider.VERSION).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Type type = new TypeToken<ArrayList<JsonObject>>() {
                }.getType();
                Gson gson = new Gson();

                ArrayList<JsonObject> jsonObjects = gson.fromJson(response.body(), type);
                ArrayList<VersionBean> arrayList = new ArrayList<>();
                for (int i = 0; i < jsonObjects.size(); i++) {
                    JsonObject jsonObject = jsonObjects.get(i);
                    Logger.e("数据:"+jsonObject);
                    VersionBean bean = gson.fromJson(jsonObject, VersionBean.class);
                    arrayList.add(bean);
                }

                VersionBean bean = arrayList.get(0);
                version = bean.getVersion();
                versionName = bean.getVersionName();
                apkUrl = bean.getApkUrl();
                apkSize = bean.getApkSize();
                versionInfo = bean.getVersionInfo();
                update();

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (isShowMessage) {
                    ToastUtils.showBottomToast("检查更新失败,请稍后重试!");
                }
            }
        });

    }


    /**
     * 更新
     */
    private void update() {
        int versionNum = getVersionNum();
        if (versionNum == -1) {
            ToastUtils.showBottomToast("检查更新失败,请稍后重试!");
            return;
        }
        if (versionNum < version) {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(versionName)
                    .setMessage(versionInfo)
                    .setPositiveButton("升级",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showCustomNotify("开始下载");
                                    // 下载apk，更新通知
                                    UpgradeTask task = new UpgradeTask(Integer.valueOf(apkSize));
                                    task.execute(apkUrl);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {
            if (isShowMessage) {
                ToastUtils.showBottomToast("当前已是最新版本!");
            }

        }


    }

    class UpgradeTask extends AsyncTask<String, Integer, String> {
        private int filelength;
        private int loadedLength;
        private File apkFile;

        public UpgradeTask(int filelength) {
            this.filelength = filelength;
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream is = null;

            FileOutputStream fos = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConn = (HttpURLConnection) url
                        .openConnection();
                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(context, "网络连接异常!", Toast.LENGTH_SHORT)
                            .show();

                    return null;
                }

                // 判断SDcard状态
                if (!Environment.MEDIA_MOUNTED.equals(Environment
                        .getExternalStorageState())) {
                    // 错误提示
                    Toast.makeText(context, "sd卡状态错误!", Toast.LENGTH_SHORT).show();
                    return null;
                }

                // 检查SDcard空间
                File SDCardRoot = Environment.getExternalStorageDirectory();
                if (SDCardRoot.getFreeSpace() < Integer.valueOf(apkSize)) {
                    // 弹出对话框提示用户空间不够
                    Toast.makeText(context, "sd卡空间不够!", Toast.LENGTH_SHORT).show();
                    return null;
                }
                String apkFileName = Environment.getExternalStorageDirectory() + "/tingwen/tingwen.apk";
                apkFile = new File(apkFileName);

                if (!apkFile.getParentFile().exists()) {
                    apkFile.getParentFile().mkdirs();
                }

                is = urlConn.getInputStream();
                fos = new FileOutputStream(apkFileName);
                // 发送通知
                publishProgress(-1);
                byte[] buffer = new byte[2048];
                int len;
                int loadedLen = 0;
                while (-1 != (len = is.read(buffer))) {
                    loadedLen += len;
                    if (loadedLen >= filelength / 50) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        publishProgress(loadedLength);
                        loadedLen = 0;
                    }
                    loadedLength += len;
                    fos.write(buffer, 0, len);

                }
                fos.flush();
                return new String(loadedLen + "");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                finishCustomNotify("下载完成", apkFile);
            } else {
                finishCustomNotify("下载失败", null);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] == -1) {
//                showCustomNotify("下载完成");
            } else {
                updateCustomNotify("下载中...", filelength, values[0]);
            }
        }
    }


    @SuppressWarnings("deprecation")
    public void showCustomNotify(String str) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            mNotify = new Notification(R.drawable.tingwen_logo, str,
                    System.currentTimeMillis());
        } else {
            mNotify = new Notification.Builder(context).setContentText("听闻")
                    .setContentTitle("升级").setTicker("听闻升级中")
                    .setSmallIcon(R.drawable.tingwen_logo).build();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        // // 定制通知点击事件
        mNotify.contentIntent = contentIntent;
        // // 定制通知栏布局
        mNotify.contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notify);
        mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1234, mNotify);

    }

    private void finishCustomNotify(String str, File apkFile) {


        if (apkFile != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data;
            if (android.os.Build.VERSION.SDK_INT < 24) {
                data = Uri.fromFile(apkFile);
                String type = "application/vnd.android.package-archive";
                intent.setDataAndType(data, type);
            } else {
                //适配7.0
                data = FileProvider.getUriForFile(context, "com.tingwen.provider", apkFile);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String type = "application/vnd.android.package-archive";
                intent.setDataAndType(data, type);
            }


            context.startActivity(intent);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // 定制通知点击事件
            mNotify.contentIntent = contentIntent;
            // 定制通知栏布局

        }
        mNotify.contentView.setTextViewText(R.id.textView1, str);
        mNotify.contentView.setProgressBar(R.id.progressBar1, 100, 100, false);
        mNotifyMgr.notify(1234, mNotify);

    }

    private void updateCustomNotify(String str, int max, int progress) {
        // 定制通知栏布局
        mNotify.contentView.setTextViewText(R.id.textView1, str);
        mNotify.contentView.setProgressBar(R.id.progressBar1, max, progress, false);
        mNotifyMgr.notify(1234, mNotify);
    }


    /**
     * 获取当前本地安装的版本号
     *
     * @return
     */
    private int getVersionNum() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
