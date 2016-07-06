package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.HomePagerAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.DefaultFragment;
import com.htlc.muchong.fragment.FourthChildOneFragment;
import com.htlc.muchong.fragment.HomeFragment;
import com.htlc.muchong.fragment.TaFragment;
import com.htlc.muchong.fragment.ThirdFragment;
import com.htlc.muchong.util.CircleTransform;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.PersonInfoBean;

/**
 * Created by sks on 2016/5/16.
 */
public class PersonActivity extends BaseActivity implements View.OnClickListener {
    public static final String Person_Id = "Person_Id";

    public static void goPersonActivity(Context context, String personId) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(Person_Id, personId);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabLayout.Tab mLastSelectTab;

    private TextView textFollow;
    private TextView textName;
    private TextView textFans;
    private RatingBar ratingBarLevel;
    private ImageView imageHead;

    private String personId;
    private boolean isLike;

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

        setStatusBarColor(R.mipmap.bg_status_bar);
        mToolbar.setBackgroundResource(0);

        textFollow = (TextView) findViewById(R.id.textFollow);
        textFollow.setOnClickListener(this);

        textName = (TextView) findViewById(R.id.textName);
        textFans = (TextView) findViewById(R.id.textFans);
        ratingBarLevel = (RatingBar) findViewById(R.id.ratingBarLevel);
        imageHead = (ImageView) findViewById(R.id.imageHead);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        Point outSize = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
        if (outSize.x > CommonUtil.dp2px(this, 90) * 5) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        ArrayList<HomeFragment> pageFragments = new ArrayList<>();
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_one), 0));
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_two), 0));
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_three), 0));
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_new), 0));
        pageFragments.add(HomeFragment.newInstance(TaFragment.class, getString(R.string.title_ta_four), 0));


        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), pageFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mLastSelectTab = mTabLayout.getTabAt(0);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 4) {
                    mLastSelectTab.select();
                    TaLikeActivity.goTaLikeActivity(PersonActivity.this, personId);
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

        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();

        if (personId.equals(LoginUtil.getUser().id)) {
            textFollow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initData() {
        App.app.appAction.getPersonInfo(personId, new BaseActionCallbackListener<PersonInfoBean>() {
            @Override
            public void onSuccess(PersonInfoBean data) {
                setIsLike("1".equals(data.islike));
                textFans.setText("粉丝  " + data.userinfo_likenum);
                ImageUtil.setCircleImageByDefault(imageHead, R.mipmap.default_fourth_two_head, Uri.parse(data.userinfo_headportrait));
                textName.setText(data.userinfo_nickname);
                ratingBarLevel.setRating(Float.parseFloat(data.userinfo_grade));
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*设置当前喜欢状态*/
    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
        textFollow.setText(isLike ? R.string.un_follow : R.string.follow);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textFollow:
                addLike();
                break;
        }
    }

    private void addLike() {
        if (!App.app.isLogin()) {
            LoginUtil.showLoginTips(this);
            return;
        }
        App.app.appAction.addLikePerson(personId, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                setIsLike(!isLike);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
