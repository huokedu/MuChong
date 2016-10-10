package com.htlc.muchong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LogUtils;
import com.larno.util.ToastUtil;
import com.pingplusplus.android.Pingpp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import model.CreateOrderBean;
import model.CreateOrderResultBean;
import model.WxBean;


/**
 * Created by sks on 2016/2/15.
 * 个人中心---充值
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {

    private EditText editMoney;
    private RadioGroup radioGroup;
    private Button button;
    private ProgressDialog progressDialog;

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
    protected void onStop() {
        super.onStop();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
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

    /*充值请求，获取支付信息*/
    private void chongZhi() {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        String payWay = PayListActivity.PayWays[0];
//        if(checkedId == R.id.radioPayWeixin){
//            payWay = PayListActivity.PayWays[2];
//        }
//        else if(checkedId == R.id.radioPayUnion){
//            payWay = PayListActivity.PayWays[1];
//        }else if(checkedId == R.id.radioPayAli){
//            payWay = PayListActivity.PayWays[3];
//        }

        if(checkedId == R.id.radioPayWeixin){
            payWay = PayListActivity.PayWays[0];
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在支付中，请稍等...");
        progressDialog.show();
        App.app.appAction.payForAccount(editMoney.getText().toString(), payWay, new BaseActionCallbackListener<CreateOrderBean>() {
            @Override
            public void onSuccess(CreateOrderBean data) {

                payWx(data);//微信支付
                //调起支付
//                Pingpp.createPayment(PayActivity.this, data.charges);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
            }
        });
    }

    IWXAPI msgApi;
    private void payWx(CreateOrderBean data) {
        WxBean wx = data.getCharges().getCredential().getWx();
        App.set("wx_appid",wx.getAppId());
        LogUtils.e("wx_appid----",""+App.get("wx_appid", ""));
        msgApi = WXAPIFactory.createWXAPI(this, wx.getAppId());
        msgApi.registerApp(wx.getAppId());

        if (msgApi != null) {
            if (msgApi.isWXAppInstalled()) {
                PayReq req = new PayReq();
                req.appId = wx.getAppId();
                req.partnerId = wx.getPartnerId();
                req.prepayId = wx.getPrepayId();
                req.packageValue = wx.getPackageValue();
                req.nonceStr = wx.getNonceStr();
                req.timeStamp = wx.getTimeStamp();
                req.sign = wx.getSign();
                msgApi.sendReq(req);
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
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
//                ToastUtil.showToast(App.app, result + ":::::" + errorMsg + "--------" + extraMsg);
                if("success".equals(result)){
                    ToastUtil.showToast(App.app, "支付成功！");
                    finish();
                }else if("cancel".equals(result)){
                    ToastUtil.showToast(App.app, "支付取消！");
                }else if("fail".equals(result)){
                    ToastUtil.showToast(App.app, "支付失败！");
                }else {
                    ToastUtil.showToast(App.app, "支付异常！");
                }
            }
        }
    }





}
