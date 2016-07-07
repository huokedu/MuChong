package com.htlc.muchong.activity;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;

import core.ActionCallbackListener;
import model.UserBean;

/**
 * Created by sks on 2016/5/21.
 * 登录界面
 */
public class LoginActivity extends BaseActivity {
    private EditText editUsername, editPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_login);
        findViewById(R.id.textRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister(false);
            }
        });
        findViewById(R.id.textForget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister(true);
            }
        });
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    /**
     * 登录方法
     */
    private void login() {
        App.app.appAction.login(editUsername.getText().toString(), editPassword.getText().toString(), new BaseActionCallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                App.app.setIsLogin(true);
                ToastUtil.showToast(App.app, "登录成功");
                finish();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /**
     * 忘记密码 或 注册
     *
     * @param isForget true 忘记密码
     */
    private void goRegister(boolean isForget) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra(RegisterActivity.IsForgetPassword, isForget);
        startActivity(intent);
    }

    @Override
    protected void initData() {

    }
}
