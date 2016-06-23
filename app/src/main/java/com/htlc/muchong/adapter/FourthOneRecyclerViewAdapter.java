package com.htlc.muchong.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.PersonUtil;

import model.PostBean;

/**
 * Created by sks on 2016/5/20.
 */
public class FourthOneRecyclerViewAdapter extends com.htlc.muchong.base.BaseRecyclerViewAdapter<PostBean> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_fourth_one, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        ViewHolder viewHolder = (ViewHolder) holder;
        PostBean bean = mList.get(position);
        ImageUtil.setCircleImageByDefault(viewHolder.imageHead, R.mipmap.default_third_gird_head, Uri.parse(bean.userinfo_headportrait));
        viewHolder.textName.setText(bean.userinfo_nickname);
        PersonUtil.setPersonLevel(viewHolder.textLevel, bean.userinfo_grade);
        DateFormat.setTextByTime(viewHolder.textTime, bean.forum_ctime);

        String[] images = bean.forum_imgstr.split(ProductDetailActivity.SPLIT_FLAG);
        if(images.length>=3){
            ImageUtil.setImageByDefault(viewHolder.image3,R.mipmap.default_first_qiang,Uri.parse(images[2]));
            viewHolder.image3.setVisibility(View.VISIBLE);
        }else {
            viewHolder.image3.setVisibility(View.INVISIBLE);
        }
        if(images.length>=2){
            ImageUtil.setImageByDefault(viewHolder.image2,R.mipmap.default_first_qiang,Uri.parse(images[1]));
            viewHolder.image2.setVisibility(View.VISIBLE);
        }else {
            viewHolder.image2.setVisibility(View.INVISIBLE);
        }
        if(images.length>=1){
            ImageUtil.setImageByDefault(viewHolder.image1,R.mipmap.default_first_qiang,Uri.parse(images[0]));
            viewHolder.image1.setVisibility(View.VISIBLE);
        }else {
            viewHolder.image1.setVisibility(View.INVISIBLE);
        }
        viewHolder.textPostTitle.setText(bean.forum_title);
        viewHolder.textDescription.setText(bean.forum_content);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageHead, image1, image2, image3;
        public TextView textName, textLevel ,textTime, textPostTitle, textDescription;

        public ViewHolder(View view) {
            super(view);
            imageHead = (ImageView)  view.findViewById(R.id.imageHead);
            textName = (TextView) view.findViewById(R.id.textName);
            textLevel = (TextView)  view.findViewById(R.id.textLevel);
            textTime = (TextView)  view.findViewById(R.id.textTime);

            image1 = (ImageView)  view.findViewById(R.id.image1);
            image2 = (ImageView)  view.findViewById(R.id.image2);
            image3 = (ImageView)  view.findViewById(R.id.image3);
            textPostTitle = (TextView)  view.findViewById(R.id.textPostTitle);
            textDescription = (TextView)  view.findViewById(R.id.textDescription);
        }
    }
}
