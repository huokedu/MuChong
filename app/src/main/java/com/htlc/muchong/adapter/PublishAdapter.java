package com.htlc.muchong.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/5/19.
 */
public class PublishAdapter extends BaseAdapter {
    public static final int MaxImageNumber = 4;
    private List<File> mList = new ArrayList();
    public void setData(List data, boolean isAdd) {
        if (isAdd) {
            mList.addAll(data);
        } else {
            mList.clear();
            mList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public List<File> getData() {
        return mList;
    }

    public void removeData(int position){
        mList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mList.size()+1 < MaxImageNumber){
            return mList.size()+1;
        }
        return MaxImageNumber;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(),R.layout.adapter_publish,null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position == mList.size()){
//            holder.imageView.setImageResource(R.mipmap.icon_publish_add_image);
            Picasso.with(parent.getContext()).load(R.mipmap.icon_publish_add_image).resizeDimen(R.dimen.publish_grid_view_size,R.dimen.publish_grid_view_size).centerCrop().into(holder.imageView);
        }else {
            File file = mList.get(position);
            Picasso.with(parent.getContext()).load(Uri.fromFile(file)).resizeDimen(R.dimen.publish_grid_view_size,R.dimen.publish_grid_view_size).centerCrop().into(holder.imageView);
        }
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
    }
}
