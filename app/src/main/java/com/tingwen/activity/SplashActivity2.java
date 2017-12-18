package com.tingwen.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.app.TwApplication;
import com.tingwen.bean.ADBean;
import com.tingwen.bean.DailyInfo;
import com.tingwen.constants.AppConfig;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.widget.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23 0023.
 */
public class SplashActivity2 extends Activity implements SplashADListener {


    private SplashAD splashAD;
    private ViewGroup container;
    private TextView skipView;
    private static final String SKIP_TEXT = "点击跳过 %d";
    private ImageView appLogo;
    public boolean canJump = false;
    public boolean isOpenAd=false;//是否打开了服务器广告
    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        skipView = (TextView) findViewById(R.id.skip_view);
        appLogo = (ImageView) findViewById(R.id.app_logo);
        getDailyInfo();

        appLogo.post(new Runnable() {
            @Override
            public void run() {
                width = appLogo.getWidth();
                height = appLogo.getHeight();
                getADImage();
            }
        });


        skipView.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                canJump = true;
                startActivity(new Intent(SplashActivity2.this, MainActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
//        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
        appLogo.setPadding(30,30,20,20);
        appLogo.setImageResource(R.drawable.splash_logo);
    }

    @Override
    public void onADClicked() {
        Log.i("AD_DEMO", "SplashADClicked");
    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onADTick(long millisUntilFinished) {
        Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
        skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
    }

    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }

    @Override
    public void onNoAD(AdError error) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        appLogo.setLayoutParams(layoutParams);
        appLogo.setScaleType(ImageView.ScaleType.FIT_XY);
        appLogo.setImageResource(R.drawable.splash_holder);
        appLogo.postDelayed(new Runnable() {

            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                if (canJump) {
                    startActivity(new Intent(SplashActivity2.this, MainActivity.class));
                    finish();
                }
            }
        }, 1000);
    }



    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        if (canJump) {
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            canJump = true;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("onResume");

        if(isOpenAd){//打开了广告后回来直接跳转至主页
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
            return;
        }
        if (canJump) {
            next();
        }
        canJump = true;
    }



    /**
     * 获取服务器开屏广告
     */
    private void getADImage() {

        OkGo.<ADBean>post(UrlProvider.AD_BOOT).execute(new SimpleJsonCallback<ADBean>(ADBean.class) {
            @Override
            public void onSuccess(Response<ADBean> response) {

                List<ADBean.ResultsBean> list = response.body().getResults();
                String ad_link = "";
                String ad_url = "";
                String ad_id = "";
                for (int i = 0; i < list.size(); i++) {
                    ADBean.ResultsBean bean = list.get(i);
                    if (!TextUtils.isEmpty(bean.getUrl_name())&&bean.getUrl_name().startsWith("Android")){
                        if(bean.getStatus().equals("1")){
                            String picSize = bean.getUrl_name();
                            if (picSize.length() > 8) {
                                String actualSize = picSize.substring(7);
                                String[] size = actualSize.split(":");
                                if (size.length == 2) {
                                    int picWidth = 0;
                                    int picHeight = 0;
                                    try {
                                        picWidth = Integer.valueOf(size[0]);
                                    } catch (Exception e) {
                                    }
                                    try {
                                        picHeight = Integer.valueOf(size[1]);
                                    } catch (Exception e) {
                                    }

                                    if (picHeight != 0 & picWidth != 0) {
                                        if (width == 1080 && height == 1800) {
                                            picHeight = 1800;
                                        } else {
                                            picHeight += 50;
                                        }
                                        if (width <= picWidth && height <= picHeight) {
                                            ad_id = bean.getAd_id();
                                            ad_link = bean.getAd_name();
                                            ad_url = bean.getAd_content();
                                            break;
                                        }
                                    }
                                }
                            }

                        }

                    }
                }

                if(!TextUtils.isEmpty(ad_id)&&!TextUtils.isEmpty(ad_url)){
                    if (!ad_url.contains("http:")) {
                        ad_url = UrlProvider.URL_IMAGE + ad_url;
                    }
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    appLogo.setLayoutParams(layoutParams);
                    appLogo.setScaleType(ImageView.ScaleType.FIT_XY);

                    if(Build.VERSION.SDK_INT < 17){
                        if(Util.isOnMainThread()) {
                            Glide.with(TwApplication.getInstance().getApplicationContext()).load(ad_url).error(R.drawable.splash_holder).into(appLogo);
                        }
                    }else {
                        if(Util.isOnMainThread()&&!SplashActivity2.this.isDestroyed()) {
                            Glide.with(SplashActivity2.this).load(ad_url).error(R.drawable.splash_holder).into(appLogo);
                        }
                    }

                    final String finalAd_link = ad_link;

                    appLogo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try {
                                isOpenAd=true;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(finalAd_link));
                                startActivity(intent);
                            } catch (Exception e) {

                            }
                        }
                    });
                    appLogo.postDelayed(new Runnable() {

                        @SuppressWarnings("unchecked")
                        @Override
                        public void run() {
                            if (canJump) {
                                startActivity(new Intent(SplashActivity2.this, MainActivity.class));
                                finish();
                            }
                        }
                    }, 2000);



                }else{
                    appLogo.setImageResource(R.drawable.splash_logo);
                    getAdFromGDT();

                }


            }


            @Override
            public void onError(Response<ADBean> response) {
                super.onError(response);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                appLogo.setLayoutParams(layoutParams);
                appLogo.setScaleType(ImageView.ScaleType.FIT_XY);
                appLogo.setImageResource(R.drawable.splash_holder);
                appLogo.postDelayed(new Runnable() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void run() {
                        if (canJump) {
                            startActivity(new Intent(SplashActivity2.this, MainActivity.class));
                            finish();
                        }
                    }
                }, 1000);
            }
        });

    }


    /**
     * 从广点通拉取广告
     */
    private  void getAdFromGDT(){
        // 如果targetSDKVersion >= 23，就要申请好权限。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            fetchSplashAD(this, container, skipView, AppConfig.APP_ID, AppConfig.SPLASH_ADD_ID, this, 0);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {

            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            fetchSplashAD(this, container, skipView, AppConfig.APP_ID, AppConfig.SPLASH_ADD_ID, this, 0);
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }


    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            fetchSplashAD(this, container, skipView, AppConfig.APP_ID, AppConfig.SPLASH_ADD_ID, this, 0);
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId         应用ID
     * @param posId         广告位ID
     * @param adListener    广告状态监听器
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }


    /**
     * 获取每日限制条数,以及服务器时间
     */
    private void getDailyInfo() {

        OkGo.<DailyInfo>post(UrlProvider.DAILY_INFO).execute(new SimpleJsonCallback<DailyInfo>(DailyInfo.class) {
            @Override
            public void onSuccess(Response<DailyInfo> response) {
                DailyInfo.ResultsEntity results = response.body().getResults();
                if (results != null && !"".equals(results.getDate())) {
                    String date = results.getDate();
                    int num = results.getNum();
                    setDaily(date, num);
                } else {
                    setDailyDefault();
                }
            }

            @Override
            public void onError(Response<DailyInfo> response) {
                super.onError(response);
                setDailyDefault();
            }
        });





    }


    /**
     * 设置限制次数
     * @param date 当前服务器时间
     * @param num 限制条数
     */
    private void setDaily(final String date, final int num) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String oldTime = AppSpUtil.getInstance().getDate();
                @SuppressLint("SimpleDateFormat") java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Calendar c1 = java.util.Calendar.getInstance();
                java.util.Calendar c2 = java.util.Calendar.getInstance();
                try {
                    c1.setTime(df.parse(oldTime));
                    c2.setTime(df.parse(date));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                int result = c1.compareTo(c2);
                AppConfig.PRE_PLAY_TIME_LIMIT_VALUE = num;
                Log.e("服务器播放次数限制: ", AppConfig.PRE_PLAY_TIME_LIMIT_VALUE+"");
                if (result < 0) { //时间过了至少一天
                    AppSpUtil.getInstance().savePlayTimes(0); //将已经听过的次数清零
                }
                AppSpUtil.getInstance().saveDate(date);//保存服务器时间
                AppSpUtil.getInstance().saveLimit(num);

            }
        }).start();
    }


    /**
     * 设置默认限制
     */
    private void setDailyDefault() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String newTime = format.format(date);
                String oldTime = AppSpUtil.getInstance().getDate();
                @SuppressLint("SimpleDateFormat") java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Calendar c1 = java.util.Calendar.getInstance();
                java.util.Calendar c2 = java.util.Calendar.getInstance();
                try {
                    c1.setTime(df.parse(oldTime));
                    c2.setTime(df.parse(newTime));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                int result = c1.compareTo(c2);
                int limit = AppSpUtil.getInstance().getLimit();
                AppConfig.PRE_PLAY_TIME_LIMIT_VALUE =limit;
                Log.e("默认播放次数限制: ", AppConfig.PRE_PLAY_TIME_LIMIT_VALUE+"");
                if (result < 0) { //时间过了至少一天
                    AppSpUtil.getInstance().savePlayTimes(0); //将已经听过的次数清零
                }
                AppSpUtil.getInstance().saveDate(newTime);//保存本地时间
            }
        }).start();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
