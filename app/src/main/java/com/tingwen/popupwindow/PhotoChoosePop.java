package com.tingwen.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import com.tingwen.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 相册/拍照
 * Created by Administrator on 2017/9/8 0008.
 */
public class PhotoChoosePop extends BasePopupWindow implements View.OnClickListener{

    private View popupView;

    public PhotoChoosePop(Activity context) {
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_photo_choose, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tv_album).setOnClickListener(this);
            popupView.findViewById(R.id.tv_camera).setOnClickListener(this);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_album:
                photoChooseListener.album();
                break;
            case R.id.tv_camera:
                photoChooseListener.camera();
                break;

            default:
                break;
        }
    }


    public void setListener(PhotoChooseListener deleteListener) {
        this.photoChooseListener = deleteListener;
    }

    private PhotoChooseListener photoChooseListener;

    public interface PhotoChooseListener {
        void album();

        void camera();

    }
}
