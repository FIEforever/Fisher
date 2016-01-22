package com.doyoteam.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 包含在ScrollView下的ListView
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version NearShop 4.0
 *          Datetime 2015-05-14 09:19
 *          Copyright 2015 东莞邮政电子商务分局 All rights reserved.
 * @since NearShop 4.0
 */
public class ScrollListView extends ListView {


    public ScrollListView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
