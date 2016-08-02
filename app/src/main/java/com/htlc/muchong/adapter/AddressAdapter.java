package com.htlc.muchong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.bean.AddressItemBean;

import java.util.List;

public class AddressAdapter extends BaseAdapter {

    private Context context;
    private List<AddressItemBean> myList;

    public AddressAdapter(Context context, List<AddressItemBean> myList) {
        this.context = context;
        this.myList = myList;
    }

    public int getCount() {
        return myList.size();
    }

    public Object getItem(int position) {
        return myList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(parent.getContext(), R.layout.adapter_city_address, null);
        }
        ((TextView)convertView).setText(myList.get(position).getName());
        return convertView;
    }

    class MyAdapterView extends LinearLayout {
        public static final String LOG_TAG = "MyAdapterView";

        public MyAdapterView(Context context, AddressItemBean myListItem) {
            super(context);
            this.setOrientation(HORIZONTAL);

            LayoutParams params = new LayoutParams(200, LayoutParams.WRAP_CONTENT);
            params.setMargins(1, 1, 1, 1);

            TextView name = new TextView(context);
            name.setText(myListItem.getName());
            addView(name, params);

            LayoutParams params2 = new LayoutParams(200, LayoutParams.WRAP_CONTENT);
            params2.setMargins(1, 1, 1, 1);

            TextView pcode = new TextView(context);
            pcode.setText(myListItem.getPcode());
            addView(pcode, params2);
            pcode.setVisibility(GONE);

        }

    }

}