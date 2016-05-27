package com.htlc.muchong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.muchong.R;

/**
 * Created by sks on 2016/5/19.
 */
public class NumberPicker extends LinearLayout {
    public static final int DefaultNumber = 1;

    private Button buttonPlus;
    private Button buttonMinus;
    private TextView textNumber;

    private int maxNumber = DefaultNumber;
    private int minNumber = DefaultNumber;
    private OnNumberChangedListener onNumberChangedListener;
    public interface OnNumberChangedListener{
        void onNumberChanged(int number);
        void onGreaterThanMax();
        void onLessThanMin();
    }
    public void setOnNumberChangedListener(OnNumberChangedListener onNumberChangedListener) {
        this.onNumberChangedListener = onNumberChangedListener;
    }

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
        buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonPlus = (Button) findViewById(R.id.buttonPlus);
        textNumber = (TextView) findViewById(R.id.textNumber);
        textNumber.setText(String.valueOf(DefaultNumber));
        buttonMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(textNumber.getText().toString());
                if (number > minNumber) {
                    textNumber.setText(String.valueOf(--number));
                    if (onNumberChangedListener != null) {
                        onNumberChangedListener.onNumberChanged(number);
                    }
                } else {
                    if (onNumberChangedListener != null) {
                        onNumberChangedListener.onLessThanMin();
                    }
                }
            }
        });
        buttonPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(textNumber.getText().toString());
                if (number < maxNumber) {
                    textNumber.setText(String.valueOf(++number));
                    if (onNumberChangedListener != null) {
                        onNumberChangedListener.onNumberChanged(number);
                    }
                } else {
                    if (onNumberChangedListener != null) {
                        onNumberChangedListener.onGreaterThanMax();
                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }
    public int getNumber(){
        int number = Integer.parseInt(textNumber.getText().toString());
        return number;
    }
    public void setNumber(int number){
        if(number>=minNumber && number <= maxNumber){
            textNumber.setText(String.valueOf(number));
        }
    }
}
