package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;
import com.pingplusplus.android.Pingpp;

import model.CreateOrderResultBean;


/**
 * Created by sks on 2016/2/15.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {

    private EditText editMoney;
    private RadioGroup radioGroup;
    private Button button;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_chong);

        editMoney = (EditText) findViewById(R.id.editMoney);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                chongZhi();
                break;
        }
    }

    private void chongZhi() {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        String payWay = PayListActivity.PayWays[2];
        if(checkedId == R.id.radioPayWeixin){
            payWay = PayListActivity.PayWays[2];
        }else if(checkedId == R.id.radioPayUnion){
            payWay = PayListActivity.PayWays[1];
        }else if(checkedId == R.id.radioPayAli){
            payWay = PayListActivity.PayWays[3];
        }
        App.app.appAction.payForAccount(editMoney.getText().toString(), payWay, new BaseActionCallbackListener<CreateOrderResultBean>() {
            @Override
            public void onSuccess(CreateOrderResultBean data) {
                Pingpp.createPayment(PayActivity.this, data.charges);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                ToastUtil.showToast(App.app, result + ":::::" + errorMsg + "--------" + extraMsg);
            }
        }
    }




}
