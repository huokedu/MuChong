package com.htlc.muchong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.htlc.muchong.fragment.MyPostListFragment;
import com.htlc.muchong.fragment.OrderListFragment;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class MyPostListPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<MyPostListFragment> mPageFragments;

    public MyPostListPagerAdapter(FragmentManager fm, ArrayList<MyPostListFragment> pageFragments) {
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
