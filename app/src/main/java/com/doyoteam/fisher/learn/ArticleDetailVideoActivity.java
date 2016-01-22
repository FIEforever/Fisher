package com.doyoteam.fisher.learn;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Image;
import com.doyoteam.fisher.db.bean.Video;
import com.doyoteam.fisher.net.HttpClient;
import com.doyoteam.fisher.net.HttpIn;
import com.doyoteam.fisher.net.learn.ArticleDetailHttpIn;
import com.doyoteam.fisher.net.learn.ArticleDetailHttpOut;
import com.doyoteam.util.ImageLoaderConfig;
import com.doyoteam.util.ImageShowListener;
import com.doyoteam.util.SlideBackAppCompatActivity;
import com.doyoteam.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 文章详情-视频
 * Created by F on 2016/1/16.
 */
public class ArticleDetailVideoActivity extends SlideBackAppCompatActivity implements View.OnClickListener{
    //界面
    private TextView title;
    private ImageView back;
    private WebView wv_web;
    private TextView article_title;
    private RelativeLayout article_like_layout;
    private ImageView article_video_img;
    private ProgressBar pb_loading;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    //数据
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }
    private void initData()
    {
        initWebSetting();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String articleId = getIntent().getStringExtra("ARTICLE_ID");
        getArticleDetailNet(articleId);
    }
    private void initView()
    {
        setContentView(R.layout.acitivity_article_video);
        back = ((ImageView) findViewById(R.id.title_bar_return));
        title = ((TextView) findViewById(R.id.title_bar_title));
        wv_web = (WebView) findViewById(R.id.webview_content);
        article_title = ((TextView) findViewById(R.id.article_title));
        article_like_layout = (RelativeLayout) findViewById(R.id.article_like_layout);
        article_video_img = (ImageView) findViewById(R.id.article_video_img);
        back.setOnClickListener(this);
        title.setText(getString(R.string.learn_artcle_detail_video));
        pb_loading = (ProgressBar) findViewById(R.id.webview_progress);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    private void initWebSetting() {
//        WebSettings webSetting = wv_web.getSettings();
//        webSetting.setJavaScriptEnabled(true);    // 设置使用够执行JS脚本
//        webSetting.setBuiltInZoomControls(true); // 设置使支持缩放
//        webSetting.setDomStorageEnabled(true);
//        webSetting.setAppCacheMaxSize(1024 * 1024 * 8);//设置缓冲大小，设的是8M
//        String appCacheDir = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
//        webSetting.setAppCachePath(appCacheDir);
//        webSetting.setAllowFileAccess(true);
//        webSetting.setAppCacheEnabled(true);
//        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
//        wv_web.addJavascriptInterface(new WebAppInterface(this), "androidApi");
        WebSettings ws = wv_web.getSettings();
        /**
         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
         * setSupportZoom 设置是否支持变焦
         * */
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/com.doyoteam.fisher/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        wv_web.setWebChromeClient(new DefaultWebChromeClient());
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("appapi:")) view.loadUrl(url); // 使用当前WebView处理跳转
                return true;      // true表示此事件在此处被处理，不需要再广播
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            default:
                break;
            case R.id.title_bar_return:
                finish();
                break;
        }
    }

    private void getArticleDetailNet(String articleId)
    {
        final ArticleDetailHttpIn request = new ArticleDetailHttpIn(articleId);
        request.setActionListener(new HttpIn.ActionListener<ArticleDetailHttpOut>() {
            @Override
            public void onSuccess(ArticleDetailHttpOut result) {
                String status = result.getStatus();
                if (status.equals("SUCCESS")) {
                    setArticleDetailData(result.getArticle(),result.getImageArrayList(),result.getVideo());
                } else {
                    Tools.showToast(status);
                }
            }

            @Override
            public void onFailure(String errInfo) {
                Tools.showToast(errInfo);
            }
        });
        HttpClient.get(request);
    }
    private void setArticleDetailData(Article article,ArrayList<Image> imageArrayList,Video video)
    {
        article_title.setText(article.title);
        article_like_layout.setVisibility(View.VISIBLE);
        article_video_img.setVisibility(View.VISIBLE);
        final String url = video.url;
        wv_web.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage(article.litpic, article_video_img,
                ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                new ImageShowListener());
        article_video_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv_web.loadUrl(url);
                article_video_img.setVisibility(View.GONE);
                wv_web.setVisibility(View.VISIBLE);
            }
        });
        Tools.Adapter adapter = new Tools.Adapter(getSupportFragmentManager());
        adapter.addFragment(ArticleDetailFragment.newInstance(article, imageArrayList), "视频简介");
        adapter.addFragment(ArticleDetailFragment.newInstance(article, imageArrayList), "相关视频");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        if(video!=null)
        {
//            wv_web.loadUrl(video.url);
        }
        else
        {
            Tools.showToast("暂无视频");
        }
//        tabLayout.setTabTextColors(R.color.secondary_text_dark,R.color.primary);

    }

    private class DefaultWebChromeClient extends WebChromeClient {
        // 一个回调接口使用的主机应用程序通知当前页面的自定义视图已被撤职
        CustomViewCallback customViewCallback;
        // 进入全屏的时候
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // 赋值给callback
            customViewCallback = callback;
            // 设置webView隐藏
            wv_web.setVisibility(View.GONE);
            // 声明video，把之后的视频放到这里面去
            FrameLayout video = (FrameLayout) findViewById(R.id.video_full_view);
            // 将video放到当前视图中
            video.addView(view);
            // 横屏显示
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // 设置全屏
            setFullScreen();
        }
        // 退出全屏的时候
        @Override
        public void onHideCustomView() {
            if (customViewCallback != null) {
                // 隐藏掉
                customViewCallback.onCustomViewHidden();
            }
            // 用户当前的首选方向
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            // 退出全屏
            quitFullScreen();
            // 设置WebView可见
            wv_web.setVisibility(View.VISIBLE);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (10 < progress && progress < 90 && pb_loading.getVisibility() != View.VISIBLE)
                pb_loading.setVisibility(View.VISIBLE);
            else if (progress > 90 && pb_loading.getVisibility() != View.GONE)
                pb_loading.setVisibility(View.GONE);
        }
        /**
         * 设置全屏
         */
        private void setFullScreen() {
            // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // 全屏下的状态码：1098974464
            // 窗口下的状态吗：1098973440
        }

        /**
         * 退出全屏
         */
        private void quitFullScreen() {
            // 声明当前屏幕状态的参数并获取
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}