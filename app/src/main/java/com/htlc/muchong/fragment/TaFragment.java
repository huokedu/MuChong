package com.htlc.muchong.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
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
import com.htlc.muchong.activity.CangDetailActivity;
import com.htlc.muchong.activity.PersonActivity;
import com.htlc.muchong.adapter.FourthFourRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthOneRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthThreeRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthTwoRecyclerViewAdapter;
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;

/**
 * Created by sks on 2016/5/20.
 */
public class TaFragment extends HomeFragment {
    private PtrClassicFrameLayout mPtrFrame;
    private BaseRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fourth_child;
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
        initAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initAdapter() {
        //一人一语  他的故事
        if (mTitle.equals(getString(R.string.title_ta_three))) {
            adapter = new FourthOneRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(getContext(), 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = space;
                }
            });
            adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ToastUtil.showToast(App.app, "mRecyclerView position " + position);
                    Intent intent = new Intent(getActivity(), CangDetailActivity.class);
                    intent.putExtra(BaseActivity.ActivityTitleId, R.string.title_ta_three);
                    startActivity(intent);
                }
            });
            //他的藏品
        } else if (mTitle.equals(getString(R.string.title_ta_one))) {
            mRecyclerView = findViewById(R.id.recyclerView);
            adapter = new ThirdRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2) {
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(getContext(), 10);

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
            adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ToastUtil.showToast(App.app, "mRecyclerView position " + position);
                    Intent intent = new Intent(getActivity(), CangDetailActivity.class);
                    intent.putExtra(BaseActivity.ActivityTitleId, R.string.title_cang_detail);
                    startActivity(intent);

                }
            });

            //他的喜欢
        } else if (mTitle.equals(getString(R.string.title_ta_four))) {

            //他的学堂
        } else if (mTitle.equals(getString(R.string.title_ta_two))) {
            adapter = new FourthFourRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(getContext(), 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = space;
                }
            });
            adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ToastUtil.showToast(App.app, "mRecyclerView position " + position);
                    Intent intent = new Intent(getActivity(), CangDetailActivity.class);
                    intent.putExtra(BaseActivity.ActivityTitleId,R.string.fourth_title_fragment_four);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void initData() {
        if (mTitle.equals(getString(R.string.title_ta_one))) {
            initDataOne();
        } else if (mTitle.equals(getString(R.string.title_ta_two))) {
            initDataTwo();
        } else if (mTitle.equals(getString(R.string.title_ta_three))) {
            initDataThree();
        } else if (mTitle.equals(getString(R.string.title_ta_four))) {

        }
    }

    private void loadMoreData() {
        if (mTitle.equals(getString(R.string.title_ta_one))) {
            loadMoreDataOne();
        } else if (mTitle.equals(getString(R.string.title_ta_two))) {
            loadMoreDataTwo();
        } else if (mTitle.equals(getString(R.string.title_ta_three))) {
            loadMoreDataThree();
        } else if (mTitle.equals(getString(R.string.title_ta_four))) {

        }
    }


    private void loadMoreDataThree() {
        mPtrFrame.loadMoreComplete(true);
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), true);
        mPtrFrame.setNoMoreData();
    }

    private void loadMoreDataTwo() {
        mPtrFrame.loadMoreComplete(true);
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), true);
        mPtrFrame.setNoMoreData();
    }

    private void loadMoreDataOne() {
        mPtrFrame.loadMoreComplete(true);
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), true);
        mPtrFrame.setNoMoreData();
    }



    private void initDataThree() {
        mPtrFrame.refreshComplete();
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), false);
        mPtrFrame.setLoadMoreEnable(true);
    }

    private void initDataTwo() {
        mPtrFrame.refreshComplete();
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), false);
        mPtrFrame.setLoadMoreEnable(true);
    }

    private void initDataOne() {
        mPtrFrame.refreshComplete();
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), false);
        mPtrFrame.setLoadMoreEnable(true);
    }
}
