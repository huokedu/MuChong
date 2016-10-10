package com.htlc.muchong.util;

import android.util.Log;

import com.htlc.muchong.App;

/**
 * Log调试工具
 *
 */
public class LogUtils {
    public static final String TAG = "tag";

    public static void i(String msg) {
        if (App.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (App.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (App.DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (App.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (App.DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (App.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (App.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (App.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (App.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (App.DEBUG) {
            Log.w(tag, msg);
        }
    }
}
