package com.htlc.muchong.fragment;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.PaiDetailActivity;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.adapter.QiangRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.widget.DaoJiShiView;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;

import core.AppActionImpl;
import model.GoodsBean;
import model.PaiGoodsBean;
import model.QiangListBean;

/**
 * Created by sks on 2016/5/23.
 * 抢购列表Fragment
 */
public class QiangListFragment extends BaseFragment {
    public static final String TYPE_1 = "1";
    public static final String TYPE_2 = "2";
    public static final String TYPE_3 = "3";
    public static final String TYPE_4 = "4";
    public CharSequence mTitle;
    private String mType;
    private DaoJiShiView daoJiShiView;
    private TextView textView;
    private View noDataView;
    private ProgressDialog progressDialog;

    public static QiangListFragment newInstance(String title, String type) {
        try {
            QiangListFragment fragment = QiangListFragment.class.newInstance();
            fragment.mTitle = title;
            fragment.mType = type;
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private PtrClassicFrameLayout mPtrFrame;
    private QiangRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qiang_list;
    }

    @Override
    protected void setupView() {
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
        noDataView = findViewById(R.id.noDataView);
//        noDataView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressDialog = new ProgressDialog(getContext());
//                progressDialog.setMessage("请稍等...");
//                progressDialog.show();
//                initData();
//            }
//        });

//        mPtrFrame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrame.autoRefresh();
//            }
//        }, 500);
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        daoJiShiView = (DaoJiShiView) findViewById(R.id.daoJiShiView);
        daoJiShiView.setData(0, 0);
        textView = (TextView) findViewById(R.id.textView);

        mRecyclerView = findViewById(R.id.recyclerView);
        adapter = new QiangRecyclerViewAdapter();
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

            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoodsBean bean = adapter.getData().get(position);
                ProductDetailActivity.goProductActivity(getContext(), bean.id, true);
            }
        });

        initData();
    }

    private void loadMoreData() {
        App.app.appAction.qiangList(mType, page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<QiangListBean>() {
            @Override
            public void onSuccess(QiangListBean data) {
                mPtrFrame.refreshComplete();
                adapter.setData(data.list, true);
                if (data.list.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setNoMoreData();
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                refreshView(data);
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
        App.app.appAction.qiangList(mType, page, ((BaseActivity) getActivity()).new BaseActionCallbackListener<QiangListBean>() {
            @Override
            public void onSuccess(QiangListBean data) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                mPtrFrame.refreshComplete();
                noDataView.setVisibility(View.GONE);
                adapter.setData(data.list, false);
                if (data.list.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                refreshView(data);
                page++;
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                mPtrFrame.refreshComplete();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                showOrHiddenNoDataView(adapter.getData(), noDataView);
                ToastUtil.showToast(App.app, message);
                mPtrFrame.setLoadMoreEnable(false);
            }
        });
    }

    /*刷新数据*/
    private void refreshView(QiangListBean data) {
        daoJiShiView.setVisibility(View.VISIBLE);
        //刷新抢购数据
        if (PaiGoodsBean.STATE_NO_START.equals(data.state)) {
            daoJiShiView.setData(Long.parseLong(data.timeStr) * 1000, Long.parseLong(data.timeend) * 1000);
        } else if (PaiGoodsBean.STATE_END.equals(data.state)) {
            daoJiShiView.setData(0, 0);
        } else if (PaiGoodsBean.STATE_STARTING.equals(data.state)) {
            daoJiShiView.setData(0, Long.parseLong(data.timeStr) * 1000);
        }
        textView.setText(getString(R.string.qiang_list_start, data.buytime));
    }
}
