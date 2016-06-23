package com.htlc.muchong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.GridView;
import android.widget.TextView;

import com.htlc.muchong.R;

/**
 * Created by sks on 2015/11/27.
 */
public class CheckTextView extends TextView implements Checkable{

    private boolean mChecked;

    public CheckTextView(Context context) {
        super(context);
    }

    public CheckTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        this.setSelected(checked);
        this.setBackgroundResource(checked ? R.drawable.filter_retangle_shape_press:R.drawable.filter_retangle_shape_normal);
        this.setTextColor(checked ? getResources().getColor(android.R.color.white):getResources().getColor(R.color.text_color_black_muchong));
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
