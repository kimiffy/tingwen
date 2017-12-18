package com.tingwen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tingwen.utils.SizeUtil;

/**
 * 听友圈发布的文字
 * Created by Administrator on 2015/12/30 0030.
 */
public class LapTextView extends TextView {
    public static final int FLAG_NORMAL = 0;
    public static final int FLAG_CLOSE = 1;
    public static final int FLAG_SHOW = 2;

    private int flag = FLAG_NORMAL;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {

        this.flag = flag;
    }

    private int width;
    private int maxLines = 5;
    private int displayLines = 0;
    private LapListener lapListener;


    public void setLapListener(LapListener lapListener) {
        this.lapListener = lapListener;
    }

    public interface LapListener {
        void OnClose();
    }

    public LapTextView(Context context) {
        super(context);
    }

    public LapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        int lines = getLineCount();
        if (lines > maxLines) {
            if (flag == FLAG_CLOSE) {
                displayLines = maxLines;
            } else if (flag == FLAG_SHOW) {
                displayLines = lines;
            } else {
                displayLines = maxLines;
                if (lapListener != null) {
                    lapListener.OnClose();
                    flag = FLAG_CLOSE;
                }
            }
        } else {
            displayLines = lines;
        }
        setMeasuredDimension(width, getLineHeight() * displayLines + SizeUtil.dip2px(getContext(),2));
    }

    public void close() {
        setHeight(getLineHeight() * maxLines);
        flag = FLAG_CLOSE;
        setText(getText().toString());
    }

    public void show() {
        setHeight(getLineHeight() * getLineCount());
        flag = FLAG_SHOW;
        setText(getText().toString());
    }

}
