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

import model.GoodsCommentBean;
import model.PaiGoodsBean;

/**
 * Created by sks on 2016/5/20.
 */
public class CommentRecyclerViewAdapter extends BaseRecyclerViewAdapter<GoodsCommentBean> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_product_comment, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder currentHolder = (ViewHolder) holder;
        GoodsCommentBean bean = mList.get(position);

        Picasso.with(currentHolder.imageView.getContext()).load(Uri.parse(bean.userinfo_headportrait)).placeholder(R.mipmap.default_first_pai).error(R.mipmap.default_first_pai).into(currentHolder.imageView);
        currentHolder.textName.setText(bean.userinfo_nickname);
        PersonUtil.setPersonLevel(currentHolder.textLevel,bean.userinfo_grade);
        currentHolder.textComment.setText(bean.commodityeval_content);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textLevel, textComment;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textName = (TextView) view.findViewById(R.id.textName);
            textLevel = (TextView) view.findViewById(R.id.textLevel);
            textComment = (TextView) view.findViewById(R.id.textComment);

        }
    }
}
