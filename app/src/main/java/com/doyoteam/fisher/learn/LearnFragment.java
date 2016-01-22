package com.doyoteam.fisher.learn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.net.HttpClient;
import com.doyoteam.fisher.net.HttpIn;
import com.doyoteam.fisher.net.learn.LearnHttpIn;
import com.doyoteam.fisher.net.learn.LearnHttpOut;
import com.doyoteam.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 钓场
 */
public class LearnFragment extends Fragment implements View.OnClickListener {
    private List<Fragment> fragmentList;
    private ViewPager viewPager;//显示页面的容器
    private View view_tab_bar;
    private View rootView;
    private RadioButton[] radioButtonArray;
    private ImageView leanrn_search_iv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentList = new ArrayList<>();
        fragmentList.add(LearnSubFragment.newInstance(Article.NEW));
        fragmentList.add(LearnSubFragment.newInstance(Article.ABC));
        fragmentList.add(LearnSubFragment.newInstance(Article.SKILL));
        fragmentList.add(LearnSubFragment.newInstance(Article.BAIT));
        fragmentList.add(LearnSubFragment.newInstance(Article.VIDEO));
        radioButtonArray = new RadioButton[5];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (rootView != null) {
            ViewGroup parentView = (ViewGroup) rootView.getParent();
            if (parentView != null) {
                parentView.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_learn, container, false);
            leanrn_search_iv = (ImageView) rootView.findViewById(R.id.leanrn_search_iv);
            leanrn_search_iv.setOnClickListener(this);
            RadioGroup rg_tab_host = (RadioGroup) rootView.findViewById(R.id.tab_host);
            view_tab_bar = rootView.findViewById(R.id.tab_bar);
            rg_tab_host.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.tab_bar1:
                            viewPager.setCurrentItem(0, false);
                            break;
                        case R.id.tab_bar2:
                            viewPager.setCurrentItem(1, false);
                            break;
                        case R.id.tab_bar3:
                            viewPager.setCurrentItem(2, false);
                            break;
                        case R.id.tab_bar4:
                            viewPager.setCurrentItem(3, false);
                            break;
                        case R.id.tab_bar5:
                            viewPager.setCurrentItem(4, false);
                            break;
                    }
                }
            });

            radioButtonArray[0] = (RadioButton) rootView.findViewById(R.id.tab_bar1);
            radioButtonArray[1] = (RadioButton) rootView.findViewById(R.id.tab_bar2);
            radioButtonArray[2] = (RadioButton) rootView.findViewById(R.id.tab_bar3);
            radioButtonArray[3] = (RadioButton) rootView.findViewById(R.id.tab_bar4);
            radioButtonArray[4] = (RadioButton) rootView.findViewById(R.id.tab_bar5);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view_tab_bar
                    .getLayoutParams();
            lp.width = (getResources().getDisplayMetrics().widthPixels-leanrn_search_iv.getLayoutParams().width) / fragmentList.size();
            view_tab_bar.setLayoutParams(lp);
            viewPager = (ViewPager) rootView.findViewById(R.id.fragment_view_pager);
            viewPager.setOffscreenPageLimit(fragmentList.size());
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int
                        positionOffsetPixels) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view_tab_bar
                            .getLayoutParams();
                    lp.leftMargin = (int) ((position + positionOffset) * lp.width);
                    view_tab_bar.requestLayout();
                }
                @Override
                public void onPageSelected(int position) {
                    check(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {

                @Override
                public int getCount() {
                    return fragmentList.size();
                }

                @Override
                public Fragment getItem(int position) {
                    return fragmentList.get(position);
                }
            });
            check(0);
        }

        return rootView;
    }

    private void check(int position) {
        radioButtonArray[position].setChecked(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
    private void setArticleFragment()
    {

    }
}