package com.tingwen.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import com.tingwen.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 回复/取消
 * Created by Administrator on 2017/9/18 0018.
 */
public class ReplyPop extends BasePopupWindow implements View.OnClickListener {
    private View popupView;

    public ReplyPop(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_reply_comment, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tv_reply).setOnClickListener(this);
            popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reply:
                replyListener.replay();
                break;
            case R.id.tv_cancel:
                replyListener.cancel2();
                break;

            default:
                break;
        }
    }

    public void setListener(ReplyListener deleteListener) {
        this.replyListener = deleteListener;
    }

    private ReplyListener replyListener;

    public interface ReplyListener {
        void replay();

        void cancel2();

    }

}
