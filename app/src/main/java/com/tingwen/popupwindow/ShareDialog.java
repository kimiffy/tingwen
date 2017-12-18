package com.tingwen.popupwindow;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tingwen.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 分享dialog
 * Created by Administrator on 2017/10/31 0031.
 */
public class ShareDialog extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.tv_weibo)
    TextView tvWeibo;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_pengyou)
    TextView tvPengyou;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.tv_qqzone)
    TextView tvQqzone;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.cancel)
    Button cancel;
    Unbinder unbinder;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_share_view, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        getDialog().setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        setCancelable(true);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.tv_weibo,R.id.tv_weixin,R.id.tv_pengyou,R.id.tv_qq,R.id.tv_qqzone, R.id.tv_copy,R.id.cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_weibo:
                shareListener.weiboShare();
                break;
            case R.id.tv_weixin:
                shareListener.weixinShare();
                break;
            case R.id.tv_pengyou:
                shareListener.pengyouShare();
                break;
            case R.id.tv_qq:
                shareListener.qqShare();
                break;
            case R.id.tv_qqzone:
                shareListener.qqZoneShare();
                break;
            case R.id.tv_copy:
                shareListener.copy();
                break;
            case R.id.cancel:
                shareListener.onDismiss();
                break;

            default:
                break;
        }

    }

    private ShareListener shareListener;

    public void setListener(ShareListener listener){
        shareListener = listener;
    }

    public interface ShareListener{
        void onDismiss();
        void weiboShare();
        void qqShare();
        void weixinShare();
        void pengyouShare();
        void qqZoneShare();
        void copy();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
