package com.htlc.muchong.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.htlc.muchong.R;


@SuppressLint("SetJavaScriptEnabled")
public class ProgressWebView extends LinearLayout {
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private onReceivedTitleListener listener;
    private boolean isLoadCompleted;
    public ProgressWebView(Context context) {
        super(context);
    }
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_progress_webview, this);
        mProgressBar=(ProgressBar) findViewById(R.id.widget_progress_webview_pb);
        mWebView=(WebView)findViewById(R.id.widget_progress_webview);
        mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
    }
    public void setWebViewClient(WebViewClient client){
    	mWebView.setWebViewClient(client);
    }
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
            	mProgressBar.setVisibility(GONE);
                isLoadCompleted = true;
            } else {
                if (mProgressBar.getVisibility() == GONE){
                    mProgressBar.setVisibility(VISIBLE);
                    isLoadCompleted = false;
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
        	if(listener!=null){
        		listener.onReceivedTitle(view, title);
            }
            super.onReceivedTitle(view, title);
        }
    }

    public void loadUrl(String url){
    	mWebView.loadUrl(url);
    }
    public boolean canGoBack(){
    	return mWebView.canGoBack();
    }
    public void goBack(){
    	mWebView.goBack();
    }
    public void setOnReceivedTitleListener(onReceivedTitleListener listener){
    	this.listener = listener;
    }
    public interface onReceivedTitleListener{
    	public void onReceivedTitle(WebView view, String title);
    }
    @SuppressLint({"AddJavascriptInterface", "JavascriptInterface"})
    public void addJavascriptInterface(Object object,String name){
        mWebView.addJavascriptInterface(object,name);
    }
    public boolean getIsLoadCompleted(){
        return this.isLoadCompleted;
    }
}