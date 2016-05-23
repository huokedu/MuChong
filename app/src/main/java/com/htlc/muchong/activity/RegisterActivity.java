package com.htlc.muchong.activity;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;

/**
 * Created by sks on 2016/5/21.
 */
public class RegisterActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_register);
    }

    @Override
    protected void initData() {

    }
}
