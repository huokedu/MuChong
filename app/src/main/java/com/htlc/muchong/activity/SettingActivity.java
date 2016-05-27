package com.htlc.muchong.activity;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;

/**
 * Created by sks on 2016/5/27.
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_setting);


        initData();
    }

    @Override
    protected void initData() {

    }
}
