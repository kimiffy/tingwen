package com.tingwen.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import com.tingwen.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 性别选择pop
 * Created by Administrator on 2017/9/30 0030.
 */
public class SexSelectPop extends BasePopupWindow implements View.OnClickListener {
    private View popupView;

    public SexSelectPop(Activity context) {
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_sex_select, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.ll_man).setOnClickListener(this);
            popupView.findViewById(R.id.ll_male).setOnClickListener(this);
            popupView.findViewById(R.id.ll_no).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_man:
                sexSelectListener.selectSex(1+"");
                break;
            case R.id.ll_male:
                sexSelectListener.selectSex(2+"");
                break;
            case R.id.ll_no:
                sexSelectListener.selectSex(0+"");
                break;
            default:
                break;
        }
    }

    public void setSexSelectListenerListener(SexSelectListener sexSelectListener) {
        this.sexSelectListener = sexSelectListener;
    }

    private SexSelectListener sexSelectListener;

    public interface SexSelectListener {
        void selectSex(String i);
    }
}
