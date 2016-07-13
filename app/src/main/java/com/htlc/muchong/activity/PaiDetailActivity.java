package com.htlc.muchong.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
import com.htlc.muchong.util.GoodsUtil;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.PersonUtil;
import com.htlc.muchong.util.SoftInputUtil;
import com.htlc.muchong.widget.DaoJiShiView;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import model.GoodsCommentBean;
import model.GoodsDetailBean;
import model.PaiGoodsBean;
import model.ShoppingCartItemBean;

/**
 * Created by sks on 2016/5/23.
 * 竞拍详情
 */
public class PaiDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String Product_Id = "Product_Id";
    public static final int REFRESH_TIME_T = 10000;
    private ProgressDialog progressDialog;

    /*去商品详情*/
    public static void goPaiActivity(Context context, String goodsId) {
        Intent intent = new Intent(context, PaiDetailActivity.class);
        intent.putExtra(PaiDetailActivity.Product_Id, goodsId);
        context.startActivity(intent);
    }

    private TextView textName;//发布者昵称
    private ImageView imageHead;//发布者头像
    private TextView textLevel;//发布者等级
    private RatingBar ratingBarLevel;//等级的星星表示

    protected BannerFragment mBannerFragment;//商品详情图片

    private TextView textGoodsName;//商品名称
    private TextView textMaterial;//商品材质
    private TextView textDescription;//商品描述

    private TextView textComment;//评论标题，显示评论总数
    private TextView textCommentMore;//查看更多评论

    private TextView paiPriceLabel;//拍卖价标签
    private TextView textPaiPrice;//拍卖价格
    private TextView textMarketPrice;//市场价格
    private TextView depositLabel;//保证金标签
    private TextView textDeposit;//保证金

    //倒拍
    private LinearLayout linearDaoPai;//倒拍模块
    private TextView textPrice;//当前实际价格
    private TextView textDaoPaiTips;//倒拍提示（多少时间降价一次）
    private DaoJiShiView daoJiShiView;//倒拍倒计时控件
    private RelativeLayout relativeBuy;//倒拍购买按钮容器
    private TextView textBuy;//立即购买按钮

    //竞拍
    private LinearLayout linearJingPai;//竞拍模块
    private TextView textLastPaiName;//最后拍的用户昵称
    private TextView textLastPaiPrice;//最后拍出的价格
    private TextView textAddPriceSmall;//小幅加价按钮
    private TextView textAddPriceBig;//大幅加价按钮
    private TextView textDaoJiShi;//竞拍倒计时TextView
    private RelativeLayout relativeInput;//评论容器
    private TextView textButton;//评论按钮
    private EditText editComment;//评论编辑框

    protected ListView mCommentListView;//评论list view
    private CommentAdapter adapter;//评论Adapter
    private String productId;//当前商品id
    private CountDownTimer timer;//倒计时，计时器
    private GoodsDetailBean data;//商品详情数据
    private Timer refreshDataTimer;//定时刷新计时器

    private String reply = "";//当前要回复人的昵称

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pai_detail;
    }

    @Override
    protected void setupView() {
        productId = getIntent().getStringExtra(Product_Id);
        mTitleTextView.setText(R.string.title_pai_detail);
        mTitleRightTextView.setText(R.string.refresh);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(PaiDetailActivity.this);
                progressDialog.setMessage("请稍等...");
                progressDialog.show();
                initData();
            }
        });

        imageHead = (ImageView) findViewById(R.id.imageHead);
        textName = (TextView) findViewById(R.id.textName);
        textLevel = (TextView) findViewById(R.id.textLevel);
        ratingBarLevel = (RatingBar) findViewById(R.id.ratingBarLevel);

        mBannerFragment = (BannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBanner);
        mBannerFragment.setRecycle(true);
        mBannerFragment.setListener(new BannerFragment.onItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ImageActivity.goImageActivity(PaiDetailActivity.this, mBannerFragment.getData().get(position));
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
        depositLabel = (TextView) findViewById(R.id.depositLabel);
        textDeposit = (TextView) findViewById(R.id.textDeposit);

        //倒拍
        linearDaoPai = (LinearLayout) findViewById(R.id.linearDaoPai);
        textPrice = (TextView) findViewById(R.id.textPrice);
        textDaoPaiTips = (TextView) findViewById(R.id.textDaoPaiTips);
        daoJiShiView = (DaoJiShiView) findViewById(R.id.daoJiShiView);
        relativeBuy = (RelativeLayout) findViewById(R.id.relativeBuy);
        textBuy = (TextView) findViewById(R.id.textBuy);
        textBuy.setOnClickListener(this);

        //竞拍
        linearJingPai = (LinearLayout) findViewById(R.id.linearJingPai);
        textLastPaiName = (TextView) findViewById(R.id.textLastPaiName);
        textLastPaiPrice = (TextView) findViewById(R.id.textLastPaiPrice);
        textAddPriceSmall = (TextView) findViewById(R.id.textAddPriceSmall);
        textAddPriceSmall.setOnClickListener(this);
        textAddPriceBig = (TextView) findViewById(R.id.textAddPriceBig);
        textAddPriceBig.setOnClickListener(this);
        textDaoJiShi = (TextView) findViewById(R.id.textDaoJiShi);
        relativeInput = (RelativeLayout) findViewById(R.id.relativeInput);
        editComment = (EditText) findViewById(R.id.editComment);
        textButton = (TextView) findViewById(R.id.textButton);
        textButton.setOnClickListener(this);
        textButton.setText(R.string.cancel);
        editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() < 1) {
                    textButton.setText(R.string.cancel);
                } else {
                    textButton.setText(R.string.comment);
                }
            }
        });

        mCommentListView = (ListView) findViewById(R.id.commentListView);
        mCommentListView.setFocusable(false);
        adapter = new CommentAdapter();
        mCommentListView.setAdapter(adapter);
        mCommentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsCommentBean goodsCommentBean = (GoodsCommentBean) adapter.getItem(position);
                reply = goodsCommentBean.commodityeval_userid;
                showInput(true);
            }
        });
        initData();
    }

    private void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogAppCompat);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(R.string.pai_tips);
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                startActivity(new Intent(PaiDetailActivity.this,PayActivity.class));
            }
        });
        view.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
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
                if (PaiDetailActivity.this.data == null) {
                    PaiDetailActivity.this.data = data;

                    textName.setText(data.userinfo_nickname);
                    PersonUtil.setPersonLevel(textLevel, data.userinfo_grade);
                    ratingBarLevel.setRating(Float.parseFloat(data.userinfo_grade));
                    ImageUtil.setCircleImageByDefault(imageHead, R.mipmap.default_third_gird_head, Uri.parse(data.userinfo_headportrait));
                    String[] images = data.commodity_imgStr.split(ProductDetailActivity.SPLIT_FLAG);
                    mBannerFragment.setData(Arrays.asList(images));
                    textGoodsName.setText(getString(R.string.pai_name, data.commodity_name));
                    textMaterial.setText(getString(R.string.pai_detail_material, data.commodity_material, data.commodity_spec));
                    textDescription.setText(data.commodity_content);
                    GoodsUtil.setPriceBySymbol(textMarketPrice, data.commodity_price);
                    GoodsUtil.setPriceBySymbol(textDeposit, data.commodity_bond);
                }
                textComment.setText(getString(R.string.product_detail_comment, data.evalcount));
                adapter.setData(data.evallist, false);
                //倒拍类型商品
                if (PaiGoodsBean.TYPE_DAO.equals(data.commodity_type)) {
                    GoodsUtil.setPriceBySymbol(textPrice, data.commodity_panicprice);
                    textDaoPaiTips.setText(getString(R.string.pai_dao_pai_tips, Double.parseDouble(data.decpricetime) / 60));
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
                } else {//竞拍类型商品
                    textLastPaiName.setText(getString(R.string.pai_last_username, data.bidname));
                    textLastPaiPrice.setText(getString(R.string.pai_last_price, data.commodity_panicprice));
                    if (PaiGoodsBean.STATE_NO_START.equals(data.state)) {
                        startStartTimer(Long.parseLong(data.timeStr) * 1000, Long.parseLong(data.timeend) * 1000);
                    } else if (PaiGoodsBean.STATE_END.equals(data.state)) {
                        textDaoJiShi.setText(getString(R.string.pai_dao_jis_shi, 0, 0, 0));
                    } else if (PaiGoodsBean.STATE_STARTING.equals(data.state)) {
                        startEndTimer(Long.parseLong(data.timeStr) * 1000);
                    }
                    GoodsUtil.setPriceByYuan(textAddPriceSmall, data.jiajia.get(0).constant_num);
                    GoodsUtil.setPriceByYuan(textAddPriceBig, data.jiajia.get(1).constant_num);
                    textPaiPrice.setVisibility(View.VISIBLE);
                    GoodsUtil.setPriceBySymbol(textPaiPrice, data.commodity_startprice);
                    paiPriceLabel.setVisibility(View.VISIBLE);

                    depositLabel.setVisibility(View.VISIBLE);
                    textDeposit.setVisibility(View.VISIBLE);

                    linearDaoPai.setVisibility(View.GONE);
                    relativeBuy.setVisibility(View.GONE);
                    linearJingPai.setVisibility(View.VISIBLE);
                    relativeInput.setVisibility(View.VISIBLE);
                }

                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtil.showToast(App.app, message);

            }
        });
        refreshDataByTimer();
    }
    /*定时刷新*/
    private void refreshDataByTimer(){
        if(refreshDataTimer == null){
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    initData();
                }
            };
            refreshDataTimer = new Timer();
            refreshDataTimer.schedule(timerTask,REFRESH_TIME_T,REFRESH_TIME_T);
        }
    }

    private void startStartTimer(long millisInFutureStart, final long millisInFutureEnd) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(millisInFutureStart, DaoJiShiView.T) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hour = millisUntilFinished / 3600000;
                long minute = millisUntilFinished % 3600000 / 60000;
                long second = millisUntilFinished % 3600000 % 60000 / 1000;
                textDaoJiShi.setText(getString(R.string.pai_dao_jis_shi_start, hour, minute, second));
            }

            @Override
            public void onFinish() {
                startEndTimer(millisInFutureEnd);
            }
        };
        timer.start();
    }

    /**
     * 结束倒计时
     */
    private void startEndTimer(long millisInFutureEnd) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(millisInFutureEnd, DaoJiShiView.T) {
            public void onTick(long millisUntilFinished) {
                long hour = millisUntilFinished / 3600000;
                long minute = millisUntilFinished % 3600000 / 60000;
                long second = millisUntilFinished % 3600000 % 60000 / 1000;
                textDaoJiShi.setText(getString(R.string.pai_dao_jis_shi, hour, minute, second));
            }

            public void onFinish() {
                textDaoJiShi.setText(getString(R.string.pai_dao_jis_shi, 0, 0, 0));
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if(refreshDataTimer != null){
            refreshDataTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textAddPriceBig:
                addPrice(true);
                break;
            case R.id.textAddPriceSmall:
                addPrice(false);
                break;
            case R.id.textButton:
                if (textButton.getText().toString().equals(getString(R.string.cancel))) {
                    showInput(false);
                } else {
                    commitComment();
                }
                break;
            case R.id.textBuy:
                buyNow();
                break;
            case R.id.textCommentMore:
                CommentListActivity.goCommentListActivity(this, productId);
                break;

        }
    }

    /*显示输入框与隐藏输入框*/
    private void showInput(boolean flag) {
        if (flag) {
            SoftInputUtil.showSoftInput(editComment);
        } else {
            reply = "";
            editComment.setText("");
            SoftInputUtil.hideSoftInput(editComment);
        }
    }

    /*立即购买*/
    private void buyNow() {
        if (!App.app.isLogin()) {
            LoginUtil.showLoginTips(this);
            return;
        }
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

    /*竞价  isBig 高档追加价格*/
    private void addPrice(boolean isBig) {
        if (!App.app.isLogin()) {
            LoginUtil.showLoginTips(this);
            return;
        }
        String bigPrice = data.jiajia.get(1).constant_num;
        String smallPrice = data.jiajia.get(0).constant_num;
        String addPrice = isBig ? bigPrice : smallPrice;
        App.app.appAction.addPaiPrice(productId, String.valueOf(addPrice), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                if (message.equals(ERROR_BOUND_NO_ENOUGH)) {
                    showTips();
                    return;
                }
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*提交评论*/
    private void commitComment() {
        if (!App.app.isLogin()) {
            LoginUtil.showLoginTips(this);
            return;
        }
        String comment = editComment.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            ToastUtil.showToast(App.app, "评论内容不能为空");
            return;
        }

        if(TextUtils.isEmpty(reply)){
            App.app.appAction.addGoodsComment(productId, comment, new BaseActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    ToastUtil.showToast(App.app, "评论成功");
                    showInput(false);
                    initData();
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                }
            });
        }else {
            App.app.appAction.replayGoodsComment(productId, comment, reply, new BaseActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    ToastUtil.showToast(App.app, "评论成功");
                    showInput(false);
                    initData();
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                }
            });
        }

    }
}
