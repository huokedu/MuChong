package com.htlc.muchong.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LogUtils;
import com.htlc.muchong.widget.ProgressWebView;

/**
 * Created by John_Libo on 2016/9/4.
 */
public class BrowserActivity extends BaseActivity {
    protected ProgressWebView mWebView;
    protected String strUrl;
    protected String title;
    protected String id;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        LogUtils.e("onCreate---","onCreate");
//        Intent mIntent = getIntent();
//        if (mIntent != null) {
//            strUrl = mIntent.getBundleExtra("bundle").getString("url");
//            title = mIntent.getBundleExtra("bundle").getString("title");
//            LogUtils.e("strUrl---", ""+strUrl);
//
//        }
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.browser;
    }

    @Override
    protected void setupView() {
        LogUtils.e("initView---","initView");
        Intent mIntent = getIntent();
        if (mIntent != null) {
            strUrl = mIntent.getExtras().getString("url");
            LogUtils.e("strUrl---", ""+strUrl);

        }
        mWebView = (ProgressWebView) findViewById(R.id.browser_webview);
        mWebView.setWebViewClient(new MyWebViewClient());
        mTitleTextView.setText(R.string.fifth_th);
//        if (!TextUtils.isEmpty(title)) {
//        }else {
//
//            mWebView.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageFinished(WebView view, String url) {
////                    BrowserActivity.this.setTitle(view.getTitle());
//                }
//            });
//        }

        initData();

    }



    @Override
    public void initData() {
        LogUtils.e("initData---","initData");
        if(null!=strUrl){
            mWebView.loadUrl(strUrl);
        }


    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyWebViewClient extends WebViewClient {
        // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } else {
                view.loadUrl(url);
            }
            // 如果不需要其他对点击链接事件的处理返回true，否则返回false
            return true;
        }
    }
}
