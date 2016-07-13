package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.JianListPagerAdapter;
import com.htlc.muchong.adapter.OrderListPagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.JianListFragment;
import com.htlc.muchong.fragment.OrderListFragment;
import com.htlc.muchong.util.LoginUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/5/24.
 * 个人中心---我的交易
 */
public class OrderListActivity extends BaseActivity {
    public static final int NO_PAY_TAB = 0;//未支付
    public static final int PAY_FINISH_TAB = 1;//已支付
    public static final int ON_THE_WAY_TAB = 2;//已发货
    public static final String TAB = "TAB";
    /*启动Activity并跳转到对应的Fragment*/
    public static void goOrderListActivity(Context context, int tab){
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra(TAB,tab);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_jiao);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<OrderListFragment> pageFragments = new ArrayList<>();

        pageFragments.add(OrderListFragment.newInstance(getString(R.string.order_list_no_pay), OrderListFragment.TYPE_1));
        pageFragments.add(OrderListFragment.newInstance(getString(R.string.order_list_pay), OrderListFragment.TYPE_2));
        pageFragments.add(OrderListFragment.newInstance(getString(R.string.order_list_finish), OrderListFragment.TYPE_3));


        OrderListPagerAdapter pagerAdapter = new OrderListPagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        int tab = getIntent().getIntExtra(TAB, 0);
        mViewPager.setCurrentItem(tab,false);
    }

    @Override
    protected void initData() {

    }
}
