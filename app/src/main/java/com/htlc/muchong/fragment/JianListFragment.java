package com.htlc.muchong.fragment;

import android.content.Intent;
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
import com.htlc.muchong.activity.JianDetailActivity;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.adapter.JianRecyclerViewAdapter;
import com.htlc.muchong.adapter.QiangRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.JianBean;
import model.PaiGoodsBean;

/**
 * Created by sks on 2016/5/23.
 * 专家鉴定列表的Fragment
 */
public class JianListFragment extends BaseFragment {
    public static final String TYPE_1 = "1";
    public static final String TYPE_2 = "2";
    public static final String TYPE_3 = "3";
    public CharSequence mTitle;
    private String mType;
    public static JianListFragment newInstance(String title, String type) {
        try {
            JianListFragment fragment = JianListFragment.class.newInstance();
            fragment.mTitle = title;
            fragment.mType = type;
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private PtrClassicFrameLayout mPtrFrame;
    private JianRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_jian_list;
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
        adapter = new JianRecyclerViewAdapter();
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2) {
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
                if(parent.getChildAdapterPosition(view) < 2){
                    outRect.top = space;
                }
            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JianDetailActivity.goJianDetailActivity(getContext(),adapter.getData().get(position).id,!mType.equals(TYPE_3));
            }
        });


    }

    private void loadMoreData() {
        App.app.appAction.jianList(page,mType, ((BaseActivity)getActivity()).new BaseActionCallbackListener<List<JianBean>>() {
            @Override
            public void onSuccess(List<JianBean> data) {
                handleData(data);
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
        App.app.appAction.jianList(page,mType, ((BaseActivity)getActivity()).new BaseActionCallbackListener<List<JianBean>>() {
            @Override
            public void onSuccess(List<JianBean> data) {
                handleData(data);
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

    private void handleData(List<JianBean> data) {
        for(JianBean bean : data){
            bean.forum_yesorno = mType;
        }
    }
}
