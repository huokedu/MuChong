package com.htlc.muchong.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.htlc.muchong.R;
import com.htlc.muchong.adapter.MyJianListPagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.MyJianListFragment;

import java.util.ArrayList;

/**
 * Created by sks on 2016/5/24.
 */
public class MyJianListActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_jian_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_jian);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_add);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<MyJianListFragment> pageFragments = new ArrayList<>();

        pageFragments.add(MyJianListFragment.newInstance(getString(R.string.jian_title_fragment_one), MyJianListFragment.TYPE_1));
        pageFragments.add(MyJianListFragment.newInstance(getString(R.string.jian_title_fragment_two), MyJianListFragment.TYPE_2));
        pageFragments.add(MyJianListFragment.newInstance(getString(R.string.jian_title_fragment_three), MyJianListFragment.TYPE_3));


        MyJianListPagerAdapter pagerAdapter = new MyJianListPagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }
}
