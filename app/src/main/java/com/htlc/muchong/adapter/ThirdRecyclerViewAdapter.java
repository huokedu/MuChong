package com.htlc.muchong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/5/18.
 */
public class ThirdRecyclerViewAdapter extends RecyclerView.Adapter {
    private List mList = new ArrayList();
    private OnItemClickListener mOnItemClickLitener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setData(List data, boolean isAdd) {
        if (isAdd) {
            mList.addAll(data);
            notifyItemRangeChanged(mList.size() - data.size(), mList.size());
        } else {
            mList.clear();
            mList.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_third, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textPrice;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textPrice = (TextView) view.findViewById(R.id.textPrice);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
