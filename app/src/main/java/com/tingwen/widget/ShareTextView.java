package com.tingwen.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class ShareTextView extends TextView {
    public ShareTextView(Context context) {
        super(context);
    }

    public ShareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-25, getMeasuredWidth()/2, getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
}
