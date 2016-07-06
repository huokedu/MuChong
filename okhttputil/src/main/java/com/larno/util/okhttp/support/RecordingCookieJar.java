package com.larno.util.okhttp.support;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public final class RecordingCookieJar implements CookieJar {
    private final Deque<List<Cookie>> requestCookies = new ArrayDeque<>();
    private final Deque<List<Cookie>> responseCookies = new ArrayDeque<>();


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        responseCookies.add(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (responseCookies.isEmpty()) return Collections.emptyList();
//        if(responseCookies.size()>1){
//            return responseCookies.removeFirst();
//        }
        return responseCookies.getLast();
    }
}