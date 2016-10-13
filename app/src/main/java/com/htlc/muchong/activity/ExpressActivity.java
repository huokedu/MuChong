package com.htlc.muchong.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.bean.AddressItemBean;
import com.htlc.muchong.database.DBManager;
import com.htlc.muchong.util.DialogUtils;
import com.htlc.muchong.util.LogUtils;
import com.htlc.muchong.util.SelectAddressHelper;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import core.AppActionImpl;
import de.greenrobot.event.EventBus;
import model.AddressBean;
import model.ExpressBean;
import model.ExpressEvent;
import model.MerchantOrderListBean;

/**
 *商户——我的订单--发货页
 */
public class ExpressActivity extends BaseActivity {
    public static final String OrderNo = "OrderNo";
    private String[] str={"顺丰","圆通","邮政","申通","韵达"};

    public static void goExpressActivity(Context context, String s) {
        Intent intent = new Intent(context, ExpressActivity.class);
        intent.putExtra(OrderNo, s);
        context.startActivity(intent);
    }


    private String orderno,logisticsno,logisticsname;
    private TextView mTv,mBtn;
    private EditText mEt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_express;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fh);
        Intent intent = getIntent();
        if(null!=intent){
            orderno = getIntent().getStringExtra(OrderNo);
        }
        mTv = (TextView) findViewById(R.id.express_tv);
        mBtn = (TextView) findViewById(R.id.express_tv_btn);
        mEt = (EditText) findViewById(R.id.express_et);
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.createListDialog(ExpressActivity.this,null,str,listener);

            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logisticsno = mEt.getText().toString();
                LogUtils.e("logisticsno---",""+logisticsno);
                LogUtils.e("logisticsname---",""+logisticsname);
                if (TextUtils.isEmpty(logisticsno)) {
                    ToastUtil.showToast(App.app, "请输入快递单号");
                    return;
                }
                if (TextUtils.isEmpty(logisticsname)) {
                    ToastUtil.showToast(App.app, "请选择快递公司");
                    return;
                }
                App.app.appAction.express(orderno,logisticsno,logisticsname,new BaseActionCallbackListener<List<MerchantOrderListBean>>() {
                    @Override
                    public void onSuccess(List<MerchantOrderListBean> data) {
                        LogUtils.e("onSuccess---","onSuccess");
                        EventBus.getDefault().post(
                                new ExpressEvent("1"));
                        finish();
                    }

                    @Override
                    public void onIllegalState(String errorEvent, String message) {
                        LogUtils.e("errorEvent---",""+errorEvent);
                        LogUtils.e("message---",""+message);
                    }
                });

            }
        });


    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mTv.setText(str[which]);
            logisticsname =str[which];
        }
    };

    @Override
    protected void initData() {

    }


}
