package com.htlc.muchong.activity;

import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.FirstFragment;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.widget.NumberPicker;
import com.htlc.muchong.widget.ShoppingCartSelectItem;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.ShoppingCartItemBean;

/**
 * Created by sks on 2016/5/26.
 */
public class ShoppingCartActivity extends BaseActivity{

    private ListView listView;
    private ShoppingCartAdapter adapter;
    private CheckBox checkbox;
    private TextView textTotalNumber;
    private TextView textTotalPrice;

    private int number;
    private double price;

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_shopping_cart);
        mTitleRightTextView.setText(R.string.delete);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShoppingData();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        adapter = new ShoppingCartAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSelectCount();
            }
        });

        findViewById(R.id.textBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });
        //全选按钮
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int childCount = listView.getChildCount();
                    for(int i=0; i<childCount; i++){
                        listView.setItemChecked(i,true);
                    }
                    updateSelectCount();
                }else {
                    listView.clearChoices();
                    updateSelectCount();
                }
            }
        });
        textTotalNumber = (TextView) findViewById(R.id.textTotalNumber);
        textTotalPrice = (TextView) findViewById(R.id.textTotalPrice);

        initData();
    }

    private void initBottomData(){
        textTotalNumber.setText(String.format(getString(R.string.shopping_cart_total_number),0));
        textTotalPrice.setText("￥0");
    }
    /*删除商品*/
    private void deleteShoppingData() {
        final List<ShoppingCartItemBean> data = adapter.getData();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        final List<ShoppingCartItemBean> tempDeleteData = new ArrayList<>();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            int key = checkedItemPositions.keyAt(i);
            if(checkedItemPositions.get(key)){
               tempDeleteData.add(data.get(key));
            }
        }
        App.app.appAction.deleteShoppingCart(tempDeleteData, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                ToastUtil.showToast(App.app, "删除成功");
                data.removeAll(tempDeleteData);
                listView.clearChoices();
                adapter.notifyDataSetChanged();
                initBottomData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });

    }

    /**
     * 更新底部信息
     */
    private void updateSelectCount() {
        number = 0;
        price = 0;
        List<ShoppingCartItemBean> data = adapter.getData();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            int key = checkedItemPositions.keyAt(i);
           if(checkedItemPositions.get(key)){
               ShoppingCartItemBean bean = data.get(key);
               int num = Integer.parseInt(bean.num);
               number += num;
               price += Double.parseDouble(bean.commodity_panicprice) * num;
           }
        }
        textTotalNumber.setText(String.format(getString(R.string.shopping_cart_total_number), number));
        GoodsUtil.setPriceBySymbol(textTotalPrice, String.valueOf(price));
    }

    /**
     * 去创建订单
     */
    private void buy() {
        List<ShoppingCartItemBean> data = adapter.getData();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        ArrayList<ShoppingCartItemBean> tempDeleteData = new ArrayList<>();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            int key = checkedItemPositions.keyAt(i);
            if(checkedItemPositions.get(key)){
                tempDeleteData.add(data.get(key));
            }
        }
        CreateOrderActivity.goCreateOrderActivity(this, tempDeleteData,true);
    }

    @Override
    protected void initData() {
        listView.clearChoices();
        checkbox.setChecked(false);
        initBottomData();
        App.app.appAction.shoppingCartList(new BaseActionCallbackListener<List<ShoppingCartItemBean>>() {
            @Override
            public void onSuccess(List<ShoppingCartItemBean> data) {
                adapter.setData(data, false);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    public class ShoppingCartAdapter extends BaseAdapter {
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
            if(convertView == null){
                ShoppingCartSelectItem view = (ShoppingCartSelectItem) View.inflate(parent.getContext(), R.layout.adapter_shopping_cart_item,null);
                convertView = view;
            }
            ShoppingCartItemBean bean = list.get(position);
            ShoppingCartSelectItem view = (ShoppingCartSelectItem) convertView;
            view.numberPicker.setMaxNumber(bean.maxNumber);
            view.numberPicker.setNumber(Integer.parseInt(bean.num));
            view.numberPicker.setOnNumberChangedListener(new NumberPicker.OnNumberChangedListener() {
                @Override
                public void onNumberChanged(int number) {
                    list.get(position).num = String.valueOf(number);
                    updateSelectCount();
                }

                @Override
                public void onGreaterThanMax() {
                    ToastUtil.showToast(App.app, "数量最多为" + list.get(position).maxNumber);
                }

                @Override
                public void onLessThanMin() {
                    ToastUtil.showToast(App.app, "数量至少为1");
                }
            });
            GoodsUtil.setPriceBySymbol(view.textPrice, bean.commodity_panicprice);
            ImageUtil.setImageByDefault(view.imageView, R.mipmap.default_first_pai, Uri.parse(bean.commodity_coverimg));
            view.textName.setText(bean.commodity_name);
            return convertView;
        }

    }

}
