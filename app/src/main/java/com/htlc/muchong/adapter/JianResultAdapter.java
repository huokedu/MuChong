package com.htlc.muchong.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.fragment.JianListFragment;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import model.JianResultBean;

/**
 * Created by sks on 2016/5/25.
 */
public class JianResultAdapter extends BaseAdapter {
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
            convertView = View.inflate(parent.getContext(), R.layout.adapter_jian_detail_professor,null);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            holder.textResult = (TextView) convertView.findViewById(R.id.textResult);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        JianResultBean bean = list.get(position);
        ImageUtil.setCircleImageByDefault(holder.imageView,R.mipmap.default_third_gird_head, Uri.parse(bean.userinfo_headportrait));
        holder.textName.setText(bean.userinfo_nickname+":");
        setTextResultByType(holder.textResult,bean.appraisal_type);

        return convertView;
    }
    public class ViewHolder{
        public TextView textResult,textName;
        public ImageView imageView;
    }
    private void setTextResultByType(TextView textView, String type){
        if(JianListFragment.TYPE_1.equals(type)){
            textView.setText("真");
            textView.setTextColor(textView.getResources().getColor(R.color.text_color_red));
        }else{
            textView.setText("假");
            textView.setTextColor(textView.getResources().getColor(R.color.text_color_orange));
        }
    }
}
