package com.htlc.muchong.adapter;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.util.LogUtils;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import model.PSchoolEvent;

/**
 * Created by John_Libo on 2016/10/11.
 */
public class PublishSchoolAdapter extends BaseAdapter {
    private List<File> mList = new ArrayList();
    public void setData(List data, boolean isAdd) {
        if (isAdd) {
            mList.addAll(data);
        } else {
            mList.clear();
            mList.addAll(data);
        }
        LogUtils.e("mList---",""+mList.size());
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
            return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.adapter_publish_school,null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ps_imageView);
            holder.et = (EditText) convertView.findViewById(R.id.ps_et);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("position---",""+position);
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        File file = mList.get(position);
        Picasso.with(parent.getContext()).load(Uri.fromFile(file)).
                resizeDimen(R.dimen.publish_grid_view_size,R.dimen.publish_grid_view_size).centerCrop().into(holder.imageView);
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
        public EditText et;

    }
}
