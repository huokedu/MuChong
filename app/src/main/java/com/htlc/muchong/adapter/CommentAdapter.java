package com.htlc.muchong.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.util.CircleTransform;
import com.htlc.muchong.util.PersonUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.GoodsCommentBean;

/**
 * Created by sks on 2016/5/25.
 */
public class CommentAdapter extends BaseAdapter {
    private List<GoodsCommentBean> list = new ArrayList();
    public void setData(List<GoodsCommentBean> list,boolean isAdd){
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
        return list.get(position);
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
            convertView = View.inflate(parent.getContext(), R.layout.adapter_product_comment,null);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            holder.textLevel = (TextView) convertView.findViewById(R.id.textLevel);
            holder.textComment = (TextView) convertView.findViewById(R.id.textComment);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsCommentBean bean = list.get(position);
        Picasso.with(holder.imageView.getContext())
                .load(Uri.parse(bean.userinfo_headportrait))
                .placeholder(R.mipmap.default_fourth_two_head)
                .error(R.mipmap.default_fourth_two_head)
                .transform(new CircleTransform()).into(holder.imageView);
        holder.textName.setText(bean.userinfo_nickname);
        PersonUtil.setPersonLevel(holder.textLevel,bean.userinfo_grade);
        holder.textComment.setText(bean.commodityeval_content);
        return convertView;
    }
    public class ViewHolder{
        public TextView textComment,textName,textLevel;
        public ImageView imageView;
    }
}
