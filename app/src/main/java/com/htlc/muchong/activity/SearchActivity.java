package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.FirstRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.List;

import core.AppActionImpl;
import model.GoodsBean;

/**
 * Created by sks on 2016/5/24.
 * 商品搜索Activity
 */
public class SearchActivity extends BaseActivity {
    private int page = 1;
    private RecyclerView.ItemDecoration decor;
    private String searchKey;

    public static void goSearchActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private PtrClassicFrameLayout mPtrFrame;
    private BaseRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private SearchView searchView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupView() {
        searchView = (SearchView) mToolbar.findViewById(R.id.searchView);
        searchView.requestFocus();
//        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchKey = query;
                initData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setTitle();
    }

    private void setTitle() {
        mRecyclerView.removeAllViews();
        mRecyclerView.removeItemDecoration(decor);
        //商品
        mRecyclerView.setBackgroundResource(android.R.color.white);
        adapter = new FirstRecyclerViewAdapter();
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2) {
        });
        decor = new RecyclerView.ItemDecoration() {
            private int space = CommonUtil.dp2px(SearchActivity.this, 10);

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
        };
        mRecyclerView.addItemDecoration(decor);

        mRecyclerView.setAdapter(mAdapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoodsBean bean = (GoodsBean) adapter.getData().get(position);
                ProductDetailActivity.goProductActivity(SearchActivity.this, bean.id);
            }
        });
    }

    private void loadMoreData() {
        App.app.appAction.searchGoods(page, searchKey, new BaseActionCallbackListener<List<GoodsBean>>() {
            @Override
            public void onSuccess(List<GoodsBean> data) {

                adapter.setData(data, true);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.loadMoreComplete(false);
                } else {
                    mPtrFrame.loadMoreComplete(true);
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
        App.app.appAction.searchGoods(page, searchKey, new BaseActionCallbackListener<List<GoodsBean>>() {
            @Override
            public void onSuccess(List<GoodsBean> data) {
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
