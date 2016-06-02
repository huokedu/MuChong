package com.htlc.muchong.activity;

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
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import model.GoodsDetailBean;

/**
 * Created by sks on 2016/5/23.
 */
public class ProductDetailActivity extends BaseActivity{
    public static final String SPLIT_FLAG = ",";
    public static final String Product_Id = "Product_Id";

    private String productId;

    protected BannerFragment mBannerFragment;
    protected ListView mCommentListView;
    private TextView textName;
    private TextView textLike;

    private CommentAdapter adapter;
    private ImageView imageRenZheng;
    private TextView textPrice;
    private TextView textDescription;
    private TextView textMaterial;
    private TextView textComment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void setupView() {
        productId = getIntent().getStringExtra(Product_Id);
        mTitleTextView.setText(R.string.title_product_detail);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "share。。。。。。。。。。");
            }
        });
        mBannerFragment = (BannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ToastUtil.showToast(App.app, "banner position = " + position);
            }
        });

        textName = (TextView) findViewById(R.id.textName);
        textLike = (TextView) findViewById(R.id.textLike);
        imageRenZheng = (ImageView) findViewById(R.id.imageRenZheng);
        textPrice = (TextView) findViewById(R.id.textPrice);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textMaterial = (TextView) findViewById(R.id.textMaterial);

        textComment = (TextView) findViewById(R.id.textComment);

        mCommentListView = (ListView) findViewById(R.id.commentListView);
        adapter = new CommentAdapter();
        mCommentListView.setAdapter(adapter);
        initData();
    }

    @Override
    protected void initData() {
        //获取商品详细信息
        App.app.appAction.goodsDetail(productId, new BaseActionCallbackListener<GoodsDetailBean>() {
            @Override
            public void onSuccess(GoodsDetailBean data) {
                mBannerFragment.setData(Arrays.asList(data.commodity_imgStr.split(SPLIT_FLAG)));
                textName.setText(data.commodity_name);
                textLike.setText(data.commodity_likenum);
                textPrice.setText(data.commodity_panicprice);
                textDescription.setText(data.commodity_content);
                String material = getString(R.string.product_detail_material,data.commodity_material,data.commodity_spec);
                textMaterial.setText(material);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });


        List<String> strings = Arrays.asList(FirstFragment.sampleNetworkImageURLs);
        adapter.setData(strings,false);
    }
}
