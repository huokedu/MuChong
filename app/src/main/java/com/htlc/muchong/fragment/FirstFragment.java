package com.htlc.muchong.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.JianListActivity;
import com.htlc.muchong.activity.PaiListActivity;
import com.htlc.muchong.activity.QiangListActivity;
import com.htlc.muchong.adapter.FirstAdapter;
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
    protected GridView mGridView;
    protected LinearLayout mlinearTypeContainer;
    private LinearLayout linearQiang, linearPai, linearJian, linearDuo;
    protected View textQiangMore, textPaiMore, textJiaoMore;

    protected View linearQiang1, linearQiang2, linearQiang3;
    protected ImageView imageQiang1, imageQiang2, imageQiang3;
    protected TextView textNameQiang1, textNameQiang2, textNameQiang3;
    protected TextView textPriceQiang1, textPriceQiang2, textPriceQiang3;
    protected TextView textDescriptionQiang1, textDescriptionQiang2, textDescriptionQiang3;

    protected View relativePai1, relativePai2;
    protected ImageView imagePai1, imagePai2;
    protected ImageView imageTypePai1, imageTypePai2;
    protected TextView textPaiName1, textPaiName2;
    protected TextView textPaiPrice1, textPaiPrice2;


    protected DaoJiShiView daoJiShiView;


    protected List mBannerList = new ArrayList();
    protected List mList = new ArrayList();
    private FirstAdapter adapter;


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

        mGridView = findViewById(R.id.gridView);
        mGridView.setFocusable(false);
        adapter = new FirstAdapter();
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast(App.app, "Grid position " + position);
            }
        });

        mlinearTypeContainer = findViewById(R.id.linearTypeContainer);
        initLinearType();

        daoJiShiView = findViewById(R.id.daoJiShiView);

        textQiangMore = findViewById(R.id.textQiangMore);
        textPaiMore = findViewById(R.id.textPaiMore);
        textJiaoMore = findViewById(R.id.textJiaoMore);
        textQiangMore.setOnClickListener(this);
        textPaiMore.setOnClickListener(this);
        textJiaoMore.setOnClickListener(this);

        linearQiang1 = findViewById(R.id.linearQiang1);
        linearQiang2 = findViewById(R.id.linearQiang2);
        linearQiang3 = findViewById(R.id.linearQiang3);
        linearQiang1.setOnClickListener(this);
        linearQiang2.setOnClickListener(this);
        linearQiang3.setOnClickListener(this);
        imageQiang1 = findViewById(R.id.imageQiang1);
        imageQiang2 = findViewById(R.id.imageQiang2);
        imageQiang3 = findViewById(R.id.imageQiang3);
        textNameQiang1 = findViewById(R.id.textNameQiang1);
        textNameQiang2 = findViewById(R.id.textNameQiang2);
        textNameQiang3 = findViewById(R.id.textNameQiang3);
        textPriceQiang1 = findViewById(R.id.textPriceQiang1);
        textPriceQiang2 = findViewById(R.id.textPriceQiang2);
        textPriceQiang3 = findViewById(R.id.textPriceQiang3);

        relativePai1 = findViewById(R.id.relativePai1);
        relativePai2 = findViewById(R.id.relativePai2);
        relativePai1.setOnClickListener(this);
        relativePai2.setOnClickListener(this);
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
        linearPai = (LinearLayout) mlinearTypeContainer.findViewById(R.id.linearPai);
        linearJian = (LinearLayout) mlinearTypeContainer.findViewById(R.id.linearJian);
        linearDuo = (LinearLayout) mlinearTypeContainer.findViewById(R.id.linearDuo);
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

    protected void refreshView() {
        daoJiShiView.setData(300000, 300000);
        mBannerFragment.setData(Arrays.asList(sampleNetworkImageURLs));
        mList.addAll(Arrays.asList(sampleNetworkImageURLs));
        adapter.setData(Arrays.asList(sampleNetworkImageURLs),false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearQiang:
            case R.id.textQiangMore:
                ToastUtil.showToast(App.app, "textQiangMore");
                startActivity(new Intent(getActivity(), QiangListActivity.class));
                break;
            case R.id.linearPai:
            case R.id.textPaiMore:
                ToastUtil.showToast(App.app, "textPaiMore");
                startActivity(new Intent(getActivity(), PaiListActivity.class));
                break;
            case R.id.linearJian:
                ToastUtil.showToast(App.app, "linearJian");
                startActivity(new Intent(getActivity(), JianListActivity.class));
                break;
            case R.id.linearDuo:
                ToastUtil.showToast(App.app, "linearDuo");
                break;
            case R.id.textJiaoMore:
                ToastUtil.showToast(App.app, "textJiaoMore");
                break;
            case R.id.linearQiang1:
                ToastUtil.showToast(App.app, "linearQiang1");
                break;
            case R.id.linearQiang2:
                ToastUtil.showToast(App.app, "linearQiang2");
                break;
            case R.id.linearQiang3:
                ToastUtil.showToast(App.app, "linearQiang3");
                break;
            case R.id.relativePai1:
                ToastUtil.showToast(App.app, "relativePai1");
                break;
            case R.id.relativePai2:
                ToastUtil.showToast(App.app, "relativePai2");
                break;
        }
    }
}
