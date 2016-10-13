package com.htlc.muchong.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.htlc.muchong.activity.PostDetailActivity;
import com.htlc.muchong.adapter.FourthFourRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthOneRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthThreeRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthTwoRecyclerViewAdapter;
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.LogUtils;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.ActivityBean;
import model.GoodsCommentBean;
import model.PersonBean;
import model.PostBean;
import model.SchoolBean;

/**
 * Created by sks on 2016/5/20.
 * 论坛的四个Fragment
 */
public class FourthChildOneFragment extends HomeFragment {
    private PtrClassicFrameLayout mPtrFrame;
    private BaseRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private View noDataView;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fourth_child;
    }

    @Override
    protected void setupView() {
        noDataView = findViewById(R.id.noDataView);
        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
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

        mRecyclerView = findViewById(R.id.recyclerView);
        initAdapter();
        mRecyclerView.setAdapter(mAdapter);

        initData();
    }

    /*根据Fragment的标题，设置对应的Adapter*/
    private void initAdapter() {
        if (getString(R.string.fourth_title_fragment_one).equals(mTitle)) {
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
                    PostBean bean = (PostBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivityByShuoShuo(getContext(), bean.id, R.string.detail,true);
                }
            });
        } else if (getString(R.string.fourth_title_fragment_two).equals(mTitle)) {
            adapter = new FourthTwoRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    PersonBean bean = (PersonBean) adapter.getData().get(position);
                    PersonActivity.goPersonActivity(getContext(), bean.id);
                }
            });
        } else if (getString(R.string.fourth_title_fragment_three).equals(mTitle)) {
            adapter = new FourthThreeRecyclerViewAdapter();
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
                    ActivityBean bean = (ActivityBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivity(getContext(), bean.id, R.string.detail);
                }
            });
        } else if (getString(R.string.fourth_title_fragment_four).equals(mTitle)) {
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
                    SchoolBean bean = (SchoolBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivity(getContext(), bean.id, R.string.detail, true);
                }
            });
        }
    }

    @Override
    protected void initData() {
        if (getString(R.string.fourth_title_fragment_one).equals(mTitle)) {
            initDataOne();
        } else if (getString(R.string.fourth_title_fragment_two).equals(mTitle)) {
            initDataTwo();
        } else if (getString(R.string.fourth_title_fragment_three).equals(mTitle)) {
            initDataThree();
        } else if (getString(R.string.fourth_title_fragment_four).equals(mTitle)) {
            initDataFour();
        }
    }

    private void loadMoreData() {
        if (getString(R.string.fourth_title_fragment_one).equals(mTitle)) {
            loadMoreDataOne();
        } else if (getString(R.string.fourth_title_fragment_two).equals(mTitle)) {
            loadMoreDataTwo();
        } else if (getString(R.string.fourth_title_fragment_three).equals(mTitle)) {
            loadMoreDataThree();
        } else if (getString(R.string.fourth_title_fragment_four).equals(mTitle)) {
            loadMoreDataFour();
        }
    }

    private void loadMoreDataFour() {
        App.app.appAction.schoolList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<SchoolBean>>() {
            @Override
            public void onSuccess(List<SchoolBean> data) {
                mPtrFrame.loadMoreComplete(true);
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

    private void loadMoreDataThree() {
        App.app.appAction.activityList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<ActivityBean>>() {
            @Override
            public void onSuccess(List<ActivityBean> data) {
                mPtrFrame.loadMoreComplete(true);
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

    private void loadMoreDataTwo() {
        App.app.appAction.personList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PersonBean>>() {
            @Override
            public void onSuccess(List<PersonBean> data) {
                mPtrFrame.loadMoreComplete(true);
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

    private void loadMoreDataOne() {
        App.app.appAction.postList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PostBean>>() {
            @Override
            public void onSuccess(List<PostBean> data) {
                mPtrFrame.loadMoreComplete(true);
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


    private void initDataFour() {
        page = 1;
        App.app.appAction.schoolList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<SchoolBean>>() {
            @Override
            public void onSuccess(List<SchoolBean> data) {
                LogUtils.e("data---",""+data);
                mPtrFrame.refreshComplete();
                adapter.setData(data, false);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(false);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }
        });
    }

    private void initDataThree() {
        page = 1;
        App.app.appAction.activityList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<ActivityBean>>() {
            @Override
            public void onSuccess(List<ActivityBean> data) {
                LogUtils.e("data---",""+data);
                mPtrFrame.refreshComplete();
                adapter.setData(data, false);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(false);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }
        });
    }

    private void initDataTwo() {
        page = 1;
        App.app.appAction.personList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PersonBean>>() {
            @Override
            public void onSuccess(List<PersonBean> data) {
                LogUtils.e("data---",""+data);
                mPtrFrame.refreshComplete();
                adapter.setData(data, false);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(false);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }
        });
    }

    private void initDataOne() {
        page = 1;
        App.app.appAction.postList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PostBean>>() {
            @Override
            public void onSuccess(List<PostBean> data) {
                LogUtils.e("data---",""+data);
                mPtrFrame.refreshComplete();
                adapter.setData(data, false);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(false);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }
        });
    }
}
