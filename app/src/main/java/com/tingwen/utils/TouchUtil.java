package com.tingwen.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * 描述: 扩大点击区域
 * 名称: TouchUtil
 */
public class TouchUtil {
    /**
     *
     * @param view 目标View
     * @param expandTouchWidth 点击区域扩大范围
     */
    public static void setTouchDelegate(final View view, final int expandTouchWidth) {
        final View parentView = (View) view.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                view.getHitRect(rect);
                rect.top -= expandTouchWidth;
                rect.bottom += expandTouchWidth;
                rect.left -= expandTouchWidth;
                rect.right += expandTouchWidth;
                TouchDelegate touchDelegate = new TouchDelegate(rect, view);
                parentView.setTouchDelegate(touchDelegate);
            }
        });
    }
}
