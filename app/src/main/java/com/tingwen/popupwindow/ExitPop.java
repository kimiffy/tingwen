package com.tingwen.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import com.tingwen.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 退出登录
 * Created by Administrator on 2017/10/16 0016.
 */
public class ExitPop  extends BasePopupWindow implements View.OnClickListener{
    private View popupView;

    public ExitPop(Activity context) {
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_exit, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tv_exit).setOnClickListener(this);
            popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                exitListener.exit();
                break;
            case R.id.tv_cancel:
                exitListener.cancel();
                break;
            default:
                break;
        }
    }


    public void setListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }

    private ExitListener exitListener;

    public interface ExitListener {
        void exit();


        void cancel();

    }
}
