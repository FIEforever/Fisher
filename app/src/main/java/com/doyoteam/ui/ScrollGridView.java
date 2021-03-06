package com.doyoteam.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 消除GridView 与 ScrollView 的滑动冲突
 * Created by imatrix on 15-4-16.
 */
public class ScrollGridView extends GridView {

    public ScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollGridView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
