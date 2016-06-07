package com.htlc.muchong.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.PersonUtil;

import java.util.ArrayList;
import java.util.List;

import model.CangBean;

/**
 * Created by sks on 2016/5/18.
 */
public class ThirdRecyclerViewAdapter extends BaseRecyclerViewAdapter<CangBean> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_third, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        CangBean bean = mList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        PersonUtil.setPersonLevel(viewHolder.textLevel, bean.userinfo_grade);
        viewHolder.textLook.setText(bean.forum_looknum);
        viewHolder.textLike.setText(bean.forum_likenum);
        viewHolder.textDescription.setText(bean.forum_content);
        viewHolder.textName.setText(bean.userinfo_nickname);

        ImageUtil.setImageByDefault(viewHolder.imageView,R.mipmap.default_first_pai, Uri.parse(bean.forum_coverimg));
        ImageUtil.setImageByDefault(viewHolder.imageHead,R.mipmap.default_third_gird_head, Uri.parse(bean.userinfo_headportrait));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textLevel;
        private final TextView textLook;
        private final TextView textLike;
        public TextView textDescription, textName;
        public ImageView imageView,imageHead;

        public ViewHolder(View view) {
            super(view);
            textDescription = (TextView) view.findViewById(R.id.textDescription);

            textName = (TextView) view.findViewById(R.id.textName);
            textLevel = (TextView) view.findViewById(R.id.textLevel);
            textLike = (TextView) view.findViewById(R.id.textLike);
            textLook = (TextView) view.findViewById(R.id.textLook);

            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageHead = (ImageView) view.findViewById(R.id.imageHead);
        }
    }

}
