package com.htlc.muchong.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.ImageUtil;

import model.SchoolBean;

/**
 * Created by sks on 2016/5/20.
 */
public class MySchoolRecyclerViewAdapter extends com.htlc.muchong.base.BaseRecyclerViewAdapter<SchoolBean> {

    public interface OnOperationListener{
        void onEditClick(int position);
        void onDeleteClick(int position);
    }
    private OnOperationListener operationListener;

    public void setOperationListener(OnOperationListener operationListener) {
        this.operationListener = operationListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_my_post_school, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        ViewHolder viewHolder = (ViewHolder) holder;
        SchoolBean bean = mList.get(position);
        viewHolder.textPostTitle.setText(bean.forum_title);
        viewHolder.textLook.setText(bean.forum_looknum);
        viewHolder.textLike.setText(bean.forum_likenum);
        DateFormat.setTextByTime(viewHolder.textTime, bean.forum_ctime);
        ImageUtil.setImageByDefault(viewHolder.imageView,R.mipmap.default_cang_detail, Uri.parse(bean.forum_coverimg));
        viewHolder.textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operationListener!=null){
                    operationListener.onDeleteClick(position);
                }
            }
        });
        viewHolder.textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operationListener!=null){
                    operationListener.onEditClick(position);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textPostTitle, textTime, textLike, textLook, textEdit, textDelete;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textPostTitle = (TextView) view.findViewById(R.id.textPostTitle);
            textTime = (TextView) view.findViewById(R.id.textTime);
            textLike = (TextView) view.findViewById(R.id.textLike);
            textLook = (TextView) view.findViewById(R.id.textLook);
            imageView = (ImageView) view.findViewById(R.id.imageView);

            textEdit = (TextView)  view.findViewById(R.id.textEdit);
            textDelete = (TextView)  view.findViewById(R.id.textDelete);
        }
    }
}
