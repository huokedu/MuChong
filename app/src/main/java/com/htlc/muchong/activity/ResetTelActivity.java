package com.htlc.muchong.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.Constant;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.ToastUtil;

/**
 * Created by sks on 2016/5/27.
 * 更换新手机Activity
 */
public class ResetTelActivity extends BaseActivity implements View.OnClickListener {


    private TextView textButton;
    private LinearLayout linearNext;
    private EditText editText;//新手机号
    private LinearLayout linearPre;
    private TextView textTips;
    private EditText editCode;
    private Button buttonCode;

    private boolean isNextState;//是否是第二步操作

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_tel;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_update_tel);

        linearPre = (LinearLayout) findViewById(R.id.linearPre);
        textTips = (TextView) findViewById(R.id.textTips);
        editCode = (EditText) findViewById(R.id.editCode);
        buttonCode = (Button) findViewById(R.id.buttonCode);
        buttonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSmsCode();
            }
        });

        linearNext = (LinearLayout) findViewById(R.id.linearNext);
        editText = (EditText) findViewById(R.id.editText);

        textButton = (TextView) findViewById(R.id.textButton);
        textButton.setOnClickListener(this);

        initData();
    }

    @Override
    protected void initData() {
        if(isNextState){
            linearPre.setVisibility(View.GONE);
            linearNext.setVisibility(View.VISIBLE);
            textButton.setText(R.string.finish);
        }else {
            textTips.setText(getString(R.string.update_tel_tips,LoginUtil.getUser().user_account));
            linearPre.setVisibility(View.VISIBLE);
            linearNext.setVisibility(View.GONE);
            textButton.setText(R.string.update_tel_next);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textButton:
                commit();
                break;
        }
    }

    private void commit() {
        if(isNextState){
            updateTel();
        }else {
            ensureSmsCode();
        }
    }
/*验证验证码*/
    private void ensureSmsCode() {
        App.app.appAction.ensureSmsCode(LoginUtil.getUser().user_account, editCode.getText().toString(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                isNextState = true;
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*设置新手机号*/
    private void updateTel() {
        App.app.appAction.resetTel(editText.getText().toString(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "手机号设置成功！");
                finish();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getSmsCode() {
        buttonCode.setEnabled(false);
        App.app.appAction.smsCode(LoginUtil.getUser().user_account, new BaseActionCallbackListener<Void>() {
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
