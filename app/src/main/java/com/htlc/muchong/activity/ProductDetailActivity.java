package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.CommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.BannerFragment;
import com.htlc.muchong.fragment.FirstFragment;
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.ShareSdkUtil;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import api.Api;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import model.GoodsCommentBean;
import model.GoodsDetailBean;
import model.ShoppingCartItemBean;

/**
 * Created by sks on 2016/5/23.
 * 商品详情Activity
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String SPLIT_FLAG = ",";
    public static final String Product_Id = "Product_Id";
    public static final String IS_QIANG_TYPE = "IS_QIANG_TYPE";
    private TextView textCommentMore;//查看更多评论
    private TextView textBuy;//立即购买
    private TextView textAddCar;//加入购物车
    private ImageView imageShoppingCart;//去购物车
    private TextView textShopNumber;//购物车商品数量

    /*去商品详情*/
    public static void goProductActivity(Context context, String goodId) {
        goProductActivity(context, goodId, false);
    }

    /**
     * 去商品详情
     *
     * @param context
     * @param goodId      商品id
     * @param isQiangType 商品是抢购类型true
     */
    public static void goProductActivity(Context context, String goodId, boolean isQiangType) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.Product_Id, goodId);
        intent.putExtra(ProductDetailActivity.IS_QIANG_TYPE, isQiangType);
        context.startActivity(intent);
    }

    private String productId;//当前商品id
    private boolean isQiangType;//是限时抢购商品 true
    private GoodsDetailBean data;//商品详情
    private boolean isLike;//当前用户是否喜欢该商品，喜欢true

    protected BannerFragment mBannerFragment;//商品图片Fragment
    protected ListView mCommentListView;//评论list view
    private TextView textName;//商品名称
    private TextView textLike;//喜欢按钮

    private CommentAdapter adapter;//评论Adapter
    private ImageView imageRenZheng;//商家是否认证图标，未认证不显示
    private TextView textPrice;//商品价格
    private TextView textBaoYou;//商品价格
    private TextView textDescription;//商品描述
    private TextView textMaterial;//商品材质
    private TextView textComment;//评论标题（显示总评论数）

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void setupView() {
        productId = getIntent().getStringExtra(Product_Id);
        isQiangType = getIntent().getBooleanExtra(IS_QIANG_TYPE, false);


        mTitleTextView.setText(R.string.title_product_detail);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null) {
                    ShareSdkUtil.shareByShareSDK(ProductDetailActivity.this, textName.getText().toString(), textDescription.getText().toString(), String.format(Api.ShareGoodsUrl, productId), data.commodity_coverimg);
                }
            }
        });
        mBannerFragment = (BannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ImageActivity.goImageActivity(ProductDetailActivity.this, mBannerFragment.getData().get(position));
            }
        });

        textName = (TextView) findViewById(R.id.textName);
        textLike = (TextView) findViewById(R.id.textLike);
        textLike.setOnClickListener(this);
        imageRenZheng = (ImageView) findViewById(R.id.imageRenZheng);
        textPrice = (TextView) findViewById(R.id.textPrice);
        textBaoYou = (TextView) findViewById(R.id.textBaoYou);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textMaterial = (TextView) findViewById(R.id.textMaterial);

        textComment = (TextView) findViewById(R.id.textComment);
        textCommentMore = (TextView) findViewById(R.id.textCommentMore);
        textCommentMore.setOnClickListener(this);

        textBuy = (TextView) findViewById(R.id.textBuy);
        textBuy.setOnClickListener(this);
        textAddCar = (TextView) findViewById(R.id.textAddCar);
        textAddCar.setOnClickListener(this);
        imageShoppingCart = (ImageView) findViewById(R.id.imageShoppingCart);
        imageShoppingCart.setOnClickListener(this);
        textShopNumber = (TextView) findViewById(R.id.textShopNumber);

        mCommentListView = (ListView) findViewById(R.id.commentListView);
        mCommentListView.setFocusable(false);
        adapter = new CommentAdapter();
        mCommentListView.setAdapter(adapter);

        if (isQiangType) {
            imageShoppingCart.setVisibility(View.INVISIBLE);
            textAddCar.setVisibility(View.INVISIBLE);
            textShopNumber.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void initData() {
        //获取商品详细信息
        App.app.appAction.goodsDetail(productId, new BaseActionCallbackListener<GoodsDetailBean>() {
            @Override
            public void onSuccess(GoodsDetailBean data) {
                ProductDetailActivity.this.data = data;
                mBannerFragment.setData(Arrays.asList(data.commodity_imgStr.split(SPLIT_FLAG)));
                textName.setText(data.commodity_name);
                textLike.setText(data.commodity_likenum);

                setIsLike(GoodsUtil.isTrue(data.islike));

                imageRenZheng.setVisibility(GoodsUtil.isTrue(data.userinfo_sincerity) ? View.VISIBLE : View.INVISIBLE);
                GoodsUtil.setPriceBySymbol(textPrice, data.commodity_panicprice);
                GoodsUtil.setBaoYouByFlag(textBaoYou, data.commodity_freemail);
                textDescription.setText(data.commodity_content);
                String materialStr;
                if ("紫檀".equals(data.commodity_material)) {
                    materialStr = data.commodity_material + "  " + data.commodity_classlevel;
                } else {
                    materialStr = data.commodity_material;
                }
                String material = getString(R.string.product_detail_material, materialStr, data.commodity_spec);
                textMaterial.setText(material);
                textShopNumber.setText(data.shopnum);
                textComment.setText(getString(R.string.product_detail_comment, data.evalcount));
                adapter.setData(data.evallist, false);
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
        textLike.setSelected(isLike);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCommentMore:
                CommentListActivity.goCommentListActivity(this, productId);
                break;
            case R.id.textBuy:
                if (App.app.isLogin()) {
                    if (isQiangType) {
                        isCanBuy();
                    } else {
                        buyNow();
                    }
                } else {
                    LoginUtil.showLoginTips(this);
                }
                break;
            case R.id.textAddCar:
                if (App.app.isLogin()) {
                    addCart();
                } else {
                    LoginUtil.showLoginTips(this);
                }
                break;
            case R.id.imageShoppingCart:
                if (App.app.isLogin()) {
                    startActivity(new Intent(this, ShoppingCartActivity.class));
                } else {
                    LoginUtil.showLoginTips(this);
                }
                break;
            case R.id.textLike:
                if (App.app.isLogin()) {
                    addLike();
                } else {
                    LoginUtil.showLoginTips(this);
                }
                break;
        }
    }

    /*判断当前抢购商品是否能购买*/
    private void isCanBuy() {
        App.app.appAction.isCanBuyQiang(productId, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                buyNow();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*立即购买*/
    private void buyNow() {
        ArrayList<ShoppingCartItemBean> shoppingCartItemBeans = new ArrayList<>();
        ShoppingCartItemBean bean = new ShoppingCartItemBean();
        bean.shopcar_commodityid = productId;
        bean.num = "1";
        bean.commodity_panicprice = data.commodity_panicprice;
        bean.commodity_coverimg = data.commodity_coverimg;
        bean.commodity_name = data.commodity_name;
        shoppingCartItemBeans.add(bean);
        CreateOrderActivity.goCreateOrderActivity(this, shoppingCartItemBeans, false);
    }

    /*加入购物车*/
    private void addCart() {
        App.app.appAction.addShoppingCart(productId, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "添加成功");
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*添加我的喜欢*/
    private void addLike() {
        App.app.appAction.addLikeGoods(productId, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                setIsLike(!isLike);
                if (isLike) {
                    textLike.setText(String.valueOf((Integer.parseInt(textLike.getText().toString()) + 1)));
                } else {
                    textLike.setText(String.valueOf((Integer.parseInt(textLike.getText().toString()) - 1)));
                }
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
