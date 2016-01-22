package com.doyoteam.fisher;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doyoteam.fisher.add.MoreWindow;
import com.doyoteam.fisher.home.HomeFragment;
import com.doyoteam.fisher.learn.LearnFragment;
import com.doyoteam.fisher.mall.MallFragment;
import com.doyoteam.fisher.user.UserFragment;
import com.doyoteam.util.Tools;

/**
 * 主程序
 */
public class MainActivity extends FragmentActivity {

    private MoreWindow mMoreWindow;
    private FragmentTabHost mTabHost = null;
    private ImageView bottom_bar_add_iv;
    private View indicator = null;
    private boolean isExit = false;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            isExit = false;
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView()
    {
        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        // 添加tab名称和图标
        View indicator = getIndicatorView(R.string.tab_home, R.drawable.bottom_bar_home, R
                .layout.indicator_tabwidget);
        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.tab_home)).setIndicator
                (indicator), HomeFragment.class, null);

        indicator = getIndicatorView(R.string.tab_mall, R.drawable.bottom_bar_mall, R.layout
                .indicator_tabwidget);
        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.tab_mall)).setIndicator(indicator)
                , MallFragment.class, null);

        indicator = getIndicatorView(R.string.tab_add, R.drawable.bottom_bar_add, R.layout
                .indicator_tabwidget);
        indicator.setVisibility(View.INVISIBLE);
        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.tab_mall)).setIndicator(indicator)
                , MallFragment.class, null);

        indicator = getIndicatorView(R.string.tab_learn, R.drawable.bottom_bar_learn, R.layout
                .indicator_tabwidget);
        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.tab_learn)).setIndicator(indicator)
                , LearnFragment.class, null);

        indicator = getIndicatorView(R.string.tab_user, R.drawable.bottom_bar_user, R.layout
                .indicator_tabwidget);
        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.tab_user)).setIndicator(indicator)
                , UserFragment.class, null);

        check(DataModel.getInstance().getWhichTab());

        bottom_bar_add_iv = (ImageView)findViewById(R.id.bottom_bar_add_iv);
        bottom_bar_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreWindow(v);
            }
        });
    }
    // 获取底部导航栏视图
    private View getIndicatorView(int stringId, int drawableId, int layoutId) {
        View v = getLayoutInflater().inflate(layoutId, null);
        TextView tv = (TextView) v;
        Drawable drawable = getResources().getDrawable(drawableId);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        tv.setText(stringId);
        return v;
    }
    //中间添加按钮
    private void showMoreWindow(View view) {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init();
        }
        mMoreWindow.showMoreWindow(view, R.dimen.bottom_bar_height);
    }
    //设置默认启动tab
    public void check(int position) {
        mTabHost.setCurrentTab(position);
    }

    @Override
    public void onBackPressed() {
        if(isExit){
            super.onBackPressed();
            finish();
            System.exit(0);
        }else{
            isExit = true;
            Tools.showToast("再按一次退出程序");
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }
}
