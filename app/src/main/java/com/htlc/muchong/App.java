package com.htlc.muchong;

import android.app.Application;

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
    }

    private void initPicasso() {
        Picasso picasso = Picasso.with(this);
        picasso.setIndicatorsEnabled(Log.debug);
        picasso.setLoggingEnabled(Log.debug);
    }
}
