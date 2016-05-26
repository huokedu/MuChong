package com.htlc.muchong.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.CommentAdapter;
import com.htlc.muchong.adapter.DetailImageAdapter;
import com.htlc.muchong.adapter.JianResultAdapter;
import com.htlc.muchong.adapter.ProfessorCommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.FirstFragment;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sks on 2016/5/24.
 */
public class CangDetailActivity extends BaseActivity {

    private ListView imageListView,  mCommentListView;
    private DetailImageAdapter imageAdapter;
    private CommentAdapter commentAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cang_detail;
    }

    @Override
    protected void setupView() {
        int activityTitleId = getIntent().getIntExtra(ActivityTitleId,0);
        mTitleTextView.setText(activityTitleId);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "share。。。。。。。。。。");
            }
        });

        imageListView = (ListView) findViewById(R.id.imageListView);
        imageAdapter = new DetailImageAdapter();
        imageListView.setAdapter(imageAdapter);


        mCommentListView = (ListView) findViewById(R.id.commentListView);
        commentAdapter = new CommentAdapter();
        mCommentListView.setAdapter(commentAdapter);

        initData();
    }

    @Override
    protected void initData() {
        List imageList = Arrays.asList(FirstFragment.sampleNetworkImageURLs);
        imageAdapter.setData(imageList,false);
        commentAdapter.setData(imageList,false);
    }
}
