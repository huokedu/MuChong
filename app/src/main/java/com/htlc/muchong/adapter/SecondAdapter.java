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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/5/19.
 */
public class SecondAdapter extends BaseAdapter {
    private List mList = new ArrayList();
    public void setData(List data, boolean isAdd) {
        if (isAdd) {
            mList.addAll(data);
        } else {
            mList.clear();
            mList.addAll(data);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return mList.size();
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
            convertView = View.inflate(parent.getContext(),R.layout.adapter_fragment_second,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
//        Picasso.with(parent.getContext()).load(Uri.parse("http://img05.tooopen.com/images/20140604/sy_62331342149.jpg")).transform(new CircleTransform()).into(holder.imageView);
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }
}
