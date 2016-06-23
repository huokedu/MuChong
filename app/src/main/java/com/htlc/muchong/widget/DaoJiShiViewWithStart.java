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
public class DaoJiShiViewWithStart extends DaoJiShiView {
    public DaoJiShiViewWithStart(Context context) {
        this(context, null);
    }

    public DaoJiShiViewWithStart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DaoJiShiViewWithStart(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        setupView();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DaoJiShiViewWithStart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setData(long millisInFutureStart, long millisInFutureEnd){
        this.millisInFutureStart = millisInFutureStart;
        this.millisInFutureEnd = millisInFutureEnd;
        if(startTimer!=null){
            startTimer.cancel();
        }
        if(endTimer!=null){
            endTimer.cancel();
        }
        if(millisInFutureStart>0){
            startStartTimer();
        }else {
            startEndTimer();
        }

    }

    /**
     * 开始倒计时
     */
    private void startStartTimer() {
        textLabel.setText(R.string.first_header_qiang);
        linearTime.setVisibility(INVISIBLE);
        startTimer = new CountDownTimer(millisInFutureStart, T) {
            public void onTick(long millisUntilFinished) {
                long hour = millisUntilFinished / 3600000;
                long minute = millisUntilFinished % 3600000 / 60000;
                long second = millisUntilFinished % 3600000 % 60000 / 1000;
                if(hour<10){
                    textHour.setText(String.format("0%d",hour));
                }else {
                    textHour.setText(String.valueOf(hour));
                }
                if(minute<10){
                    textMinute.setText(String.format("0%d", minute));
                }else {
                    textMinute.setText(String.valueOf(minute));
                }
                if(second<10){
                    textSecond.setText(String.format("0%d", second));
                }else {
                    textSecond.setText(String.valueOf(second));
                }
            }

            public void onFinish() {
                startEndTimer();
            }
        };
        startTimer.start();
    }

    /**
     * 结束倒计时
     */
    private void startEndTimer(){
        textLabel.setText(R.string.first_header_qiang);
        linearTime.setVisibility(VISIBLE);
        endTimer = new CountDownTimer(millisInFutureEnd, T) {
            public void onTick(long millisUntilFinished) {
                long hour = millisUntilFinished / 3600000;
                long minute = millisUntilFinished % 3600000 / 60000;
                long second = millisUntilFinished % 3600000 % 60000 / 1000;
                if(hour<10){
                    textHour.setText(String.format("0%d",hour));
                }else {
                    textHour.setText(String.valueOf(hour));
                }
                if(minute<10){
                    textMinute.setText(String.format("0%d", minute));
                }else {
                    textMinute.setText(String.valueOf(minute));
                }
                if(second<10){
                    textSecond.setText(String.format("0%d", second));
                }else {
                    textSecond.setText(String.valueOf(second));
                }
            }

            public void onFinish() {
                textLabel.setText(R.string.first_header_qiang);
                textHour.setText(String.valueOf("00"));
                textMinute.setText(String.valueOf("00"));
                textSecond.setText(String.valueOf("00"));
            }
        };
        endTimer.start();
    }
}
