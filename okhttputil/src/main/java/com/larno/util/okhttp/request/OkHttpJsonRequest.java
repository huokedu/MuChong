package com.larno.util.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by sks on 2016/5/12.
 */
public class OkHttpJsonRequest extends OkHttpPostRequest {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public OkHttpJsonRequest(String url, Object tag, Map<String, String> headers, String content, String cache) {
        super(url, tag, null, headers, JSON, null, null, null, cache);
    }
}
