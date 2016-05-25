package com.htlc.muchong.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import com.htlc.muchong.adapter.ProductRecyclerViewAdapter;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;

/**
 * Created by sks on 2015/12/29.
 */
public class ProductListFragment extends BaseFragment {
    public static final String TYPE_1 = "1";
    public static final String TYPE_2 = "2";
    public static final String TYPE_3 = "3";
    public static final String TYPE_4 = "4";

    public CharSequence mTitle;
    public int mIconId;
    public String mType;

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
                ToastUtil.showToast(App.app, "mRecyclerView " + position);
                startActivity(new Intent(getActivity(), ProductDetailActivity.class));

            }
        });


    }

    private void loadMoreData() {
        mPtrFrame.loadMoreComplete(true);
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), true);
        mPtrFrame.setNoMoreData();
    }

    @Override
    protected void initData() {
        mPtrFrame.refreshComplete();
        adapter.setData(Arrays.asList(SecondFragment.sampleNetworkImageURLs), false);
        mPtrFrame.setLoadMoreEnable(true);
    }
}
