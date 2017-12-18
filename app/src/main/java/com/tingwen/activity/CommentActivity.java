package com.tingwen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.NewsBean;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.constants.AppConfig;
import com.tingwen.event.NewsRefreshEvent;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.BitmapUtil;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.KeyBoardUtils;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提交评论
 * Created by Administrator on 2017/9/12 0012.
 */
public class CommentActivity extends BaseActivity {


    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.iv_send)
    TextView ivSend;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.ll_wx_share)
    LinearLayout llWxShare;
    @BindView(R.id.ll_wb_share)
    LinearLayout llWbShare;
    private String post_id,to_uid,comment_id;
    private NewsBean newsBean;
    private IWXAPI wxapi;
    private Bitmap bitmap;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_comment;

    }


    @Override
    protected void initData() {
        super.initData();
        newsBean = (NewsBean) getIntent().getSerializableExtra("news");
        post_id = getIntent().getStringExtra("post_id");
        to_uid = getIntent().getStringExtra("to_uid");
        comment_id = getIntent().getStringExtra("comment_id");


    }


    @Override
    protected void initUI() {
        super.initUI();

        llWxShare.setSelected(false);
        llWbShare.setSelected(false);
    }



    @OnClick({R.id.ivLeft, R.id.iv_send,R.id.ll_wx_share,R.id.ll_wb_share})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                 finish();
                break;
            case R.id.iv_send:
                comment();
                break;
            case R.id.ll_wx_share:
                if(!TextUtils.isEmpty(etComment.getText())){
                    llWxShare.setSelected(true);
                    llWbShare.setSelected(false);
                }


                break;
            case R.id.ll_wb_share:
                if(!TextUtils.isEmpty(etComment.getText())){
                    llWbShare.setSelected(true);
                    llWxShare.setSelected(false);
                }
                break;

        }
    }

    /**
     * 发表评论
     */
    private void comment() {
        if(!LoginUtil.isUserLogin()){
            ToastUtils.showBottomToast("登录异常，请尝试重新登录评论!");
        }
        if (etComment.getText().toString().equals("")) {
            ToastUtils.showBottomToast("请输入评论内容~");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("post_id", post_id);
        map.put("post_table", "posts");
        String content = EmojiUtil.codeMsg(etComment.getText().toString());
        map.put("content", content);
        if (comment_id != null&&!comment_id.equals("")) {
                map.put("comment_id", comment_id);
        }
        if (to_uid != null&&!to_uid.equals("")) {
                map.put("to_uid", to_uid);
        }
        OkGo.<SimpleMsgBean>post(UrlProvider.AddComments).tag(this).params(map,true).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    EventBus.getDefault().post(new NewsRefreshEvent(1));
                    if(llWxShare.isSelected()){
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


                    }else if(llWbShare.isSelected()){
                        Intent i = new Intent(CommentActivity.this, WBShareActivity.class);
                        i.putExtra("news",newsBean);
                        i.putExtra("comment",etComment.getText().toString());
                        startActivity(i);

                    }

                    ToastUtils.showBottomToast("评论成功!");
                    etComment.setText("");
                }else if(status==0){
                    String msg = response.body().getMsg();
                    ToastUtils.showBottomToast(msg);
                }

            }

            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("评论失败!");
            }
        });
    }



    /**
     * 微信分享
     */
    private void wxShare() {

        wxapi = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, false);
        wxapi.registerApp(AppConfig.WX_APP_ID);

        String title = newsBean.getPost_title();
        String musicUrl = newsBean.getPost_mp();
        String id = newsBean.getId();
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
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        wxapi.sendReq(req);
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

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
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




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                KeyBoardUtils.closeKeyboard(v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }



}
