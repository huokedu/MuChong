package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.larno.util.ToastUtil;
import com.pingplusplus.android.Pingpp;

import java.util.ArrayList;
import java.util.List;

import model.AddressBean;
import model.CreateOrderResultBean;
import model.ShoppingCartItemBean;

/**
 * Created by sks on 2016/6/14.
 * 创建订单  支付界面
 */
public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {
    public static final String Shopping_Items = "Shopping_Items";
    public static final String Is_Shopping_Cart = "Is_Shopping_Cart";
    private static final int RequestAddressCode = 852;
    private static final int RequestPayCode = 853;

    public static void goCreateOrderActivity(Context context, ArrayList<ShoppingCartItemBean> shoppingCartItemBeans, boolean isShoppingCart) {
        Intent intent = new Intent(context, CreateOrderActivity.class);
        intent.putParcelableArrayListExtra(Shopping_Items, shoppingCartItemBeans);
        intent.putExtra(Is_Shopping_Cart, isShoppingCart);
        context.startActivity(intent);
    }

    private LinearLayout linearAddress;
    private TextView textAddressTips;
    private RelativeLayout relativeAddress;
    private TextView textName;
    private TextView textTel;
    private TextView textAddress;

    private LinearLayout linearPay;
    private TextView textPayWay;

    private ListView listView;
    private CreateOrderAdapter adapter;
    private TextView textTotalPrice;

    private ArrayList<ShoppingCartItemBean> shoppingCartItemBeans;
    private boolean isShoppingCart;
    private AddressBean addressBean;
    private String payWay;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_order;
    }

    @Override
    protected void setupView() {
        shoppingCartItemBeans = getIntent().getParcelableArrayListExtra(Shopping_Items);
        isShoppingCart = getIntent().getBooleanExtra(Is_Shopping_Cart, false);

        mTitleTextView.setText(R.string.title_create_order);

        linearAddress = (LinearLayout) findViewById(R.id.linearAddress);
        linearAddress.setOnClickListener(this);
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

        findViewById(R.id.textBuy).setOnClickListener(this);
        textTotalPrice = (TextView) findViewById(R.id.textTotalPrice);
        initData();
    }

    /*创建订单*/
    private void buy() {
        if(addressBean == null){
            ToastUtil.showToast(App.app, "请选择收获地址");
            return;
        }
        if(payWay == null){
            ToastUtil.showToast(App.app, "请选择支付方式");
            return;
        }
        String addressId = addressBean.id;
        if (isShoppingCart) {
            App.app.appAction.buyByShoppingCart(payWay,shoppingCartItemBeans, addressId, new BaseActionCallbackListener<CreateOrderResultBean>() {
                @Override
                public void onSuccess(CreateOrderResultBean data) {
                    ToastUtil.showToast(App.app, "创建   多个商品   订单成功去支付！  " + data.charges);
                    Pingpp.createPayment(CreateOrderActivity.this, data.charges);
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                }
            });
        } else {
            ShoppingCartItemBean bean = adapter.getData().get(0);
            App.app.appAction.buyNow(payWay,bean.shopcar_commodityid, bean.num, addressId, new BaseActionCallbackListener<CreateOrderResultBean>() {
                @Override
                public void onSuccess(CreateOrderResultBean data) {
                    ToastUtil.showToast(App.app, "创建   单个商品   订单成功去支付！  " + data.charges);
                    Pingpp.createPayment(CreateOrderActivity.this, data.charges);
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                }
            });
        }
    }

    @Override
    protected void initData() {
        adapter.setData(shoppingCartItemBeans, false);
        Double totalPrice = 0.0;
        for (ShoppingCartItemBean bean : shoppingCartItemBeans) {
            totalPrice += Double.parseDouble(bean.commodity_panicprice) * Integer.parseInt(bean.num);
        }
        textTotalPrice.setText(String.valueOf(totalPrice));
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
                startActivityForResult(intent1,RequestPayCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == RequestAddressCode){
            addressBean = data.getParcelableExtra(AddressActivity.AddressBean);
            textName.setText(addressBean.addr_name);
            textAddress.setText(addressBean.addr_address);
            textTel.setText(addressBean.addr_mobile);

            textAddressTips.setVisibility(View.GONE);
            relativeAddress.setVisibility(View.VISIBLE);
        }
        if(resultCode == Activity.RESULT_OK && requestCode == RequestPayCode){
            int payId = data.getIntExtra(PayListActivity.PayId, 0);
            textPayWay.setText(PayListActivity.PayNameIds[payId]);
            payWay = PayListActivity.PayWays[payId];
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
                ToastUtil.showToast(App.app, errorMsg+"--------"+extraMsg);
            }
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
}
