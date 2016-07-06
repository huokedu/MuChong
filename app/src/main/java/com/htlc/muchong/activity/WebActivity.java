package com.htlc.muchong.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import api.Api;
import okhttp3.OkHttpClient;

/**
 * Created by sks on 2016/6/16.
 */
public class WebActivity extends BaseActivity {
    public static final String URL = "URL";
    private JavaScriptBridge javaScriptBridge;

    public static void goWebActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ActivityTitle, title);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    private String title;
    private String url;

    private WebView webView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void setupView() {
        title = getIntent().getStringExtra(ActivityTitle);
        url = getIntent().getStringExtra(URL);

        mTitleTextView.setText(title);
        webView = (WebView) findViewById(R.id.webView);

        initWebView();
        initData();
    }

    private void initWebView() {
        webView.getSettings().setSupportZoom(true);          //支持缩放
        webView.getSettings().setBuiltInZoomControls(true);  //启用内置缩放装置
        webView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDisplayZoomControls(false);//隐藏缩放图标
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//缓存
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); //支持内容重新布局
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//不显示滚动条
        webView.getSettings().setDefaultTextEncodingName("UTF -8");
        //运行js代码
        webView.getSettings().setJavaScriptEnabled(true);
        javaScriptBridge = new JavaScriptBridge();
        webView.addJavascriptInterface(javaScriptBridge, "bridge");
        //先不加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        /*WebViewClient主要帮助WebView处理各种通知、请求事件的，比如：
            onLoadResource
            onPageStart
            onPageFinish
            onReceiveError
            onReceivedHttpAuthRequest*/

//        url = Api.TestISpring;
        String username = String.valueOf(Math.random());
        CookieManager.getInstance().setCookie(Api.TestISpringPost, "username="+username);


        webView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                webView.loadData("<h1>这是Larno的博客</h1>", "text/html; charset=UTF-8", null);
                return super.shouldOverrideUrlLoading(view,url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
//                webView.getTitle();
            }
        });
          /*WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等比如
            onCloseWindow(关闭WebView)
            onCreateWindow()
            onJsAlert (WebView上alert无效，需要定制WebChromeClient处理弹出)
            onJsPrompt
            onJsConfirm
            onProgressChanged
            onReceivedIcon
            onReceivedTitle*/
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                setProgress(newProgress);
            }
        });
    }

    @Override
    protected void initData() {
        HashMap<String, String> headers = new HashMap<>();
//        headers.put("username", "username");
//        headers.put("password", "password");
        webView.loadUrl(url, headers);
    }

    /*与HTML交互接口*/
    @SuppressLint("NewApi")
    public class JavaScriptBridge {
        /**
         * 定义接口提供给js调用
         */
        @JavascriptInterface
        public void invokeJavaByJs() {

        }

        public void invokeJsByJava() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript: pause()");
                }
            });
        }
    }
}
