package com.htlc.muchong.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.htlc.muchong.activity.JiaoListActivity;
import com.htlc.muchong.activity.PaiDetailActivity;
import com.htlc.muchong.activity.PaiListActivity;
import com.htlc.muchong.activity.ProductDetailActivity;
import com.htlc.muchong.activity.QiangListActivity;
import com.htlc.muchong.activity.WebActivity;
import com.htlc.muchong.adapter.FirstAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.widget.DaoJiShiView;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import api.Api;
import model.GoodsBean;
import model.HomeBean;
import model.PaiGoodsBean;


/**
 * Created by sks on 2016/1/27.
 * 首页Fragment
 */
public class FirstFragment extends HomeFragment implements View.OnClickListener {


    protected BannerFragment mBannerFragment;//banner
    protected PtrClassicFrameLayout mPtrFrame;
    protected GridView mGridView;//精品交易gridview
    protected LinearLayout mlinearTypeContainer;//banner下边的分类容器
    private LinearLayout linearQiang, linearPai, linearJian, linearDuo;//首页的分类
    protected View textQiangMore, textPaiMore, textJiaoMore;//首页的 还有更多+

    protected View linearQiang1, linearQiang2, linearQiang3;//限时抢购的三个条目
    protected ImageView imageQiang1, imageQiang2, imageQiang3;
    protected TextView textNameQiang1, textNameQiang2, textNameQiang3;
    protected TextView textPriceQiang1, textPriceQiang2, textPriceQiang3;
    protected TextView textOldPriceQiang1,textOldPriceQiang2,textOldPriceQiang3;

    protected View relativePai1, relativePai2;//竞拍 的两个条目
    protected ImageView imagePai1, imagePai2;
    protected ImageView imageTypePai1, imageTypePai2;
    protected TextView textPaiName1, textPaiName2;
    protected TextView textPaiPrice1, textPaiPrice2;

    protected DaoJiShiView daoJiShiView;

