package com.larno.util.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.larno.util.okhttp.callback.ResultCallback;
import com.larno.util.okhttp.support.DefaultInterceptor;
import com.larno.util.okhttp.support.RecordingCookieJar;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpClientManager {

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private static Context mContext;
    private Handler mDelivery;
    private Cache mCache;
    private static final long CACHE_SIZE = 10 * 1024 * 1024;

    private OkHttpClientManager() {
        File cacheFileDir = mContext.getExternalCacheDir();
        mCache = new Cache(cacheFileDir.getAbsoluteFile(), CACHE_SIZE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(7676, TimeUnit.MILLISECONDS);
        builder.connectTimeout(7676, TimeUnit.MILLISECONDS);
        builder.cookieJar(new RecordingCookieJar());
        builder.addInterceptor(new DefaultInterceptor(mContext));
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        builder.cache(mCache);
        mOkHttpClient = builder.build();
        mDelivery = new Handler(Looper.getMainLooper());


    }

    public static void init(Context context, boolean isDebug) {
        if (context == null) {
            throw new IllegalArgumentException("Context not be null");
        }
        if (mContext == null) {
            synchronized (OkHttpClientManager.class) {
                if (mContext == null) {
                    mContext = context.getApplicationContext();
                    Log.setDebug(isDebug);
                }
            }
        }
    }

    public static OkHttpClientManager getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Before getInstance(), You must be invoke init(Context context).");
        }
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public void execute(final Request request, ResultCallback callback) {
        if (callback == null) callback = ResultCallback.DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        resCallBack.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback(call.request(), e, resCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (Log.debug) {
                    Response response1 = response.cacheResponse();
                    Response response2 = response.networkResponse();
                    Log.e((response1 == null) + " cache ? network " + (response2 == null)+" response is network "+ (response == response2));
                }
                if (response.code() >= 400 && response.code() <= 599) {
                    sendFailResultCallback(call.request(), new RuntimeException(response.body().string()), resCallBack);
                    return;
                }
                final String string = response.body().string();
                sendSuccessResultCallback(string, resCallBack);
            }
        });
    }

    public String execute(Request request) throws IOException {
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        String string = execute.body().string();
        return string;
    }


    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final String object, final ResultCallback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }


    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
    }


}

