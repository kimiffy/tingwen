package com.tingwen.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.tingwen.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 屏蔽/投诉
 * Created by Administrator on 2017/9/7 0007.
 */
public class ShieldPop extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public ShieldPop(Activity context) {
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_shield_or_complain, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tv_shield).setOnClickListener(this);
            popupView.findViewById(R.id.tv_complain).setOnClickListener(this);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shield:
                shieldListener.shield();
                break;
            case R.id.tv_complain:
                shieldListener.complain();
                break;

            default:
                break;
        }
    }

    public void setListener(ShieldListener shieldListener) {
        this.shieldListener = shieldListener;
    }

    private ShieldListener shieldListener;

    public interface ShieldListener {
        void shield();

        void complain();

    }
}
