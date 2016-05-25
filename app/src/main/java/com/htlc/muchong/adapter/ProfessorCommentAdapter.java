package com.htlc.muchong.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/5/25.
 */
public class ProfessorCommentAdapter extends BaseAdapter {
    private List list = new ArrayList<>();
    public void setData(List list,boolean isAdd){
        if(isAdd){
            this.list.addAll(list);
        }else {
            this.list.clear();
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
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
            convertView = View.inflate(parent.getContext(), R.layout.adapter_professor_comment,null);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            holder.textLevel = (TextView) convertView.findViewById(R.id.textLevel);
            holder.textComment = (TextView) convertView.findViewById(R.id.textComment);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    public class ViewHolder{
        public TextView textComment,textName,textLevel;
        public ImageView imageView;
    }
}
