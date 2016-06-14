package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.CreateOrderResultBean;
import model.ShoppingCartItemBean;

/**
 * Created by sks on 2016/6/14.
 */
public class CreateOrderActivity extends BaseActivity{
    public static final String Shopping_Items = "Shopping_Items";
    public static final String Is_Shopping_Cart = "Is_Shopping_Cart";
    public static void goCreateOrderActivity(Context context, ArrayList<ShoppingCartItemBean> shoppingCartItemBeans, boolean isShoppingCart){
        Intent intent = new Intent(context, CreateOrderActivity.class);
        intent.putParcelableArrayListExtra(Shopping_Items, shoppingCartItemBeans);
        intent.putExtra(Is_Shopping_Cart, isShoppingCart);
        context.startActivity(intent);
    }

    private ListView listView;
    private CreateOrderAdapter adapter;
    private TextView textTotalPrice;

   private ArrayList<ShoppingCartItemBean> shoppingCartItemBeans;
   private boolean isShoppingCart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_order;
    }

    @Override
    protected void setupView() {
        shoppingCartItemBeans = getIntent().getParcelableArrayListExtra(Shopping_Items);
        isShoppingCart = getIntent().getBooleanExtra(Is_Shopping_Cart,false);

        mTitleTextView.setText(R.string.title_create_order);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CreateOrderAdapter();
        listView.setAdapter(adapter);

        findViewById(R.id.textBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });
        textTotalPrice = (TextView) findViewById(R.id.textTotalPrice);
        initData();
    }

    /*创建订单*/
    private void buy() {
        if(isShoppingCart){
            String addressId = "1";
            App.app.appAction.buyByShoppingCart(shoppingCartItemBeans, addressId, new BaseActionCallbackListener<CreateOrderResultBean>() {
                @Override
                public void onSuccess(CreateOrderResultBean data) {
                    ToastUtil.showToast(App.app, "创建   单个商品   订单成功去支付！  " + data.node);
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                }
            });
        }else {
            String addressId = "1";
            ShoppingCartItemBean bean = adapter.getData().get(0);
            App.app.appAction.buyNow(bean.shopcar_commodityid, bean.num, addressId, new BaseActionCallbackListener<CreateOrderResultBean>() {
                @Override
                public void onSuccess(CreateOrderResultBean data) {
                    ToastUtil.showToast(App.app, "创建   单个商品   订单成功去支付！  "+data.node);
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
        for(ShoppingCartItemBean bean : shoppingCartItemBeans){
            totalPrice += Double.parseDouble(bean.commodity_panicprice) * Integer.parseInt(bean.num);
        }
        textTotalPrice.setText(String.valueOf(totalPrice));
    }

    public class CreateOrderAdapter extends BaseAdapter {
        private List<ShoppingCartItemBean> list = new ArrayList<>();
        public void setData(List list,boolean isAdd){
            if(isAdd){
                this.list.addAll(list);
            }else {
                this.list.clear();
                this.list.addAll(list);
            }
            notifyDataSetChanged();
        }
        public List<ShoppingCartItemBean> getData(){
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
            if(convertView == null){
                View view = View.inflate(parent.getContext(), R.layout.adapter_create_order,null);
                convertView = view;
                holder = new ViewHolder(view);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            ShoppingCartItemBean bean = list.get(position);
            holder.textName.setText(bean.commodity_name);
            GoodsUtil.setPriceBySymbol(holder.textPrice, bean.commodity_panicprice);
            GoodsUtil.setNumber(holder.textNumber, bean.num);
            ImageUtil.setImageByDefault(holder.imageView,R.mipmap.default_first_pai, Uri.parse(bean.commodity_coverimg));
            return convertView;
        }
        class ViewHolder{
            public ImageView imageView;
            public TextView textName, textPrice, textNumber;

            public ViewHolder(View view) {
                imageView = (ImageView)view.findViewById(R.id.imageView);
                textName = (TextView) view.findViewById(R.id.textName);
                textPrice = (TextView) view.findViewById(R.id.textPrice);
                textNumber = (TextView) view.findViewById(R.id.textNumber);
            }
        }

    }
}
