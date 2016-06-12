package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.htlc.muchong.R;
import com.htlc.muchong.adapter.HomePagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.DefaultFragment;
import com.htlc.muchong.fragment.FourthChildOneFragment;
import com.htlc.muchong.fragment.HomeFragment;
import com.htlc.muchong.fragment.TaFragment;
import com.htlc.muchong.fragment.ThirdFragment;

import java.util.ArrayList;

/**
 * Created by sks on 2016/5/16.
 */
public class PersonActivity extends BaseActivity {
    public static final String Person_Id = "Person_Id";
    public static void goPersonActivity(Context context, String personId){
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(Person_Id, personId);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabLayout.Tab mLastSelectTab;

    private String personId;

    public String getPersonId() {
        return personId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void setupView() {
        personId = getIntent().getStringExtra(Person_Id);

        setStatusBarColor(R.mipmap.bg_fragment_fifth_header);
        mToolbar.setBackgroundResource(0);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<HomeFragment> pageFragments = new ArrayList<>();
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_one), 0));
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_two), 0));
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_three), 0));
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_four), 0));


        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mLastSelectTab = mTabLayout.getTabAt(0);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 3) {
                    mLastSelectTab.select();
                    startActivity(new Intent(PersonActivity.this,TaLikeActivity.class));
                } else {
                    mViewPager.setCurrentItem(tab.getPosition(), false);
                    mLastSelectTab = tab;
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
