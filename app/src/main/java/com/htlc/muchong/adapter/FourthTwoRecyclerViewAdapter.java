package com.htlc.muchong.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.PersonUtil;

import model.PersonBean;

/**
 * Created by sks on 2016/5/20.
 */
public class FourthTwoRecyclerViewAdapter extends com.htlc.muchong.base.BaseRecyclerViewAdapter<PersonBean> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_fourth_two, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        ViewHolder viewHolder = (ViewHolder) holder;
        PersonBean bean = mList.get(position);
        viewHolder.textName.setText(bean.userinfo_nickname);
        viewHolder.textLike.setText(bean.userinfo_likenum);
        viewHolder.textOrder.setText(String.valueOf(position+1));
        PersonUtil.setPersonLevel(viewHolder.textLevel, bean.userinfo_grade);
        ImageUtil.setImageByDefault(viewHolder.imageHead,R.mipmap.default_fourth_two_head, Uri.parse(bean.userinfo_headportrait));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textLevel, textOrder, textLike;
        public ImageView imageHead;

        public ViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textOrder = (TextView) view.findViewById(R.id.textOrder);
            textLike = (TextView) view.findViewById(R.id.textLike);
            textLevel = (TextView) view.findViewById(R.id.textLevel);
            imageHead = (ImageView) view.findViewById(R.id.imageHead);
        }
    }
}
