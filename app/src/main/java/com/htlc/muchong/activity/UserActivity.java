package com.htlc.muchong.activity;

import android.view.View;
import android.widget.ImageView;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.SelectPicDialog;

/**
 * Created by sks on 2016/5/27.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageHead;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_user);

        imageHead = (ImageView) findViewById(R.id.imageHead);
        imageHead.setOnClickListener(this);
        initData();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageHead:
                SelectPicDialog.showDialog(this);
                break;
        }
    }
}
