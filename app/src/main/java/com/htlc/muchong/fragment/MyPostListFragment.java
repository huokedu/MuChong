package com.htlc.muchong.fragment;

import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.CreateOrderActivity;
import com.htlc.muchong.activity.PostDetailActivity;
import com.htlc.muchong.adapter.FourthFourRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthOneRecyclerViewAdapter;
import com.htlc.muchong.adapter.MySchoolRecyclerViewAdapter;
import com.htlc.muchong.adapter.MyTalkRecyclerViewAdapter;
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Collections;
import java.util.List;

import core.AppActionImpl;
import model.OrderBean;
import model.PostBean;
import model.SchoolBean;

/**
 * Created by sks on 2016/5/23.
 * 我的订单列表
 */
public class MyPostListFragment extends BaseFragment {
    public CharSequence mTitle;//Fragment的标题

    public static MyPostListFragment newInstance(String title) {
        try {
            MyPostListFragment fragment = MyPostListFragment.class.newInstance();
            fragment.mTitle = title;
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private PtrClassicFrameLayout mPtrFrame;
    private BaseRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private View noDataView;

    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_post_list;
    }

    @Override
    protected void setupView() {
        noDataView = findViewById(R.id.noDataView);
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
        mRecyclerView.setBackgroundResource(R.color.bg_gray);
        initAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initAdapter() {
        if(mTitle.equals(getString(R.string.fourth_title_fragment_four))){
            adapter = new MySchoolRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(getContext(), 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = space;
                }
            });
            adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    SchoolBean bean = (SchoolBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivity(getContext(), bean.id, R.string.detail, true);
                }
            });
            ((MySchoolRecyclerViewAdapter) adapter).setOperationListener(new MySchoolRecyclerViewAdapter.OnOperationListener() {
                @Override
                public void onEditClick(int position) {
                    // TODO: 2016/8/27 定义学堂的发布和修改界面，并调试接口
                    ToastUtil.showToast(App.app,"onEditClick: "+position);
                }

                @Override
                public void onDeleteClick(int position) {
                    // TODO: 2016/8/27 实现学堂删除功能
                    ToastUtil.showToast(App.app,"onDeleteClick: "+position);
                }
            });
        }else {
            adapter = new MyTalkRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(getContext(), 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = space;
                }
            });
            adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    PostBean bean = (PostBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivity(getActivity(), bean.id, R.string.detail);
                }
            });
            ((MyTalkRecyclerViewAdapter) adapter).setOperationListener(new MyTalkRecyclerViewAdapter.OnOperationListener() {
                @Override
                public void onEditClick(int position) {
                    // TODO: 2016/8/27 定义说说的修改界面，并调试接口
                    ToastUtil.showToast(App.app,"onEditClick: "+position);
                }

                @Override
                public void onDeleteClick(int position) {
                    // TODO: 2016/8/27 实现说说删除功能
                    ToastUtil.showToast(App.app,"onDeleteClick: "+position);
                }
            });
        }

    }

    private void loadMoreData() {
        if (getString(R.string.fourth_title_fragment_one).equals(mTitle)) {
            loadMoreDataOne();
        } else if (getString(R.string.fourth_title_fragment_four).equals(mTitle)) {
            loadMoreDataTwo();
        }

    }

    private void loadMoreDataTwo() {
        // TODO: 2016/8/27 需要修改为当前用户的学堂数据（现在是论坛中的学堂的数据）
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

    private void loadMoreDataOne() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        App.app.appAction.postListByPersonId(page, LoginUtil.getUser().id, baseActivity.new BaseActionCallbackListener<List<PostBean>>() {
            @Override
            public void onSuccess(List<PostBean> data) {
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
        if (getString(R.string.fourth_title_fragment_one).equals(mTitle)) {
            initDataOne();
        } else if (getString(R.string.fourth_title_fragment_four).equals(mTitle)) {
            initDataTwo();
        }

    }

    private void initDataTwo() {
        page = 1;
        // TODO: 2016/8/27 需要修改为当前用户的学堂数据（现在是论坛中的学堂的数据）
        App.app.appAction.schoolList(page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<SchoolBean>>() {
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
        BaseActivity baseActivity = (BaseActivity) getActivity();
        page = 1;
        App.app.appAction.postListByPersonId(page, LoginUtil.getUser().id,  baseActivity.new BaseActionCallbackListener<List<PostBean>>() {
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

    /*删除确认对话框*/
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogAppCompat);
        View view = View.inflate(getContext(), R.layout.dialog_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(R.string.order_delete_tips);
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
    }
}
