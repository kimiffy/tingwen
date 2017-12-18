package com.tingwen.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import com.tingwen.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 微信 / 支付宝支付
 * Created by Administrator on 2017/9/21 0021.
 */
public class PayPop extends BasePopupWindow implements View.OnClickListener{
    private View popupView;

    public PayPop(Activity context) {
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_pay, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.ll_wx_pay).setOnClickListener(this);
            popupView.findViewById(R.id.ll_zfb_pay).setOnClickListener(this);
            popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wx_pay:
                payChooseListener.WxPay();
                break;
            case R.id.ll_zfb_pay:
                payChooseListener.ALiPay();
                break;

            case R.id.tv_cancel:
                payChooseListener.cancel();
                break;
            default:
                break;
        }
    }


    public void setListener(PayChooseListener payChooseListener) {
        this.payChooseListener = payChooseListener;
    }

    private PayChooseListener payChooseListener;

    public interface PayChooseListener {
        void WxPay();

        void ALiPay();

        void cancel();

    }
}
