package com.htlc.muchong.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.activity.ProductListActivity;
import com.htlc.muchong.adapter.ProductRecyclerViewAdapter;
import com.htlc.muchong.base.BaseFragment;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.widget.CheckTextView;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import core.AppActionImpl;
import model.JiaoGoodsBean;
import model.MaterialBean;

/**
 * Created by sks on 2015/12/29.
 * 商城---分类商品列表Fragment
 */
public class ProductListFragment extends BaseFragment {
    public static final String TYPE_1 = "1";//综合
    public static final String TYPE_2 = "2";//销量
    public static final String TYPE_3 = "3";//价格
    public static final String TYPE_4 = "4";//筛选
    public static final String ORDER_NORMAL = "1";//商品综合排序
    public static final String ORDER_SALES_DOWN = "2";//商品销量降序
    public static final String ORDER_SALES_UP = "5";//商品销量升序
    public static final String ORDER_PRICE_DOWN = "4";//商品价格降序
    public static final String ORDER_PRICE_UP = "3";//商品价格升序

    public static final String[] Titles = {"商品种类", "价格", "级别"};
    public static final String[] Prices = {"全部", "0-500", "500-1000", "1000-3000", "3000-5000", "5000以上"};
    public static final String[] PricesId = {"", "0-500", "500-1000", "1000-3000", "3000-5000", "5000"};
    public static final String[] Levels = {"全部","A级", "A+级", "AA级", "AA+级", "AAA级", "AAA+级","AAAA级","AAAA+级","AAAAA级", "AAAAA+级"};
    public static final String[] LevelsId = {"全部","A级", "A+级", "AA级", "AA+级", "AAA级", "AAA+级","AAAA级","AAAA+级","AAAAA级", "AAAAA+级"};


