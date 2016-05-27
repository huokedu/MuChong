package com.htlc.muchong.activity;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;

/**
 * Created by sks on 2016/5/27.
 */
public class UserActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_user);


        initData();
    }

    @Override
    protected void initData() {

    }
}
