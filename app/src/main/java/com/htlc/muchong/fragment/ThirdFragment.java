package com.htlc.muchong.fragment;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
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
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;

/**
 * Created by sks on 2016/1/27.
 */
public class ThirdFragment extends HomeFragment {
    private PtrClassicFrameLayout mPtrFrame;
    private ThirdRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_third;
    }

    @Override
    protected void setupView() {
        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
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

        mRecyclerView = findViewById(R.id.recyclerView);
        adapter = new ThirdRecyclerViewAdapter();
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2) {
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration(){
            private int space = CommonUtil.dp2px(getContext(),10);
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if(parent.getChildAdapterPosition(view)%2 == 0){
                    outRect.bottom = space;
                    outRect.left = space;
                    outRect.right = space/2;
                }else {
                    outRect.bottom = space;
                    outRect.right = space;
                    outRect.left = space/2;
                }
                if(parent.getChildAdapterPosition(view) < 2){
                    outRect.top = space;
                }

            }
        });
        adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showToast(App.app, "mRecyclerView position " +position);
            }
        });


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
