package com.htlc.muchong.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;

import java.util.ArrayList;
import java.util.List;

import model.JianResultBean;

/**
 * Created by sks on 2016/5/25.
 */
public class ProfessorCommentAdapter extends BaseAdapter {
    private List<JianResultBean> list = new ArrayList<>();
    public void setData(List<JianResultBean> list,boolean isAdd){
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
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        JianResultBean bean = list.get(position);
        TextView textView = (TextView) convertView;
        textView.setText(bean.userinfo_nickname+":  "+bean.appraisal_content);
        return convertView;
    }
    public class ViewHolder{
        public TextView textProfessorComment;
    }
}
