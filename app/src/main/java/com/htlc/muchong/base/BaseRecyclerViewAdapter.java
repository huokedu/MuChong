package com.htlc.muchong.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/5/20.
 * RecyclerView.Adapter的父类，对其进行了简单封装
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter{
    protected List<T> mList = new ArrayList();
    protected OnItemClickListener mOnItemClickListener;

    /**
     * 条目点击事件监听器
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 设置条目点击事件监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 给Adapter设置数据
     * @param data 数据集合
     * @param isAdd 是追加数据为true，false覆盖数据
     */
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

    /**
     * 获取Adapter中的数据集合
     * @return
     */
    public List<T> getData() {
        return mList;
    }

    /**
     * 给position位置的条目添加了点击事件
     * Adapter子类onBindViewHolder应调用这个方法
     * @param holder
     * @param position
     */
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
