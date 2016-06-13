package com.htlc.muchong.activity;

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
import com.htlc.muchong.adapter.JiaoRecyclerViewAdapter;
import com.htlc.muchong.adapter.PaiRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.List;

import core.AppActionImpl;
import model.JiaoGoodsBean;
import model.PaiGoodsBean;

/**
 * Created by sks on 2016/5/23.
 */
public class JiaoListActivity extends BaseActivity {
    private PtrClassicFrameLayout mPtrFrame;
    private JiaoRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;

    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jiao_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.first_header_jiao);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
       mPtrFrame.setLastUpdateTimeKey(null);
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
        }, 500);
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new JiaoRecyclerViewAdapter();
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2) {
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int space = CommonUtil.dp2px(JiaoListActivity.this, 10);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) % 2 == 0) {
                    outRect.bottom = space;
                    outRect.left = space;
                    outRect.right = space / 2;
                } else {
                    outRect.bottom = space;
                    outRect.right = space;
                    outRect.left = space / 2;
                }
                if (parent.getChildAdapterPosition(view) < 2) {
                    outRect.top = space;
                }

            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductDetailActivity.goProductActivity(JiaoListActivity.this, adapter.getData().get(position).id);
            }
        });

    }

    private void loadMoreData() {
        App.app.appAction.jiaoList(page, new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
            @Override
            public void onSuccess(List<JiaoGoodsBean> data) {
                mPtrFrame.refreshComplete();
                adapter.setData(data, true);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setNoMoreData();
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setFail();
            }
        });
    }

    @Override
    protected void initData() {
        page = 1;
        App.app.appAction.jiaoList(page, new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
            @Override
            public void onSuccess(List<JiaoGoodsBean> data) {
                mPtrFrame.refreshComplete();
                adapter.setData(data, false);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(false);
            }
        });
    }
}
