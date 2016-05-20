package com.htlc.muchong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.htlc.muchong.fragment.HomeFragment;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class FourthPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<HomeFragment> mPageFragments;

    public FourthPagerAdapter(FragmentManager fm, ArrayList<HomeFragment> pageFragments) {
        super(fm);
        this.mPageFragments = pageFragments;
    }

    @Override
    public int getCount() {
        return mPageFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mPageFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageFragments.get(position).mTitle;
    }

}
