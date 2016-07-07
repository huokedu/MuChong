package com.htlc.muchong.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.Constant;
import com.larno.util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2016/5/21.
 * 注册Activity
 */
public class RegisterActivity extends BaseActivity {
    public static final String IsForgetPassword = "IsForgetPassword";
    public static final String IsResetPassword = "IsResetPassword";
    private boolean isForgetPassword;
    private boolean isResetPassword;
    private LinearLayout linearLayout;//用户协议 布局
    private CheckBox checkBox;// 用户协议 checkbox
    private View textProtocol;//用户协议 按钮
    private Button buttonRegister;//注册 按钮
    private Button buttonCode;//获取验证码 按钮
    private EditText editUsername, editCode, editPassword, editPassword2;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void setupView() {
        isForgetPassword = getIntent().getBooleanExtra(IsForgetPassword, false);
        isResetPassword = getIntent().getBooleanExtra(IsResetPassword, false);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editCode = (EditText) findViewById(R.id.editCode);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPassword2 = (EditText) findViewById(R.id.editPassword2);

        buttonCode = (Button) findViewById(R.id.buttonCode);
        buttonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSmsCode();
            }
        });
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        textProtocol = findViewById(R.id.textProtocol);
        textProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goProtocol();
            }
        });
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isForgetPassword || isResetPassword){
                    forget();
                }else {
                    register();
                }
            }
        });
        initData();
    }

    @Override
    protected void initData() {
        if(isResetPassword){
            mTitleTextView.setText(R.string.user_update_password);
            linearLayout.setVisibility(View.INVISIBLE);
            buttonRegister.setText(R.string.commit);
        } else if (isForgetPassword) {
            mTitleTextView.setText(R.string.title_forget);
            linearLayout.setVisibility(View.INVISIBLE);
            buttonRegister.setText(R.string.commit);
        } else {
            mTitleTextView.setText(R.string.title_register);
        }
    }

    /**
     * 获取验证码
     */
    private void getSmsCode() {
        buttonCode.setEnabled(false);
        App.app.appAction.smsCode(editUsername.getText().toString(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                showTimer();
                ToastUtil.showToast(App.app,"验证码已发送");
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
                buttonCode.setEnabled(true);
            }
        });
    }

    /**
     * 注册操作
     */
    private void register() {
        App.app.appAction.register(editUsername.getText().toString(), editCode.getText().toString(), editPassword.getText().toString(), editPassword2.getText().toString(), checkBox.isChecked(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"注册成功");
                finish();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    /**
     * 忘记密码
     */
    private void forget() {
        App.app.appAction.resetPassword(editUsername.getText().toString(), editCode.getText().toString(), editPassword.getText().toString(), editPassword2.getText().toString(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"密码设置成功");
                finish();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }


    /**
     * 查看用户协议
     */
    private void goProtocol() {
        ToastUtil.showToast(App.app,"查看用户协议");
    }

    /**
     * 显示倒计时
     */
    private void showTimer() {
        buttonCode.setEnabled(false);//不允许再次获取验证码
        CountDownTimer countDownTimer = new CountDownTimer(Constant.Sms_Code_Timer, Constant.Sms_Code_Timer_T) {
            @Override
            public void onTick(long millisUntilFinished) {
                buttonCode.setText("(" + millisUntilFinished / Constant.Sms_Code_Timer_T + "s)");
            }

            @Override
            public void onFinish() {
                buttonCode.setText(R.string.register_code);
                buttonCode.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

}
