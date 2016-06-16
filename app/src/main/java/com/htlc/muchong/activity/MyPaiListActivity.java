package com.htlc.muchong.activity;

import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import api.Api;
import core.AppActionImpl;
import model.MessageBean;
import model.MyPaiBean;
import model.UserBean;

/**
 * Created by sks on 2016/5/23.
 */
public class MyPaiListActivity extends BaseActivity {
    private PtrClassicFrameLayout mPtrFrame;
    private MyPaiRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;

    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pai_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_jing);
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
            private int space = CommonUtil.dp2px(MyPaiListActivity.this, 10);

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
        App.app.appAction.myPaiList(page, new BaseActionCallbackListener<List<MyPaiBean>>() {
            @Override
            public void onSuccess(List<MyPaiBean> data) {
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


//    protected void initData() {
//        List<MyPaiBean> list = new ArrayList<>();
//        for(int i=0; i<20; i++){
//            list.add(new MyPaiBean());
//        }
//        adapter.setData(list, false);
//    }
    @Override
    protected void initData() {
        page = 1;
        App.app.appAction.myPaiList(page, new BaseActionCallbackListener<List<MyPaiBean>>() {
            @Override
            public void onSuccess(List<MyPaiBean> data) {
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

    private class MyPaiRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyPaiBean>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.adapter_my_pai, null);
            ViewHolder vh = new ViewHolder(view);
            return vh;

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder,position);
            ViewHolder viewHolder = (ViewHolder) holder;
//            MyPaiBean bean = mList.get(position);
//            DateFormat.setTextByTime(viewHolder.textTime, bean.mybid_ctime);
//            viewHolder.textName.setText(bean.mybid_commodityname);
//            GoodsUtil.setPriceBySymbol(viewHolder.textPaiPrice, bean.commodity_startprice);
//            GoodsUtil.setPriceBySymbol(viewHolder.textResultPrice,bean.mybid_money);

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textTime, textName, textStatus,textPaiPrice,textResultPrice;

            public ViewHolder(View view) {
                super(view);
                textTime = (TextView)  view.findViewById(R.id.textTime);
                textName = (TextView)  view.findViewById(R.id.textName);
                textStatus = (TextView)  view.findViewById(R.id.textStatus);
                textPaiPrice = (TextView)  view.findViewById(R.id.textPaiPrice);
                textResultPrice = (TextView)  view.findViewById(R.id.textResultPrice);
            }
        }
    }
}
