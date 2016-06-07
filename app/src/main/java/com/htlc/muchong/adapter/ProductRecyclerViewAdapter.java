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
import com.htlc.muchong.util.PersonUtil;
import com.squareup.picasso.Picasso;

import model.JiaoGoodsBean;

/**
 * Created by sks on 2016/5/20.
 */
public class ProductRecyclerViewAdapter extends BaseRecyclerViewAdapter<JiaoGoodsBean> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_product_list, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        JiaoGoodsBean bean = mList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textName.setText(bean.userinfo_nickname);
        GoodsUtil.setPriceBySymbol(viewHolder.textPrice, bean.commodity_panicprice);
        PersonUtil.setPersonLevel(viewHolder.textLevel, bean.userinfo_grade);
        viewHolder.textDescription.setText(bean.commodity_content);
        viewHolder.textLike.setText(bean.commodity_likenum);
        viewHolder.textLook.setText(bean.commodity_looknum);
        viewHolder.imageFlag.setVisibility(GoodsUtil.isTrue(bean.userinfo_sincerity) ? View.VISIBLE : View.INVISIBLE);
        int intrinsicWidth = viewHolder.imageView.getResources().getDrawable(R.mipmap.default_first_pai).getIntrinsicWidth();
        int intrinsicHeight = viewHolder.imageView.getResources().getDrawable(R.mipmap.default_first_pai).getIntrinsicHeight();
        Picasso.with(viewHolder.itemView.getContext()).load(Uri.parse(bean.commodity_coverimg)).placeholder(R.mipmap.default_first_pai).error(R.mipmap.default_first_pai).resize(intrinsicWidth,intrinsicHeight).into(viewHolder.imageView);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLevel;
        private final TextView textDescription;
        private final TextView textLike;
        private final TextView textLook;
        public TextView textName, textPrice;
        public ImageView imageView, imageFlag, imageHead;

        public ViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textPrice = (TextView) view.findViewById(R.id.textPrice);
            textLevel = (TextView) view.findViewById(R.id.textLevel);
            textDescription = (TextView) view.findViewById(R.id.textDescription);
            textLike = (TextView) view.findViewById(R.id.textLike);
            textLook = (TextView) view.findViewById(R.id.textLook);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageFlag = (ImageView) view.findViewById(R.id.imageFlag);
            imageHead = (ImageView) view.findViewById(R.id.imageHead);

        }
    }
}
