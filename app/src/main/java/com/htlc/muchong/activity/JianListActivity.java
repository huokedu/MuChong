package com.htlc.muchong.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.JianListPagerAdapter;
import com.htlc.muchong.adapter.QiangListPagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.JianListFragment;
import com.htlc.muchong.fragment.QiangListFragment;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;

import model.UserBean;

/**
 * Created by sks on 2016/5/24.
 */
public class JianListActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_jian_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.first_header_jian);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_add);
        mTitleRightTextView.setVisibility(LoginUtil.getUser().user_role.equals(UserBean.TYPE_EXPERT)?View.INVISIBLE:View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.app.isLogin()){
                    startActivity(new Intent(JianListActivity.this,JianPublishActivity.class));
                }else {
                    LoginUtil.showLoginTips(JianListActivity.this);
                }
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<JianListFragment> pageFragments = new ArrayList<>();

        pageFragments.add(JianListFragment.newInstance(getString(R.string.jian_title_fragment_one), JianListFragment.TYPE_1));
        pageFragments.add(JianListFragment.newInstance(getString(R.string.jian_title_fragment_two), JianListFragment.TYPE_2));
        pageFragments.add(JianListFragment.newInstance(getString(R.string.jian_title_fragment_three), JianListFragment.TYPE_3));


        JianListPagerAdapter pagerAdapter = new JianListPagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }
}