    protected HomeBean homeBean;
    protected FirstAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void setupView() {

        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeKey(null);
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
//        mPtrFrame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrame.autoRefresh();
//            }
//        }, 500);

        mBannerFragment = (BannerFragment) getChildFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                String banner_type = homeBean.banner.get(position).banner_type;
                String banner_title = homeBean.banner.get(position).banner_title;
                String id = homeBean.banner.get(position).id;
                if ("6".equals(banner_type)) {
                    WebActivity.goWebActivity(getContext(), banner_title, id);
                } else if ("3".equals(banner_type) || "4".equals(banner_type) || "5".equals(banner_type)) {
                    PaiDetailActivity.goPaiActivity(getContext(), id);
                } else if ("1".equals(banner_type)) {
                    ProductDetailActivity.goProductActivity(getContext(), id);
                } else if("2".equals(banner_type)){
                    ProductDetailActivity.goProductActivity(getContext(), id, true);
                }
            }
        });

        mGridView = findViewById(R.id.gridView);
        mGridView.setFocusable(false);
        adapter = new FirstAdapter();
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDetailActivity.goProductActivity(getContext(), ((GoodsBean) adapter.getItem(position)).id);
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
        imageQiang1.setVisibility(View.VISIBLE);
        imageQiang2 = findViewById(R.id.imageQiang2);
        imageQiang2.setVisibility(View.VISIBLE);
        imageQiang3 = findViewById(R.id.imageQiang3);
        imageQiang3.setVisibility(View.VISIBLE);
        textNameQiang1 = findViewById(R.id.textNameQiang1);
        textNameQiang2 = findViewById(R.id.textNameQiang2);
        textNameQiang3 = findViewById(R.id.textNameQiang3);
        textPriceQiang1 = findViewById(R.id.textPriceQiang1);
        textPriceQiang2 = findViewById(R.id.textPriceQiang2);
        textPriceQiang3 = findViewById(R.id.textPriceQiang3);
        textOldPriceQiang1 = findViewById(R.id.textOldPriceQiang1);
        textOldPriceQiang2 = findViewById(R.id.textOldPriceQiang2);
        textOldPriceQiang3 = findViewById(R.id.textOldPriceQiang3);

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

        initData();
    }

    /*初始化banner下边的  分类容器*/
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
        App.app.appAction.home(((BaseActivity) getActivity()).new BaseActionCallbackListener<HomeBean>() {
            @Override
            public void onSuccess(HomeBean data) {
                homeBean = data;
                mPtrFrame.refreshComplete();
                refreshView();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
//                refreshView();
            }
        });
    }

    protected void refreshView() {
        //刷新Banner数据
        String[] bannerArray = new String[homeBean.banner.size()];
        for (int i = 0; i < homeBean.banner.size(); i++) {
            bannerArray[i] = homeBean.banner.get(i).banner_coverimg;
        }
        mBannerFragment.setData(Arrays.asList(bannerArray));
        //刷新抢购数据
        if (PaiGoodsBean.STATE_NO_START.equals(homeBean.limittime.state)) {
            daoJiShiView.setData(Long.parseLong(homeBean.limittime.timeStr) * 1000, Long.parseLong(homeBean.limittime.timeend) * 1000);
        } else if (PaiGoodsBean.STATE_END.equals(homeBean.limittime.state)) {
            daoJiShiView.setData(0, 0);
        } else if (PaiGoodsBean.STATE_STARTING.equals(homeBean.limittime.state)) {
            daoJiShiView.setData(0, Long.parseLong(homeBean.limittime.timeStr) * 1000);
        }
        if (homeBean.limittime.list.size() >= 1) {
            linearQiang1.setVisibility(View.VISIBLE);
            GoodsBean goodsBean = homeBean.limittime.list.get(0);
            ImageUtil.setImageByDefault(imageQiang1, R.mipmap.default_first_qiang, Uri.parse(goodsBean.commodity_coverimg));
            textNameQiang1.setText(goodsBean.commodity_name);
            GoodsUtil.setPriceBySymbolAndNew(textPriceQiang1, goodsBean.commodity_panicprice);
            GoodsUtil.setPriceBySymbolAndOld(textOldPriceQiang1, goodsBean.commodity_panicprice);
        } else {
            linearQiang1.setVisibility(View.INVISIBLE);
        }
        if (homeBean.limittime.list.size() >= 2) {
            linearQiang2.setVisibility(View.VISIBLE);
            GoodsBean goodsBean = homeBean.limittime.list.get(1);
            ImageUtil.setImageByDefault(imageQiang2, R.mipmap.default_first_qiang, Uri.parse(goodsBean.commodity_coverimg));
            textNameQiang2.setText(goodsBean.commodity_name);
            GoodsUtil.setPriceBySymbolAndNew(textPriceQiang2, goodsBean.commodity_panicprice);
            GoodsUtil.setPriceBySymbolAndOld(textOldPriceQiang2, goodsBean.commodity_panicprice);
        } else {
            linearQiang2.setVisibility(View.INVISIBLE);
        }
        if (homeBean.limittime.list.size() >= 3) {
            linearQiang3.setVisibility(View.VISIBLE);
            GoodsBean goodsBean = homeBean.limittime.list.get(2);
            ImageUtil.setImageByDefault(imageQiang3, R.mipmap.default_first_qiang, Uri.parse(goodsBean.commodity_coverimg));
            textNameQiang3.setText(goodsBean.commodity_name);
            GoodsUtil.setPriceBySymbolAndNew(textPriceQiang3, goodsBean.commodity_panicprice);
            GoodsUtil.setPriceBySymbolAndOld(textOldPriceQiang3, goodsBean.commodity_panicprice);
        } else {
            linearQiang3.setVisibility(View.INVISIBLE);
        }

        //刷新竞拍数据
        if (homeBean.bid.size() >= 1) {
            relativePai1.setVisibility(View.VISIBLE);
            PaiGoodsBean paiGoodsBean = homeBean.bid.get(0);
            ImageUtil.setImageByDefault(imagePai1, R.mipmap.default_first_pai, Uri.parse(paiGoodsBean.commodity_coverimg));
            GoodsUtil.setImageByPaiType(imageTypePai1, paiGoodsBean.commodity_type);
            textPaiName1.setText(paiGoodsBean.commodity_name);
            GoodsUtil.setPriceBySymbolAndNew(textPaiPrice1, paiGoodsBean.commodity_panicprice);
        } else {
            relativePai1.setVisibility(View.INVISIBLE);
        }
        if (homeBean.bid.size() >= 2) {
            relativePai2.setVisibility(View.VISIBLE);
            PaiGoodsBean paiGoodsBean = homeBean.bid.get(1);
            ImageUtil.setImageByDefault(imagePai2, R.mipmap.default_first_pai, Uri.parse(paiGoodsBean.commodity_coverimg));
            GoodsUtil.setImageByPaiType(imageTypePai2, paiGoodsBean.commodity_type);
            textPaiName2.setText(paiGoodsBean.commodity_name);
            GoodsUtil.setPriceBySymbolAndNew(textPaiPrice2, paiGoodsBean.commodity_panicprice);
        } else {
            relativePai2.setVisibility(View.INVISIBLE);
        }

        //刷新精品数据
        adapter.setData(homeBean.jingpin, false);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearQiang:
            case R.id.textQiangMore:
                startActivity(new Intent(getActivity(), QiangListActivity.class));
                break;
            case R.id.linearPai:
            case R.id.textPaiMore:
                startActivity(new Intent(getActivity(), PaiListActivity.class));
                break;
            case R.id.linearJian:
                startActivity(new Intent(getActivity(), JianListActivity.class));
                break;
            case R.id.linearDuo:
//                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(Api.DuoBao)));
                WebActivity.goWebActivity(getContext(), getString(R.string.first_header_duo), Api.DuoBao);
                break;
            case R.id.textJiaoMore:
                startActivity(new Intent(getActivity(), JiaoListActivity.class));
                break;
            case R.id.linearQiang1:
                if (homeBean != null && homeBean.limittime.list.size() >= 1) {
                    ProductDetailActivity.goProductActivity(getContext(), homeBean.limittime.list.get(0).id, true);
                }
                break;
            case R.id.linearQiang2:
                if (homeBean != null && homeBean.limittime.list.size() >= 2) {
                    ProductDetailActivity.goProductActivity(getContext(), homeBean.limittime.list.get(1).id, true);
                }
                break;
            case R.id.linearQiang3:
                if (homeBean != null && homeBean.limittime.list.size() >= 3) {
                    ProductDetailActivity.goProductActivity(getContext(), homeBean.limittime.list.get(2).id, true);
                }
                break;
            case R.id.relativePai1:
                if (homeBean != null && homeBean.bid.size() >= 1) {
                    PaiDetailActivity.goPaiActivity(getContext(), homeBean.bid.get(0).id);
                }
                break;
            case R.id.relativePai2:
                if (homeBean != null && homeBean.bid.size() >= 2) {
                    PaiDetailActivity.goPaiActivity(getContext(), homeBean.bid.get(1).id);
                }
                break;
        }
    }


}
