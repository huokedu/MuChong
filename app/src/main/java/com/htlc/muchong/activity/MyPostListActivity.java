package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.FourthOneRecyclerViewAdapter;
import com.htlc.muchong.adapter.MyPostListPagerAdapter;
import com.htlc.muchong.adapter.OrderListPagerAdapter;
import com.htlc.muchong.adapter.PaiRecyclerViewAdapter;
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.fragment.MyPostListFragment;
import com.htlc.muchong.fragment.OrderListFragment;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import core.AppActionImpl;
import model.PaiGoodsBean;
import model.PostBean;

/**
 * Created by sks on 2016/5/23.
 * 个人中心---我的论坛My
 *
 */
public class MyPostListActivity extends BaseActivity {
    public static void goMyPostListActivity(Context context){
        Intent intent = new Intent(context, MyPostListActivity.class);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_post_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_lun);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<MyPostListFragment> pageFragments = new ArrayList<>();

        pageFragments.add(MyPostListFragment.newInstance(getString(R.string.fourth_title_fragment_one)));
        pageFragments.add(MyPostListFragment.newInstance(getString(R.string.fourth_title_fragment_four)));

        MyPostListPagerAdapter pagerAdapter = new MyPostListPagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }
}
