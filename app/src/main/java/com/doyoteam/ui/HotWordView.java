package com.doyoteam.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doyoteam.fisher.R;

import java.util.LinkedList;
import java.util.List;

/**
 * 热门词汇展示控件
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version NearShop 4.0
 *          Datetime 2015-05-27 14:40
 *          Copyright 2015 东莞邮政电子商务分局 All rights reserved.
 * @since NearShop 4.0
 */
public class HotWordView extends RelativeLayout implements ViewPager.OnPageChangeListener,
        View.OnClickListener {

    private OnItemClickListener itemClickListener;
    private Context context;
    private ViewPager viewPager;        // 热词展示Pager
    private RadioGroup radioGroup;      // 热词页码展示
    private List<Keyword> keywordList;  // 热词库
    private int totalPage;              // 总页数
    private int[] indexArray;           // 位置指引数组

    public HotWordView(Context context) {
        super(context);
        initView(context);
    }

    public HotWordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HotWordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // 初始化一些视图
    private void initView(Context context) {
        this.context = context;

        // ViewPager 滑动事件优先
        viewPager = new ViewPager(context) {

            GestureDetector mGesture = new GestureDetector(getContext(),
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                                float distanceY) {
                            return true;
                        }
                    });

            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                return mGesture.onTouchEvent(ev) || super.onInterceptTouchEvent(ev);
            }

        };
        radioGroup = new RadioGroup(context);

        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.addView(viewPager, lp);
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        lp.setMargins(0, 0, 0, dp2px(16));
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(radioGroup, lp);
    }

    // 初始化热门词汇
    public void initHotWord(String[] keywords) {
        RadioButton radioButton;
        int totalCount = 1;

        if (keywordList == null) keywordList = new LinkedList<>();
        keywordList.clear();
        keywordList.add(new Keyword("热门搜索", Keyword.TYPE_REMIND));

        for (String keyword : keywords) {
            Keyword kw = new Keyword(keyword);
            keywordList.add(kw);
            totalCount += kw.count;
        }
        totalPage = (totalCount - 1) / 12 + 1;      // 总页数
        viewPager.setOffscreenPageLimit(totalPage);
        indexArray = new int[totalPage + 1];

        for (int i = 0; i < totalPage; i++) {
            radioButton = new RadioButton(context);
            radioButton.setId(R.id.radio + i);
            radioButton.setButtonDrawable(0);
            radioButton.setBackgroundResource(R.drawable.bg_ad_index);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(dp2px(8), dp2px(8));
            radioButton.setButtonDrawable(android.R.color.transparent);
            if (i > 0) {
                lp.setMargins(dp2px(8), 0, 0, 0);
            }
            radioGroup.addView(radioButton, lp);
        }
        radioGroup.check(R.id.radio);
        resortHotWord();        // 从新对热门词汇进行排序, 以方便显示

        totalCount = 0;
        int j = 0;
        for (int i = 0; i < keywordList.size(); i++) {
            if (totalCount % 12 == 0) {
                indexArray[j++] = i;
            }
            totalCount += keywordList.get(i).count;
        }
        indexArray[j] = keywordList.size();

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return totalPage;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeViewAt(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View pageView = initPageView(keywordList.subList(indexArray[position],
                        indexArray[position + 1]));
                container.addView(pageView);
                return pageView;
            }
        });
        viewPager.setOnPageChangeListener(this);
    }

    // 将dp转换称px
    private int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5);
    }

    // 重新对热门词汇进行排序
    private void resortHotWord() {
        int index = 0, counts = 0, i;
        for (Keyword keyword : keywordList) {
            counts += keyword.count;
            if (index + 1 < keywordList.size() && counts % 4 == 3 &&
                    keywordList.get(index + 1).count == 2) {
                for (i = index + 2; i < keywordList.size(); i++) {
                    if (keywordList.get(i).count == 1) {
                        Keyword tmp = keywordList.get(index + 1);
                        keywordList.set(index + 1, keywordList.get(i));
                        keywordList.set(i, tmp);
                        break;
                    }
                }
                if (i == keywordList.size()) {   // 后面全是2个, 从前面找出1个的插到最后一个
                    for (i = index; i >= 0; i--) {
                        if (keywordList.get(i).count == 1) {
                            Keyword tmp = keywordList.get(keywordList.size() - 1);
                            keywordList.set(keywordList.size() - 1, keywordList.get(i));
                            keywordList.set(i, tmp);
                            counts++;
                            break;
                        }
                    }
                }
            }
            index++;
        }
    }

    // 初始化ViewPager View
    private View initPageView(List<Keyword> wordList) {
        LinearLayout rootView = new LinearLayout(context);
        LinearLayout rowLayout = rootView;       // 随意初始化值避开IDE空指针提醒
        LinearLayout.LayoutParams itemParams;
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setBaselineAligned(false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int totalCount = 0;
        int dp8 = dp2px(8);

        LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(dp2px(1), LinearLayout
                .LayoutParams.MATCH_PARENT);
        ilp.setMargins(0, dp8, 0, dp8);

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, dp2px(1));
        rlp.setMargins(dp8, 0, dp8, 0);

        for (Keyword keyword : wordList) {              // 填写单元格数据
            if (totalCount % 4 == 0) {
                rowLayout = new LinearLayout(context);  // 新增行
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setWeightSum(4);
                rootView.addView(rowLayout, params);
                View divider = new View(context);
                divider.setBackgroundResource(R.color.background_app);
                rootView.addView(divider, rlp);
//                rowLayout.setOnTouchListener(this);
            }
            TextView textView = new TextView(context);

            // 设置点击反馈背景
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                int[] attrs = new int[]{android.R.attr.selectableItemBackground};
                TypedArray ta = context.obtainStyledAttributes(attrs);
                Drawable drawable = ta.getDrawable(0);
                ta.recycle();
                textView.setBackgroundDrawable(drawable);
            }

            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER);
            textView.setText(keyword.content);
            textView.setTextColor(getResources().getColor(keyword.color));
            textView.setTextSize(14);
            if (keyword.isClickable) {
                textView.setClickable(true);
                textView.setOnClickListener(this);
            }
            itemParams = new LinearLayout.LayoutParams(0, dp2px(40),
                    keyword.count);
            rowLayout.addView(textView, itemParams);
            totalCount += keyword.count;
            if (totalCount % 4 != 0) {
                View divider = new View(context);
                divider.setBackgroundResource(R.color.background_app);
                rowLayout.addView(divider, ilp);
            }
        }
        rootView.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        return rootView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        radioGroup.check(R.id.radio + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        if(v instanceof TextView && itemClickListener != null)
            itemClickListener.onItemClick(((TextView)v).getText().toString());
    }

    public interface OnItemClickListener {
        void onItemClick(String keyword);
    }
}

class Keyword {
    public final static int TYPE_REMIND = 0;        // 提示
    public final static int TYPE_RECOMMEND = 1;     // 推荐

    public String content;
    public int count;
    public boolean isClickable;
    public int color;

    public Keyword(String content) {
        this.content = content;
        this.count = cmpCount(content);
        this.isClickable = true;
        this.color = R.color.secondary_text_dark;
    }

    public Keyword(String content, int type) {
        this.content = content;
        this.count = cmpCount(content);
        this.color = R.color.primary;
        this.isClickable = (type == TYPE_RECOMMEND);
    }

    static int cmpCount(String content) {
        if (content != null && content.getBytes().length > 10 && content.length() > 4) return 2;
        return 1;
    }
}
