package com.htlc.muchong.fragment;

import android.graphics.Rect;
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
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.GoodsUtil;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.List;

import core.AppActionImpl;
import model.JianBean;
import model.OrderBean;

/**
 * Created by sks on 2016/5/23.
 * 我的订单列表
 */
public class OrderListFragment extends BaseFragment {
    public static final String TYPE_1 = "2";//未付款
    public static final String TYPE_2 = "3";//已付款
    public static final String TYPE_3 = "1";//已发货
    public CharSequence mTitle;
    private String mType;

    public static OrderListFragment newInstance(String title, String type) {
        try {
            OrderListFragment fragment = OrderListFragment.class.newInstance();
            fragment.mTitle = title;
            fragment.mType = type;
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private PtrClassicFrameLayout mPtrFrame;
    private OrderRecyclerViewAdapter adapter;
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
        adapter = new OrderRecyclerViewAdapter();
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
                CreateOrderActivity.goCreateOrderActivity(getContext(),adapter.getData().get(position).id, !TYPE_1.equals(mType));
            }
        });
        mRecyclerView.setAdapter(mAdapter);


    }

    private void loadMoreData() {
        App.app.appAction.myOrderList(page, mType, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<OrderBean>>() {
            @Override
            public void onSuccess(List<OrderBean> data) {
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
        App.app.appAction.myOrderList(page, mType, ((BaseActivity) getActivity()).new BaseActionCallbackListener<List<OrderBean>>() {
            @Override
            public void onSuccess(List<OrderBean> data) {
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


    public class OrderRecyclerViewAdapter extends BaseRecyclerViewAdapter<OrderBean> {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.adapter_order_list, null);
            ViewHolder vh = new ViewHolder(view);
            return vh;

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder,position);
            ViewHolder viewHolder = (ViewHolder) holder;
            OrderBean bean = mList.get(position);
            DateFormat.setTextByTime(viewHolder.textTime, bean.order_ctime);
            viewHolder.textName.setText(getString(R.string.order_list_order_id,bean.order_no));
            viewHolder.textPaiPrice.setText(bean.commodity_name);
            GoodsUtil.setPriceBySymbol(viewHolder.textResultPrice,bean.order_money);

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
