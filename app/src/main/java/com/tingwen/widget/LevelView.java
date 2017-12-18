package com.tingwen.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tingwen.R;
import com.tingwen.utils.SizeUtil;


/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class LevelView extends View {

    private Paint paint,paintColor;
    private Rect mBitmap;//需要绘制的bitmap
    private Rect whereBitmap;//需要绘制在什么地方
    private Paint paintWhite;
    private Paint textPaint;
    private Paint topTextPaint;

    private int textLevel;//等级
    private int textSize;//字体大小
    private String textTop;//顶部的文字等级

    private int width,height;//图形的宽高

    public LevelView(Context context) {
        super(context,null);
    }

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        inite(attrs);
    }

    public LevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inite(attrs);

    }

    public void setLevel(int level){
        textLevel = level;
        invalidate();
    }

    private void inite(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LevelView);
        textLevel = array.getInteger(R.styleable.LevelView_textLevel,1);
        textSize = array.getInteger(R.styleable.LevelView_mTextSzie, SizeUtil.dip2px(getContext(), 10));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paintColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintColor.setColor(Color.parseColor("#68D5F2"));
        paintColor.setStyle(Paint.Style.FILL);
        paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintWhite.setColor(Color.parseColor("#ffffff"));
        paintWhite.setStyle(Paint.Style.FILL);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(SizeUtil.dip2px(getContext(), 10));
        textPaint.setColor(Color.parseColor("#ffffff"));
        topTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topTextPaint.setTextSize(SizeUtil.dip2px(getContext(), 11));
        topTextPaint.setColor(Color.parseColor("#8899a6"));
        whereBitmap = new Rect(0,0, SizeUtil.dip2px(getContext(),40),SizeUtil.dip2px(getContext(),15));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = null;
        if(textLevel < 10){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_1);
            paintColor.setColor(Color.parseColor("#68D5F2"));
            textTop = textLevel + "/10";
        }else if(textLevel < 20){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_2);
            paintColor.setColor(Color.parseColor("#51D9C0"));
            textTop = textLevel + "/20";
        }else if(textLevel < 30){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_3);
            paintColor.setColor(Color.parseColor("#1BD534"));
            textTop = textLevel + "/30";
        }else if(textLevel < 40){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_4);
            paintColor.setColor(Color.parseColor("#BDE10A"));
            textTop = textLevel + "/40";
        }else if( textLevel < 50){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_5);
            paintColor.setColor(Color.parseColor("#F2D90A"));
            textTop = textLevel + "/50";
        }else if(textLevel < 60){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_6);
            paintColor.setColor(Color.parseColor("#FA9116"));
            textTop = textLevel + "/60";
        }else if(textLevel < 70){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_7);
            paintColor.setColor(Color.parseColor("#F73333"));
            textTop = textLevel + "/70";
        }else if(textLevel < 80){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_8);
            paintColor.setColor(Color.parseColor("#E30989"));
            textTop = textLevel + "/80";
        }else if(textLevel < 90){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_9);
            paintColor.setColor(Color.parseColor("#831AD5"));
            textTop = textLevel + "/90";
        }else if(textLevel < 100){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_10);
            paintColor.setColor(Color.parseColor("#2564E4"));
            textTop = textLevel + "/100";
        }else if(textLevel == 100){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_level_100);
            paintColor.setColor(Color.parseColor("#45A3EB"));
            textTop = textLevel + "";
        }
        canvas.drawRoundRect(new RectF(SizeUtil.dip2px(getContext(), 30), SizeUtil.dip2px(getContext(), 10),
                SizeUtil.dip2px(getContext(), 130),SizeUtil.dip2px(getContext(), 15)), 5, 5, paintWhite);
        if (textLevel % 10 == 9) {
            canvas.drawRoundRect(new RectF(SizeUtil.dip2px(getContext(), 30), SizeUtil.dip2px(getContext(), 10),
                            SizeUtil.dip2px(getContext(), 40 + 100 * (textLevel % 10) / 10), SizeUtil.dip2px(getContext(), 15)),
                    5, 5, paintColor);
        } else if(textLevel == 100){
            canvas.drawRoundRect(new RectF(SizeUtil.dip2px(getContext(), 30), SizeUtil.dip2px(getContext(), 10),
                    SizeUtil.dip2px(getContext(), 140), SizeUtil.dip2px(getContext(), 15)), 5,5,paintColor);
        }else{
            canvas.drawRect(new RectF(SizeUtil.dip2px(getContext(), 30), SizeUtil.dip2px(getContext(), 10),
                    SizeUtil.dip2px(getContext(), 40 + 100 * (textLevel % 10) / 10), SizeUtil.dip2px(getContext(), 15)), paintColor);
        }
        if(null!=bitmap){
            canvas.drawBitmap(bitmap, null, whereBitmap, paint);
        }
        canvas.drawText(textLevel + "", SizeUtil.dip2px(getContext(), 22), SizeUtil.dip2px(getContext(), 10), textPaint);
        canvas.drawText(textTop,width / 2,SizeUtil.dip2px(getContext(), 8),topTextPaint);
        super.onDraw(canvas);
    }
}
