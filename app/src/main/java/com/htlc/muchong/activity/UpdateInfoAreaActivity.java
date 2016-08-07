package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.SelectAddressHelper;
import com.larno.util.ToastUtil;

/**
 * Created by sks on 2016/5/31.
 * 编辑用户信息Activity
 */
public class UpdateInfoAreaActivity extends BaseActivity{
    public static final String Hint  = "Hint";
    public static final String Value  = "Value";

    private EditText editText;
    private TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_info_area;
    }

    @Override
    protected void setupView() {
        Intent intent = getIntent();

        mTitleTextView.setText(intent.getIntExtra(ActivityTitleId,0));

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddress();
            }
        });
        findViewById(R.id.textButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void selectAddress() {
        SelectAddressHelper selectAddressHelper = new SelectAddressHelper(this,null,null,null,textView);
        selectAddressHelper.selectAddress();
    }

    private void confirm() {
        String selectValue = textView.getText().toString();
        if(TextUtils.isEmpty(selectValue)){
            ToastUtil.showToast(App.app, R.string.address_edit_province_hint);
            return;
        }
        String value = editText.getText().toString().trim();
        if(TextUtils.isEmpty(value)){
            ToastUtil.showToast(App.app, R.string.address_edit_address_hint);
            return;
        }
        Intent data = new Intent();
        data.putExtra(Value,selectValue+value);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    protected void initData() {

    }
}
