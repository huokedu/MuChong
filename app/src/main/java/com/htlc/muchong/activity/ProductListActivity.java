package com.htlc.muchong.activity;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
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
 */
public class ProductListActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private boolean priceOrderIsDown;
    private boolean salesOrderIsDown;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText("手串");
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<ProductListFragment> pageFragments = new ArrayList<>();
        pageFragments.add(ProductListFragment.newInstance(0, getString(R.string.product_list_title_one),ProductListFragment.TYPE_1));
        pageFragments.add(ProductListFragment.newInstance(R.mipmap.icon_order_normal, getString(R.string.product_list_title_two),ProductListFragment.TYPE_2));
        pageFragments.add(ProductListFragment.newInstance(R.mipmap.icon_order_normal, getString(R.string.product_list_title_three),ProductListFragment.TYPE_3));
        pageFragments.add(ProductListFragment.newInstance(R.mipmap.icon_product_list_type, getString(R.string.product_list_title_four),ProductListFragment.TYPE_4));


        ProductPagerAdapter pagerAdapter = new ProductPagerAdapter(getSupportFragmentManager(), pageFragments);
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
                TextView textView = (TextView) view.findViewById(R.id.textView);
                if (position == 1) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, salesOrderIsDown ? R.mipmap.icon_order_down : R.mipmap.icon_order_up, 0);
                } else if (position == 2) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, priceOrderIsDown ? R.mipmap.icon_order_down : R.mipmap.icon_order_up, 0);
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
                } else if (position == 2) {
                    priceOrderIsDown = !priceOrderIsDown;
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, priceOrderIsDown ? R.mipmap.icon_order_down : R.mipmap.icon_order_up, 0);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}