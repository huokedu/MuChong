package com.htlc.muchong.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.CreateOrderActivity;
import com.htlc.muchong.activity.OrderListActivity;
import com.htlc.muchong.util.LogUtils;
import com.larno.util.ToastUtil;
import com.larno.util.okhttp.Log;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;
import model.OrderPayEvent;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    // 实现IWXAPIEventHandler接口，微信发送的请求将回调到onReq方法，发送到微信请求的响应结果将回调到onResp方法
    // 在WXEntryActivity中将接收到的intent及实现了IWXAPIEventHandler接口的对象传递给IWXAPI接口的handleIntent方法，示例如下图
    // 当微信发送请求到你的应用，将通过IWXAPIEventHandler接口的onReq方法进行回调，类似的，应用请求微信的响应结果将通过onResp回调


    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, App.get("wx_appid", ""));
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            if (resp.errCode == 0) {
                App.set("sc","1");
                LogUtils.e("支付成功---",""+resp.errCode);
                ToastUtil.showToast(App.app, "付款成功");
                OrderListActivity.goOrderListActivity(this, OrderListActivity.PAY_FINISH_TAB);
                EventBus.getDefault().post(
                        new OrderPayEvent("支付成功"));

            } else {
                String error = resp.errStr;
                int errcode = resp.errCode;
                LogUtils.e("支付失败---","error----"+error+"errcode----"+errcode);
            }
            WXPayEntryActivity.this.finish();


        }
    }


}