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

import model.JianBean;

/**
 * Created by sks on 2016/5/20.
 */
public class CangPersonRecyclerViewAdapter extends BaseRecyclerViewAdapter<JianBean> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_jian_list, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        JianBean bean = mList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textName.setText(bean.userinfo_nickname);
        viewHolder.textLike.setText(bean.forum_likenum);
        viewHolder.textLook.setText(bean.forum_looknum);
        PersonUtil.setPersonLevel(viewHolder.textLevel, bean.userinfo_grade);
        ImageUtil.setImageByDefault(viewHolder.imageView, R.mipmap.default_first_pai, Uri.parse(bean.forum_coverimg));
        ImageUtil.setCircleImageByDefault(viewHolder.imageHead, R.mipmap.default_third_gird_head, Uri.parse(bean.userinfo_headportrait));

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textLevel, textLike, textLook;
        public ImageView imageView, imageFlag, imageHead;

        public ViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textLevel = (TextView) view.findViewById(R.id.textLevel);
            textLike = (TextView) view.findViewById(R.id.textLike);
            textLook = (TextView) view.findViewById(R.id.textLook);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageFlag = (ImageView) view.findViewById(R.id.imageFlag);
            imageHead = (ImageView) view.findViewById(R.id.imageHead);

        }
    }
}
