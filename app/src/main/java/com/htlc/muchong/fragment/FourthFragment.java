package com.htlc.muchong.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.MainActivity;
import com.htlc.muchong.activity.PostPublishActivity;
import com.htlc.muchong.adapter.FourthPagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LoginUtil;

import java.util.ArrayList;

/**
 * Created by sks on 2016/5/20.
 * 论坛Fragment
 */
public class FourthFragment extends HomeFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fourth;
    }

    @Override
    protected void setupView() {
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);
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
                final int position = tab.getPosition();
                mViewPager.setCurrentItem(position, false);
                refreshToolbar(position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        View.OnClickListener rightTextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.app.isLogin()) {
                    PostPublishActivity.goPostPublishActivity(getContext(), PostPublishActivity.Publish_Types[0], R.string.title_post_publish);
                } else {
                    LoginUtil.showLoginTips(getActivity());
                }
            }
        };
        ((MainActivity) getActivity()).setFourthFragmentOnClickListener(rightTextClickListener);
    }

    /*根据Tab的位置，刷新Toolbar；当是藏家Tab是，隐藏发布帖子按钮，并取消点击事件*/
    private void refreshToolbar(final int position) {
        mTitle = mViewPager.getAdapter().getPageTitle(position);
        if (position == 1) {
            ((BaseActivity) getActivity()).mTitleRightTextView.setVisibility(View.INVISIBLE);
            ((BaseActivity) getActivity()).mTitleTextView.setText(mTitle);
            ((MainActivity) getActivity()).setFourthFragmentOnClickListener(null);
        } else {
            ((BaseActivity) getActivity()).mTitleTextView.setText(mTitle);
            ((BaseActivity) getActivity()).mTitleRightTextView.setBackgroundResource(0);
            ((BaseActivity) getActivity()).mTitleRightTextView.setText(R.string.publish);
            View.OnClickListener rightTextClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (App.app.isLogin()) {
                        PostPublishActivity.goPostPublishActivity(getContext(), PostPublishActivity.Publish_Types[position], R.string.title_post_publish);
                    } else {
                        LoginUtil.showLoginTips(getActivity());
                    }
                }
            };
            ((MainActivity) getActivity()).setFourthFragmentOnClickListener(rightTextClickListener);
            ((BaseActivity) getActivity()).mTitleRightTextView.setOnClickListener(rightTextClickListener);
            ((BaseActivity) getActivity()).mTitleRightTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
