package com.htlc.muchong;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.htlc.muchong.util.Constant;
import com.larno.util.okhttp.Log;
import com.larno.util.okhttp.OkHttpClientManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import core.AppAction;
import core.AppActionImpl;

/**
 * Created by sks on 2016/5/13.
 */
public class App extends Application {
    public static App app;
    public AppAction appAction;
    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appAction = new AppActionImpl(this);
        initHttp();
        initPicasso();
        initBugTags();
        LeakCanary.install(this);
    }

    private void initHttp() {
        OkHttpClientManager.init(this,false);
    }

    private void initBugTags() {
        BugtagsOptions options = new BugtagsOptions.Builder().
                trackingLocation(true).//是否获取位置，默认 true
                trackingCrashLog(true).//是否收集crash，默认 true
                trackingConsoleLog(true).//是否收集console log，默认 true
                trackingUserSteps(true).//是否收集用户操作步骤，默认 true
                trackingNetworkURLFilter("(.*)").//自定义网络请求跟踪的 url 规则，默认 null
                build();
        //BTGInvocationEventNone BTGInvocationEventShake   BTGInvocationEventBubble
        Bugtags.start(Constant.BugTags_App_Key, this, Bugtags.BTGInvocationEventNone, options);
    }

    private void initPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
//        builder.addRequestHandler(new RequestHandler() {
//            @Override
//            public boolean canHandleRequest(Request data) {
//                return false;
//            }
//
//            @Override
//            public Result load(Request request, int networkPolicy) throws IOException {
//                return null;
//            }
//        });
//        builder.downloader(new OkHttpDownloader(this));
//        builder.listener(new Picasso.Listener() {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//
//            }
//        });
        builder.defaultBitmapConfig(Bitmap.Config.ALPHA_8);
        builder.indicatorsEnabled(Log.debug);
        builder.loggingEnabled(Log.debug);
//        builder.memoryCache(new LruCache(this));
//        builder.requestTransformer(new Picasso.RequestTransformer() {
//            @Override
//            public Request transformRequest(Request request) {
//                return null;
//            }
//        });
       Picasso.setSingletonInstance(builder.build());
    }
}
