package com.htlc.muchong.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.htlc.muchong.activity.JianDetailActivity;
import com.htlc.muchong.activity.PaiDetailActivity;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.activity.ProductListActivity;
import com.htlc.muchong.adapter.ProductRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.JiaoGoodsBean;
import model.PaiGoodsBean;

/**
 * Created by sks on 2015/12/29.
 */
public class ProductListFragment extends BaseFragment {
    public static final String TYPE_1 = "1";
    public static final String TYPE_2 = "2";
    public static final String TYPE_3 = "3";
    public static final String TYPE_4 = "4";
    public static final String ORDER_NORMAL = "1";
    public static final String ORDER_SALES_DOWN = "2";
    public static final String ORDER_SALES_UP = "5";
    public static final String ORDER_PRICE_DOWN = "4";
    public static final String ORDER_PRICE_UP = "3";


    public CharSequence mTitle;
    public int mIconId;
    public String mType;
    private int page = 1;
    private ProductListActivity activity;

    public static ProductListFragment newInstance(int iconId, String title,String type) {
        try {
            ProductListFragment fragment = new ProductListFragment();
            fragment.mTitle = title;
            fragment.mIconId = iconId;
            fragment.mType = type;
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public View getTabView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_product_layout_item, null);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(mTitle);
        textView.setCompoundDrawablesWithIntrinsicBounds(0,0,mIconId,0);
        return view;
    }






    private PtrClassicFrameLayout mPtrFrame;
    private ProductRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_list;
    }
    @Override
    protected void setupView() {
        activity = (ProductListActivity) getActivity();
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
        adapter = new ProductRecyclerViewAdapter();
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
                ProductDetailActivity.goProductActivity(getContext(),adapter.getData().get(position).id);

            }
        });


    }

    private void loadMoreData() {
        String order = getOderType();
        App.app.appAction.jiaoListBySmallClass(page, activity.getSmallClassId(), order, activity.getMaterial(), activity.new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
            @Override
            public void onSuccess(List<JiaoGoodsBean> data) {
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
    public void initData() {
        if(mType.equals(TYPE_4)){
            mPtrFrame.refreshComplete();
            mPtrFrame.setLoadMoreEnable(false);
            return;
        }
        String order = getOderType();
        page = 1;
        App.app.appAction.jiaoListBySmallClass(page,activity.getSmallClassId(),order,activity.getMaterial(), activity.new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
            @Override
            public void onSuccess(List<JiaoGoodsBean> data) {
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

    @NonNull
    private String getOderType() {
        String order = ORDER_NORMAL;
        if(mType.equals(TYPE_1)){
            order = ORDER_NORMAL;
        }else  if(mType.equals(TYPE_2) && activity.isSalesOrderIsDown()){
            order = ORDER_SALES_DOWN;
        }else if(mType.equals(TYPE_3) && activity.isPriceOrderIsDown()){
            order = ORDER_PRICE_DOWN;
        }else if(mType.equals(TYPE_3) && !(activity.isPriceOrderIsDown())){
            order = ORDER_PRICE_UP;
        }else if(mType.equals(TYPE_2) && !(activity.isSalesOrderIsDown())){
            order = ORDER_SALES_UP;
        }
        return order;
    }
}
