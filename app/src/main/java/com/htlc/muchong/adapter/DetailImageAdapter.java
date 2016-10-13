package com.htlc.muchong.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.htlc.muchong.R;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/5/25.
 */
public class DetailImageAdapter extends BaseAdapter{
    private ArrayList<String> descriptions = new ArrayList<>();
    private List<String> list = new ArrayList<>();

    public void setContentJsonArrayStr(String contentJsonArrayStr) {
        JSONArray objects = JSON.parseArray(contentJsonArrayStr);
        for(int i=0; i<objects.size(); i++){
            String desc = objects.getJSONObject(i).getString("forum_content");
            descriptions.add(desc);
        }
        LogUtils.e("descriptions--",""+descriptions);
    }

    public void setData(List<String> list, boolean isAdd){
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
            convertView = View.inflate(parent.getContext(), R.layout.adapter_jian_detail_image,null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textDescription = (TextView) convertView.findViewById(R.id.textDescription);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        LogUtils.e("list.get(position)---",""+list.get(position));
        ImageUtil.setImageByDefault(holder.imageView,R.mipmap.default_cang_detail, Uri.parse(list.get(position)));
//        Picasso.with().load(Uri.parse(list.get(position))).placeholder(R.mipmap.default_cang_detail).
//                error(R.mipmap.default_cang_detail).resizeDimen(R.dimen.banner_width,R.dimen.banner_height).into(holder.imageView);
        if(descriptions.size()>position){
            holder.textDescription.setText(descriptions.get(position));
            holder.textDescription.setVisibility(View.VISIBLE);
        }else {
            holder.textDescription.setVisibility(View.GONE);
        }
        return convertView;
    }
    public class ViewHolder{
        public ImageView imageView;
        public TextView textDescription;
    }
}
