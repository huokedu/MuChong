package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import model.ShoppingCartItemBean;

/**
 * Created by sks on 2016/6/14.
 * 创建订单  支付界面
 */
public class PayListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final int[] PayNameIds = {R.string.pay_way_1,R.string.pay_way_2,R.string.pay_way_3,R.string.pay_way_4};
    public static final int[] PayIconIds = {R.mipmap.icon_pay_1,R.mipmap.icon_pay_2,R.mipmap.icon_pay_3,R.mipmap.icon_pay_4};
    public static final String[] PayWays = {"wallet","upacp","wx","alipay"};
    public static final String PayId = "PayId";

    private ListView listView;
    private PayListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_select_pay_way);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new PayListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        initData();
    }
    @Override
    protected void initData() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent data = new Intent();
        data.putExtra(PayId, position);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public class PayListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 4;
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
                View view = View.inflate(parent.getContext(), R.layout.adapter_pay_way, null);
                convertView = view;
                holder = new ViewHolder(view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textName.setText(PayNameIds[position]);
            holder.imageView.setImageResource(PayIconIds[position]);
            return convertView;
        }

        class ViewHolder {
            public ImageView imageView;
            public TextView textName;

            public ViewHolder(View view) {
                imageView = (ImageView) view.findViewById(R.id.imageView);
                textName = (TextView) view.findViewById(R.id.textName);
            }
        }

    }
}
