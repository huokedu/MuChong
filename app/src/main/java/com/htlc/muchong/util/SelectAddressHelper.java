package com.htlc.muchong.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.htlc.muchong.bean.AddressItemBean;
import com.htlc.muchong.database.DBManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/7.
 */
public class SelectAddressHelper {
    private OptionsPickerView mPickViePwOptions;
    private ArrayList<AddressItemBean> provinces = new ArrayList<AddressItemBean>();
    private ArrayList<AddressItemBean> citys = new ArrayList<AddressItemBean>();
    private ArrayList<AddressItemBean> countys = new ArrayList<AddressItemBean>();
    private DBManager dbm;
    private SQLiteDatabase db;
    private Context context;
    private String province = null;
    private String city = null;
    private String district = null;
    private TextView textView;
    public SelectAddressHelper(Context context, String province, String city, String district, TextView textView){
        this.context = context;
        this.province = province;
        this.city = city;
        this.district = district;
        this.textView = textView;
    }
    public void selectAddress() {
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(context);
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
                textView.setText(province+city+district);
            }
        });
        mPickViePwOptions.show();
    }

    public ArrayList<AddressItemBean> initSpinner1() {
        dbm = new DBManager(context);
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
        dbm = new DBManager(context);
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
        dbm = new DBManager(context);
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

}
