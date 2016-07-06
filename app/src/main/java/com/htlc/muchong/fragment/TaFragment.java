package com.htlc.muchong.fragment;

import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
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
import com.htlc.muchong.adapter.CangPersonRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthFourRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthOneRecyclerViewAdapter;
import com.htlc.muchong.adapter.PersonCommentRecyclerViewAdapter;
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.List;

import core.AppActionImpl;
import model.CangBean;
import model.JianBean;
import model.PersonCommentBean;
import model.PostBean;
import model.SchoolBean;

/**
 * Created by sks on 2016/5/20.
 */
public class TaFragment extends HomeFragment {
    private PtrClassicFrameLayout mPtrFrame;
    private BaseRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private int page;
    private String personId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fourth_child;
    }

    @Override
    protected void setupView() {
        personId = ((PersonActivity) getActivity()).getPersonId();
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
                    PostBean bean = (PostBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivity(getContext(), bean.id, R.string.detail);
                }
            });
            //他的藏品
        } else if (mTitle.equals(getString(R.string.title_ta_one))) {
            adapter = new CangPersonRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (mAdapter.isHeader(position) || mAdapter.isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
                }
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
                    JianBean bean = (JianBean) adapter.getData().get(position);
                    CangDetailActivity.goCangDetailActivity(getContext(), bean.id, R.string.title_cang_detail);

                }
            });

            //他的喜欢
        } else if (mTitle.equals(getString(R.string.title_ta_four))) {

            //他的评论
        } else if (mTitle.equals(getString(R.string.title_ta_new))) {
            adapter = new PersonCommentRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(getContext(), 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = space;
                }
            });
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
                    SchoolBean bean = (SchoolBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivity(getContext(), bean.id, R.string.detail, true);
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
        } else if (mTitle.equals(getString(R.string.title_ta_new))) {
            initDataComment();
        } else if (mTitle.equals(getString(R.string.title_ta_four))) {
            mPtrFrame.refreshComplete();
            mPtrFrame.setLoadMoreEnable(false);
        }
    }

    private void loadMoreData() {
        if (mTitle.equals(getString(R.string.title_ta_one))) {
            loadMoreDataOne();
        } else if (mTitle.equals(getString(R.string.title_ta_two))) {
            loadMoreDataTwo();
        } else if (mTitle.equals(getString(R.string.title_ta_three))) {
            loadMoreDataThree();
        } else if (mTitle.equals(getString(R.string.title_ta_new))) {
            loadMoreDataComment();
        } else if (mTitle.equals(getString(R.string.title_ta_four))) {

        }
    }

    private void loadMoreDataComment() {
        App.app.appAction.personCommentList(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PersonCommentBean>>() {
            @Override
            public void onSuccess(List<PersonCommentBean> data) {
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
        App.app.appAction.postListByPersonId(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PostBean>>() {
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

    private void loadMoreDataTwo() {
        App.app.appAction.schoolListByPersonId(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<SchoolBean>>() {
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

    private void loadMoreDataOne() {
        App.app.appAction.cangListByPersonId(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<JianBean>>() {
            @Override
            public void onSuccess(List<JianBean> data) {

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


    private void initDataComment() {
        page = 1;
        App.app.appAction.personCommentList(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PersonCommentBean>>() {
            @Override
            public void onSuccess(List<PersonCommentBean> data) {
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

    private void initDataThree() {
        page = 1;
        App.app.appAction.postListByPersonId(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<PostBean>>() {
            @Override
            public void onSuccess(List<PostBean> data) {
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

    private void initDataTwo() {
        page = 1;
        App.app.appAction.schoolListByPersonId(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<SchoolBean>>() {
            @Override
            public void onSuccess(List<SchoolBean> data) {
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

    private void initDataOne() {
        page = 1;
        App.app.appAction.cangListByPersonId(page, personId, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<JianBean>>() {
            @Override
            public void onSuccess(List<JianBean> data) {
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
