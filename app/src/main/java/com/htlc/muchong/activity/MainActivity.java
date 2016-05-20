package com.htlc.muchong.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.htlc.muchong.R;
import com.htlc.muchong.adapter.HomePagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.FirstFragment;
import com.htlc.muchong.fragment.FourthFragment;
import com.htlc.muchong.fragment.HomeFragment;
import com.htlc.muchong.fragment.SecondFragment;
import com.htlc.muchong.fragment.DefaultFragment;
import com.htlc.muchong.fragment.ThirdFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<HomeFragment> pageFragments = new ArrayList<>();
        pageFragments.add(HomeFragment.newInstance(FirstFragment.class, getString(R.string.title_fragment_first), R.mipmap.tab_1));
        pageFragments.add(HomeFragment.newInstance(SecondFragment.class, getString(R.string.title_fragment_second), R.mipmap.tab_2));
        pageFragments.add(HomeFragment.newInstance(ThirdFragment.class, getString(R.string.title_fragment_third), 0));
        pageFragments.add(HomeFragment.newInstance(FourthFragment.class, getString(R.string.fourth_title_fragment_one), R.mipmap.tab_4));
        pageFragments.add(HomeFragment.newInstance(DefaultFragment.class, getString(R.string.title_fragment_fifth), R.mipmap.tab_5));



        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setTag(pageFragments.get(i));
                tab.setCustomView(pageFragments.get(i).getTabView(this));
            }
        }
        mTitleTextView.setText(mViewPager.getAdapter().getPageTitle(0));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position, false);
                mTitleTextView.setText(mViewPager.getAdapter().getPageTitle(position));
                if(position==4){
                    mToolbar.setVisibility(View.GONE);
                }else {
                    mToolbar.setVisibility(View.VISIBLE);
                }
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
