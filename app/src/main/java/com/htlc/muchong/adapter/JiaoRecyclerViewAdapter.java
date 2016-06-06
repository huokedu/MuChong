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
import com.squareup.picasso.Picasso;

import model.JiaoGoodsBean;

/**
 * Created by sks on 2016/5/20.
 */
public class JiaoRecyclerViewAdapter extends BaseRecyclerViewAdapter<JiaoGoodsBean>{


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_jiao_list, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder currentHolder = (ViewHolder) holder;
        JiaoGoodsBean goodsBean = mList.get(position);

        int intrinsicWidth = currentHolder.imageView.getResources().getDrawable(R.mipmap.default_first_pai).getIntrinsicWidth();
        int intrinsicHeight = currentHolder.imageView.getResources().getDrawable(R.mipmap.default_first_pai).getIntrinsicHeight();
        Picasso.with(currentHolder.imageView.getContext()).load(Uri.parse(goodsBean.commodity_coverimg)).placeholder(R.mipmap.default_first_pai).error(R.mipmap.default_first_pai).resize(intrinsicWidth,intrinsicHeight).into(currentHolder.imageView);
        currentHolder.textDescription.setText(goodsBean.commodity_content);
        currentHolder.textLike.setText(goodsBean.commodity_likenum);
        currentHolder.textLook.setText(goodsBean.commodity_looknum);
        GoodsUtil.setPriceBySymbol(currentHolder.textPrice, goodsBean.commodity_panicprice);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textDescription, textPrice,textLike,textLook;
        public ImageView imageView, imageType;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageType = (ImageView) view.findViewById(R.id.imageType);
            textDescription = (TextView) view.findViewById(R.id.textDescription);
            textPrice = (TextView) view.findViewById(R.id.textPrice);
            textLike = (TextView) view.findViewById(R.id.textLike);
            textLook = (TextView) view.findViewById(R.id.textLook);

        }
    }
}
