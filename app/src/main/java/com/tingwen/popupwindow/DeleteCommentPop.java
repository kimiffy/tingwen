package com.tingwen.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import com.tingwen.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 删除/取消
 * Created by Administrator on 2017/9/7 0007.
 */
public class DeleteCommentPop extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public DeleteCommentPop(Activity context) {
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_delete_comment, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tv_delete).setOnClickListener(this);
            popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                deleteListener.delete();
                break;
            case R.id.tv_cancel:
                deleteListener.cancel();
                break;

            default:
                break;
        }
    }

    public void setListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    private DeleteListener deleteListener;

    public interface DeleteListener {
        void delete();

        void cancel();

    }

}
