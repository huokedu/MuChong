package com.htlc.muchong.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.R;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.ImageUtil;

import model.PersonCommentBean;
import model.SchoolBean;

/**
 * Created by sks on 2016/5/20.
 */
public class PersonCommentRecyclerViewAdapter extends com.htlc.muchong.base.BaseRecyclerViewAdapter<PersonCommentBean> {
    private static final String TIPS = "%1$s  在<font color=\"#CD8E58\">%2$s</font>的%3$s中,评论：";
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_person_comment, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        ViewHolder viewHolder = (ViewHolder) holder;
        PersonCommentBean bean = mList.get(position);
        String html = String.format(TIPS,bean.nickname,bean.pname,bean.ptype);
        viewHolder.textName.setText(Html.fromHtml(html));
        viewHolder.textComment.setText(bean.content);
        DateFormat.setTextByTime(viewHolder.textTime, bean.ctime);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textTime, textComment;

        public ViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textTime = (TextView) view.findViewById(R.id.textTime);
            textComment = (TextView) view.findViewById(R.id.textComment);
        }
    }
}
