package com.htlc.muchong.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/5/20.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter{
    protected List<T> mList = new ArrayList();
    protected OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setData(List<T> data, boolean isAdd) {
        if (isAdd) {
            mList.addAll(data);
            notifyItemRangeChanged(mList.size() - data.size(), mList.size());
        } else {
            mList.clear();
            mList.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mList;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
}
