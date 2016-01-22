package com.doyoteam.fisher.learn;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.util.SlideBackActivity;

/**
 * 发现页面界面控制器
 */
public class WebViewActivity extends SlideBackActivity implements View.OnClickListener {

    private Thread mUiThread;
    private WebView wv_web;
    private ProgressBar pb_loading;
    private TextView tv_title;
    private TextView tv_titlebar_action;
    private Handler handler = new Handler();

    private String title;
    private String url;
    //网页类型（标题栏控制）：1默认、2订单编辑
    private String webViewMode = Constants.WEBVIEW_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initUI();
        combine();
    }

    private void initData() {
        mUiThread = Thread.currentThread();
        title = getIntent().getStringExtra("action_name");
        url = getIntent().getStringExtra("action_url");
        webViewMode = getIntent().getStringExtra("webViewMode");
        if (webViewMode == null) {
            webViewMode = Constants.WEBVIEW_NORMAL;
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_webview);
        tv_title = (TextView) findViewById(R.id.title_bar_title);
        tv_title.setText(R.string.activity_web_view_null);
        tv_titlebar_action = (TextView) findViewById(R.id.title_bar_action_text);
        wv_web = (WebView) findViewById(R.id.webview_content);
        pb_loading = (ProgressBar) findViewById(R.id.webview_progress);
//        if (webViewMode.equals(Constants.WEBVIEW_WIKI_DETAIL)) {
//            tv_titlebar_action.setText(getResources().getText(R.string.activity_web_view_share));
//            tv_titlebar_action.setVisibility(View.VISIBLE);
//            tv_titlebar_action.setOnClickListener(this);
//        }
    }

    private void combine() {
        if (title != null && title.length() > 0) tv_title.setText(title);
        findViewById(R.id.title_bar_return).setOnClickListener(this);
        WebSettings webSetting = wv_web.getSettings();
        webSetting.setJavaScriptEnabled(true);    // 设置使用够执行JS脚本
        webSetting.setBuiltInZoomControls(true); // 设置使支持缩放
        webSetting.setDomStorageEnabled(true);
        webSetting.setAppCacheMaxSize(1024 * 1024 * 8);//设置缓冲大小，设的是8M
        String appCacheDir = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webSetting.setAppCachePath(appCacheDir);
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
//        wv_web.addJavascriptInterface(new WebAppInterface(this), "androidApi");

        wv_web.loadUrl(url);
        wv_web.setWebChromeClient(new DefaultWebChromeClient());
//        wv_web.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int progress) {
//                if (10 < progress && progress < 90 && pb_loading.getVisibility() != View.VISIBLE)
//                    pb_loading.setVisibility(View.VISIBLE);
//                else if (progress > 90 && pb_loading.getVisibility() != View.GONE)
//                    pb_loading.setVisibility(View.GONE);
//            }
//
//        });
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("appapi:")) view.loadUrl(url); // 使用当前WebView处理跳转
                return true;      // true表示此事件在此处被处理，不需要再广播
            }
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
////                StringBuilder builder = new StringBuilder();
////                builder.append("javascript:window.id=");
////                builder.append(action_id);
////                view.loadUrl(builder.toString());
//            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_return:
                if (wv_web.canGoBack()) {
                    wv_web.goBack();
//                    if (!titleStack.empty()) tv_title.setText(titleStack.pop());
                } else finish();
                break;
            case R.id.title_bar_action_text:
                titleBarAction();
                break;
            default:
                break;
        }
    }

    private void titleBarAction() {
    }

    @Override
    // 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_web.canGoBack()) {
            wv_web.goBack();
//            if (!titleStack.empty()) tv_title.setText(titleStack.pop());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        // 设置标题
        @JavascriptInterface
        public void setTitle(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_title.setText(title);
                }
            });
        }
        public final void runOnUiThread(Runnable action) {
            if (Thread.currentThread() != mUiThread) {
                handler.post(action);
            } else {
                action.run();
            }
        }
    }
    @Override
    public void onPause() {// 继承自Activity
        super.onPause();
        wv_web.onPause();
    }

    @Override
    public void onResume() {// 继承自Activity
        super.onResume();
        wv_web.onResume();
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
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
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
