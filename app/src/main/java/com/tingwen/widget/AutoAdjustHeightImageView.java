package com.tingwen.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tingwen.R;


public class AutoAdjustHeightImageView extends ImageView {


    private Bitmap imageBitMap;

    private int imageWidth;

    private int imageHeight;

    private boolean isDetailMainImage = false;
    private int defaultImageResource;

    private int screenWidth;
    private int screenHeight;

    public AutoAdjustHeightImageView(Context context) {
        super(context);
    }

    public AutoAdjustHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    protected void initImageSize() {
        try {
            Drawable imageDrawable = this.getBackground();
            if (imageDrawable == null && imageBitMap != null) {
                imageWidth = imageBitMap.getWidth();
                imageHeight = imageBitMap.getHeight();
            }
            if (imageDrawable != null && imageBitMap == null && !isDetailMainImage) {
                Bitmap bitmap = ((BitmapDrawable) imageDrawable).getBitmap();
                imageWidth = bitmap.getWidth();
                imageHeight = bitmap.getHeight();
            }


            if (isDetailMainImage && imageWidth > 0) {
                getWidthAndHeight();
                int maxHeight = screenHeight - getResources().getDimensionPixelSize(R.dimen.class_detail_sreen_height);
                int adjustImageHeight = imageHeight * screenWidth / imageWidth;
                if (adjustImageHeight > maxHeight) {
                    imageWidth = screenWidth;
                    imageHeight = maxHeight;
                }
            }
        } catch (Exception e) {
            Log.e("", e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        initImageSize();
        if (imageWidth == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int adjustHeight = width * imageHeight / imageWidth;
            this.setMeasuredDimension(width, adjustHeight);
        }
    }


    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        imageBitMap = bm;
    }


    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        defaultImageResource = resId;
    }


    public void getWidthAndHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    public boolean isDetailMainImage() {
        return isDetailMainImage;
    }

    public void setDetailMainImage(boolean isDefault) {
        this.isDetailMainImage = isDefault;
    }
}
