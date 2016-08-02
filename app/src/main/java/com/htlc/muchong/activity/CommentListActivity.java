package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.CommentRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.SoftInputUtil;
import com.larno.util.ToastUtil;

import java.util.List;

import core.AppActionImpl;
import model.GoodsCommentBean;
import model.PostCommentBean;

/**
 * Created by sks on 2016/5/23.
 * 评论列表Activity
 */
public class CommentListActivity extends BaseActivity {
    public static final String Product_Id = "Product_Id";
    private String productId;
    private EditText editComment;
    private TextView textButton;

    private String reply = "";//当前要回复人的昵称

    public static void goCommentListActivity(Context context, String goodsId) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(Product_Id, goodsId);
        context.startActivity(intent);
    }

    private PtrClassicFrameLayout mPtrFrame;
    private CommentRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private View noDataView;

    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void setupView() {
        productId = getIntent().getStringExtra(Product_Id);

        mTitleTextView.setText(R.string.title_comment_list);
        noDataView =  findViewById(R.id.noDataView);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
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
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 500);
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        //评论部分
        editComment = (EditText) findViewById(R.id.editComment);
        textButton = (TextView) findViewById(R.id.textButton);
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
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textButton.getText().toString().equals(getString(R.string.cancel))) {
                    showInput(false);
                } else {
                    commitComment();
                }
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new CommentRecyclerViewAdapter();
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoodsCommentBean goodsCommentBean = adapter.getData().get(position);
                reply = goodsCommentBean.commodityeval_userid;
                editComment.setHint("回复:"+goodsCommentBean.userinfo_nickname);
                showInput(true);
            }
        });
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    /*显示输入框与隐藏输入框*/
    private void showInput(boolean flag) {
        if (flag) {
            SoftInputUtil.showSoftInput(editComment);
        } else {
            reply = "";
            editComment.setHint("");
            editComment.setText("");
            SoftInputUtil.hideSoftInput(editComment);
        }
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
                    ToastUtil.showToast(App.app, "评论成功！");
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
                    ToastUtil.showToast(App.app, "评论成功！");
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

    private void loadMoreData() {
        App.app.appAction.goodsCommentList(productId, page, new BaseActionCallbackListener<List<GoodsCommentBean>>() {
            @Override
            public void onSuccess(List<GoodsCommentBean> data) {
                mPtrFrame.loadMoreComplete(true);
                adapter.setData(data, true);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setNoMoreData();
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setFail();
            }
        });
    }

    @Override
    protected void initData() {
        page = 1;
        App.app.appAction.goodsCommentList(productId, page, new BaseActionCallbackListener<List<GoodsCommentBean>>() {
            @Override
            public void onSuccess(List<GoodsCommentBean> data) {
                mPtrFrame.refreshComplete();
                adapter.setData(data, false);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(false);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }
        });
    }
}
