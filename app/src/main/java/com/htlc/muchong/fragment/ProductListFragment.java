package com.htlc.muchong.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.htlc.muchong.activity.JianDetailActivity;
import com.htlc.muchong.activity.PaiDetailActivity;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.activity.ProductListActivity;
import com.htlc.muchong.adapter.ProductRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.widget.CheckTextView;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.JiaoGoodsBean;
import model.MaterialBean;
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

    public static final String[] Titles = {"材质","价格"};
    public static final String[] Prices = {"全部","0-500","500-1000","1000-3000","3000-5000","5000以上"};
    public static final String[] PricesId = {"","0-500","500-1000","1000-3000","3000-5000","5000"};


    public CharSequence mTitle;
    public int mIconId;
    public String mType;
    private int page = 1;
    private ProductListActivity activity;
    private List<MaterialBean> materials;

    public static ProductListFragment newInstance(int iconId, String title, String type) {
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
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, mIconId, 0);
        return view;
    }


    private PtrClassicFrameLayout mPtrFrame;
    private ProductRecyclerViewAdapter adapter;
    private FilterRecyclerAdapter filterAdapter;
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
        if (mType.equals(TYPE_4)) {
            filterAdapter = new FilterRecyclerAdapter();
            mAdapter = new RecyclerAdapterWithHF(filterAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            adapter = new ProductRecyclerViewAdapter();
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
                    if (parent.getChildAdapterPosition(view) < 2) {
                        outRect.top = space;
                    }
                }
            });
            adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ProductDetailActivity.goProductActivity(getContext(), adapter.getData().get(position).id);

                }
            });
        }

    }

    private void loadMoreData() {
        String order = getOderType();
        App.app.appAction.jiaoListBySmallClass(page, activity.getSmallClassId(), order, activity.getMaterial(),activity.getPrice(), activity.new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
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
        if (mType.equals(TYPE_4)) {
            /*获取材质列表*/
            App.app.appAction.getGoodsMaterials(activity.new BaseActionCallbackListener<List<MaterialBean>>() {
                @Override
                public void onSuccess(List<MaterialBean> data) {
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                    materials = data;
                    List<MaterialBean> prices = new ArrayList<MaterialBean>();
                    for(int i=0; i<Prices.length; i++){
                        MaterialBean materialBean = new MaterialBean();
                        materialBean.name = Prices[i];
                        materialBean.id = PricesId[i];
                        prices.add(materialBean);
                    }
                    filterAdapter.setData(materials,prices);
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                }
            });
        } else {
            String order = getOderType();
            page = 1;
            App.app.appAction.jiaoListBySmallClass(page, activity.getSmallClassId(), order, activity.getMaterial(),activity.getPrice(), activity.new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
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
    }

    @NonNull
    private String getOderType() {
        String order = ORDER_NORMAL;
        if (mType.equals(TYPE_1)) {
            order = ORDER_NORMAL;
        } else if (mType.equals(TYPE_2) && activity.isSalesOrderIsDown()) {
            order = ORDER_SALES_DOWN;
        } else if (mType.equals(TYPE_3) && activity.isPriceOrderIsDown()) {
            order = ORDER_PRICE_DOWN;
        } else if (mType.equals(TYPE_3) && !(activity.isPriceOrderIsDown())) {
            order = ORDER_PRICE_UP;
        } else if (mType.equals(TYPE_2) && !(activity.isSalesOrderIsDown())) {
            order = ORDER_SALES_UP;
        }
        return order;
    }

    public class FilterBean{
        public String title;
        public List<MaterialBean> list;
    }
    /*筛选分类*/
    public class FilterRecyclerAdapter extends RecyclerView.Adapter{
        private FilterBean[] array = new FilterBean[2];
        public FilterRecyclerAdapter() {
            for(int i=0; i<array.length; i++){
                FilterBean filterBean = new FilterBean();
                filterBean.title = Titles[i];
                filterBean.list = new ArrayList<>();
                array[i] = filterBean;
            }
        }
        public void setData(List<MaterialBean> materials, List<MaterialBean> prices){
            array[0].list.clear();
            MaterialBean materialBean  = new MaterialBean();
            materialBean.id = "";
            materialBean.name = "全部";
            array[0].list.add(materialBean);
            array[0].list.addAll(materials);
            array[1].list.clear();
            array[1].list.addAll(prices);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_product_filter, null);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            FilterBean filterBean = array[position];
            viewHolder.textName.setText(filterBean.title);
            FilterGridAdapter filterGridAdapter = new FilterGridAdapter(filterBean.list);
            viewHolder.gridView.setAdapter(filterGridAdapter);
            viewHolder.gridView.setOnItemClickListener(new GridViewOnItemClickListener(position,array, filterGridAdapter));
            viewHolder.gridView.setSelection(filterGridAdapter.getCheckPosition());
            viewHolder.gridView.setItemChecked(filterGridAdapter.getCheckPosition(), true);
        }

        @Override
        public int getItemCount() {
            return array.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textName;
            public GridView gridView;
            public ViewHolder(View view) {
                super(view);
                textName = (TextView) view.findViewById(R.id.textName);
                gridView = (GridView) view.findViewById(R.id.gridView);

            }
        }
    }
    /*GridView 点击*/
    public class GridViewOnItemClickListener implements AdapterView.OnItemClickListener{
        private int position;
        private FilterBean[] array;
        private FilterGridAdapter filterGridAdapter;
        public GridViewOnItemClickListener(int position, FilterBean[] array, FilterGridAdapter filterGridAdapter) {
            this.position = position;
            this.array = array;
            this.filterGridAdapter = filterGridAdapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            int checkedItemPosition = ((GridView)parent).getCheckedItemPosition();
            if(position == 0){
                activity.setMaterial(array[0].list.get(checkedItemPosition).id);
            }else {
                activity.setPrice(array[1].list.get(checkedItemPosition).id);
            }
            filterGridAdapter.setCheckPosition(checkedItemPosition);
            filterGridAdapter.notifyDataSetChanged();
        }
    }
    /*GridView Adapter*/
    public class FilterGridAdapter extends BaseAdapter{
        private List<MaterialBean> list;
        private int checkPosition;

        public int getCheckPosition() {
            return checkPosition;
        }

        public void setCheckPosition(int checkPosition) {
            this.checkPosition = checkPosition;
        }

        public FilterGridAdapter(List<MaterialBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(parent.getContext(), R.layout.adapter_fragment_product_grid_filter,null);
            }
            CheckTextView textView = (CheckTextView) convertView;
            if(position == checkPosition){
                textView.setChecked(true);
            }
            textView.setText(list.get(position).name);
            return convertView;
        }
    }
}
