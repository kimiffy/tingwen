package com.tingwen.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 描述：ViewHolder工具类
 * 名称：ViewHolderUtil
 */
public class ViewHolderUtil {
    @SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
