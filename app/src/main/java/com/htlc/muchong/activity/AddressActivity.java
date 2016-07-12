package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.AddressBean;

/**
 * Created by sks on 2016/6/14.
 * 个人中心---个人信息---收货地址列表Activity
 */
public class AddressActivity extends BaseActivity {
    public static final String Is_Select = "Is_Select";
    public static final String AddressBean = "AddressBean";

    private ListView listView;
    private AddressAdapter adapter;
    private View noDataView;

    private boolean isSelect;//判断开启该界面是选择地址，还是查看；true为选择

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void setupView() {
        isSelect = getIntent().getBooleanExtra(Is_Select, false);

        mTitleTextView.setText(isSelect ? R.string.title_address: R.string.user_address);

        noDataView = findViewById(R.id.noDataView);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new AddressAdapter();
        listView.setAdapter(adapter);
        if(isSelect){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    responseResult((AddressBean) parent.getItemAtPosition(position));
                }
            });
        }

        //新增收获地址按钮
        findViewById(R.id.buttonAddAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddress(null);
            }
        });
        initData();
    }
/*返回选择的地址*/
    private void responseResult(AddressBean bean) {
        Intent intent = new Intent();
        intent.putExtra(AddressBean, bean);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    /*收货地址列表*/
    @Override
    protected void initData() {
        adapter.setData(Collections.EMPTY_LIST,false);
        App.app.appAction.myAddressList(new BaseActionCallbackListener<List<AddressBean>>() {
            @Override
            public void onSuccess(List<AddressBean> data) {
                adapter.setData(data,false);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }
        });
    }

    /*删除地址*/
    private void deleteAddress(String addressId) {
        App.app.appAction.deleteAddress(addressId, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"删除成功");
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    /*去编辑地址界面*/
    private void editAddress(AddressBean bean) {
        EditAddressActivity.goEditAddressActivity(this, bean);
    }


    public class AddressAdapter extends BaseAdapter {
        private List<AddressBean> list = new ArrayList<>();

        public void setData(List list, boolean isAdd) {
            if (isAdd) {
                this.list.addAll(list);
            } else {
                this.list.clear();
                this.list.addAll(list);
            }
            notifyDataSetChanged();
        }

        public List<AddressBean> getData() {
            return list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                View view = View.inflate(parent.getContext(), R.layout.adapter_address, null);
                convertView = view;
                holder = new ViewHolder(view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AddressBean bean = list.get(position);
            holder.textName.setText(bean.addr_name);
            holder.textTel.setText(bean.addr_mobile);
            holder.textAddress.setText(bean.addr_address);
            holder.textEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddressBean tempBean = list.get(position);
                    editAddress(tempBean);
                }
            });
            holder.textDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddressBean tempBean = list.get(position);
                    deleteAddress(tempBean.id);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView textName, textTel, textAddress, textEdit, textDelete;

            public ViewHolder(View view) {
                textName = (TextView) view.findViewById(R.id.textName);
                textTel = (TextView) view.findViewById(R.id.textTel);
                textAddress = (TextView) view.findViewById(R.id.textAddress);
                textEdit = (TextView) view.findViewById(R.id.textEdit);
                textDelete = (TextView) view.findViewById(R.id.textDelete);
            }
        }
    }
}
