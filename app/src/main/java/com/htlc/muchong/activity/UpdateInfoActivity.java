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

/**
 * Created by sks on 2016/5/31.
 * 编辑用户信息Activity
 */
public class UpdateInfoActivity extends BaseActivity{
    public static final String Hint  = "Hint";
    public static final String Value  = "Value";

    private EditText editText;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_info;
    }

    @Override
    protected void setupView() {
        Intent intent = getIntent();

        mTitleTextView.setText(intent.getIntExtra(ActivityTitleId,0));

        editText = (EditText) findViewById(R.id.editText);
        editText.setHint(intent.getIntExtra(Hint, 0));
        editText.setText(intent.getStringExtra(Value));
        findViewById(R.id.textButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void confirm() {
        String value = editText.getText().toString().trim();
        if(TextUtils.isEmpty(value)){
            ToastUtil.showToast(App.app, "内容不能为空");
            return;
        }
        Intent data = new Intent();
        data.putExtra(Value,value);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    protected void initData() {

    }
}
