package com.htlc.muchong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.muchong.R;

/**
 * Created by sks on 2016/5/19.
 */
public class NumberPicker extends LinearLayout {

    private Button buttonPlus;
    private Button buttonMinus;
    private TextView textNumber;


    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        setupView();
    }

    private void setupView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_number_picker,this,true);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }
}
