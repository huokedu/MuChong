package com.htlc.muchong.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.widget.DaoJiShiView;
import com.larno.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by sks on 2016/1/27.
 */
public class FirstFragment extends HomeFragment implements View.OnClickListener {


    public static final String[] sampleNetworkImageURLs = {
            "http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg",
            "http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg",
            "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
            "http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg"
    };


    protected BannerFragment mBannerFragment;
    protected PtrClassicFrameLayout mPtrFrame;
    protected LinearLayout mLinearContainer;
    protected LinearLayout mlinearTypeContainer;
    private LinearLayout linearQiang, linearPai, linearJian, linearDuo;
    protected View textQiangMore, textPaiMore, textJiaoMore;

    protected View frameQiang1, frameQiang2, frameQiang3;
    protected ImageView imageQiang1, imageQiang2, imageQiang3;
    protected TextView textNameQiang1, textNameQiang2, textNameQiang3;
    protected TextView textPriceQiang1, textPriceQiang2, textPriceQiang3;
    protected TextView textDescriptionQiang1, textDescriptionQiang2, textDescriptionQiang3;

    protected View relativeJing1, relativeJing2;
    protected ImageView imagePai1, imagePai2;
    protected ImageView imageTypePai1, imageTypePai2;
    protected TextView textPaiName1, textPaiName2;
    protected TextView textPaiPrice1, textPaiPrice2;


    protected DaoJiShiView daoJiShiView;


    protected List mBannerList = new ArrayList();
    protected List mList = new ArrayList();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void setupView() {

        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);

        mBannerFragment = (BannerFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ToastUtil.showToast(App.app, "banner position = " + position);
            }
        });
        mLinearContainer = findViewById(R.id.linearContainer);

        mlinearTypeContainer = findViewById(R.id.linearTypeContainer);
        initLinearType();

        daoJiShiView = findViewById(R.id.daoJiShiView);

        textQiangMore = findViewById(R.id.textQiangMore);
        textPaiMore = findViewById(R.id.textPaiMore);
        textJiaoMore = findViewById(R.id.textJiaoMore);
        textQiangMore.setOnClickListener(this);
        textPaiMore.setOnClickListener(this);
        textJiaoMore.setOnClickListener(this);

        frameQiang1 = findViewById(R.id.frameQiang1);
        frameQiang2 = findViewById(R.id.frameQiang2);
        frameQiang3 = findViewById(R.id.frameQiang3);
        frameQiang1.setOnClickListener(this);
        frameQiang2.setOnClickListener(this);
        frameQiang3.setOnClickListener(this);
        imageQiang1 = findViewById(R.id.imageQiang1);
        imageQiang2 = findViewById(R.id.imageQiang2);
        imageQiang3 = findViewById(R.id.imageQiang3);
        textNameQiang1 = findViewById(R.id.textNameQiang1);
        textNameQiang2 = findViewById(R.id.textNameQiang2);
        textNameQiang3 = findViewById(R.id.textNameQiang3);
        textPriceQiang1 = findViewById(R.id.textPriceQiang1);
        textPriceQiang2 = findViewById(R.id.textPriceQiang2);
        textPriceQiang3 = findViewById(R.id.textPriceQiang3);
        textDescriptionQiang1 = findViewById(R.id.textDescriptionQiang1);
        textDescriptionQiang2 = findViewById(R.id.textDescriptionQiang2);
        textDescriptionQiang3 = findViewById(R.id.textDescriptionQiang3);

        relativeJing1 = findViewById(R.id.relativeJing1);
        relativeJing2 = findViewById(R.id.relativeJing2);
        relativeJing1.setOnClickListener(this);
        relativeJing2.setOnClickListener(this);
        imagePai1 = findViewById(R.id.imagePai1);
        imagePai2 = findViewById(R.id.imagePai2);
        imageTypePai1 = findViewById(R.id.imageTypePai1);
        imageTypePai2 = findViewById(R.id.imageTypePai2);
        textPaiName1 = findViewById(R.id.textPaiName1);
        textPaiName2 = findViewById(R.id.textPaiName2);
        textPaiPrice1 = findViewById(R.id.textPaiPrice1);
        textPaiPrice2 = findViewById(R.id.textPaiPrice2);
    }

    protected void initLinearType() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_first_type, mlinearTypeContainer, true);
        linearQiang = (LinearLayout) mlinearTypeContainer.findViewById(R.id.linearQiang);
        linearPai =  (LinearLayout) mlinearTypeContainer.findViewById(R.id.linearPai);
        linearJian =  (LinearLayout) mlinearTypeContainer.findViewById(R.id.linearJian);
        linearDuo =  (LinearLayout) mlinearTypeContainer.findViewById(R.id.linearDuo);
        linearQiang.setOnClickListener(this);
        linearPai.setOnClickListener(this);
        linearJian.setOnClickListener(this);
        linearDuo.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPtrFrame.refreshComplete();
        refreshView();
    }
    protected void refreshView(){
        daoJiShiView.setData(300000,300000);
        mBannerFragment.setData(Arrays.asList(sampleNetworkImageURLs));
        mList.addAll(Arrays.asList(sampleNetworkImageURLs));
        mLinearContainer.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            View view = View.inflate(getContext(), R.layout.adapter_fragment_first, null);
            final int position = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(App.app, "mLinearContainer position = " + position);
                }
            });
            TextView textName = (TextView) view.findViewById(R.id.textName);
            TextView textPrice = (TextView) view.findViewById(R.id.textPrice);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            mLinearContainer.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearQiang:
            case R.id.textQiangMore:
                ToastUtil.showToast(App.app, "textQiangMore");
                break;
            case R.id.linearPai:
            case R.id.textPaiMore:
                ToastUtil.showToast(App.app, "textPaiMore");
                break;
            case R.id.linearJian:
                ToastUtil.showToast(App.app, "linearJian");
                break;
            case R.id.linearDuo:
                ToastUtil.showToast(App.app, "linearDuo");
                break;
            case R.id.textJiaoMore:
                ToastUtil.showToast(App.app, "textJiaoMore");
                break;
            case R.id.frameQiang1:
                ToastUtil.showToast(App.app, "frameQiang1");
                break;
            case R.id.frameQiang2:
                ToastUtil.showToast(App.app, "frameQiang2");
                break;
            case R.id.frameQiang3:
                ToastUtil.showToast(App.app, "frameQiang3");
                break;
            case R.id.relativeJing1:
                ToastUtil.showToast(App.app, "relativeJing1");
                break;
            case R.id.relativeJing2:
                ToastUtil.showToast(App.app, "relativeJing2");
                break;
        }
    }
}
