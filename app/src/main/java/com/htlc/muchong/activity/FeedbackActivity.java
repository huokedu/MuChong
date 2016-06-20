package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;

import model.OrderDetailBean;

/**
 * Created by sks on 2016/5/31.
 */
public class FeedbackActivity extends BaseActivity{

    private EditText editText;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void setupView() {

        mTitleTextView.setText(R.string.setting_feedback);

        editText = (EditText) findViewById(R.id.editText);
        findViewById(R.id.textButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void confirm() {
        String value = editText.getText().toString().trim();
        App.app.appAction.feedback(value, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"提交成功");
                finish();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
