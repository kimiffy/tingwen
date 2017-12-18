package com.tingwen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 描述：自定义ListView(解决ScrollView嵌套ListView的问题)
 *
 */
public class UnScrollListView extends ListView {
    public UnScrollListView(Context paramContext) {
        super(paramContext);
    }

    public UnScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 该方法解决嵌套
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureSize = measureHeight(Integer.MAX_VALUE >> 2, heightMeasureSpec);
        int expandSize = MeasureSpec.makeMeasureSpec(measureSize,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSize);
    }

    /**
     * 测量实际高度
     *
     * @param size
     * @param measureSpec
     * @return
     */
    private int measureHeight(int size, int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY)
            result = specSize;
        else {
            result = size;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


}
