package com.htlc.muchong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.muchong.R;

/**
 * Created by sks on 2016/5/19.
 */
public class DaoJiShiView extends LinearLayout {

    private TextView textHour;
    private TextView textMinute;
    private TextView textSecond;
    private TextView textLabel;

    private long millisInFutureStart;
    private long millisInFutureEnd;


    public DaoJiShiView(Context context) {
        this(context, null);
    }

    public DaoJiShiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DaoJiShiView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        setupView();
    }

    private void setupView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_dao_ji_shi_text_view,this,true);
        textLabel = (TextView) findViewById(R.id.textLabel);
        textHour = (TextView) findViewById(R.id.textHour);
        textMinute = (TextView) findViewById(R.id.textMinute);
        textSecond = (TextView) findViewById(R.id.textSecond);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DaoJiShiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setData(long millisInFutureStart, long millisInFutureEnd){
        this.millisInFutureStart = millisInFutureStart;
        this.millisInFutureEnd = millisInFutureEnd;
        new CountDownTimer(millisInFutureStart, 1000) {
            public void onTick(long millisUntilFinished) {
                long hour = millisUntilFinished/3600000;
                long minute = millisUntilFinished%3600000/60000;
                long second = millisUntilFinished%3600000%60000/1000;
                textHour.setText(String.valueOf(hour));
                textMinute.setText(String.valueOf(minute));
                textSecond.setText(String.valueOf(second));

            }
            public void onFinish() {
                startEndTimer();
            }
        }.start();
    }
    private void startEndTimer(){
        textLabel.setText(R.string.first_header_end);
        new CountDownTimer(millisInFutureEnd, 1000) {
            public void onTick(long millisUntilFinished) {
                long hour = millisUntilFinished/3600000;
                long minute = millisUntilFinished%3600000/60000;
                long second = millisUntilFinished%3600000%60000/1000;
                textHour.setText(String.valueOf(hour));
                textMinute.setText(String.valueOf(minute));
                textSecond.setText(String.valueOf(second));

            }
            public void onFinish() {

            }
        }.start();
    }
}
