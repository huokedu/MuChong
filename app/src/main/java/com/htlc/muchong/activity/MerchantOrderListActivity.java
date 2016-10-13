package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.LogUtils;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import core.AppActionImpl;
import model.AddressBean;
import model.MerchantOrderListBean;
import model.MyPaiBean;
import model.OrderPayEvent;
import model.ShoppingCartItemBean;

/**
 * 我的——商户----我的订单页
 */
public class MerchantOrderListActivity extends BaseActivity {
    private PtrClassicFrameLayout mPtrFrame;
    private MyPaiRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private View noDataView;

    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pai_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_dd);
        noDataView =  findViewById(R.id.noDataView);
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
        adapter = new MyPaiRecyclerViewAdapter();
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int space = CommonUtil.dp2px(MerchantOrderListActivity.this, 10);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = space;
            }
        });
        adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadMoreData() {
        App.app.appAction.merchantOrderList(page, new BaseActionCallbackListener<List<MerchantOrderListBean>>() {
            @Override
            public void onSuccess(List<MerchantOrderListBean> data) {
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
        App.app.appAction.merchantOrderList(page, new BaseActionCallbackListener<List<MerchantOrderListBean>>() {
            @Override
            public void onSuccess(List<MerchantOrderListBean> data) {
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



    private void goExpressActivity(String s) {
        ExpressActivity.goExpressActivity(this, s);
    }

    List<String> sList = new ArrayList<>();
    private class MyPaiRecyclerViewAdapter extends BaseRecyclerViewAdapter<MerchantOrderListBean> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.adapter_mchant_order_list, null);
            ViewHolder vh = new ViewHolder(view);
            return vh;

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            super.onBindViewHolder(holder, position);
            ViewHolder viewHolder = (ViewHolder) holder;
            MerchantOrderListBean bean = mList.get(position);
            sList.add(position,bean.order_no);
            viewHolder.tv1.setText(bean.order_no);
            viewHolder.tv2.setText(bean.order_userid);
            viewHolder.tv3.setText(bean.order_ispay);
            viewHolder.tv4.setText(bean.order_payment);
            viewHolder.tv5.setText(bean.order_status);
            viewHolder.tv6.setText(bean.order_ctime);
            viewHolder.tv_address.setText(bean.order_address);
            viewHolder.tv_name.setText(bean.pname[0]);
            if(bean.logistics.equals("1")){
                viewHolder.tv_btn.setText("发货");
            }else {
                viewHolder.tv_btn.setText("已发货");
            }
            viewHolder.tv_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mBtn = (TextView) v;
                    goExpressActivity(sList.get(position));
//                    if(mBtn.getText().toString().equals("发货")){
//                        goExpressActivity(sList.get(position));
//
//
//                    }
                }
            });

        }



        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv1, tv2, tv3, tv4, tv5,tv6,tv_btn,tv_name,tv_address;

            public ViewHolder(View view) {
                super(view);
                tv1 = (TextView) view.findViewById(R.id.mol_tv1);
                tv2 = (TextView) view.findViewById(R.id.mol_tv2);
                tv3 = (TextView) view.findViewById(R.id.mol_tv3);
                tv4 = (TextView) view.findViewById(R.id.mol_tv4);
                tv5 = (TextView) view.findViewById(R.id.mol_tv5);
                tv6 = (TextView) view.findViewById(R.id.mol_tv6);
                tv_btn = (TextView) view.findViewById(R.id.tv_btn);
                tv_name = (TextView) view.findViewById(R.id.mol_tv_name);
                tv_address = (TextView) view.findViewById(R.id.mol_tv_address);
            }
        }
    }


    public void onEventMainThread(OrderPayEvent event) {
        String msg = event.getMsg();
        LogUtils.e("msg---", "" + msg);
        if (TextUtils.isEmpty(msg)) {
        } else {
            if (msg.equals("1")) {
                initData();
            }
        }
    }


}
