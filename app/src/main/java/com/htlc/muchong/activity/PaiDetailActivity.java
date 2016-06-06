package com.htlc.muchong.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.CommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.BannerFragment;
import com.htlc.muchong.fragment.FirstFragment;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.PersonUtil;
import com.htlc.muchong.widget.DaoJiShiView;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.GoodsDetailBean;
import model.PaiGoodsBean;

/**
 * Created by sks on 2016/5/23.
 */
public class PaiDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String Product_Id = "Product_Id";

    /*去商品详情*/
    public static void goPaiActivity(Context context, String goodsId) {
        Intent intent = new Intent(context, PaiDetailActivity.class);
        intent.putExtra(PaiDetailActivity.Product_Id, goodsId);
        context.startActivity(intent);
    }

    private TextView textName;//发布者昵称
    private ImageView imageHead;//发布者头像
    private TextView textLevel;//发布者等级
    private RatingBar ratingBarLevel;

    protected BannerFragment mBannerFragment;//商品详情图片

    private TextView textGoodsName;
    private TextView textMaterial;
    private TextView textDescription;

    private TextView textComment;
    private TextView textCommentMore;

    private TextView paiPriceLabel;
    private TextView textPaiPrice;
    private TextView textMarketPrice;
    private TextView textDeposit;

    //倒拍
    private LinearLayout linearDaoPai;
    private TextView textPrice;
    private TextView textDaoPaiTips;
    private DaoJiShiView daoJiShiView;
    private RelativeLayout relativeBuy;
    private TextView textBuy;

    //竞拍
    private LinearLayout linearJingPai;
    private TextView textLastPaiName;
    private TextView textLastPaiPrice;
    private TextView textAddPriceSmall;
    private TextView textAddPriceBig;
    private TextView textDaoJiShi;
    private RelativeLayout relativeInput;
    private TextView textButton;//评论按钮
    private EditText editComment;

    protected ListView mCommentListView;
    private CommentAdapter adapter;
    private String productId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pai_detail;
    }

    @Override
    protected void setupView() {
        productId = getIntent().getStringExtra(Product_Id);
        mTitleTextView.setText(R.string.title_pai_detail);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "share。。。。。。。。。。");
            }
        });

        imageHead = (ImageView)findViewById(R.id.imageHead);
        textName = (TextView) findViewById(R.id.textName);
        textLevel = (TextView) findViewById(R.id.textLevel);
        ratingBarLevel = (RatingBar) findViewById(R.id.ratingBarLevel);

        mBannerFragment = (BannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ToastUtil.showToast(App.app, "banner position = " + position);
            }
        });
        textGoodsName = (TextView) findViewById(R.id.textGoodsName);
        textMaterial = (TextView) findViewById(R.id.textMaterial);
        textDescription = (TextView) findViewById(R.id.textDescription);

        textComment = (TextView) findViewById(R.id.textComment);
        textCommentMore = (TextView) findViewById(R.id.textCommentMore);
        textCommentMore.setOnClickListener(this);

        paiPriceLabel = (TextView) findViewById(R.id.paiPriceLabel);
        textPaiPrice = (TextView) findViewById(R.id.textPaiPrice);
        textMarketPrice = (TextView) findViewById(R.id.textMarketPrice);
        textDeposit = (TextView) findViewById(R.id.textDeposit);

        //倒拍
        linearDaoPai = (LinearLayout) findViewById(R.id.linearDaoPai);
        textPrice = (TextView) findViewById(R.id.textPrice);
        textDaoPaiTips = (TextView) findViewById(R.id.textDaoPaiTips);
        daoJiShiView = (DaoJiShiView) findViewById(R.id.daoJiShiView);
        relativeBuy = (RelativeLayout) findViewById(R.id.relativeBuy);
        textBuy = (TextView) findViewById(R.id.textBuy);

        //竞拍
        linearJingPai = (LinearLayout) findViewById(R.id.linearJingPai);
        textLastPaiName = (TextView) findViewById(R.id.textLastPaiName);
        textLastPaiPrice = (TextView) findViewById(R.id.textLastPaiPrice);
        textAddPriceSmall = (TextView) findViewById(R.id.textAddPriceSmall);
        textAddPriceBig = (TextView) findViewById(R.id.textAddPriceBig);
        textDaoJiShi = (TextView) findViewById(R.id.textDaoJiShi);
        relativeInput = (RelativeLayout) findViewById(R.id.relativeInput);
        editComment = (EditText) findViewById(R.id.editComment);
        textButton = (TextView) findViewById(R.id.textButton);
        textButton.setOnClickListener(this);

        mCommentListView = (ListView) findViewById(R.id.commentListView);
        mCommentListView.setFocusable(false);
        adapter = new CommentAdapter();
        mCommentListView.setAdapter(adapter);
        initData();
    }

    private void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogAppCompat);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(R.string.pai_tips);
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null){
                    alertDialog.dismiss();
                }
                ToastUtil.showToast(App.app,"去充值咯!");
            }
        });
        view.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null){
                    alertDialog.dismiss();
                }
            }
        });


    }

    @Override
    protected void initData() {
        App.app.appAction.goodsDetail(productId, new BaseActionCallbackListener<GoodsDetailBean>() {
            @Override
            public void onSuccess(GoodsDetailBean data) {
                textName.setText(data.userinfo_nickname);
                PersonUtil.setPersonLevel(textLevel, data.userinfo_grade);
                ratingBarLevel.setRating(Float.parseFloat(data.userinfo_grade));
                int intrinsicWidth = getResources().getDrawable(R.mipmap.default_third_gird_head).getIntrinsicWidth();
                int intrinsicHeight = getResources().getDrawable(R.mipmap.default_third_gird_head).getIntrinsicHeight();
                Picasso.with(PaiDetailActivity.this).load(Uri.parse(data.userinfo_headportrait)).placeholder(R.mipmap.default_third_gird_head).error(R.mipmap.default_third_gird_head)
                        .resize(intrinsicWidth,intrinsicHeight).into(imageHead);
                String[] images = data.commodity_imgStr.split(ProductDetailActivity.SPLIT_FLAG);
                mBannerFragment.setData(Arrays.asList(images));
                textGoodsName.setText(getString(R.string.pai_name, data.commodity_name));
                textMaterial.setText(getString(R.string.pai_detail_material, data.commodity_material, data.commodity_spec));
                textDescription.setText(data.commodity_content);
                textComment.setText(getString(R.string.product_detail_comment,data.evalcount));
                adapter.setData(data.evallist, false);
                GoodsUtil.setPriceBySymbol(textMarketPrice, data.commodity_price);
                GoodsUtil.setPriceBySymbol(textDeposit, data.commodity_bond);
                if(PaiGoodsBean.TYPE_DAO.equals(data.commodity_type)){
                    GoodsUtil.setPriceBySymbol(textPrice, data.commodity_panicprice);
                    textDaoPaiTips.setText(getString(R.string.pai_dao_pai_tips, Double.parseDouble(data.decpricetime)/60));
                    //刷新抢购数据
                    if (PaiGoodsBean.STATE_NO_START.equals(data.state)) {
                        daoJiShiView.setData(Long.parseLong(data.timeStr) * 1000, Long.parseLong(data.timeend) * 1000);
                    } else if (PaiGoodsBean.STATE_END.equals(data.state)) {
                        daoJiShiView.setData(0, 0);
                    } else if (PaiGoodsBean.STATE_STARTING.equals(data.state)) {
                        daoJiShiView.setData(0, Long.parseLong(data.timeStr) * 1000);
                    }
                    textPaiPrice.setVisibility(View.GONE);
                    paiPriceLabel.setVisibility(View.GONE);
                    linearDaoPai.setVisibility(View.VISIBLE);
                    relativeBuy.setVisibility(View.VISIBLE);
                    linearJingPai.setVisibility(View.GONE);
                    relativeInput.setVisibility(View.GONE);
                }else {

                    textPaiPrice.setVisibility(View.VISIBLE);
                    paiPriceLabel.setVisibility(View.VISIBLE);
                    linearDaoPai.setVisibility(View.GONE);
                    relativeBuy.setVisibility(View.GONE);
                    linearJingPai.setVisibility(View.VISIBLE);
                    relativeInput.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButton:
                break;
            case R.id.textCommentMore:
                CommentListActivity.goCommentListActivity(this,productId);
                break;

        }
    }
}
