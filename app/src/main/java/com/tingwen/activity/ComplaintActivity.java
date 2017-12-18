package com.tingwen.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.flyco.roundview.RoundTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.base.BaseActivity;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 听友圈投诉
 * Created by Administrator on 2017/11/13 0013.
 */
public class ComplaintActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.et_complaint)
    EditText etComplaint;
    @BindView(R.id.tv_sure)
    RoundTextView tvSure;
    private String to_uid;
    private String comment_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitivity_complaint;
    }

    @Override
    protected void initData() {
        super.initData();
        to_uid = getIntent().getStringExtra("to_uid");
        comment_id = getIntent().getStringExtra("comment_id");


    }

    @OnClick({R.id.ivLeft,R.id.tv_sure})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tv_sure:
                sendComplaint();
                break;

            default:
                break;
        }
    }


    /**
     * 投诉
     */
    private void sendComplaint() {

        if(null==to_uid||null==comment_id){
            ToastUtils.showBottomToast("获取信息失败,请稍后尝试");
            return;
        }

        if(TextUtils.isEmpty(etComplaint.getText().toString())){
            ToastUtils.showBottomToast("请输入投诉内容");
            return;
        }


        Map<String,String> map = new HashMap<>();
        map.put("accessToken", LoginUtil.getAccessToken());
        map.put("to_uid",to_uid);
        map.put("comment_id",comment_id);
        map.put("content",etComplaint.getText().toString());
        OkGo.<SimpleMsgBean>post(UrlProvider.SEND_COMPLAINT).params(map).tag(this).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
            @Override
            public void onSuccess(Response<SimpleMsgBean> response) {
                int status = response.body().getStatus();
                if(status==1){
                    ToastUtils.showBottomToast("投诉成功!");
                }else{
                    ToastUtils.showBottomToast(response.body().getMsg());
                }
            }


            @Override
            public void onError(Response<SimpleMsgBean> response) {
                super.onError(response);
                ToastUtils.showBottomToast("投诉失败!");
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
