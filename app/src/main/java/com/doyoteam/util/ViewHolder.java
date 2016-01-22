package com.doyoteam.util;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by xiaogang on 2015/6/2.
 * viewholder的简洁用法
 */
public class ViewHolder {
    public static <T extends View> T getAdapterView(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
         }
         View childView = viewHolder.get(id);
         if (childView == null) {
             childView = convertView.findViewById(id);
             viewHolder.put(id, childView);
         }
          return (T) childView;
       }
}
