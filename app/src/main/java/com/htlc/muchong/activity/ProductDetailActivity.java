package com.htlc.muchong.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.CommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.BannerFragment;
import com.htlc.muchong.fragment.FirstFragment;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sks on 2016/5/23.
 */
public class ProductDetailActivity extends BaseActivity{
    protected BannerFragment mBannerFragment;
    protected ListView mCommentListView;
    private CommentAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_product_detail);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "share。。。。。。。。。。");
            }
        });
        mBannerFragment = (BannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ToastUtil.showToast(App.app, "banner position = " + position);
            }
        });

        mCommentListView = (ListView) findViewById(R.id.commentListView);
        adapter = new CommentAdapter();
        mCommentListView.setAdapter(adapter);
        initData();
    }

    @Override
    protected void initData() {
        mBannerFragment.setData(Arrays.asList(FirstFragment.sampleNetworkImageURLs));
        List<String> strings = Arrays.asList(FirstFragment.sampleNetworkImageURLs);
        adapter.setData(strings,false);
    }
}
