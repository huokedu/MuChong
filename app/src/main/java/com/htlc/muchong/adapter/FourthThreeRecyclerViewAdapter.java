package com.htlc.muchong.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.util.ImageUtil;

import model.ActivityBean;

/**
 * Created by sks on 2016/5/20.
 */
public class FourthThreeRecyclerViewAdapter extends com.htlc.muchong.base.BaseRecyclerViewAdapter<ActivityBean> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_fourth_three, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder viewHolder = (ViewHolder) holder;
        ActivityBean bean = mList.get(position);
        ImageUtil.setImageByDefault(viewHolder.imageView,R.mipmap.default_cang_detail, Uri.parse(bean.forum_coverimg));
        viewHolder.textPostTitle.setText(bean.forum_title);
        long remainingTime = Long.parseLong(bean.lastDay);
        remainingTime = (long) Math.ceil(remainingTime*1.0/3600/24);
        viewHolder.textTime.setText("剩余" + String.valueOf(remainingTime) + "天");
        viewHolder.frameOver.setVisibility(remainingTime > 0 ? View.INVISIBLE : View.VISIBLE);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textPostTitle, textTime;
        public ImageView imageView;
        public FrameLayout frameOver;

        public ViewHolder(View view) {
            super(view);
            textPostTitle = (TextView) view.findViewById(R.id.textPostTitle);
            textTime = (TextView) view.findViewById(R.id.textTime);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            frameOver = (FrameLayout) view.findViewById(R.id.frameOver);
        }
    }
}
