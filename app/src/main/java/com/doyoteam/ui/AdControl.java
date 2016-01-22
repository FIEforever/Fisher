package com.doyoteam.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Ad;
import com.doyoteam.util.ImageLoaderConfig;
import com.doyoteam.util.ImageShowListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告控制器
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version Post 210
 *          Datetime 2015-11-10 14:29
 *          Copyright 2015 东莞市邮政公司电子商务分局 All rights reserved.
 * @since Post 1.0
 */
public class AdControl extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private Context context;
    private List<Ad> mAdList;

    private PagerAdapter mAdapter;  // View Pager的Adapter
    private ViewPager viewPager;    // 用于左右滑动的广告
    private RadioGroup radioGroup;  // 用户广告页面的指示

    private Timer timer;
    private TimerTask timerTask;
    private Runnable time = new Runnable() {
        @Override
        public void run() {
            isManual = false;
            adPosition++;
            viewPager.setCurrentItem(adPosition % mAdList.size(), true);
        }
    };

    private static int adPosition = 0;
    private boolean isManual;

    public AdControl(Context context) {
        super(context);
        init(context);
    }

    public AdControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 广告控件初始化
    private void init(Context context) {
        this.context = context;
        this.isManual = false;
        this.viewPager = new ViewPager(context);
        this.radioGroup = new RadioGroup(context);
        this.mAdList = new ArrayList<>();

        mAdapter = new AdAdapter(context, mAdList);
        viewPager.setAdapter(mAdapter);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        addView(viewPager, lp);

        lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        lp.setMargins(0, 0, 0, dp2px(8));           // 8dp
        lp.alignWithParent = true;
        lp.getRules()[CENTER_HORIZONTAL] = TRUE;
        lp.getRules()[ALIGN_PARENT_BOTTOM] = TRUE;
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        addView(radioGroup, lp);
    }

    // 将dp转换称px
    private int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5);
    }

    // 广告内容初始化
    public void initData(List<Ad> adList, float ratio) {
        this.mAdList.clear();
        this.radioGroup.removeAllViews();
        this.mAdList.addAll(adList);

        for (int i = 0; i < mAdList.size(); i++) {
            RadioButton rb = new RadioButton(context);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(dp2px(8), dp2px(8));
            if (i > 0) lp.setMargins(dp2px(8), 0, 0, 0);
            else rb.setChecked(true);
            rb.setId(R.id.ad_index_base + i);
            rb.setButtonDrawable(android.R.color.transparent);
            rb.setBackgroundResource(R.drawable.bg_ad_index);
            radioGroup.addView(rb, lp);
        }

        if (ratio > 0) {
            getLayoutParams().width = LayoutParams.MATCH_PARENT;
            getLayoutParams().height = (int) (getResources().getDisplayMetrics().widthPixels /
                    ratio + 0.5f);
            invalidate();
        }

//        adPosition = 0;

        viewPager.setCurrentItem(adPosition % mAdList.size(), true);
        viewPager.setOnPageChangeListener(this);
        mAdapter.notifyDataSetChanged();

        if (adList.size() > 0) {
            radioGroup.check(R.id.ad_index_base + adPosition % mAdList.size());
            startTimer();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        adPosition = position;
        radioGroup.check(R.id.ad_index_base + position);
        if (isManual) {
            pauseTimer();
            startTimer();
        }
        isManual = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    // 开始计时器
    public void startTimer() {
        if (timer == null) {
            timer = new Timer("PLAY_AD", false);
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(time);
            }
        };
        timer.schedule(timerTask, 5000, 5000);
    }

    // 停止计时器
    public void stopTimer() {
        timerTask.cancel();
        timer.cancel();
    }

    private void pauseTimer() {
        timerTask.cancel();
    }
}

class AdAdapter extends PagerAdapter {

    private Context mContext;
    private List<Ad> mAdList;

    public AdAdapter(Context context, List<Ad> adList) {
        this.mContext = context;
        this.mAdList = adList;
    }

    @Override
    public int getCount() {
        return mAdList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        if (mAdList.get(position).url == null || mAdList.get(position).url.length() == 0)
            imageView.setImageResource(mAdList.get(position).id);
        else
            ImageLoader.getInstance().displayImage(mAdList.get(position).url, imageView,
                    ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_AD).getDisplayImageOptions(),
                    new ImageShowListener());

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setTag(position);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Ad ad = mAdList.get(position);
//                            Intent intent;
                switch (ad.action) {
                    case 0:
                        // 搜索团购详情页面
//                                    intent = new Intent(context, WebViewActivity.class);
//                                    intent.putExtra("action_url", ad.keyWord);
//                                    intent.putExtra("action_name", ad.name);
//                                    startActivity(intent);
                        break;
                    case 1:
                        // 搜索团购详情页面
//                                    intent = new Intent(context, SearchGroupActivity.class);
//                                    intent.putExtra("action_keyword", ad.keyWord);
//                                    startActivity(intent);
                        break;
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }
}
