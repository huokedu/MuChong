package com.htlc.muchong.activity;

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
public class JianDetailActivity extends BaseActivity {

    private ListView imageListView, resultListView, mProfessorCommentListView, mCommentListView;
    private DetailImageAdapter imageAdapter;
    private JianResultAdapter jianResultAdapter;
    private CommentAdapter commentAdapter;
    private ProfessorCommentAdapter professorCommentAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jian_detail;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_jian_detail);
        mTitleRightTextView.setText(R.string.jian_detail_jian);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app,R.string.jian_detail_jian);
            }
        });
        imageListView = (ListView) findViewById(R.id.imageListView);
        imageAdapter = new DetailImageAdapter();
        imageListView.setAdapter(imageAdapter);

        resultListView = (ListView) findViewById(R.id.resultListView);
        jianResultAdapter = new JianResultAdapter();
        resultListView.setAdapter(jianResultAdapter);

        mProfessorCommentListView = (ListView) findViewById(R.id.professorCommentListView);
        professorCommentAdapter = new ProfessorCommentAdapter();
        mProfessorCommentListView.setAdapter(professorCommentAdapter);

        mCommentListView = (ListView) findViewById(R.id.commentListView);
        commentAdapter = new CommentAdapter();
        mCommentListView.setAdapter(commentAdapter);

        initData();
    }

    @Override
    protected void initData() {
        List imageList = Arrays.asList(FirstFragment.sampleNetworkImageURLs);
        imageAdapter.setData(imageList,false);
        jianResultAdapter.setData(imageList,false);
        professorCommentAdapter.setData(imageList,false);
        commentAdapter.setData(imageList,false);
    }
}
