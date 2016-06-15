package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;

import model.AddressBean;

/**
 * Created by sks on 2016/5/27.
 */
public class EditAddressActivity extends BaseActivity {
    public static final String AddressBean = "AddressBean";

    public static void goEditAddressActivity(Context context, AddressBean bean) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra(AddressBean, bean);
        context.startActivity(intent);
    }

    private EditText editName,editTel,editAddress;

    private AddressBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected void setupView() {
        bean = getIntent().getParcelableExtra(AddressBean);

        mTitleTextView.setText(R.string.title_address_edit);
        mTitleRightTextView.setText(R.string.save);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });


        editName = (EditText) findViewById(R.id.editName);
        editTel = (EditText) findViewById(R.id.editTel);
        editAddress = (EditText) findViewById(R.id.editAddress);

        initData();
    }

    @Override
    protected void initData() {
        if (bean == null) {

        } else {
            editName.setText(bean.addr_name);
            editTel.setText(bean.addr_mobile);
            editAddress.setText(bean.addr_address);
        }
    }

    /*保存收货地址*/
    private void commit() {
        if (bean == null) {
            App.app.appAction.addAddress(editAddress.getText().toString().trim(), editName.getText().toString().trim(), editTel.getText().toString(), new BaseActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    ToastUtil.showToast(App.app, "保存成功");
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                }
            });
        } else {
            App.app.appAction.updateAddress(bean.id, editAddress.getText().toString().trim(), editName.getText().toString().trim(), editTel.getText().toString(), new BaseActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    ToastUtil.showToast(App.app, "保存成功");
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                }
            });
        }
    }


}
