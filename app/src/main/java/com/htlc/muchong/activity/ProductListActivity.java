package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.HomePagerAdapter;
import com.htlc.muchong.adapter.JianListPagerAdapter;
import com.htlc.muchong.adapter.ProductPagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.DefaultFragment;
import com.htlc.muchong.fragment.FifthFragment;
import com.htlc.muchong.fragment.FirstFragment;
import com.htlc.muchong.fragment.FourthFragment;
import com.htlc.muchong.fragment.HomeFragment;
import com.htlc.muchong.fragment.JianListFragment;
import com.htlc.muchong.fragment.ProductListFragment;
import com.htlc.muchong.fragment.SecondFragment;
import com.htlc.muchong.fragment.ThirdFragment;
import com.larno.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/5/24.
 * 主页商城---按分类的商品列表
 */
public class ProductListActivity extends BaseActivity {
    private static final String Small_Class_Id  = "Small_Class_Id";
    private static final String Small_Class_Name  = "Small_Class_Name";

    public static void goProductListActivity(Context context, String smallClassId, String smallClassName){
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.putExtra(Small_Class_Id,smallClassId);
        intent.putExtra(Small_Class_Name,smallClassName);
        context.startActivity(intent);
    }
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ProductPagerAdapter pagerAdapter;

    private boolean priceOrderIsDown;
    private boolean salesOrderIsDown;
    private String smallClassId;//商品小类Id
    private String smallClassName;//商品小类 名字
    private String material;//筛选条件 材质
    private String price;//筛选条件 价格

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMaterial() {
        return material;
    }

    public String getSmallClassId() {
        return smallClassId;
    }

    public boolean isSalesOrderIsDown() {
        return salesOrderIsDown;
    }

    public boolean isPriceOrderIsDown() {
        return priceOrderIsDown;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_list;
    }

    @Override
    protected void setupView() {
        Intent intent = getIntent();
        smallClassId = intent.getStringExtra(Small_Class_Id);
        smallClassName = intent.getStringExtra(Small_Class_Name);

        mTitleTextView.setText(smallClassName);
        mTitleRightTextView.setText(R.string.confirm);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductListFragment fragment =  (ProductListFragment) pagerAdapter.getItem(mViewPager.getCurrentItem());
                fragment.setFilter();
                ((ProductListFragment) pagerAdapter.getItem(0)).initData();
                mViewPager.setCurrentItem(0, false);
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<ProductListFragment> pageFragments = new ArrayList<>();
        pageFragments.add(ProductListFragment.newInstance(0, getString(R.string.product_list_title_one),ProductListFragment.TYPE_1));
        pageFragments.add(ProductListFragment.newInstance(R.mipmap.icon_order_normal, getString(R.string.product_list_title_two),ProductListFragment.TYPE_2));
        pageFragments.add(ProductListFragment.newInstance(R.mipmap.icon_order_normal, getString(R.string.product_list_title_three),ProductListFragment.TYPE_3));
        pageFragments.add(ProductListFragment.newInstance(R.mipmap.icon_product_list_type, getString(R.string.product_list_title_four),ProductListFragment.TYPE_4));


        pagerAdapter = new ProductPagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setTag(pageFragments.get(i));
                tab.setCustomView(pageFragments.get(i).getTabView(this));
            }
        }
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                View view = tab.getCustomView();
                mViewPager.setCurrentItem(position,false);
                TextView textView = (TextView) view.findViewById(R.id.textView);
                if (position == 1) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, salesOrderIsDown ? R.mipmap.icon_order_down : R.mipmap.icon_order_up, 0);
                } else if (position == 2) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, priceOrderIsDown ? R.mipmap.icon_order_down : R.mipmap.icon_order_up, 0);
                }
                if(position!=3){
                    mTitleRightTextView.setVisibility(View.INVISIBLE);
                    ProductListFragment fragment = (ProductListFragment) pagerAdapter.getItem(position);
                    fragment.initData();
                }else {
                    mTitleRightTextView.setVisibility(View.VISIBLE);
                    ProductListFragment fragment = (ProductListFragment) pagerAdapter.getItem(position);
                    fragment.notifyFilterDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 1 || position == 2) {
                    View view = tab.getCustomView();
                    TextView textView = (TextView) view.findViewById(R.id.textView);
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_order_normal, 0);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                View view = tab.getCustomView();
                TextView textView = (TextView) view.findViewById(R.id.textView);
                if (position == 1) {
                    salesOrderIsDown = !salesOrderIsDown;
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, salesOrderIsDown ? R.mipmap.icon_order_down : R.mipmap.icon_order_up, 0);
                    ProductListFragment fragment = (ProductListFragment) pagerAdapter.getItem(position);
                    fragment.initData();
                } else if (position == 2) {
                    priceOrderIsDown = !priceOrderIsDown;
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, priceOrderIsDown ? R.mipmap.icon_order_down : R.mipmap.icon_order_up, 0);
                    ProductListFragment fragment = (ProductListFragment) pagerAdapter.getItem(position);
                    fragment.initData();
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}
