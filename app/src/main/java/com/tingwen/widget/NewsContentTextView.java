package com.tingwen.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tingwen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻详情里显示内容的TextView
 */
public class NewsContentTextView extends TextView {
    private List<String> textList = new ArrayList<>();
    private List<Integer> spaceXCountList = new ArrayList<>();
    private boolean hasCalc = false;
    private float width;
    private TextPaint mPaint;
    private int spaceX;
    private int spaceY;

    public NewsContentTextView(Context context) {
        super(context);
    }

    public NewsContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewsContentTextView);
        spaceX = typedArray.getDimensionPixelSize(R.styleable.NewsContentTextView_spaceX,0);
        spaceY = typedArray.getDimensionPixelSize(R.styleable.NewsContentTextView_spaceY, 0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!hasCalc) {
            textList.clear();
            spaceXCountList.clear();

            width = getMeasuredWidth();
            mPaint = getPaint();
            mPaint.setColor(getCurrentTextColor());
            mPaint.drawableState = getDrawableState();

            String[] items = getText().toString().split("\n");
            for(String s : items) {
                calcText(getPaint(),s);
            }

            int realHeight = getLineHeight() * textList.size() + (spaceY * textList.size());
            setHeight(realHeight);

            hasCalc = true;
        }

        for (int i = 0; i < textList.size(); i++) {
            String line = textList.get(i);

            float drawY = getLineHeight() * i + getTextSize();

            for (int j = 0; j < line.length(); j++) {
                float textWidth;
                if(i == textList.size() - 1) {
                    textWidth = mPaint.measureText(line.substring(0, j));
                }else {
                    if(spaceXCountList.get(i) != -1) {
                        float gap = width - mPaint.measureText(line) - spaceX * (spaceXCountList.get(i));
                        if(gap > 0) {
                            float interval = gap / (line.length() -1);
                            textWidth = mPaint.measureText(line.substring(0, j)) + interval* j;
                        }else {
                            textWidth = mPaint.measureText(line.substring(0, j));
                        }
                    }else {
                        textWidth = mPaint.measureText(line.substring(0, j));
                    }
                }
                String currentText = line.substring(j, j + 1);
                canvas.drawText(currentText, textWidth + j * spaceX, drawY+ i * spaceY, mPaint);
            }
        }

    }

    private void calcText(Paint paint, String text) {
        if (text.length() == 0) {
            textList.add("\n");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int startLocation = 0;
        int spaceLocation = 0;


        for (int i = 0; i < text.length(); i++) {
            if (paint.measureText(text.substring(startLocation, i + 1)) + (spaceX * (spaceLocation)) > width ) {
                textList.add(stringBuilder.toString());
                spaceXCountList.add(spaceLocation);
                stringBuilder = new StringBuilder();
                startLocation = i;
                spaceLocation = 0;
            }
            stringBuilder.append(text.charAt(i));
            spaceLocation ++;
        }

        if(!stringBuilder.toString().isEmpty()) {
            textList.add(stringBuilder.toString());
            spaceXCountList.add(-1);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        hasCalc = false;
        super.setText(text, type);
    }
}
