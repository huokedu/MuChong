package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.AddressAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.bean.AddressItemBean;
import com.htlc.muchong.database.DBManager;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import model.AddressBean;

/**
 * Created by sks on 2016/5/27.
 */
public class EditAddressActivity extends BaseActivity {
    public static final String AddressBean = "AddressBean";

    public static void goEditAddressActivity(Context context, AddressBean bean) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra(AddressBean, bean);
        context.startActivity(intent);
    }

    private EditText editName, editTel, editAddress;
    private Switch switchView;
    private TextView textProvince;
    private DBManager dbm;
    private SQLiteDatabase db;
    private String province = null;
    private String city = null;
    private String district = null;

    private AddressBean bean;//开启Activity传递的bean，如果有值则为修改地址，没有则为添加新地址
    private OptionsPickerView mPickViePwOptions;
    private ArrayList<AddressItemBean> provinces = new ArrayList<AddressItemBean>();
    private ArrayList<AddressItemBean> citys = new ArrayList<AddressItemBean>();
    private ArrayList<AddressItemBean> countys = new ArrayList<AddressItemBean>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected void setupView() {
        bean = getIntent().getParcelableExtra(AddressBean);

        mTitleTextView.setText(R.string.title_address_edit);
        mTitleRightTextView.setText(R.string.save);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });


        editName = (EditText) findViewById(R.id.editName);
        editTel = (EditText) findViewById(R.id.editTel);
        editAddress = (EditText) findViewById(R.id.editAddress);
        textProvince = (TextView) findViewById(R.id.textProvince);
        textProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddress();
            }
        });
        switchView = (Switch) findViewById(R.id.switchView);

        initData();
        initSpinner1();
    }


    @Override
    protected void initData() {
        if (bean == null) {

        } else {
            editName.setText(bean.addr_name);
            editTel.setText(bean.addr_mobile);
            editAddress.setText(bean.addr_address);
            province = bean.addr_province;
            city = bean.addr_city;
            district = bean.addr_county;
            textProvince.setText(province+city+district);
            switchView.setChecked(!"1".equals(bean.addr_type));
        }
    }

    /*保存收货地址*/
    private void commit() {
        mTitleRightTextView.setEnabled(false);
        if (province == null || city == null || district == null) {
            ToastUtil.showToast(App.app, "请选择省市县");
            return;
        }
        boolean flag = switchView.isChecked();
        String type;
        if (flag) {
            type = "2";
        } else {
            type = "1";
        }
        if (bean == null) {
            App.app.appAction.addAddress(type, province, city, district, editAddress.getText().toString().trim(), editName.getText().toString().trim(), editTel.getText().toString(), new BaseActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    ToastUtil.showToast(App.app, "保存成功");
                    finish();
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mTitleRightTextView.setEnabled(true);
                }
            });
        } else {
            App.app.appAction.updateAddress(bean.id, type, province, city, district, editAddress.getText().toString().trim(), editName.getText().toString().trim(), editTel.getText().toString(), new BaseActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    ToastUtil.showToast(App.app, "保存成功");
                    finish();
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mTitleRightTextView.setEnabled(true);
                }
            });
        }
    }

    private void selectAddress() {
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        provinces.clear();
        citys.clear();
        countys.clear();

        provinces.addAll(initSpinner1());

        if (provinces.size() > 0) {
            citys.addAll(initSpinner2(provinces.get(0).getPcode()));
        }
        if (citys.size() > 0) {
            countys.addAll(initSpinner3(citys.get(0).getPcode()));
        }
        //三级不联动效果  false
        mPickViePwOptions.setPicker(provinces, citys, countys, false);
        mPickViePwOptions.getWheelOptions().getWv_option1().setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                ArrayList<AddressItemBean> temp = initSpinner2(provinces.get(index).getPcode());
                ArrayWheelAdapter adapter = new ArrayWheelAdapter(temp);
                citys.clear();
                citys.addAll(temp);
                mPickViePwOptions.getWheelOptions().getWv_option2().setAdapter(adapter);
                if (citys.size() == 0) {
                    mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(new ArrayWheelAdapter(new ArrayList<AddressItemBean>()));
                    return;
                }
                ArrayList<AddressItemBean> temp1 = initSpinner3(citys.get(0).getPcode());
                ArrayWheelAdapter adapter1 = new ArrayWheelAdapter(temp1);
                countys.clear();
                countys.addAll(temp1);
                mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(adapter1);
                mPickViePwOptions.setSelectOptions(index,0,0);

            }
        });
        mPickViePwOptions.getWheelOptions().getWv_option2().setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                ArrayList<AddressItemBean> temp = initSpinner3(citys.get(index).getPcode());
                ArrayWheelAdapter adapter = new ArrayWheelAdapter(temp);
                countys.clear();
                countys.addAll(temp);
                mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(adapter);
            }
        });
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        mPickViePwOptions.setTitle("选择城市");
        mPickViePwOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0, 0, 0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                province = provinces.get(options1).getName();
                city = citys.get(option2).getName();
                district = countys.get(options3).getName();
                textProvince.setText(province+city+district);
            }
        });
        mPickViePwOptions.show();
    }

    public ArrayList<AddressItemBean> initSpinner1() {
        dbm = new DBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        ArrayList<AddressItemBean> list = new ArrayList<AddressItemBean>();

        try {
            String sql = "select * from province";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                AddressItemBean myListItem = new AddressItemBean();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            AddressItemBean myListItem = new AddressItemBean();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbm.closeDatabase();
        db.close();
        return list;
    }

    public ArrayList<AddressItemBean> initSpinner2(String pcode) {
        dbm = new DBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        ArrayList<AddressItemBean> list = new ArrayList<AddressItemBean>();
        try {
            String sql = "select * from city where pcode='" + pcode + "'";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                AddressItemBean myListItem = new AddressItemBean();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            AddressItemBean myListItem = new AddressItemBean();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbm.closeDatabase();
        db.close();
        return list;
    }

    public  ArrayList<AddressItemBean> initSpinner3(String pcode) {
        dbm = new DBManager(this);
        dbm.openDatabase();
        db = dbm.getDatabase();
        ArrayList<AddressItemBean> list = new ArrayList<AddressItemBean>();

        try {
            String sql = "select * from district where pcode='" + pcode + "'";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                AddressItemBean myListItem = new AddressItemBean();
                myListItem.setName(name);
                myListItem.setPcode(code);
                list.add(myListItem);
                cursor.moveToNext();
            }
            String code = cursor.getString(cursor.getColumnIndex("code"));
            byte bytes[] = cursor.getBlob(2);
            String name = new String(bytes, "gbk");
            AddressItemBean myListItem = new AddressItemBean();
            myListItem.setName(name);
            myListItem.setPcode(code);
            list.add(myListItem);

        } catch (Exception e) {
        }
        dbm.closeDatabase();
        db.close();

        return list;
    }

    class SpinnerOnSelectedListener1 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            province = ((AddressItemBean) adapterView.getItemAtPosition(position)).getName();
            String pcode = ((AddressItemBean) adapterView.getItemAtPosition(position)).getPcode();

            initSpinner2(pcode);
            initSpinner3(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener2 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            city = ((AddressItemBean) adapterView.getItemAtPosition(position)).getName();
            String pcode = ((AddressItemBean) adapterView.getItemAtPosition(position)).getPcode();

            initSpinner3(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener3 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                   long id) {
            district = ((AddressItemBean) adapterView.getItemAtPosition(position)).getName();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

}
