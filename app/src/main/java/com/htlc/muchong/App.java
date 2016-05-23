package com.htlc.muchong;

import android.app.Application;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.htlc.muchong.util.Constant;
import com.larno.util.okhttp.Log;
import com.squareup.picasso.Picasso;

import core.AppAction;
import core.AppActionImpl;

/**
 * Created by sks on 2016/5/13.
 */
public class App extends Application {
    public static App app;
    public AppAction appAction;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appAction = new AppActionImpl(this);
        initPicasso();
        initBugTags();
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
        Bugtags.start(Constant.BugTags_App_Key, this, Bugtags.BTGInvocationEventBubble, options);
    }

    private void initPicasso() {
        Picasso picasso = Picasso.with(this);
        picasso.setIndicatorsEnabled(Log.debug);
        picasso.setLoggingEnabled(Log.debug);
    }
}
