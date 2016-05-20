package com.htlc.muchong.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.htlc.muchong.R;
import com.htlc.muchong.adapter.FourthPagerAdapter;
import com.htlc.muchong.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by sks on 2016/5/20.
 */
public class FourthFragment extends HomeFragment{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fourth;
    }

    @Override
    protected void setupView() {
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = findViewById(R.id.tabLayout);
        ArrayList<HomeFragment> pageFragments = new ArrayList<>();
        pageFragments.add(HomeFragment.newInstance(FourthChildOneFragment.class, getString(R.string.fourth_title_fragment_one), 0));
        pageFragments.add(HomeFragment.newInstance(FourthChildOneFragment.class, getString(R.string.fourth_title_fragment_two), 0));
        pageFragments.add(HomeFragment.newInstance(FourthChildOneFragment.class, getString(R.string.fourth_title_fragment_three), 0));
        pageFragments.add(HomeFragment.newInstance(FourthChildOneFragment.class, getString(R.string.fourth_title_fragment_four), 0));



        FourthPagerAdapter pagerAdapter = new FourthPagerAdapter(getChildFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position, false);
                mTitle = mViewPager.getAdapter().getPageTitle(position);
                ((BaseActivity)getActivity()).mTitleTextView.setText(mTitle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void initData() {

    }
}
