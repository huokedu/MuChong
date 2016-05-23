package com.htlc.muchong.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;

/**
 * Created by sks on 2016/5/20.
 */
public class FifthRecyclerViewAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEAD = 1;
    public static final int TYPE_BODY = 0;
    protected int[] iconArray = {R.mipmap.icon_fifth_chong,R.mipmap.icon_fifth_gou,R.mipmap.icon_fifth_shou,R.mipmap.icon_fifth_jian,
            R.mipmap.icon_fifth_jiao,R.mipmap.icon_fifth_jing,R.mipmap.icon_fifth_lun,R.mipmap.icon_fifth_xiao,R.mipmap.icon_fifth_setting};
    protected int[] nameArray = {R.string.fifth_chong,R.string.fifth_gou,R.string.fifth_shou,R.string.fifth_jian,
            R.string.fifth_jiao,R.string.fifth_jing,R.string.fifth_lun,R.string.fifth_xiao,R.string.fifth_setting};
    protected OnItemClickListener mOnItemClickListener;
    private HeadViewHolder headViewHolder;

    private int money;

    public void setMoney(int money) {
        this.money = money;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return iconArray.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BODY) {
            View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_fifth, null);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        } else {
            View view = View.inflate(parent.getContext(), R.layout.header_fragment_fifth, null);
            WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);//屏幕宽度
            int height = (int) (point.x * 0.568);
            view.setLayoutParams(new RecyclerView.LayoutParams(point.x, height));
            headViewHolder = new HeadViewHolder(view);
            return headViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_BODY) {
            final int pos = position-1;
            if(mOnItemClickListener != null ){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(pos);
                    }
                });
            }
            ViewHolder tempHolder = (ViewHolder) holder;
            tempHolder.imageView.setImageResource(iconArray[pos]);
            tempHolder.textName.setText(nameArray[pos]);
            if(pos==0){
                tempHolder.textOther.setText("余额￥"+money);
            }else {
                tempHolder.textOther.setText("");
            }
        } else if (holder.getItemViewType() == TYPE_HEAD) {

        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textOther;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textOther = (TextView) view.findViewById(R.id.textOther);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textPrice;
        public ImageView imageView;

        public HeadViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textPrice = (TextView) view.findViewById(R.id.textPrice);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
