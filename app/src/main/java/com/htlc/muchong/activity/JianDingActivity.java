package com.htlc.muchong.activity;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;

/**
 * Created by sks on 2016/5/27.
 */
public class JianDingActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_jian_ding;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.jian_detail_jian);


        initData();
    }

    @Override
    protected void initData() {

    }
}
