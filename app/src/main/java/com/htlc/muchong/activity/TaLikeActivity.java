package com.htlc.muchong.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.PaiRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.SecondFragment;
import com.larno.util.ToastUtil;

import java.util.Arrays;

/**
 * Created by sks on 2016/5/24.
 */
public class TaLikeActivity extends BaseActivity {

    private PtrClassicFrameLayout mPtrFrame;
    private PaiRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pai_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_ta_like);
        mTitleRightTextView.setText("");
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "share。。。。。。。。。。");
            }
        });

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 200);
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new PaiRecyclerViewAdapter();
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadMoreData() {
        mPtrFrame.loadMoreComplete(true);
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), true);
        mPtrFrame.setNoMoreData();
    }

    @Override
    protected void initData() {
        mPtrFrame.refreshComplete();
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), false);
        mPtrFrame.setLoadMoreEnable(true);
    }
}
