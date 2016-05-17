package com.larno.util.okhttp.support;

import android.content.Context;

import com.larno.util.NetworkUtil;
import com.larno.util.okhttp.Log;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sks on 2016/5/12.
 */
public class DefaultInterceptor implements Interceptor{
    private Context context;
    public DefaultInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
