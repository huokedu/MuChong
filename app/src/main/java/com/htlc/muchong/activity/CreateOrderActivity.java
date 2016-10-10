package com.htlc.muchong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LogUtils;
import com.larno.util.ToastUtil;
import com.larno.util.okhttp.Log;
import com.pingplusplus.android.Pingpp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.AddressBean;
import model.CreateOrderBean;
import model.CreateOrderResultBean;
import model.OrderDetailBean;
import model.OrderPayEvent;
import model.ShoppingCartItemBean;
import model.WxBean;

/**
 * Created by sks on 2016/6/14.
 * 创建订单  支付界面
 */
public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {
    public static final String Shopping_Items = "Shopping_Items";
    public static final String Is_Shopping_Cart = "Is_Shopping_Cart";
    public static final String Is_jing_Pai_Cart = "Is_jing_Pai_Cart";
    public static final String Is_Pay = "Is_Pay";
    public static final String Order_Id = "Order_Id";
    private static final int RequestAddressCode = 852;
    private static final int RequestPayCode = 853;
    private ProgressDialog progressDialog;

    /**
     * 创建订单
     * @param context
     * @param shoppingCartItemBeans
     * @param isShoppingCart 是否是购物车进入（true 是）
     */
    public static void goCreateOrderActivity(Context context, ArrayList<ShoppingCartItemBean> shoppingCartItemBeans, boolean isShoppingCart) {
        Intent intent = new Intent(context, CreateOrderActivity.class);
        intent.putParcelableArrayListExtra(Shopping_Items, shoppingCartItemBeans);
        intent.putExtra(Is_Shopping_Cart, isShoppingCart);
        context.startActivity(intent);
    }

    /**
     *
     * @param context
     * @param isJingPaiCart 是否是我的竞拍列表进入 true 是
     * @param shoppingCartItemBeans 商品数组
     */
    public static void goCreateOrderActivity(Context context, boolean isJingPaiCart, ArrayList<ShoppingCartItemBean> shoppingCartItemBeans) {
        Intent intent = new Intent(context, CreateOrderActivity.class);
        intent.putParcelableArrayListExtra(Shopping_Items, shoppingCartItemBeans);
        intent.putExtra(Is_jing_Pai_Cart, isJingPaiCart);
        context.startActivity(intent);
    }

    /**
     * 从订单列表进入
     * @param context
     * @param orderId
     * @param isPay
     */
    public static void goCreateOrderActivity(Context context, String orderId, boolean isPay) {
        Intent intent = new Intent(context, CreateOrderActivity.class);
        intent.putExtra(Order_Id, orderId);
        intent.putExtra(Is_Pay, isPay);
        context.startActivity(intent);
    }

    //地址模块
    private LinearLayout linearAddress;
    private TextView textAddressTips;
    private RelativeLayout relativeAddress;
    private TextView textName;
    private TextView textTel;
    private TextView textAddress;

    //支付方式模块
    private LinearLayout linearPay;
    private TextView textPayWay;

    //订单商品列表模块
    private ListView listView;
    private CreateOrderAdapter adapter;

    //底部总价，支付模块
    private TextView textTotalPrice;
    private TextView textBuy;
    private LinearLayout linearClearing;

    private ArrayList<ShoppingCartItemBean> shoppingCartItemBeans;//商品条目
    private boolean isShoppingCart;//是购物车创建订单
    private boolean isJingPaiCart;//是我的竞拍创建订单
    private AddressBean addressBean;//地址信息
    private String payWay;//支付方式


    private String orderId;//订单id
    private boolean isPay;//是否已经支付 true 是

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_order;
    }

    @Override
    protected void setupView() {
        shoppingCartItemBeans = getIntent().getParcelableArrayListExtra(Shopping_Items);
        isShoppingCart = getIntent().getBooleanExtra(Is_Shopping_Cart, false);
        isJingPaiCart = getIntent().getBooleanExtra(Is_jing_Pai_Cart, false);

        orderId = getIntent().getStringExtra(Order_Id);
        isPay = getIntent().getBooleanExtra(Is_Pay, true);

        linearAddress = (LinearLayout) findViewById(R.id.linearAddress);
        textAddressTips = (TextView) findViewById(R.id.textAddressTips);
        relativeAddress = (RelativeLayout) findViewById(R.id.relativeAddress);
        textName = (TextView) findViewById(R.id.textName);
        textTel = (TextView) findViewById(R.id.textTel);
        textAddress = (TextView) findViewById(R.id.textAddress);

        linearPay = (LinearLayout) findViewById(R.id.linearPay);
        linearPay.setOnClickListener(this);
        textPayWay = (TextView) findViewById(R.id.textPayWay);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CreateOrderAdapter();
        listView.setAdapter(adapter);

        linearClearing = (LinearLayout) findViewById(R.id.linearClearing);
        textBuy = (TextView) findViewById(R.id.textBuy);
        textBuy.setOnClickListener(this);
        textTotalPrice = (TextView) findViewById(R.id.textTotalPrice);

        //创建订单
        if (TextUtils.isEmpty(orderId)) {
            mTitleTextView.setText(R.string.title_create_order);
            linearAddress.setOnClickListener(this);
            //订单详情
        } else {
            mTitleTextView.setText(R.string.title_order_detail);
            if (isPay) {
//                linearClearing.setVisibility(View.GONE);
                textBuy.setVisibility(View.INVISIBLE);
                linearPay.setVisibility(View.GONE);
            }
        }
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //如果订单生成，地址不能修改
        if (!TextUtils.isEmpty(orderId)) {
            linearAddress.setOnClickListener(null);
        }
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
        //如果没有订单，商品由上个界面传递进来
        if (TextUtils.isEmpty(orderId)) {
            adapter.setData(shoppingCartItemBeans, false);
            Double totalPrice = 0.0;
            for (ShoppingCartItemBean bean : shoppingCartItemBeans) {
                totalPrice += Double.parseDouble(bean.commodity_panicprice) * Integer.parseInt(bean.num);
            }
            GoodsUtil.setPriceBySymbol(textTotalPrice, String.valueOf(totalPrice));
            getDefaultAddress();
        } else {
            //有订单，根据订单id获取商品
            getOrderDetail();
        }

    }

    /*获取订单详情*/
    private void getOrderDetail() {
        App.app.appAction.myOrderDetail(orderId, new BaseActionCallbackListener<OrderDetailBean>() {
            @Override
            public void onSuccess(OrderDetailBean data) {
                adapter.setData(data.list, false);
                GoodsUtil.setPriceBySymbol(textTotalPrice, data.order_money);
                //设置地址
                addressBean = new AddressBean();
                addressBean.addr_address = data.order_address.address;
                addressBean.addr_name = data.order_address.name;
                addressBean.addr_mobile = data.order_address.mobile;
                refreshAddress();
                //设置支付方式
                int payId = Arrays.asList(PayListActivity.PayWays).indexOf(data.order_payment);
                payWay = data.order_payment;
                textPayWay.setText(PayListActivity.PayNameIds[payId]);

            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textBuy:
                buy();
                break;
            case R.id.linearAddress:
                Intent intent = new Intent(this, AddressActivity.class);
                intent.putExtra(AddressActivity.Is_Select, true);
                startActivityForResult(intent, RequestAddressCode);
                break;
            case R.id.linearPay:
                Intent intent1 = new Intent(this, PayListActivity.class);
                startActivityForResult(intent1, RequestPayCode);
                break;
        }
    }
    /*结算操作*/
    private void buy() {
        LogUtils.e("orderId----",""+orderId);
        if (TextUtils.isEmpty(orderId)) {
            createOrderAndBuy();
        } else {
            buyFromOrderDetail();
        }

    }

    /*根据订单id支付*/
    private void buyFromOrderDetail() {
        App.app.appAction.pay(orderId, payWay, new BaseActionCallbackListener<CreateOrderBean>() {
            @Override
            public void onSuccess(CreateOrderBean data) {
                payWx(data);//微信支付
//                payByPingPlus(data);

            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*调用ping++进行支付*/
    private void payByPingPlus(CreateOrderResultBean data) {
        if (!payWay.equals(PayListActivity.PayWays[0])) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在支付中，请稍等...");
            progressDialog.show();
            Pingpp.createPayment(CreateOrderActivity.this, data.charges);
        } else {
            ToastUtil.showToast(App.app, "付款成功");
            OrderListActivity.goOrderListActivity(this, OrderListActivity.PAY_FINISH_TAB);
            finish();
        }
    }

    /*创建订单并支付*/
    private void createOrderAndBuy() {
        if (addressBean == null) {
            ToastUtil.showToast(App.app, "请选择收获地址");
            return;
        }
        if (payWay == null) {
            ToastUtil.showToast(App.app, "请选择支付方式");
            return;
        }
        String addressId = addressBean.id;

        LogUtils.e("isShoppingCart----",""+isShoppingCart);
        if (isShoppingCart) {
            buyByShoppingCart(addressId);
        } else {
            buyNow(addressId);
        }
    }

    /*立即购买创建订单并支付*/
    private void buyNow(String addressId) {
        ShoppingCartItemBean bean = adapter.getData().get(0);
        App.app.appAction.buyNow(payWay, bean.shopcar_commodityid, bean.num, addressId, isJingPaiCart, new BaseActionCallbackListener<CreateOrderBean>() {
            @Override
            public void onSuccess(CreateOrderBean data) {
//                ToastUtil.showToast(App.app, "创建   单个商品   订单成功去支付！  " + data.charges);
                CreateOrderActivity.this.orderId = data.getOrder_id();
                payWx(data);//微信支付
//                payByPingPlus(data);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
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

    /*购物车创建订单并支付*/
    private void buyByShoppingCart(String addressId) {
        App.app.appAction.buyByShoppingCart(payWay, shoppingCartItemBeans, addressId, new BaseActionCallbackListener<CreateOrderBean>() {
            @Override
            public void onSuccess(CreateOrderBean data) {
//                ToastUtil.showToast(App.app, "创建   多个商品   订单成功去支付！  " + data.charges);
                CreateOrderActivity.this.orderId = data.getOrder_id();
                payWx(data);//微信支付
//                payByPingPlus(data);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择收获地址
        if (resultCode == Activity.RESULT_OK && requestCode == RequestAddressCode) {
            addressBean = data.getParcelableExtra(AddressActivity.AddressBean);
            refreshAddress();
        }
        //选择支付方式
        if (resultCode == Activity.RESULT_OK && requestCode == RequestPayCode) {
            int payId = data.getIntExtra(PayListActivity.PayId, 0);
            textPayWay.setText(PayListActivity.PayNameIds[payId]);
            payWay = PayListActivity.PayWays[payId];
        }
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
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
                if("success".equals(result)){
                    ToastUtil.showToast(App.app, "支付成功！");
                    OrderListActivity.goOrderListActivity(this, OrderListActivity.PAY_FINISH_TAB);
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

    /*获取默认收获地址*/
    private void getDefaultAddress(){
        App.app.appAction.getDefaultAddress(new BaseActionCallbackListener<AddressBean>() {
            @Override
            public void onSuccess(AddressBean data) {
                addressBean = data;
                refreshAddress();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {

            }
        });
    }

    /**
     * 刷新地址模块
     */
    private void refreshAddress() {
        if(addressBean != null){
            textName.setText(addressBean.addr_name);
            textAddress.setText(addressBean.addr_province+addressBean.addr_city+addressBean.addr_county+addressBean.addr_address);
            textTel.setText(addressBean.addr_mobile);

            textAddressTips.setVisibility(View.GONE);
            relativeAddress.setVisibility(View.VISIBLE);
        }

    }


    public class CreateOrderAdapter extends BaseAdapter {
        private List<ShoppingCartItemBean> list = new ArrayList<>();

        public void setData(List list, boolean isAdd) {
            if (isAdd) {
                this.list.addAll(list);
            } else {
                this.list.clear();
                this.list.addAll(list);
            }
            notifyDataSetChanged();
        }

        public List<ShoppingCartItemBean> getData() {
            return list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                View view = View.inflate(parent.getContext(), R.layout.adapter_create_order, null);
                convertView = view;
                holder = new ViewHolder(view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ShoppingCartItemBean bean = list.get(position);
            holder.textName.setText(bean.commodity_name);
            GoodsUtil.setPriceBySymbol(holder.textPrice, bean.commodity_panicprice);
            GoodsUtil.setNumber(holder.textNumber, bean.num);
            ImageUtil.setImageByDefault(holder.imageView, R.mipmap.default_first_pai, Uri.parse(bean.commodity_coverimg));
            return convertView;
        }

        class ViewHolder {
            public ImageView imageView;
            public TextView textName, textPrice, textNumber;

            public ViewHolder(View view) {
                imageView = (ImageView) view.findViewById(R.id.imageView);
                textName = (TextView) view.findViewById(R.id.textName);
                textPrice = (TextView) view.findViewById(R.id.textPrice);
                textNumber = (TextView) view.findViewById(R.id.textNumber);
            }
        }

    }


    public void onEventMainThread(OrderPayEvent event) {
        String msg = event.getMsg();
        LogUtils.e("msg---", "" + msg);
        if (TextUtils.isEmpty(msg)) {
        } else {
            if (msg.equals("支付成功")) {
                CreateOrderActivity.this.finish();
            }
        }
    }
}
