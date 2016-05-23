package com.htlc.muchong.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sks on 2016/5/23.
 */
public class DateFormat {
    public static String getMonthAndDay(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        return dateFormat.format(date);
    }
}
