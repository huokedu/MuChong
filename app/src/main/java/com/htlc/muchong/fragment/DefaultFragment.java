package com.htlc.muchong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htlc.muchong.R;

/**
 * Created by sks on 2016/1/27.
 */
public class DefaultFragment extends HomeFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = new TextView(getContext());
        view.setTextSize(50);
        view.setText(mTitle);
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_default;
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void initData() {

    }
}
