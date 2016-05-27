package com.htlc.muchong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.muchong.R;

/**
 * Created by sks on 2016/4/7.
 */
public class ShoppingCartSelectItem extends LinearLayout implements Checkable {
    public TextView textName, textPrice;
    public ImageView imageView;
    public CheckBox checkbox;
    public NumberPicker numberPicker;

    public ShoppingCartSelectItem(Context context) {
        this(context, null);
    }

    public ShoppingCartSelectItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingCartSelectItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        this.setBackgroundColor(Color.WHITE);
        this.addView(View.inflate(context, R.layout.adapter_shopping_cart, null));
        textName = (TextView) findViewById(R.id.textName);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        textPrice = (TextView) findViewById(R.id.textPrice);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShoppingCartSelectItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView(context);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checkbox != null) {
            checkbox.setChecked(checked);
        }

    }

    @Override
    public boolean isChecked() {
        return checkbox.isChecked();
    }

    @Override
    public void toggle() {
        setChecked(!checkbox.isChecked());
    }
}
