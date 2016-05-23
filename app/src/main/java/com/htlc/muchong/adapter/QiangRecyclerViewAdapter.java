package com.htlc.muchong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;

/**
 * Created by sks on 2016/5/20.
 */
public class QiangRecyclerViewAdapter extends BaseRecyclerViewAdapter {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_qiang_list, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
          super.onBindViewHolder(holder,position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textPrice;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            textName = (TextView) view.findViewById(R.id.textName);
            textPrice = (TextView) view.findViewById(R.id.textPrice);
            imageView = (ImageView) view.findViewById(R.id.imageView);

        }
    }
}
