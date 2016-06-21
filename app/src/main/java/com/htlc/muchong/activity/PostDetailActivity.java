package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.DetailImageAdapter;
import com.htlc.muchong.adapter.PostCommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.PersonUtil;
import com.htlc.muchong.util.ShareSdkUtil;
import com.htlc.muchong.widget.LoadMoreScrollView;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import api.Api;
import core.AppActionImpl;
import model.PostCommentBean;
import model.PostDetailBean;

/**
 * Created by sks on 2016/5/24.
 */
public class PostDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String Post_Id = "Post_Id";
    private ImageView imageHead;
    private TextView textName;
    private TextView textLevel;
    private TextView textTime;
    private TextView textPostTitle;
    private TextView textContent;
    private TextView textComment;
    private TextView textCommentMore;

    private LoadMoreScrollView scrollView;
    private Button textButton;
    private EditText editComment;

    public static void goPostDetailActivity(Context context, String id, int titleId) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(BaseActivity.ActivityTitleId, titleId);
        intent.putExtra(Post_Id, id);
        context.startActivity(intent);
    }

    private ListView imageListView, mCommentListView;
    private DetailImageAdapter imageAdapter;
    private PostCommentAdapter commentAdapter;

    private String postId;
    private boolean hasMore = false;
    private boolean isLoading = true;
    private int page = 2;
    private PostDetailBean data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void setupView() {
        postId = getIntent().getStringExtra(Post_Id);
        int activityTitleId = getIntent().getIntExtra(ActivityTitleId, 0);
        mTitleTextView.setText(activityTitleId);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data!=null){
                    ShareSdkUtil.shareByShareSDK(PostDetailActivity.this, textName.getText().toString(), textContent.getText().toString(), String.format(Api.SharePostUrl, postId), data.forum_coverimg);
                }
            }
        });

        scrollView = (LoadMoreScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollChangedListener(new LoadMoreScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onScrollTop() {

            }

            @Override
            public void onScrollBottom() {
                if (hasMore && !isLoading) {
                    loadMoreData();
                }
            }
        });

        imageHead = (ImageView) findViewById(R.id.imageHead);
        textName = (TextView) findViewById(R.id.textName);
        textLevel = (TextView) findViewById(R.id.textLevel);
        textTime = (TextView) findViewById(R.id.textTime);

        textPostTitle = (TextView) findViewById(R.id.textPostTitle);
        textContent = (TextView) findViewById(R.id.textContent);
        imageListView = (ListView) findViewById(R.id.imageListView);
        imageAdapter = new DetailImageAdapter();
        imageListView.setAdapter(imageAdapter);


        textComment = (TextView) findViewById(R.id.textComment);
        textCommentMore = (TextView) findViewById(R.id.textCommentMore);
        mCommentListView = (ListView) findViewById(R.id.commentListView);
        commentAdapter = new PostCommentAdapter();
        mCommentListView.setAdapter(commentAdapter);

        textButton = (Button) findViewById(R.id.textButton);
        editComment = (EditText) findViewById(R.id.editComment);
        textButton.setOnClickListener(this);

        initData();
    }

    /*获取评论列表*/
    private void loadMoreData() {
        isLoading = true;
        App.app.appAction.postCommentList(postId, page, new BaseActionCallbackListener<List<PostCommentBean>>() {
            @Override
            public void onSuccess(List<PostCommentBean> data) {
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                commentAdapter.setData(data, true);
                page++;

                isLoading = false;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                isLoading = false;
            }
        });
    }

    @Override
    protected void initData() {
        isLoading = true;
        App.app.appAction.postDetail(postId, new BaseActionCallbackListener<PostDetailBean>() {
            @Override
            public void onSuccess(PostDetailBean data) {
                PostDetailActivity.this.data = data;
                ImageUtil.setCircleImageByDefault(imageHead, R.mipmap.default_third_gird_head, Uri.parse(data.userinfo_headportrait));
                textName.setText(data.userinfo_nickname);
                PersonUtil.setPersonLevel(textLevel, data.userinfo_grade);
                DateFormat.setTextByTime(textTime, data.forum_ctime);

                String[] images = data.forum_imgstr.split(ProductDetailActivity.SPLIT_FLAG);
                imageAdapter.setData(Arrays.asList(images), false);
                textPostTitle.setText(data.forum_title);
                textContent.setText(data.forum_content);

                textComment.setText(getString(R.string.product_detail_comment, data.evalcount));
                textCommentMore.setVisibility(View.INVISIBLE);
                if (data.evallist.size() < AppActionImpl.PAGE_SIZE) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                commentAdapter.setData(data.evallist, false);

                isLoading = false;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                isLoading = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textButton:
                    commitComment();
                break;
        }
    }

    /*提交评论*/
    private void commitComment() {
        App.app.appAction.addPostComment(postId, editComment.getText().toString().trim(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "评论成功");
                editComment.setText("");
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

}
