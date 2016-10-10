package com.larno.util.okhttp;

/**
 * Created by zhy on 15/11/6.
 */
public class Log {
    public static boolean debug = true;

    public static void e(String msg) {
        if (debug) {
            android.util.Log.e("OkHttp-----", msg);
        }
    }

    public static void setDebug(boolean debug) {
        Log.debug = debug;
    }
}

