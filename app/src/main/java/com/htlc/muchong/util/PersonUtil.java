package com.htlc.muchong.util;

import android.widget.TextView;

/**
 * Created by sks on 2016/6/3.
 */
public class PersonUtil {
    public static void setPersonLevel(TextView textView, String level) {
        textView.setText("等级: " + level);
    }
}
