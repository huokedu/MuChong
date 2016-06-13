package com.htlc.muchong.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;
import com.squareup.picasso.Picasso;

import model.GoodsBean;
import model.PaiGoodsBean;

/**
 * Created by sks on 2016/5/20.
 */
public class FirstRecyclerViewAdapter extends BaseRecyclerViewAdapter<GoodsBean> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_first, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder currentHolder = (ViewHolder) holder;
        GoodsBean goodsBean = mList.get(position);
        ImageUtil.setImageByDefault(currentHolder.imageView, R.mipmap.default_first_pai, Uri.parse(goodsBean.commodity_coverimg));
        currentHolder.textName.setText(goodsBean.commodity_name);
        GoodsUtil.setPriceBySymbol(currentHolder.textPrice, goodsBean.commodity_panicprice);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textPrice;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imagePai1);
            textName = (TextView) view.findViewById(R.id.textPaiName1);
            textPrice = (TextView) view.findViewById(R.id.textPaiPrice1);

        }
    }
}