    public CharSequence mTitle;//标题  综合，价格等
    public int mIconId;//标题图标资源id
    public String mType;//当前Fragment类型  综合，价格等
    private int page = 1;
    private ProductListActivity activity;//getActivity的返回值
    private List<MaterialBean> materials;//材质列表
    private String materialId;//当前选择的材质id
    private String priceId;//当前选择的价格id
    private String levelId;//当前选择的价格id
    private int materialPosition;//当前选择的材质在gridview的位置
    private int pricePosition;//当前选择的价格在gridview的位置
    private int levelPosition;//当前选择的级别在gridview的位置
    private int materialPositionSelected;//确认选择的材质在gridview的位置
    private int pricePositionSelected;//确认选择的价格在gridview的位置
    private int levelPositionSelected;//确认选择的级别在gridview的位置

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
    private ProductRecyclerViewAdapter adapter;//商品adapter
    private FilterRecyclerAdapter filterAdapter;//筛选adapter
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private View noDataView;//无数据的提示view

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void setupView() {
        activity = (ProductListActivity) getActivity();

        noDataView = findViewById(R.id.noDataView);
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
        if (TYPE_4.equals(mType)) {
            filterAdapter = new FilterRecyclerAdapter();
            mAdapter = new RecyclerAdapterWithHF(filterAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            adapter = new ProductRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (mAdapter.isHeader(position) || mAdapter.isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
            mRecyclerView.setLayoutManager(gridLayoutManager);
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

    /*加载第2，3...的商品数据*/
    private void loadMoreData() {
        String order = getOderType();
        App.app.appAction.jiaoListBySmallClass(page, activity.getSmallClassId(), order, activity.getMaterial(), activity.getPrice(),activity.getLevel(), activity.new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
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
        if (TYPE_4.equals(mType)) {
            /*获取材质列表*/

            App.app.appAction.getGoodsMaterials(activity.getSmallClassId(),activity.new BaseActionCallbackListener<List<MaterialBean>>() {
                @Override
                public void onSuccess(List<MaterialBean> data) {
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                    //将价格和材质 可选择的数据封装为集合
                    materials = data;
                    List<MaterialBean> prices = new ArrayList<MaterialBean>();
                    for (int i = 0; i < Prices.length; i++) {
                        MaterialBean materialBean = new MaterialBean();
                        materialBean.materialsub_name = Prices[i];
                        materialBean.id = PricesId[i];
                        prices.add(materialBean);
                    }
                    List<MaterialBean> levels = new ArrayList<MaterialBean>();
                    for (int i = 0; i < Levels.length; i++) {
                        MaterialBean materialBean = new MaterialBean();
                        materialBean.materialsub_name = Levels[i];
                        materialBean.id = LevelsId[i];
                        levels.add(materialBean);
                    }
                    //设置给FilterAdapter
                    filterAdapter.setData(materials, prices, levels);
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
            App.app.appAction.jiaoListBySmallClass(page, activity.getSmallClassId(), order, activity.getMaterial(), activity.getPrice(),activity.getLevel(), activity.new BaseActionCallbackListener<List<JiaoGoodsBean>>() {
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
    }

    /*根据当前Fragment类型和Activity中记录的价格销量状态判断，商品排序类型*/
    private String getOderType() {
        String order = ORDER_NORMAL;
        if (TYPE_1.equals(mType)) {
            order = ORDER_NORMAL;
        } else if (TYPE_2.equals(mType) && activity.isSalesOrderIsDown()) {
            order = ORDER_SALES_DOWN;
        } else if (TYPE_3.equals(mType) && activity.isPriceOrderIsDown()) {
            order = ORDER_PRICE_DOWN;
        } else if (TYPE_3.equals(mType) && !(activity.isPriceOrderIsDown())) {
            order = ORDER_PRICE_UP;
        } else if (TYPE_2.equals(mType) && !(activity.isSalesOrderIsDown())) {
            order = ORDER_SALES_UP;
        }
        return order;
    }

    /**
     * 设置筛选条件
     * 将当前选中的 材质和价格 设置为筛选条件
     * 并记录选中的位置
     */
    public void setFilter() {
        activity.setMaterial(materialId);
        activity.setPrice(priceId);
        activity.setLevel(levelId);
        materialPositionSelected = materialPosition;
        pricePositionSelected = pricePosition;
        levelPositionSelected = levelPosition;
    }

    /*当选择筛选Fragment时，刷新筛选数据状态（默认选择的位置）*/
    public void notifyFilterDataSetChanged() {
        if (filterAdapter != null) {
            filterAdapter.notifyDataSetChanged();
        }
    }

    /*筛选条目Bean，筛选类型 和 可选择的数据集合*/
    public class FilterBean {
        public String title;
        public List<MaterialBean> list;
    }

    /*筛选分类*/
    public class FilterRecyclerAdapter extends RecyclerView.Adapter {
        private FilterBean[] array;

        public FilterRecyclerAdapter() {
            if(activity.getSmallClassName().equals("紫檀")){
                array = new FilterBean[3];
            }else {
                array = new FilterBean[2];
            }

            for (int i = 0; i < array.length; i++) {
                FilterBean filterBean = new FilterBean();
                filterBean.title = Titles[i];
                filterBean.list = new ArrayList<>();
                array[i] = filterBean;
            }
        }

        /*将可选择的 价格集合 和 材质集合，分装为FilterBean，作为FilterAdapter的一个条目的数据源*/
        public void setData(List<MaterialBean> materials, List<MaterialBean> prices, List<MaterialBean> levels) {
            array[0].list.clear();
            MaterialBean materialBean = new MaterialBean();
            materialBean.id = "";
            materialBean.materialsub_name = "全部";
            array[0].list.add(materialBean);
            array[0].list.addAll(materials);
            array[1].list.clear();
            array[1].list.addAll(prices);
            if(array.length>2){
                array[2].list.clear();
                array[2].list.addAll(levels);
            }
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
            viewHolder.gridView.setOnItemClickListener(new GridViewOnItemClickListener(position, array, filterGridAdapter));
//            viewHolder.gridView.setSelection(filterGridAdapter.getCheckPosition());
//            viewHolder.gridView.setItemChecked(filterGridAdapter.getCheckPosition(), true);
            //将上次选中的位置，设置为选中状态
            if(position==0){
                viewHolder.gridView.setSelection(materialPositionSelected);
                viewHolder.gridView.setItemChecked(materialPositionSelected,true);
            }else if(position==1){
                viewHolder.gridView.setSelection(pricePositionSelected);
                viewHolder.gridView.setItemChecked(pricePositionSelected, true);
            }else if(position==2){
                viewHolder.gridView.setSelection(levelPositionSelected);
                viewHolder.gridView.setItemChecked(levelPositionSelected, true);
            }
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
    public class GridViewOnItemClickListener implements AdapterView.OnItemClickListener {
        private int position;
        private FilterBean[] array;
        private FilterGridAdapter filterGridAdapter;

        public GridViewOnItemClickListener(int position, FilterBean[] array, FilterGridAdapter filterGridAdapter) {
            this.position = position;
            this.array = array;
            this.filterGridAdapter = filterGridAdapter;
        }

        /*将GridView点击的条目设置为选中状态，并记录选择位置和对应的数据*/
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            int checkedItemPosition = ((GridView) parent).getCheckedItemPosition();
            if (position == 0) {
                materialPosition = checkedItemPosition;
                materialId = array[0].list.get(checkedItemPosition).id;
            } else if(position == 1){
                pricePosition = checkedItemPosition;
                priceId = array[1].list.get(checkedItemPosition).id;
            } else if(position == 2){
                levelPosition = checkedItemPosition;
                levelId = array[2].list.get(checkedItemPosition).id;
            }
            filterGridAdapter.setCheckPosition(checkedItemPosition);
            filterGridAdapter.notifyDataSetChanged();
        }
    }

    /*GridView Adapter*/
    public class FilterGridAdapter extends BaseAdapter {
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
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.adapter_fragment_product_grid_filter, null);
            }
            CheckTextView textView = (CheckTextView) convertView;
            if (position == checkPosition) {
                textView.setChecked(true);
            }
            textView.setText(list.get(position).materialsub_name);
            return convertView;
        }
    }
}
