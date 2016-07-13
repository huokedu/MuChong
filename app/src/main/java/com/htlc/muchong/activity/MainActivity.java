package com.htlc.muchong.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.HomePagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.FifthFragment;
import com.htlc.muchong.fragment.FirstFragment;
import com.htlc.muchong.fragment.FourthFragment;
import com.htlc.muchong.fragment.HomeFragment;
import com.htlc.muchong.fragment.SecondFragment;
import com.htlc.muchong.fragment.DefaultFragment;
import com.htlc.muchong.fragment.ThirdFragment;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.ToastUtil;

import java.util.ArrayList;

import model.UserBean;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private View imageViewButton;//藏品专区底部ImageView按钮
    private View.OnClickListener fourthFragmentOnClickListener;

    public void setFourthFragmentOnClickListener(View.OnClickListener fourthFragmentOnClickListener) {
        this.fourthFragmentOnClickListener = fourthFragmentOnClickListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        imageViewButton = findViewById(R.id.imageViewButton);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<HomeFragment> pageFragments = new ArrayList<>();
        pageFragments.add(HomeFragment.newInstance(FirstFragment.class, getString(R.string.app_name), R.mipmap.tab_1));
        pageFragments.add(HomeFragment.newInstance(SecondFragment.class, getString(R.string.app_name), R.mipmap.tab_2));
        pageFragments.add(HomeFragment.newInstance(ThirdFragment.class, getString(R.string.title_fragment_third), 0));
        pageFragments.add(HomeFragment.newInstance(FourthFragment.class, getString(R.string.fourth_title_fragment_one), R.mipmap.tab_4));
        pageFragments.add(HomeFragment.newInstance(FifthFragment.class, getString(R.string.title_fragment_fifth), R.mipmap.tab_5));


        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setTag(pageFragments.get(i));
                tab.setCustomView(pageFragments.get(i).getTabView(this));
                if (i == 2) {
                    tab.getCustomView().setBackgroundResource(R.mipmap.bg_tab_layout);
                }
            }
        }
        //首次启动，将首页的标题设置为Toolbar的标题；显示右边按钮，并设置为搜索按钮
        mTitleTextView.setText(mViewPager.getAdapter().getPageTitle(0));
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_search);
        mTitleRightTextView.setText("");
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.goSearchActivity(MainActivity.this);
            }
        });
        mTitleRightTextView.setVisibility(View.VISIBLE);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position, false);
                //设置当前Fragment的标题作为Toolbar标题
                mTitleTextView.setText(mViewPager.getAdapter().getPageTitle(position));
                //首页，显示右侧搜索按钮
                if (position == 0) {
                    setStatusBarColor();
                    mTitleRightTextView.setBackgroundResource(R.mipmap.icon_search);
                    mTitleRightTextView.setText("");
                    mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SearchActivity.goSearchActivity(MainActivity.this);
                        }
                    });
                    mTitleLeftTextView.setVisibility(View.INVISIBLE);
                    mTitleRightTextView.setVisibility(View.VISIBLE);
                    mToolbar.setVisibility(View.VISIBLE);
                } else if (position == 1) {//商城，显示右侧搜索按钮；根据用户身份判断是否显示-发布商品-按钮；
                    setStatusBarColor();
                    mTitleRightTextView.setBackgroundResource(R.mipmap.icon_search);
                    mTitleRightTextView.setText("");
                    mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SearchActivity.goSearchActivity(MainActivity.this);
                        }
                    });
                    if (App.app.isLogin() && LoginUtil.getUser().user_role.equals(UserBean.TYPE_MERCHANT)) {
                        mTitleLeftTextView.setText(R.string.publish);
                        mTitleLeftTextView.setVisibility(View.VISIBLE);
                        mTitleRightTextView.setVisibility(View.VISIBLE);
                        mTitleLeftTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, PublishActivity.class));
                            }
                        });
                    }
                    mToolbar.setVisibility(View.VISIBLE);
                } else if (position == 2) {//藏品专区，显示右侧-发布藏品-按钮；将imageview设置为选中状态
                    setStatusBarColor();
                    mTitleRightTextView.setBackgroundResource(R.mipmap.icon_add);
                    mTitleRightTextView.setText("");
                    mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (App.app.isLogin()) {
                                startActivity(new Intent(MainActivity.this, CangPublishActivity.class));
                            } else {
                                LoginUtil.showLoginTips(MainActivity.this);
                            }

                        }
                    });
                    mTitleLeftTextView.setVisibility(View.INVISIBLE);
                    mTitleRightTextView.setVisibility(View.VISIBLE);
                    mToolbar.setVisibility(View.VISIBLE);
                    imageViewButton.setSelected(true);
                } else if (position == 3) {//论坛，根据给发布按钮设置的点击事件，判断是否显示发布按钮；如果点击事件监听为null，不显示
                    setStatusBarColor();
                    mTitleRightTextView.setBackgroundResource(R.mipmap.icon_add);
                    mTitleRightTextView.setText("");
                    mTitleRightTextView.setOnClickListener(fourthFragmentOnClickListener);
                    mTitleLeftTextView.setVisibility(View.INVISIBLE);
                    mTitleRightTextView.setVisibility(fourthFragmentOnClickListener==null ? View.INVISIBLE : View.VISIBLE);
                    mToolbar.setVisibility(View.VISIBLE);
                } else if (position == 4) {//个人中心，隐藏标题栏
                    setStatusBarColor(R.mipmap.bg_status_bar);
                    mToolbar.setVisibility(View.GONE);
                }
            }

            /*当藏品专区取消选中时，取消ImageView的选中状态*/
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 2) {
                    imageViewButton.setSelected(false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        initData();
    }

    @Override
    protected void initData() {
        UserBean user = LoginUtil.getUser();
        if (!TextUtils.isEmpty(user.user_token)) {
            App.app.setIsLogin(true);
        }
    }
}
