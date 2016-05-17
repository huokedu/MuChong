package com.htlc.muchong.fragment;

import android.support.v4.app.Fragment;

import com.htlc.muchong.R;

import java.util.ArrayList;
import java.util.List;

import model.BannerBean;

/**
 * Created by sks on 2016/1/27.
 */
public class FirstFragment extends HomeFragment{



    String[] sampleNetworkImageURLs = {
            "http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg",
            "http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg",
            "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
            "http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg"
    };
    protected BannerFragment mBannerFragment;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void setupView() {
        mBannerFragment = (BannerFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        List<BannerBean> list = new ArrayList<>();
        for(int i=0;i<sampleNetworkImageURLs.length;i++){
            BannerBean bean = new BannerBean();
            bean.img = sampleNetworkImageURLs[i];
            bean.url = sampleNetworkImageURLs[i];
        }
        mBannerFragment.setData(list);
    }

    @Override
    protected void initData() {

    }
}
