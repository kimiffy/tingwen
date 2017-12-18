package com.tingwen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tingwen.R;
import com.tingwen.utils.NoDoubleClickIn1S;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/1/19.
 */
public class NineGridLayout extends ViewGroup {
    private String[] images;
    private int gap = 5;
    private int imageWidth = 0;
    private OnItemImageViewClickListener onItemImageViewClickListener;
    private int height = -1;
    private List<String> list=new ArrayList<>();

    public void setOnItemImageViewClickListener(OnItemImageViewClickListener onItemImageViewClickListener) {
        this.onItemImageViewClickListener = onItemImageViewClickListener;
    }

    public NineGridLayout(Context context) {
        super(context);
        init(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        for (int i = 0; i < 9; i++) {
            generateImageView(context, i);
        }
    }

    public interface OnItemImageViewClickListener {
        void onItemImageViewClick(List<String> list,int position);
    }

    private void generateImageView(final Context context, final int position) {
        ImageView imageView = new ImageView(context);
        imageView.setOnClickListener(new NoDoubleClickIn1S() {
            @Override
            public void onNoDoubleClick(View v) {
                if (images != null) {
                    if (onItemImageViewClickListener != null) {
                        onItemImageViewClickListener.onItemImageViewClick(list,position);
                    }
                }
            }
        });
        addView(imageView, generateDefaultLayoutParams());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        imageWidth = (width - (2 * gap)) / 3;
        int height = imageWidth;

        if (images != null) {
            if (images.length <= 3) {
                height = imageWidth;
            } else if (images.length <= 6) {
                height = 2 * imageWidth + gap;
            } else {
                height = 3 * imageWidth + 2 * gap;
            }
        }

        int newHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, newHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            int[] position = findPosition(i);
            int left = (imageWidth + gap) * position[1];
            int top = (imageWidth + gap) * position[0];
            int right = left + imageWidth;
            int bottom = top + imageWidth;
            imageView.layout(left, top, right, bottom);
        }

        if (images != null) {
            setImages(images);
        }
    }

    private void setHeight() {
        LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = 0;
        setLayoutParams(layoutParams);
    }

    private int[] findPosition(int index) {
        int[] position = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i * 3 + j) == index) {
                    position[0] = i;
                    position[1] = j;
                    break;
                }
            }
        }
        return position;
    }

    public void setImages(String[] images) {
        this.images = images;
        list.clear();
        if (imageWidth != 0) {
            setHeight();
            for (int i = 0; i < getChildCount(); i++) {
                ImageView imageView = (ImageView) getChildAt(i);
                if (i < images.length) {
                    imageView.setVisibility(View.VISIBLE);
                    String imageUrl = images[i];
                    list.add(imageUrl);
                    Glide.with(getContext()).load(imageUrl).error(R.drawable.tingwen_bg_square).centerCrop()
                            .placeholder(R.drawable.tingwen_bg_square).into(imageView);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        }



    }
}
