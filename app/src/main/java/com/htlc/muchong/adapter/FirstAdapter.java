package com.htlc.muchong.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.util.GoodsUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.GoodsBean;

/**
 * Created by sks on 2016/5/19.
 */
public class FirstAdapter extends BaseAdapter {
    private List<GoodsBean> mList = new ArrayList();
    public void setData(List data, boolean isAdd) {
        if (isAdd) {
            mList.addAll(data);
        } else {
            mList.clear();
            mList.addAll(data);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(parent.getContext(),R.layout.adapter_fragment_first,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imagePai1);
            holder.textName = (TextView) convertView.findViewById(R.id.textPaiName1);
            holder.textPrice = (TextView) convertView.findViewById(R.id.textPaiPrice1);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsBean goodsBean = mList.get(position);
        int intrinsicWidth = holder.imageView.getResources().getDrawable(R.mipmap.default_first_pai).getIntrinsicWidth();
        int intrinsicHeight = holder.imageView.getResources().getDrawable(R.mipmap.default_first_pai).getIntrinsicHeight();
        Picasso.with(parent.getContext()).load(Uri.parse(goodsBean.commodity_coverimg)).placeholder(R.mipmap.default_first_pai).error(R.mipmap.default_first_pai).resize(intrinsicWidth,intrinsicHeight).into(holder.imageView);
        holder.textName.setText(goodsBean.commodity_name);
        GoodsUtil.setPriceBySymbol(holder.textPrice,goodsBean.commodity_panicprice);
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
        public TextView textName;
        public TextView textPrice;
    }
}
